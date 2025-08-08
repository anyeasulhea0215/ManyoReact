package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.Wishlist;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WishlistMapper {

    /*
     * 찜 등록
     */
    @Insert("""
        INSERT INTO wishlist (
            member_id, product_id, option_info, quantity, added_at
        ) VALUES (
            #{memberId}, #{productId}, #{optionInfo}, #{quantity}, NOW()
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "wishlistId", keyColumn = "wishlist_id")
    int insert(Wishlist input);

    /*
     * 찜 개수 조회 
     */
    @Select("""
        <script>
        SELECT COUNT(*) FROM wishlist
        <where>
            <if test='memberId != 0'> AND member_id = #{memberId} </if>
            <if test='productId != 0'> AND product_id = #{productId} </if>
        </where>
        </script>
    """)
    int selectCount(Wishlist input);

    /*
     * 찜 상세 조회
     */
    @Select("""
        SELECT wishlist_id, member_id, product_id, option_info, quantity, added_at
        FROM wishlist
        WHERE wishlist_id = #{wishlistId}
    """)
    @Results(id = "wishlistMap", value = {
        @Result(column = "wishlist_id", property = "wishlistId"),
        @Result(column = "member_id", property = "memberId"),
        @Result(column = "product_id", property = "productId"),
        @Result(column = "option_info", property = "optionInfo"),
        @Result(column = "quantity", property = "quantity"),
        @Result(column = "added_at", property = "addedAt")
    })
    Wishlist selectItem(Wishlist input);

    /*
     * 찜 목록 조회 
     */
    @Select("""
        SELECT wishlist_id, member_id, product_id, option_info, quantity, added_at
        FROM wishlist
        WHERE member_id = #{memberId}
        ORDER BY added_at DESC
    """)
    @ResultMap("wishlistMap")
    List<Wishlist> selectListByMember(int memberId);

    @Delete("""
    <script>
        DELETE FROM wishlist
        WHERE wishlist_id IN
        <foreach item="id" collection="wishlistIds" open="(" separator="," close=")">
            #{id}
        </foreach>
    </script>
""")
    int deleteByIds(List<Integer> wishlistIds);


    /*
     * 찜 삭제
     */
    @Delete("""
        DELETE FROM wishlist
        WHERE wishlist_id = #{wishlistId}
    """)
    int delete(Wishlist input);
    /*
    *전체삭제용
     */
    @Delete("""
    DELETE FROM wishlist
    WHERE member_id = #{memberId}
""")
    int deleteAllByMemberId(int memberId);

    /**
     * 전체 상품 보여주기
     * @param memberId
     * @return
     */
    @Select("""
    SELECT w.wishlist_id, w.member_id, w.product_id, w.option_info, w.quantity, w.added_at,
           p.product_name, p.product_price, p.product_img
    FROM wishlist w
    JOIN products p ON w.product_id = p.product_id
    WHERE w.member_id = #{memberId}
    ORDER BY w.added_at DESC
""")
    @Results({
            @Result(column = "wishlist_id", property = "wishlistId"),
            @Result(column = "member_id", property = "memberId"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "option_info", property = "optionInfo"),
            @Result(column = "quantity", property = "quantity"),
            @Result(column = "added_at", property = "addedAt"),
            @Result(column = "product_name", property = "productName"),
            @Result(column = "product_price", property = "productPrice"),
            @Result(column = "product_img", property = "productImg")
    })
    List<Wishlist> selectListByMemberWithProduct(int memberId);

    /**
     * 선택항목 장바구니에 담기
     * @param wishlistIds
     * @return
     */
    @Select("""
    <script>
    SELECT w.wishlist_id, w.member_id, w.product_id, w.option_info, w.quantity, w.added_at,
           p.product_name, p.product_price, p.product_img
    FROM wishlist w
    JOIN products p ON w.product_id = p.product_id
    WHERE w.wishlist_id IN
    <foreach item="id" collection="list" open="(" separator="," close=")">
        #{id}
    </foreach>
    </script>
""")
    @Results({
            @Result(column = "wishlist_id", property = "wishlistId"),
            @Result(column = "member_id", property = "memberId"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "option_info", property = "optionInfo"),
            @Result(column = "quantity", property = "quantity"),
            @Result(column = "added_at", property = "addedAt"),
            @Result(column = "product_name", property = "productName"),
            @Result(column = "product_price", property = "productPrice"),
            @Result(column = "product_img", property = "productImg")
    })
    List<Wishlist> selectListByIds(List<Integer> wishlistIds);

}

