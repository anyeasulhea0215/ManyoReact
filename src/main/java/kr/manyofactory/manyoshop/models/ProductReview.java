package kr.manyofactory.manyoshop.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductReview implements Serializable {
    private int reviewId;
    private int memberId;
    private int productId;
    private int rating;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime regDate;
    private LocalDateTime editDate;
    private int likeCount;
    private List<ReviewComment> comments;
    @Getter
    @Setter
    private static int listCount = 0;

    @Getter
    @Setter
    private static int offset = 0;
}
