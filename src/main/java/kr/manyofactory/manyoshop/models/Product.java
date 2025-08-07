package kr.manyofactory.manyoshop.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class Product implements Serializable {
    private int productId;
    private Integer categoryId;
    private int bestId;
    private String productName;
    private int productPrice;
    private int salePrice;
    private int discount;
    private String regDate;
    private String editDate;
    private String productImg;

    @Getter
    @Setter
    private int listCount = 0;

    @Getter
    @Setter
    private int offset = 0;

    
}

