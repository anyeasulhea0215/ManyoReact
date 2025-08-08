package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.RefundItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RefundMapper {

    @Insert("""
        INSERT INTO refund_items (
            order_item_id, payment_id, order_img,
            order_bname, order_pname, order_count,
            order_price, reg_date, edit_date
        ) VALUES (
            #{orderItemId}, #{paymentId}, #{orderImg},
            #{orderBname}, #{orderPname}, #{orderCount},
            #{orderPrice}, #{regDate}, #{editDate}
        )
    """)
    int insert(RefundItem item);

    @Select("SELECT * FROM refund_items ORDER BY refund_id DESC")
    List<RefundItem> selectAll();

    @Select("""
    SELECT r.*
    FROM refund_items r
    JOIN payment p ON r.payment_id = p.payment_id
    WHERE p.member_id = #{memberId}
    ORDER BY r.refund_id DESC
""")
List<RefundItem> getListByMemberId(@Param("memberId") int memberId);

}
