package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.ReturnItem;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.*;

@Mapper
public interface ReturnMapper {

    @Insert("""
                INSERT INTO return_items (
                    order_item_id,
                    payment_id,
                    order_img,
                    order_bname,
                    order_pname,
                    order_count,
                    order_price,
                    reg_date,
                    edit_date
                ) VALUES (
                    #{orderItemId},
                    #{paymentId},
                    #{orderImg},
                    #{orderBname},
                    #{orderPname},
                    #{orderCount},
                    #{orderPrice},
                    #{regDate},
                    #{editDate}
                )
            """)
    void insertReturn(ReturnItem item);

    @Select("""
                 SELECT * FROM return_items r
            JOIN payment p ON r.payment_id = p.payment_id
            LEFT JOIN refund_items f ON r.order_item_id = f.order_item_id
            WHERE p.member_id = #{memberId}
              AND r.reg_date BETWEEN #{startDate} AND #{endDate}
              AND f.order_item_id IS NULL
            ORDER BY r.reg_date DESC
            """)
    @Results(id = "returnItemResultMap",
            value = {@Result(column = "order_item_id", property = "orderItemId"),
                    @Result(column = "payment_id", property = "paymentId"),
                    @Result(column = "order_img", property = "orderImg"),
                    @Result(column = "order_bname", property = "orderBname"),
                    @Result(column = "order_pname", property = "orderPname"),
                    @Result(column = "order_count", property = "orderCount"),
                    @Result(column = "order_price", property = "orderPrice"),
                    @Result(column = "reg_date", property = "regDate"),
                    @Result(column = "edit_date", property = "editDate")})
    List<ReturnItem> selectReturnsByDateRange(@Param("memberId") int memberId,
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Select("""
                SELECT * FROM return_items
                JOIN payment p ON return_items.payment_id = p.payment_id
                WHERE order_item_id = #{orderItemId}
                  AND p.member_id = #{memberId}
            """)
    @ResultMap("returnItemResultMap")
    List<ReturnItem> selectByOrderItemId(@Param("orderItemId") int orderItemId,
            @Param("memberId") int memberId);
}
