package kr.manyofactory.manyoshop.models;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewComment {
    private int commentId;
    private int reviewId;
    private int memberId;
    private String content;
    private LocalDateTime createdAt;
    private String memberName;
}
