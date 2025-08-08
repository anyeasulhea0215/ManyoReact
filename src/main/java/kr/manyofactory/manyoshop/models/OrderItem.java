package kr.manyofactory.manyoshop.models;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OrderItem {
    private int orderItemId;
    private int paymentId;
    private String orderImg;
    private String orderBname;
    private String orderPname;
    private int orderCount;
    private int orderPrice;
    private LocalDate regDate;
    private LocalDate editDate;
    private Product product;
    private Integer productId;

    private static int listCount = 0;
    private static int offset = 0;
    private String orderStatus;

    private boolean reviewed; // 리뷰작성 여부

}
