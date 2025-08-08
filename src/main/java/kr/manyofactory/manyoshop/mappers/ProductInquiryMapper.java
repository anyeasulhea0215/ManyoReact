package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.Product;
import kr.manyofactory.manyoshop.models.ProductInquiry;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductInquiryMapper {

    /*
     * 상품문의 등록
     */
    @Insert("""
        INSERT INTO product_inquiries (
            member_id, product_id, inquiry_type, title, content,
            answer, attached_file_url, reg_date, answer_date, is_answered
        ) VALUES (
            #{memberId}, #{productId}, #{inquiryType}, #{title}, #{content},
            #{answer}, #{attachedFileUrl}, NOW(), #{answerDate}, #{isAnswered}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "inquiryId", keyColumn = "inquiry_id")
    int insert(ProductInquiry input);

    /*
     * 상품문의 수 조회
     */
    @Select("""
        <script>
        SELECT COUNT(*) FROM product_inquiries
        <where>
            <if test='memberId != 0'> AND member_id = #{memberId} </if>
            <if test='isAnswered != null and isAnswered != ""'> AND is_answered = #{isAnswered} </if>
        </where>
        </script>
    """)
    int selectCount(ProductInquiry input);

    /*
     * 상품문의 상세 조회
     */
    @Select("""
        SELECT inquiry_id, member_id, product_id, inquiry_type, title,
               content, answer, attached_file_url, reg_date, answer_date, is_answered
        FROM product_inquiries
        WHERE inquiry_id = #{inquiryId}
    """)
    @Results(id = "inquiryMap", value = {
        @Result(column = "inquiry_id", property = "inquiryId"),
        @Result(column = "member_id", property = "memberId"),
            @Result(column = "product_id", property = "productId"),
        @Result(column = "inquiry_type", property = "inquiryType"),
        @Result(column = "title", property = "title"),
        @Result(column = "content", property = "content"),
        @Result(column = "answer", property = "answer"),
        @Result(column = "attached_file_url", property = "attachedFileUrl"),
        @Result(column = "reg_date", property = "regDate"),
        @Result(column = "answer_date", property = "answerDate"),
        @Result(column = "is_answered", property = "isAnswered")
    })
    ProductInquiry selectItem(ProductInquiry input);

    /*
     * 상품문의 목록 조회
     */
    @Select("""
    SELECT inquiry_id, member_id,product_id, inquiry_type, title,
           content, answer, attached_file_url, reg_date, answer_date, is_answered
    FROM product_inquiries
    WHERE member_id = #{memberId}
    ORDER BY inquiry_id DESC
""")
    @ResultMap("inquiryMap")
    List<ProductInquiry> selectListByMemberId(int memberId);


    /*
     * 상품문의 답변 등록
     */
    @Update("""
        UPDATE product_inquiries
        SET answer = #{answer},
            answer_date = NOW(),
            is_answered = #{isAnswered}
        WHERE inquiry_id = #{inquiryId}
    """)
    int updateAnswer(ProductInquiry input);

    /**
     * 상품문의 삭제
     */
    @Delete("""
        DELETE FROM product_inquiries
        WHERE inquiry_id = #{inquiryId}
    """)
    int delete(ProductInquiry input);

   @Select("""
    SELECT DISTINCT p.product_id, p.product_name
    FROM payment pay
    JOIN order_items oi ON pay.payment_id = oi.payment_id
    JOIN products p ON oi.product_id = p.product_id
    WHERE pay.member_id = #{memberId}
""")
List<Product> findPurchasedProducts(int memberId);

    @Select("""
    SELECT inquiry_id, member_id, inquiry_type, title,
           content, answer, attached_file_url, reg_date, answer_date, is_answered
    FROM product_inquiries
    WHERE member_id = #{memberId}
      AND product_id = #{productId}
    ORDER BY inquiry_id DESC
""")
    @ResultMap("inquiryMap")
    List<ProductInquiry> selectMyInquiriesByProduct(@Param("memberId") int memberId,
                                                    @Param("productId") int productId);



}
