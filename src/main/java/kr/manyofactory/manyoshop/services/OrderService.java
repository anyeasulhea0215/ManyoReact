package kr.manyofactory.manyoshop.services;
import kr.manyofactory.manyoshop.models.Members;
import kr.manyofactory.manyoshop.models.OrderItem;
import kr.manyofactory.manyoshop.models.Payment;


import java.time.LocalDate;
import java.util.List;
import java.util.Map; 



public interface OrderService {
    List<OrderItem> getOrdersByDateRange(int memberId, LocalDate startDate, LocalDate endDate);

    Payment saveOrder(Members member, List<OrderItem> orderItems, String paymentMethod, String message) throws Exception;

   OrderItem getOrderItemById(int orderItemId);

   public void deleteOrderItem(int orderItemId);

   void updateOrderStatus(int orderItemId, String status);
    int countOrderStatusByMember(int memberId, String status);
   

    Map<String, Integer> getOrderStatusCounts(int memberId);


}

