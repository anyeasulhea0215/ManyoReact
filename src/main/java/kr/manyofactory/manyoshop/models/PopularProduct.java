package kr.manyofactory.manyoshop.models;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PopularProduct {

    private int productId;
    private String productName;
    private int totalCount;
    private LocalDate statDate;
}
