package cn.com.recharge.service.mapper;

import cn.com.recharge.entity.PhoneEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhoneMapper {

//    @Insert("insert into phone(phone,province,city,zip_code,operator,area_code,desc) values(#{phone},#{province},#{city},#{zipCode},#{operator},#{areaCode},#{desc})")
    long insert(PhoneEntity record);

    PhoneEntity selectByPhone(String phone);

    List<PhoneEntity> selectByPhoneList(List<String> phones);

    PhoneEntity selectById(Long id);

    String findMaxPhone();

}
