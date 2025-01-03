package cn.com.recharge.entity;

import lombok.*;

@Builder
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PhoneEntity {

    private long id;
    private String phone;
    private String province;
    private String city;
    private String zipCode;
    private String operator;
    private String areaCode;
    private String description;


}
