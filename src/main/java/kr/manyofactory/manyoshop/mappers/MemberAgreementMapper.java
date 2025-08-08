package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.MemberAgreement;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MemberAgreementMapper {

    /* 약관 동의 정보 저장 */
    @Insert("""
        INSERT INTO member_agreements (
            member_id, term_type, is_agreed
        ) VALUES (
            #{memberId}, #{termType}, #{isAgreed}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "agreeId", keyColumn = "agree_id")
    int insert(MemberAgreement input);

    /* 특정 회원의 특정 약관 동의 조회 */
    @Select("""
        SELECT agree_id, member_id, term_type, is_agreed
        FROM member_agreements
        WHERE member_id = #{memberId}
          AND term_type = #{termType}
    """)
    @Results(id = "agreementResultMap", value = {
            @Result(column = "agree_id", property = "agreeId"),
            @Result(column = "member_id", property = "memberId"),
            @Result(column = "term_type", property = "termType"),
            @Result(column = "is_agreed", property = "isAgreed")
    })
    MemberAgreement selectOne(MemberAgreement input);

    /* 삭제 ,회원 ID 기준 전체 삭제 */
    @Delete("""
        DELETE FROM member_agreements
        WHERE member_id = #{memberId}
    """)
    int deleteByMemberId(int memberId);
}
