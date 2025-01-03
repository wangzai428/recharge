package cn.com.recharge.service.impl;

import cn.com.recharge.entity.PhoneEntity;
import cn.com.recharge.service.PhoneService;
import cn.com.recharge.service.mapper.PhoneMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PhoneServiceImpl implements PhoneService {

    @Autowired
    private PhoneMapper phoneMapper;

    @Override
    public PhoneEntity create(PhoneEntity phoneEntity) {
        long insert = phoneMapper.insert(phoneEntity);
        phoneEntity.setId(insert);
        return phoneEntity;
    }

    @Override
    public String findMaxPhone() {
        return phoneMapper.findMaxPhone();

    }
}
