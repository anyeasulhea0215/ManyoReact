package kr.manyofactory.manyoshop.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Likes implements Serializable {
    private int likeId;
    private int reviewId;
    private LocalDateTime likedAt;

    @Getter
    @Setter
    private static int listCount = 0;

    @Getter
    @Setter
    private static int offset = 0;
}
