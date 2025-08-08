package kr.manyofactory.manyoshop.models;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberStatics {
    private int id;
    private LocalDate date;
    private int count;
}
