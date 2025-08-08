package kr.manyofactory.manyoshop.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import kr.manyofactory.manyoshop.mappers.ReturnMapper;
import kr.manyofactory.manyoshop.models.ReturnItem;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnService {

    private final ReturnMapper returnMapper;
    

    public void registerReturn(ReturnItem item) {
        returnMapper.insertReturn(item);
    }
     public List<ReturnItem> getReturnsByDateRange(int memberId, LocalDate startDate, LocalDate endDate) {
         return returnMapper.selectReturnsByDateRange(memberId, startDate, endDate);
    }
    public List<ReturnItem> getReturnByOrderItemId(int orderItemId, int memberId) {
         System.out.println(">>> [ReturnService] orderItemId: " + orderItemId + ", memberId: " + memberId);
    return returnMapper.selectByOrderItemId(orderItemId, memberId);
}




}
