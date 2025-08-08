package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.OrderItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderItemMapper {


    /*
     * 주문 항목 등록
     */
    @Insert("""
                INSERT INTO order_items (
                    payment_id, order_img, order_bname, order_pname,
                    order_count, order_price, reg_date, product_id, order_status
                ) VALUES (
                    #{paymentId}, #{orderImg}, #{orderBname}, #{orderPname},
                    #{orderCount}, #{orderPrice}, #{regDate}, #{productId}, #{orderStatus}
                )
            """)
    int insert(OrderItem input);



    @Select("SELECT * FROM order_items WHERE order_item_id = #{orderItemId}")
    @ResultMap("orderItemResultMap")
    OrderItem selectItem(int orderItemId);


    @Select("""
            <script>
            SELECT COUNT(*) FROM order_items
            <where>
                <if test='paymentId != null'> AND payment_id = #{paymentId} </if>
                <if test='orderPname != null and orderPname != ""'> AND order_pname = #{orderPname} </if>
            </where>
            </script>
             """)
    int selectCount(OrderItem input);
    // 삭제 delete

    @Delete("DELETE FROM order_items WHERE order_item_id = #{orderItemId}")
    void deleteById(int orderItemId);

    @Select("""
                SELECT
                    oi.order_item_id, oi.payment_id, oi.order_img, oi.order_bname,
                    oi.order_pname, oi.order_count, oi.order_price,
                    oi.reg_date, oi.edit_date, oi.product_id, oi.order_status
                FROM order_items oi
                JOIN payment p ON oi.payment_id = p.payment_id
                WHERE p.member_id = #{memberId}
                ORDER BY oi.order_item_id DESC
            """)
    @Results(id = "orderItemResultMap",
            value = {@Result(column = "order_item_id", property = "orderItemId"),
                    @Result(column = "payment_id", property = "paymentId"),
                    @Result(column = "order_img", property = "orderImg"),
                    @Result(column = "order_bname", property = "orderBname"),
                    @Result(column = "order_pname", property = "orderPname"),
                    @Result(column = "order_count", property = "orderCount"),
                    @Result(column = "order_price", property = "orderPrice"),
                    @Result(column = "reg_date", property = "regDate"),
                    @Result(column = "edit_date", property = "editDate"),
                    @Result(column = "product_id", property = "productId"),
                    @Result(column = "order_status", property = "orderStatus")})
    List<OrderItem> selectByMemberId(@Param("memberId") int memberId);


    // 상품목록 조회할떄 return_items에 잇는 아이디는 빼고 조회되게 햇어요
    @Select("""
                SELECT oi.order_item_id, oi.payment_id, oi.order_img, oi.order_bname,
                       oi.order_pname, oi.order_count, oi.order_price,
                       oi.reg_date, oi.edit_date, oi.product_id, oi.order_status
                FROM order_items oi
                JOIN payment p ON oi.payment_id = p.payment_id
                WHERE p.member_id = #{memberId}
                AND oi.reg_date BETWEEN #{startDate} AND #{endDate}
                AND oi.order_item_id NOT IN (SELECT order_item_id FROM return_items)
                ORDER BY oi.order_item_id DESC
            """)
    @ResultMap("orderItemResultMap")
    List<OrderItem> selectByDateRange(@Param("memberId") int memberId,
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Update("""
                UPDATE order_items
                SET order_status = #{status}
                WHERE order_item_id = #{orderItemId}
            """)
    void updateOrderStatus(@Param("orderItemId") int orderItemId, @Param("status") String status);


    @Select("""
    SELECT COUNT(*) FROM order_items
    WHERE payment_id IN (
        SELECT payment_id FROM payment WHERE member_id = #{memberId}
    )
    AND order_status = #{status}
""")
        int countOrderStatusByMember(@Param("memberId") int memberId, @Param("status") String status);


        @Select("""
            SELECT order_status, COUNT(*) as count
            FROM order_items oi
            JOIN payment p ON oi.payment_id = p.payment_id
            WHERE p.member_id = #{memberId}
            AND oi.reg_date >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
            GROUP BY order_status
        """)
        @MapKey("order_status")
        Map<String, Integer> countOrderStatusByMemberId(@Param("memberId") int memberId);

}
