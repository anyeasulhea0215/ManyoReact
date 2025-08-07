package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.Members;
import java.util.List;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MemberMapper {

    /**
     * 회원 등록
     */
    @Insert("""
            INSERT INTO members (
                user_id, user_pw, user_name, email, is_email_agree,
                tel, is_sms_agree, postcode, addr1, addr2,
                gender, birthday, login_date, join_date, edit_date, is_out,
                photo
            ) VALUES (
                #{userId}, MD5(#{userPw}), #{userName}, #{email}, #{isEmailAgree},
                #{phone}, #{isSmsAgree}, #{postcode}, #{addr1}, #{addr2},
                #{gender}, #{birthday}, NULL, NOW(), NOW(), #{isOut},
                #{photo}
            )
             """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int insert(Members input);

    /**
     * 회원 수 조회 (조건부)
     */
    @Select("""
                <script>
                SELECT COUNT(*) FROM members
                <where>
                    <if test='userId != null and userId != ""'> AND user_id = #{userId} </if>
                    <if test='email != null and email != ""'> AND email = #{email} </if>
                    <if test='id != 0'> AND id != #{id} </if>
                </where>
                </script>
            """)
    public int selectCount(Members input);

    /**
     * 회원 상세 조회 (by ID)
     */
    @Select("""
                SELECT id, user_id, user_name, email, is_email_agree,
                       tel, user_pw, is_sms_agree, postcode, addr1, addr2,
                       gender, birthday, login_date, join_date, edit_date, is_out,photo
                FROM members
                WHERE id = #{id}
            """)
    @Results(id = "resultMap",
            value = {@Result(column = "user_id", property = "userId"),
                    @Result(column = "user_name", property = "userName"),
                    @Result(column = "email", property = "email"),
                    @Result(column = "is_email_agree", property = "isEmailAgree"),
                    @Result(column = "tel", property = "phone"),
                    @Result(column = "user_pw", property = "userPw"),
                    @Result(column = "is_sms_agree", property = "isSmsAgree"),
                    @Result(column = "postcode", property = "postcode"),
                    @Result(column = "addr1", property = "addr1"),
                    @Result(column = "addr2", property = "addr2"),
                    @Result(column = "gender", property = "gender"),
                    @Result(column = "birthday", property = "birthday"),
                    @Result(column = "login_date", property = "loginDate"),
                    @Result(column = "join_date", property = "joinDate"),
                    @Result(column = "edit_date", property = "editDate"),
                    @Result(column = "is_out", property = "isOut"),
                    @Result(column = "photo", property = "photo")})
    public Members selectItem(Members input);



    @Select("SELECT "
            + "id,user_id, user_pw, user_name, email, tel, birthday, gender, postcode, addr1, addr2, is_out, login_date, join_date, edit_date,photo "
            + "FROM members "
            + " WHERE user_id=#{userId} AND user_pw=MD5(#{userPw}) AND is_out='N' ")
    // 조회 결과를 resultmap이라는 이름의 Member갹채로 매핑한다
    @ResultMap("resultMap")
    public Members login(Members input);


    @Update("UPDATE members SET login_date=NOW() WHERE id=#{id} AND is_out='N' ")
    public int updateLoginDate(Members input);


    /**
     * 이름과 이메일 주소가 일치하는 회원아이디를 검색한다
     *
     * @param input -회원정보
     * @return Member -조회된 회원 아이디를 포함는 객체
     */
    @Select("SELECT user_id FROM members WHERE user_name = #{userName} AND email = #{email} AND is_out='N' ")
    @ResultMap("resultMap")
    public Members findId(Members input);

    /**
     * 아이디와 이메일 주소가 일치하는 회원의 비밀번호를 초기화한다
     *
     * @param input -회원정보
     * @return -수정된 행의 수
     */
    @Update("UPDATE members SET user_pw=MD5(#{userPw}) WHERE user_id=#{userId} AND email=#{email} AND is_out='N' ")
    public int resetPw(Members input);


    /**
     * 회원 정보를 수정한다. 회원정보 수정시 입력한 비밀번호는 MD5 해시로 암호화하여 저장한다. 신규 비밀번호가 입력된 경우에만 비밀번호를 수정한다.
     *
     * @param input - 회원 정보
     * @return int - 수정된 행의 수
     */
    @Update("<script>" + "UPDATE members SET "
    // 아이디는 수정하지 않는다.
            + "user_name = #{userName},"
            // 신규 비밀번호가 입력 된 경우만 UPDATE절에 추가함
            + "<if test='newUserPw != null and newUserPw != \"\"'>user_pw = MD5(#{newUserPw}),</if>"
            + "email = #{email}," + " is_sms_agree=#{isSmsAgree}, " + "tel = #{phone},"
            + "birthday = #{birthday}," + "gender = #{gender}," + "postcode = #{postcode},"
            + "addr1 = #{addr1}," + "addr2 = #{addr2}," + "photo = #{photo}," + "edit_date = NOW()"
            // 세션의 일련번호와 입력한 비밀번호가 일치할 경우만 수정
            + "WHERE id = #{id} AND user_pw = MD5(#{userPw})" + "</script>")
    public int update(Members input);


    /**
     * 탈퇴한 회원정보의 is_out 수정
     *
     * @param input
     * @return
     */
    @Update("UPDATE members SET is_out='Y',edit_date=NOW() WHERE id=#{id} AND user_pw=MD5(#{userPw}) AND is_out='N'")
    public int out(Members input);

    /**
     * 탈퇴한 회원의 프로필 사진을 조회한다 탈퇴한 회원 중에는 edit_date가 현재 시간으로부터 1분 이전인 회원의 사진만 조회한다
     *
     * @return -List <Member>
     */
    @Select("SELECT photo FROM members WHERE is_out='Y' AND edit_date<DATE_ADD(NOW(),interval -1 minute) AND photo IS NOT NULL")
    @ResultMap("resultMap")
    public List<Members> selectOutMemberPhoto();

    /**
     * is_out ='Y'인 회원 삭제
     *
     * @return
     */
    @Delete("DELETE  FROM members WHERE is_out='Y' AND edit_date<DATE_ADD(NOW(),interval -1 minute)")
    public int deleteOutMembers();

    /**
     * 삭제하려는 회원의 장바구니 삭제
     *
     * @param input
     * @return
     */
    @Delete("""
            DELETE FROM basket
            WHERE member_id IN (
                SELECT id FROM members
                WHERE is_out = 'Y'
            )
            """)
    public int deleteBasketByMemberId();



}
