package kr.manyofactory.manyoshop.services;

import org.springframework.stereotype.Service;
import kr.manyofactory.manyoshop.mappers.MemberAgreementMapper;
import kr.manyofactory.manyoshop.models.MemberAgreement;
import lombok.RequiredArgsConstructor;

/**
 * 회원가입 약관 동의 mapper
 */
@Service
@RequiredArgsConstructor
public class MemberAgreementService {

    private final MemberAgreementMapper memberAgreementMapper;

    public void saveAgreemenet(int memberId, String termType, String isAgreed) {
        MemberAgreement agreement = new MemberAgreement();
        agreement.setMemberId(memberId);
        agreement.setTermType(termType);
        agreement.setIsAgreed(isAgreed);

        memberAgreementMapper.insert(agreement);
    }
}
