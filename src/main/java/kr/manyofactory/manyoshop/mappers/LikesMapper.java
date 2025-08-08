package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.Likes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LikesMapper {

    /** 좋아요 추가 */
    @Insert("""
        INSERT INTO likes (review_id, liked_at)
        VALUES (#{reviewId}, NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "likeId", keyColumn = "like_id")
    public int insert(Likes input);

    /** 좋아요 개수 조회 (리뷰별) */
    @Select("""
        SELECT COUNT(*) FROM likes WHERE review_id = #{reviewId}
    """)
    public int countByReviewId(int reviewId);


    /** 특정 리뷰의 좋아요 목록 */
    @Select("""
        SELECT like_id, review_id, liked_at
        FROM likes
        WHERE review_id = #{reviewId}
    """)
    List<Likes> selectListByReviewId(int reviewId);

    /** 좋아요 삭제 */
    @Delete("""
        DELETE FROM likes WHERE like_id = #{likeId}
    """)
    public int delete(int likeId);
}
