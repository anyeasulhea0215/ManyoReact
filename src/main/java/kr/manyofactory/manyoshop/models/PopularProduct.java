package kr.manyofactory.manyoshop.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PopularProduct {
    private String productName;
    private int totalCount;
    private LocalDate statDate;
}
