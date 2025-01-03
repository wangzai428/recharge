package cn.com.recharge.task;

import cn.com.recharge.comm.util.PhoneUtils;
import cn.com.recharge.entity.PhoneEntity;
import cn.com.recharge.service.PhoneService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class PhoneTask {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PhoneService phoneService;

    private final String str = "00000000";

    @PostConstruct
    public void getPhoneInfo() {
        int index = 0;
        int start = 0;
        String maxPhone = phoneService.findMaxPhone();
        if(StringUtils.isNotBlank(maxPhone)){
            String phone_pre = maxPhone.substring(0, 3);
            String phone_suf = maxPhone.substring(3);

            index = PhoneUtils.PHONES_HEADER.indexOf(phone_pre);
            start = Integer.valueOf(phone_suf);
        }

        while (index < PhoneUtils.PHONES_HEADER.size()) {
            String phone_pre = PhoneUtils.PHONES_HEADER.get(index);
            start++;
            for (; start < 100000000; start++) {
                String phone = "";
                try {
                    int len = String.valueOf(start).length();
                    int subIndex = str.length() - len;
                    phone = phone_pre + str.substring(0,subIndex) + start;
                    PhoneEntity phoneEntity = getPhoneEntity(phone);
                    log.info("phoneEntity:{}", phoneEntity);
                    phoneService.create(phoneEntity);
                    Thread.sleep(5000);
                } catch (Exception e) {
                    log.error("phone:{}", phone, e);
                }
            }
            index++;
            start = 0;
        }
    }

    public PhoneEntity getPhoneEntity(String phone) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
            httpHeaders.add("Server", "nginx");
            httpHeaders.add("Date", new Date().toString());
            httpHeaders.add("Content-Type", " text/html; charset=utf-8");
            httpHeaders.add("Transfer-Encoding", " chunked");
            httpHeaders.add("Connection", " keep-alive");
            httpHeaders.add("Vary", " Accept-Encoding");
            httpHeaders.add("X-Powered-By", " PHP/7.4.33");
            httpHeaders.add("Content-Encoding", " gzip");

            HttpEntity entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<String> exchange = restTemplate.exchange(PhoneUtils.URL, HttpMethod.GET, entity, String.class,phone);

            String htmlContent = exchange.getBody();

            // 使用 Jsoup 解析 HTML
            Document doc = Jsoup.parse(htmlContent);

            // 提取并打印需要的信息
            Element form = doc.getElementById("form");
            Elements ps = form.getElementsByTag("p");
            Element ret = ps.stream().filter(p -> p.text().contains("查询结果")).findFirst().get();
            List<TextNode> textNodes = ret.textNodes();

            PhoneEntity.PhoneEntityBuilder builder = PhoneEntity.builder()
                    .phone(phone)
                    .description(ret.text());
            if(textNodes.size() == 4){
                String area = textNodes.get(0).text();
                builder.phone(phone)
                        .province(area.split("\\s+")[0])
                        .city(area.split("\\s+")[1])
                        .operator(textNodes.get(1).text())
                        .zipCode(textNodes.get(2).text())
                        .areaCode(textNodes.get(3).text());
            }
            return builder.build();
        } catch (Exception e) {
            log.error("phone:{}", phone, e);
            return PhoneEntity.builder().phone(phone).description(e.getMessage()).build();
        }
    }

}
