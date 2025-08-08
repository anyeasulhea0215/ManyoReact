package kr.manyofactory.manyoshop.services.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import kr.manyofactory.manyoshop.exceptions.ServiceNoResultException;
import kr.manyofactory.manyoshop.mappers.MemberMapper;
import kr.manyofactory.manyoshop.models.Members;
import kr.manyofactory.manyoshop.services.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper; // 주입됨


    @Override
    public void isUniqueUserId(Members input) throws Exception {

        if (memberMapper.selectCount(input) > 0) {
            throw new ServiceNoResultException("사용할수없는 아이디");
        }

    }

    @Override
    public void isUniqueEmail(Members input) throws Exception {

        if (memberMapper.selectCount(input) > 0) {
            throw new ServiceNoResultException("사용할수없는 이메일");
        }
    }

    @Override
    public Members join(Members input) throws Exception {


        Members temp1 = new Members();
        temp1.setUserId(input.getUserId());
        this.isUniqueUserId(temp1);

        Members temp2 = new Members();
        temp2.setEmail(input.getEmail());
        this.isUniqueEmail(temp2);

        // 회원가입 수행후 결과 체크
        if (memberMapper.insert(input) == 0) {
            throw new ServiceNoResultException(" 저장된 Member객체가 없습니다");
        }
        // 가입된 회원 정보를 반환
        return memberMapper.selectItem(input);
    }

    /**
     * login service메서드
     */
    @Override
    public Members login(Members input) throws Exception {
        // 입력된 아이디와 비밀번호로 회원 정보 조회
        Members output = memberMapper.login(input);

        // 조회된 정보강 없으면 에외 발생
        if (output == null) {
            throw new ServiceNoResultException("아이디 또는 비밀번호가 잘못됨");
        }

        // 로그인 성공시 마지막 로그인 일시 업데이트
        memberMapper.updateLoginDate(output);

        return output;
    }

    @Override
    public Members findId(Members input) throws Exception {

        Members output = memberMapper.findId(input);

        if (output == null) {
            throw new Exception("조회된 아이디가 없습니다");
        }

        return output;
    }

    @Override
    public void resetPw(Members input) throws Exception {
        if (memberMapper.resetPw(input) == 0) {
            throw new Exception("아이디와 이메일을 확인하세요");
        }
    }

    @Override
    public void out(Members input) throws Exception {
        if (memberMapper.out(input) == 0) {
            throw new ServiceNoResultException("회원 탈퇴에 실패했습니다 비밀번호가 잘못되었거나 가입되어 있지 않은 회원입니다 ");
        }
    }

    @Override
    public List<Members> processOutMembers() throws Exception {

        List<Members> output = null;

        try {
            // 1.is_out이 Y인 상태로 특정 시간이 지난 데이터를 조회.
            output = memberMapper.selectOutMemberPhoto();

            memberMapper.deleteBasketByMemberId(); // 해당 회원들의 장바구니 먼저 삭제

            memberMapper.deleteOutMembers(); // 2)탈퇴 요청된 데이터를 삭제
        } catch (Exception e) {

            throw new Exception("탈퇴 처리에 실패했습니다");
        }

        return output;

    }

    @Override
    public Members update(Members input) throws Exception {
        if (memberMapper.update(input) == 0) {
            throw new Exception("현재 비밀번호를 확인하세요");
        }

        return memberMapper.selectItem(input);
    }

}
