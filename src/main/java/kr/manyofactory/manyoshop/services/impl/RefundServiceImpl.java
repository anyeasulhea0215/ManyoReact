package kr.manyofactory.manyoshop.services.impl;

import kr.manyofactory.manyoshop.mappers.RefundMapper;
import kr.manyofactory.manyoshop.models.RefundItem;
import kr.manyofactory.manyoshop.services.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundMapper refundMapper;

    @Override
    public void insert(RefundItem item) {
        refundMapper.insert(item);
    }

    @Override
    public List<RefundItem> getList() {
        return refundMapper.selectAll();
    }

    @Override
    public List<RefundItem> getListByMemberId(int memberId) {
        return refundMapper.getListByMemberId(memberId); 
    
}

}
