package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.ReviewComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewCommentMapper {

    @Insert("""
        INSERT INTO review_comment (review_id, member_id, content, created_at)
        VALUES (#{reviewId}, #{memberId}, #{content}, NOW())
    """)
    int insert(ReviewComment comment);

    @Select("""
        SELECT rc.*, m.user_name AS member_name
        FROM review_comment rc
        JOIN members m ON rc.member_id = m.id
        WHERE rc.review_id = #{reviewId}
        ORDER BY rc.created_at ASC
    """)
    List<ReviewComment> selectByReviewId(int reviewId);
}
