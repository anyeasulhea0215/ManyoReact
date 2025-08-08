package kr.manyofactory.manyoshop.models;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RefundItem {
    public int refundItemId;
    public int orderItemId;
    public int paymentId;
    public String orderImg;
    public String orderBname;
    public String orderPname;
    public int orderCount;
    public int orderPrice;
    public LocalDate regDate;
    public LocalDate editDate;

}
