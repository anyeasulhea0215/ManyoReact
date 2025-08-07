package kr.manyofactory.manyoshop.services.impl;

import kr.manyofactory.manyoshop.mappers.OrderItemMapper;
import kr.manyofactory.manyoshop.mappers.PaymentMapper;
import kr.manyofactory.manyoshop.models.Members;
import kr.manyofactory.manyoshop.models.OrderItem;
import kr.manyofactory.manyoshop.models.Payment;
import kr.manyofactory.manyoshop.models.Product;
import kr.manyofactory.manyoshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import kr.manyofactory.manyoshop.services.OrderService;
import kr.manyofactory.manyoshop.services.ProductReviewService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.mockito.ArgumentMatchers.booleanThat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderItemMapper orderItemMapper;
    private final PaymentMapper paymentMapper;
    private final ProductService productService;
    private final ProductReviewService productReviewService;


    /**
     * 회원 ID와 날짜 범위를 기준으로 주문 목록을 조회합니다.
     *
     * @param memberId 조회할 회원의 ID
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @return 주문 항목 리스트
     */
    @Override
    public List<OrderItem> getOrdersByDateRange(int memberId, LocalDate startDate,
            LocalDate endDate) {

        List<OrderItem> orderList = orderItemMapper.selectByDateRange(memberId, startDate, endDate);

        for (OrderItem item : orderList) {
            Integer productId = item.getProductId();

            if (productId != null) {

                boolean reviewed = productReviewService.hasUserReviewedProduct(memberId, productId); // 리뷰작성기록이
                                                                                                     // 있다면
                item.setReviewed(reviewed);

            } else {
                item.setReviewed(false);
            }
        }
        return orderList;

        // return orderItemMapper.selectByDateRange(memberId, startDate, endDate);
    }


    /**
     *
     * @param member
     * @param orderItems
     * @param paymentMethod
     * @param message
     * @return
     * @throws Exception
     */
    @Override
    public Payment saveOrder(Members member, List<OrderItem> orderItems, String paymentMethod,
            String message) throws Exception {
        Payment payment = new Payment();
        payment.setMemberId(member.getId());
        payment.setPaymentNumber("ORD" + System.currentTimeMillis());


        int totalAmount = orderItems.stream()
                .mapToInt(item -> item.getOrderPrice() * item.getOrderCount()).sum();

        String PaymentMethod;
        if ("card".equalsIgnoreCase(paymentMethod) || "신용카드".equals(paymentMethod)) {
            PaymentMethod = "card";
        } else if ("bank".equalsIgnoreCase(paymentMethod) || "계좌이체".equals(paymentMethod)) {
            PaymentMethod = "bank";
        } else {
            throw new IllegalArgumentException("지원하지 않는 결제 수단입니다.");
        }
        payment.setPaymentMethod(PaymentMethod);

        payment.setTotalAmount(totalAmount);
        payment.setPaymentStatus("ordered");
        payment.setAddr(member.getAddr1() + " " + member.getAddr2());
        payment.setMemberTel(member.getPhone());
        payment.setAddrMessage(message);

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        payment.setPaymentDate(now);

        paymentMapper.insert(payment);
        for (OrderItem item : orderItems) {
            Product product = productService.getProductByNameLike(item.getOrderPname());

            if (product != null) {
                item.setProductId(product.getProductId());
            } else {
                throw new Exception("상품을 찾을 수 없습니다: " + item.getOrderPname());
            }
            item.setPaymentId(payment.getPaymentId());
            item.setRegDate(LocalDate.now());
             item.setOrderStatus("결제완료");
            orderItemMapper.insert(item);
        }

        return payment;
    }

    @Override
    public OrderItem getOrderItemById(int orderItemId) {
        return orderItemMapper.selectItem(orderItemId);

    }

    @Override
    public void deleteOrderItem(int orderItemId) {
        orderItemMapper.deleteById(orderItemId);
    }

    @Override
    public void updateOrderStatus(int orderItemId, String status) {
        orderItemMapper.updateOrderStatus(orderItemId, status);
    }
      @Override
    public int countOrderStatusByMember(int memberId, String status) {
        return orderItemMapper.countOrderStatusByMember(memberId, status);
    }

    @Override
    public Map<String, Integer> getOrderStatusCounts(int memberId) {
        return orderItemMapper.countOrderStatusByMemberId(memberId);
    }



}
