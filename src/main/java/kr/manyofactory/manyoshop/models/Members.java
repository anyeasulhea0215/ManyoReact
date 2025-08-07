package kr.manyofactory.manyoshop.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Members implements Serializable {
    private int id;
    private String userId;
    private String userName;
    private String email;
    private String isEmailAgree;
    private String phone;
    private String userPw;
    private String isSmsAgree;
    private String postcode;
    private String addr1;
    private String addr2;
    private String gender; // 성별 남:M 여:F
    private String birthday;
    private LocalDateTime loginDate;
    private LocalDateTime joinDate;
    private LocalDateTime editDate;

    private String isOut; // 탈퇴여부 enum

    private String photo;

    private String newUserPw; // 회원정보 수정에서 사용할 신규 비밀번호

    @Getter
    @Setter
    private static int listCount = 0;

    @Getter
    @Setter
    private static int offset = 0;
}
