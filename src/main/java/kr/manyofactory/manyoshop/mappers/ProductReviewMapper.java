package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.ProductReview;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductReviewMapper {

    @Insert("""
                INSERT INTO product_review (
                    member_id, product_id, rating, title, content, image_url, reg_date
                ) VALUES (
                    #{memberId}, #{productId}, #{rating}, #{title}, #{content}, #{imageUrl}, NOW()
                )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "reviewId", keyColumn = "review_id")
    public int insert(ProductReview review);

    @Select("""
                SELECT review_id, member_id, product_id, rating, title, content, image_url, reg_date, edit_date
                FROM product_review
                WHERE review_id = #{reviewId}
            """)
    @Results(id = "reviewResultMap",
            value = {@Result(column = "review_id", property = "reviewId"),
                    @Result(column = "member_id", property = "memberId"),
                    @Result(column = "product_id", property = "productId"),
                    @Result(column = "rating", property = "rating"),
                    @Result(column = "title", property = "title"),
                    @Result(column = "content", property = "content"),
                    @Result(column = "image_url", property = "imageUrl"),
                    @Result(column = "reg_date", property = "regDate"),
                    @Result(column = "edit_date", property = "editDate")})
    public ProductReview selectItem(ProductReview input);

    @Select("""
                SELECT review_id, member_id, product_id, rating, title, content, image_url, reg_date, edit_date
                FROM product_review
                WHERE product_id = #{productId}
                ORDER BY reg_date DESC
            """)
    @ResultMap("reviewResultMap")
    List<ProductReview> selectByProductId(@Param("productId") int productId);

    @Update("""
                UPDATE product_review SET
                    rating = #{rating},
                    title = #{title},
                    content = #{content},
                    image_url = #{imageUrl},
                    edit_date = NOW()
                WHERE review_id = #{reviewId}
            """)
    public int update(ProductReview review);

    @Delete("""
                DELETE FROM product_review WHERE review_id = #{reviewId}
            """)
    public int delete(@Param("reviewId") int reviewId);

    @Select("""
                SELECT review_id, member_id, product_id, rating, title, content, image_url, reg_date, edit_date
                FROM product_review
                WHERE member_id = #{memberId}
                ORDER BY reg_date DESC
            """)
    @ResultMap("reviewResultMap")
    List<ProductReview> selectByMemberId(@Param("memberId") int memberId);


    /**
     * 구매한 제품의 상품 리뷰 존재시 구매자 일련번호와 구매상품 아이디가 일치하는 행 존재 확인
     *
     * @param memberId -구매자 일련번호
     * @param productId -구매한 상품 ID
     * @return -존재하는지 여부
     */
    @Select("SELECT COUNT(*) > 0 FROM product_review WHERE member_id = #{memberId} AND product_id = #{productId}")
    boolean existsByMemberIdAndProductId(@Param("memberId") int memberId,
            @Param("productId") int productId);



@Select("""
    SELECT product_id FROM product_review WHERE review_id = #{reviewId}
""")
int getProductIdByReviewId(int reviewId);}
