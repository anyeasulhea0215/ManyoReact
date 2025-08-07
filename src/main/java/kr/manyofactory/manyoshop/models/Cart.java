package kr.manyofactory.manyoshop.models;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Cart implements Serializable {
    private int basketId;             
    private int quantity;             
    private LocalDateTime basketAddDate;   
    private int productId;             
    private int memberId;              
    private LocalDateTime basketEditDate;

    private Product product;
}
