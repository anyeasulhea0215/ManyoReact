package kr.manyofactory.manyoshop.models;

import lombok.Data;

@Data
public class MemberAgreement {
    private int agreeId;     
    private int memberId;    
    private String termType;  
    private String isAgreed;  
}
