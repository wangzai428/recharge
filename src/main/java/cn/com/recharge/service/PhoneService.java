package cn.com.recharge.service;

import cn.com.recharge.entity.PhoneEntity;

public interface PhoneService {

    PhoneEntity create(PhoneEntity phoneEntity);

    String findMaxPhone();

}
