package kr.manyofactory.manyoshop.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class SpecialProduct {
    private int spaecialId;
    private int productId;
    private LocalDateTime regDate;
    private LocalDateTime editDate;

    @Getter
    @Setter
    private static int listCount = 0;

    @Getter
    @Setter
    private static int offset = 0;

}
