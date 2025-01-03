package cn.com.recharge.comm.conf;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Value("${config.http.read_timeout}")
    private int readTimeout;

    @Value("${config.http.connect_timeout}")
    private int connectTimeout;

    @Bean
    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RestTemplate template = new RestTemplate(getClientHttpRequestFactory());
        //解决乱码
        List<HttpMessageConverter<?>> httpMessageConverters = template.getMessageConverters();
        httpMessageConverters.stream().forEach(httpMessageConverter ->{
            if(httpMessageConverter instanceof StringHttpMessageConverter messageConverter){
                messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
        });
        return template;
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(getHttpClient());
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setConnectionRequestTimeout(readTimeout);
        return clientHttpRequestFactory;
    }

    private CloseableHttpClient getHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (chain, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        //调整了这里
        return HttpClients.custom().setConnectionManager(PoolingHttpClientConnectionManagerBuilder
                .create().setSSLSocketFactory(sslsf).build()).build();
    }

   @Bean
   public RestTemplate restTemplate1() {
       RestTemplate restTemplate = null;
       try {
           TrustStrategy acceptingTrustStrategy = (chain, authType) -> true;
           SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
           SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

           HttpClientBuilder clientBuilder = HttpClients.custom();

           //调整了这里
           CloseableHttpClient httpClient = clientBuilder.setConnectionManager(PoolingHttpClientConnectionManagerBuilder
                   .create().setSSLSocketFactory(sslsf).build()).build();

           HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
           httpRequestFactory.setConnectionRequestTimeout(30000);
           httpRequestFactory.setConnectTimeout(30000);

           httpRequestFactory.setHttpClient(httpClient);

           restTemplate = new RestTemplate(httpRequestFactory);

       } catch (Exception e) {
           e.printStackTrace();
       }
       return restTemplate;
   }

}
