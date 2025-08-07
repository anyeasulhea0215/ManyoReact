package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    @Insert("""
        INSERT INTO basket (
            quantity, basket_add_date, product_id, member_id, basket_edit_date
        ) VALUES (
            #{quantity}, NOW(), #{productId}, #{memberId}, NOW()
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "basketId", keyColumn = "basket_id")
    int insert(Cart input);

    @Select("""
        SELECT basket_id, quantity, basket_add_date, product_id, member_id, basket_edit_date
        FROM basket
        WHERE member_id = #{memberId}
        ORDER BY basket_add_date DESC
    """)
    List<Cart> selectByMemberId(int memberId);

    @Select("""
        SELECT basket_id, quantity, basket_add_date, product_id, member_id, basket_edit_date
        FROM basket
        WHERE basket_id = #{basketId}
    """)
    Cart selectItem(int basketId);

    @Update("""
        UPDATE basket
        SET quantity = #{quantity}, basket_edit_date = NOW()
        WHERE basket_id = #{basketId}
    """)
    int updateQuantity(Cart input);

    @Delete("""
        DELETE FROM basket
        WHERE basket_id = #{basketId}
    """)
    int delete(int basketId);

    @Delete("""
        DELETE FROM basket
        WHERE member_id = #{memberId}
    """)
    int deleteByMemberId(int memberId);

    @Select("""
        SELECT b.basket_id, b.quantity, b.basket_add_date, b.product_id, b.member_id, b.basket_edit_date,
               p.product_name, p.product_img, p.sale_price, p.discount
        FROM basket b
        JOIN products p ON b.product_id = p.product_id
        WHERE b.member_id = #{memberId}
        ORDER BY b.basket_add_date DESC
    """)
    @Results(id = "CartWithProductMap", value = {
            @Result(property = "basketId", column = "basket_id"),
            @Result(property = "quantity", column = "quantity"),
            @Result(property = "basketAddDate", column = "basket_add_date"),
            @Result(property = "basketEditDate", column = "basket_edit_date"),
            @Result(property = "memberId", column = "member_id"),
            @Result(property = "productId", column = "product_id"),
            @Result(property = "product.productName", column = "product_name"),
            @Result(property = "product.productImg", column = "product_img"),
            @Result(property = "product.salePrice", column = "sale_price"),
            @Result(property = "product.discount", column = "discount")
    })
    List<Cart> selectId(int memberId);
    @Select({
            "<script>",
            "SELECT b.basket_id, b.quantity, b.basket_add_date, b.product_id, b.member_id, b.basket_edit_date,",
            "p.product_name, p.product_img, p.sale_price, p.discount",
            "FROM basket b",
            "JOIN products p ON b.product_id = p.product_id",
            "WHERE b.member_id = #{memberId} AND b.basket_id IN",
            "<foreach item='id' collection='basketIds' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    @ResultMap("CartWithProductMap")
    List<Cart> selectByIds(int memberId, List<Integer> basketIds);

    @Update("UPDATE basket SET quantity = #{quantity} WHERE basket_id = #{basketId}")
    void updateCartQuantity(Cart cart);


}
