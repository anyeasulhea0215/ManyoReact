package kr.manyofactory.manyoshop.services;

import java.util.List;
import kr.manyofactory.manyoshop.models.Members;

public interface MemberService {
    /**
     * 회원 아이디가 중복되는지 확인 중복되 경우 예외를 발생시킨다
     *
     * @param input -회원정보
     * @throws Exception -중복된 아이디가 있는 경우
     */
    public void isUniqueUserId(Members input) throws Exception;

    /**
     * 회원 이메일 중복되는지 확인 중복되 경우 예외를 발생시킨다
     *
     * @param input -회원정보
     * @throws Exception -중복된 이메일 있는 경우
     */
    public void isUniqueEmail(Members input) throws Exception;

    /**
     * 회원가입을 처리한다. 입력한 회원정보를 바탕으로 회원 정보를 등록하고 등록된 회원 정보를 반환한다.
     *
     * @param input -회원정보
     * @return Member-등록된 회원정보
     * @throws Exception -예외 발생시
     */
    public Members join(Members input) throws Exception;



    /**
     * 로그인을 수행. 아이디와 비밀번호가 일치하는 회원정보를 조회하고 조회결과가 잇을 경우 마지막 로그인 시각을 업데이트한다
     *
     * @param input -회원정보
     * @return -조회된 회원 정보
     * @throws Exception -예외비ㅏㄹ생 시
     */
    public Members login(Members input) throws Exception;


    /**
     * 입력된 정보(이름,이메일)을 바탕으로 회원 아이디를 조회
     *
     * @param input -회원정보: 이름,이메일
     * @return
     * @throws Exception
     */
    public Members findId(Members input) throws Exception;


    /**
     * 입력된 회원정보를 바탕으로 비밀번호를 재설정한다 비밀번호는 해시로 암호화하옂 ㅓ장한다
     *
     * @param input -회원정보(아이디,새 비밀번호)
     * @throws Exception
     */
    public void resetPw(Members input) throws Exception;


    /**
     * 회원 탈퇴를 처리. 입력된 비밀번호가 일치하는 경우에만 탈퇴처리를 수행하며 is_out값을 'Y'로 변경하고 edit_date를 현재시간으로 설정한다
     *
     * @param input -회원정보
     * @throws Exception -예외발생시
     */
    public void out(Members input) throws Exception;

    /**
     * 탈퇴한 회원들의 정보를 조회 is_out값이 'Y'인 회우너들 중에 탈퇴일시가 현재 시각을 기준으로 1분이전인 회원들의 정보를 조회한다 실제 서비스 개발시에는 3개월에
     * 해당하는 시간을 설정하는 등 사이트 정책에 따라 달라져야 한다
     *
     * @return -List<Member>
     * @throws Exception -예외 발생 시
     */
    public List<Members> processOutMembers() throws Exception;

    /**
     *
     * @param input -수정할 회원의 정보
     * @return -수저된 회원정보
     * @throws Exception
     */
    public Members update(Members input) throws Exception;

}
