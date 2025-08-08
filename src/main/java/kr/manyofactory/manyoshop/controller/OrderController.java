package kr.manyofactory.manyoshop.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kr.manyofactory.manyoshop.models.*;
import kr.manyofactory.manyoshop.services.BestProductService;
import kr.manyofactory.manyoshop.services.CartService;
import kr.manyofactory.manyoshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kr.manyofactory.manyoshop.mappers.OrderItemMapper;
import kr.manyofactory.manyoshop.mappers.ProductMapper;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private BestProductService bestProductService;

    @Autowired
    private ProductMapper productMapper;


    @GetMapping("/cart")
    public String cartPage(HttpSession session, Model model) throws Exception {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login/login";
        }
        Integer memberId = loginMember.getId();

        List<Cart> items = cartService.getCartItems(memberId);
        int totalPrice = cartService.calculateTotalPrice(items);
        int deliveryFee = (totalPrice >= 30000) ? 0 : 3000;

        model.addAttribute("items", items);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("deliveryFee", deliveryFee);

        return "orders/cart";
    }


    /* 주문 목록 및 배송 조회 페이지 */
    // 중복된 두 개의 @GetMapping("") 메서드를 하나로 합쳤습니다.
    @GetMapping("")
    public String orderList(@RequestParam(name = "period", required = false) Integer period,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpSession session, Model model) {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login/login";
        }
        Integer memberId = loginMember.getId();


        List<OrderItem> orderList = orderItemMapper.selectByMemberId(memberId);

        if (startDate == null || endDate == null) {
            if (period == null) {
                period = 7;
            }
            endDate = LocalDate.now();
            startDate = endDate.minusDays(period);
        }

        List<OrderItem> filteredList = new ArrayList<>();
        for (OrderItem item : orderList) {
            if (!item.getRegDate().isBefore(startDate) && !item.getRegDate().isAfter(endDate)) {
                filteredList.add(item);
            }
        }

        orderList = filteredList;


        model.addAttribute("orderList", orderList);
        model.addAttribute("orderCount", orderList.size());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "mypage/orders";
    }

    @GetMapping("/order")
    public String orderPage(@RequestParam(name = "productId", required = false) Integer productId,
            @RequestParam(name = "quantity", required = false, defaultValue = "1") int quantity,
            HttpSession session, Model model) throws Exception {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login/login";
        }
        Integer memberId = loginMember.getId();

        List<Cart> items = new ArrayList<>();

        if (productId != null) {
             System.out.println("바로구매 productId: " + productId); 
            Product product = productMapper.selectProductById(productId);
            
            if (product == null) {
                return "redirect:/error?msg=상품을 찾을 수 없습니다";
            }

            Cart tempCart = new Cart();
            tempCart.setProduct(product);
            tempCart.setQuantity(quantity);
            tempCart.setProductId(productId);
            items.add(tempCart);

            model.addAttribute("product", product);
        }
        else {
            items = cartService.getCartItems(memberId);
        }

        int totalPrice = cartService.calculateTotalPrice(items);
        int deliveryFee = (totalPrice >= 30000) ? 0 : 3000;
        int finalAmount = totalPrice + deliveryFee;
        model.addAttribute("productId", productId);
        model.addAttribute("quantity", quantity);
        model.addAttribute("basketList", items);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("deliveryFee", deliveryFee);
        model.addAttribute("finalPrice", finalAmount);
        model.addAttribute("member", loginMember);

        return "orders/order";
    }


    @PostMapping("/order-ok")
    public String processOrderOk(@RequestParam("paymentMethod") String paymentMethod,
            @RequestParam("message") String message,
            @RequestParam(name = "basketIds[]", required = false) List<Integer> basketIds,
            @RequestParam(value = "productId", required = false) Integer productId,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
            HttpSession session, Model model) throws Exception {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login/login";
        }
        Integer memberId = loginMember.getId();
        List<Cart> selectedCarts = new ArrayList<>();


        if (basketIds != null && !basketIds.isEmpty() && basketIds.stream().allMatch(id -> id != null && id > 0)) {
    // 장바구니 주문 처리

            selectedCarts = cartService.getCartItemsByBasketIds(memberId, basketIds);
        } else if (productId != null) {
            // 바로구매 처리
            Product product = productMapper.selectProductById(productId);

            if (product == null) {
                return "redirect:/orders/order?error=productNotFound";
            }

            Cart tempCart = new Cart();
            tempCart.setProduct(product);
            tempCart.setQuantity(quantity);
            tempCart.setProductId(productId);
            selectedCarts = List.of(tempCart);
        } else {
            return "redirect:/orders/order";
        }


        List<OrderItem> orderItems = selectedCarts.stream().map(cart -> {
            OrderItem oi = new OrderItem();
            oi.setOrderCount(cart.getQuantity());
            oi.setOrderPrice(cart.getProduct().getSalePrice());
            oi.setOrderPname(cart.getProduct().getProductName());
            oi.setOrderImg(cart.getProduct().getProductImg());
            oi.setOrderBname(cart.getProduct().getProductName());
            oi.setProductId(cart.getProduct().getProductId());
            oi.setRegDate(LocalDate.now());
            return oi;
        }).toList();

        int totalPrice = cartService.calculateTotalPrice(selectedCarts);
        int deliveryFee = (totalPrice >= 30000) ? 0 : 3000;
        int finalAmount = totalPrice + deliveryFee;
        Payment payment = orderService.saveOrder(loginMember, orderItems, paymentMethod, message);
        LocalDate deadlineDate = LocalDate.now().plusDays(2);
        String deadline = deadlineDate.toString();
        model.addAttribute("userName", loginMember.getUserName());
        model.addAttribute("name", loginMember.getUserName());
        model.addAttribute("orderNumber", payment.getPaymentNumber());
        model.addAttribute("phone", payment.getMemberTel());
        model.addAttribute("address", payment.getAddr());
        model.addAttribute("message", message);
        model.addAttribute("bank", "국민은행");
        model.addAttribute("accountNumber", "123456-78-90123");
        model.addAttribute("accountHolder", "마녀공장");
        model.addAttribute("deadline", deadline);
        model.addAttribute("paymentAmount", finalAmount);
        model.addAttribute("deliveryFee", deliveryFee);
        model.addAttribute("member", loginMember);
        model.addAttribute("totalPrice", totalPrice);


        if (basketIds != null) {
            for (Integer basketId : basketIds) {
                cartService.deleteCartItem(basketId);
            }
        }

        return "orders/order-ok";
    }


    // 장바구니 수량 업데이트
    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam("basketId") int basketId,
            @RequestParam("action") String action) {
        cartService.updateCartQuantity(basketId, action);
        return "redirect:/orders/cart";
    }

    // 장바구니 삭제
    @PostMapping("/cart/delete")
    public String deleteCartItem(@RequestParam("basketId") int basketId) throws Exception {
        cartService.deleteCartItem(basketId);
        return "redirect:/orders/cart";
    }

    

}
