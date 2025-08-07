package kr.manyofactory.manyoshop.models;

import lombok.Data;

@Data
public class Payment {
    public int paymentId;
    public int memberId;
    public String paymentNumber;
    public String paymentDate;
    public String paymentStatus;
    public int totalAmount;
    public String paymentMethod;
    public String paidAt;
    public String canceledAt;
    public String regDate;
    public String addr;
    public String memberTel;
    public String addrMessage;

}
