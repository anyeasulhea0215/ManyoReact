package kr.manyofactory.manyoshop.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import kr.manyofactory.manyoshop.models.Payment;

@Mapper
public interface PaymentMapper {

    /*
     * 결재건 생성
     */
    @Insert("""
        INSERT INTO payment (
            member_id, payment_number, payment_date, payment_status,
            total_amount, payment_method, paid_at, canceled_at, reg_date,
            addr, member_tel, addr_message
        ) VALUES (
            #{memberId}, #{paymentNumber}, #{paymentDate}, #{paymentStatus},
            #{totalAmount}, #{paymentMethod}, #{paidAt}, #{canceledAt}, NOW(),
            #{addr}, #{memberTel}, #{addrMessage}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "paymentId", keyColumn = "payment_id")
    int insert(Payment input);


    /*
     * 결제 단일 조회
     */
    @Select("""
                SELECT id, payment_id, member_id, payment_number, payment_date, payment_status,
                       total_amount, payment_method, paid_at, canceled_at, reg_date,
                       addr, member_tel, addr_message
                FROM payment
                WHERE id = #{id}
            """)
    @Results(id = "paymentResultMap",
            value = {@Result(column = "payment_id", property = "paymentId"),
                    @Result(column = "member_id", property = "memberId"),
                    @Result(column = "payment_number", property = "paymentNumber"),
                    @Result(column = "payment_date", property = "paymentDate"),
                    @Result(column = "payment_status", property = "paymentStatus"),
                    @Result(column = "total_amount", property = "totalAmount"),
                    @Result(column = "payment_method", property = "paymentMethod"),
                    @Result(column = "paid_at", property = "paidAt"),
                    @Result(column = "canceled_at", property = "canceledAt"),
                    @Result(column = "reg_date", property = "regDate"),
                    @Result(column = "addr", property = "addr"),
                    @Result(column = "member_tel", property = "memberTel"),
                    @Result(column = "addr_message", property = "addrMessage")})
    Payment selectItem(Payment input);

    // 결재 건수
    @Select("""
                <script>
                SELECT COUNT(*) FROM payment
                <where>
                    <if test='memberId != null'> AND member_id = #{memberId} </if>
                    <if test='paymentStatus != null'> AND payment_status = #{paymentStatus} </if>
                </where>
                </script>
            """)
    int selectCount(Payment input);

}
