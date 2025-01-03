package cn.com.recharge.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderEntity implements Serializable {

    private Long id;
    private String orderId;
    private String phone;
    private String status;
    private String createTime;
    private String updateTime;
    private String remark;
    private String payTime;
    private String payType;
    private Double payMoney;
    private String payOrderId;
    private String payRemark;
    private String payStatus;
    private String payMsg;



}
