package kr.manyofactory.manyoshop.services;

import kr.manyofactory.manyoshop.models.RefundItem;
import java.util.List;

public interface RefundService {
    void insert(RefundItem item);           // 환불 내역 저장



    List<RefundItem> getList();             // 환불 내역 전체 조회
    List<RefundItem> getListByMemberId(int memberId);
}
