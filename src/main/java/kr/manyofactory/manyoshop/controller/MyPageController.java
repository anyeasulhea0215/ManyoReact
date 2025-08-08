package kr.manyofactory.manyoshop.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.manyofactory.manyoshop.helpers.SessionCheckHelper;
import kr.manyofactory.manyoshop.mappers.ProductMapper;
import kr.manyofactory.manyoshop.mappers.WishlistMapper;
import kr.manyofactory.manyoshop.models.*;
import kr.manyofactory.manyoshop.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.http.HttpSession;
import kr.manyofactory.manyoshop.services.OrderService;
import kr.manyofactory.manyoshop.services.ProductInquiryService;
import kr.manyofactory.manyoshop.services.ProductService;
import kr.manyofactory.manyoshop.services.RefundService;
import kr.manyofactory.manyoshop.services.ReturnService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {



    private final ProductService productService;

    private final OrderService orderService;
    private final ReturnService returnService;
    private final RefundService refundService;

    @Autowired
    private WishlistMapper wishlistMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductInquiryService productInquiryService;


    @GetMapping("/reviews")
    public String reviewPage() {
        return "mypage/reviews";
    }

    @PostMapping("/inquiry")
    public String submitInquiry(@SessionAttribute("memberInfo") Members loginMember,
            ProductInquiry input, Model model) throws Exception {
        input.setMemberId(loginMember.getId());
        input.setIsAnswered("N");
        productInquiryService.add(input);

        model.addAttribute("inquiry", input);
        model.addAttribute("userId", loginMember.getUserId());

        return "mypage/inquiry-complete";
    }

    /*
     * @GetMapping("/product-inquiries")
     *
     * @SessionCheckHelper(enable = true) // 로그인 상태에서만 접근 가능 public String inquiriespage() { return
     * "mypage/product-inquiries"; }
     */


    @GetMapping("/product-inquiries")
    @SessionCheckHelper(enable = true)
    public String productinquiriesPage(HttpSession session, Model model) {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        int memberId = loginMember.getId();


        List<Product> productList = productInquiryService.getPurchasedProductsByMemberId(memberId);

        model.addAttribute("productList", productList);
        model.addAttribute("userId", loginMember.getUserId());

        return "mypage/product-inquiries"; // HTML 파일명
    }

    @GetMapping("/product-inquiries/list")
    @SessionCheckHelper(enable = true)
    public String myProductInquiryListPage(HttpSession session, Model model) {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        List<ProductInquiry> myInquiries =
                productInquiryService.getMyInquiries(loginMember.getId());
        model.addAttribute("myInquiries", myInquiries);

        return "mypage/product-inquiry-list";
    }

    @PostMapping("/product-inquiries/delete")
    public String deleteInquiry(@RequestParam("inquiryId") int inquiryId, HttpSession session)
            throws Exception {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }
        int memberId = loginMember.getId();

        ProductInquiry inquiry = new ProductInquiry();
        inquiry.setInquiryId(inquiryId);
        ProductInquiry existing = productInquiryService.getItem(inquiry);

        if (existing == null || existing.getMemberId() != memberId) {
            return "redirect:/mypage/product-inquiries/list?error=notAuthorized";
        }

        productInquiryService.delete(inquiry);
        return "redirect:/mypage/product-inquiries/list";
    }


    @PostMapping("/product-inquiries")
    public String submitProductInquiry(@SessionAttribute("memberInfo") Members loginMember,
            ProductInquiry input, Model model) throws Exception {

        input.setMemberId(loginMember.getId());
        input.setIsAnswered("N");

        productInquiryService.add(input);

        model.addAttribute("inquiry", input);
        model.addAttribute("userId", loginMember.getUserId());

        return "mypage/inquiry-complete2";
    }

    @GetMapping("/inquiry")
    public String inquiryForm(@RequestParam(required = false) Integer productId, Model model) {
        if (productId != null) {
            Product product = productService.getProductById(productId);
            model.addAttribute("product", product);
        }
        return "mypage/inquiry";
    }



    /**
     * 페이지 조회하기
     *
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/wishlist")
    public String wishlistpage(HttpSession session, Model model) {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login/login";
        }
        List<Wishlist> wishlist = wishlistMapper.selectListByMemberWithProduct(loginMember.getId());

        model.addAttribute("wishlist", wishlist);
        return "mypage/wishlist";
    }


    /**
     * 상품 추가하기
     *
     * @param productId
     * @param session
     * @return
     */
    @PostMapping("/add")
    public String addToWishlist(@RequestParam("productId") int productId, HttpSession session) {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login/login";
        }

        Wishlist input = new Wishlist();
        input.setMemberId(loginMember.getId());
        input.setProductId(productId);
        input.setOptionInfo("기본 옵션");
        input.setQuantity(1);

        wishlistMapper.insert(input);

        return "redirect:/mypage/wishlist";
    }

    /**
     * 개별항목 삭제하기
     *
     * @param wishlistIds
     * @return
     */
    @PostMapping("/wishlist/delete")
    public String deleteSelectedWishlist(
            @RequestParam(name = "wishlistIds", required = false) List<Integer> wishlistIds) {
        if (wishlistIds != null && !wishlistIds.isEmpty()) {
            wishlistMapper.deleteByIds(wishlistIds);
        }
        return "redirect:/mypage/wishlist";
    }

    /**
     * 선택상품 장바구니
     *
     * @param wishlistIds
     * @param session
     * @return
     * @throws Exception
     */
    @PostMapping("/wishlist/add-to-cart")
    public String addSelectedWishlistToCart(
            @RequestParam(name = "wishlistIds", required = false) List<Integer> wishlistIds,
            HttpSession session) throws Exception {
        Members member = (Members) session.getAttribute("memberInfo");
        if (member == null) {
            return "redirect:/login/login";
        }

        if (wishlistIds == null || wishlistIds.isEmpty()) {
            return "redirect:/mypage/wishlist";
        }

        List<Wishlist> selectedItems = wishlistMapper.selectListByIds(wishlistIds);

        for (Wishlist item : selectedItems) {
            cartService.addOrUpdateCartItem(member.getId(), item.getProductId(),
                    item.getQuantity());
        }

        return "redirect:/cart/list";
    }

    /**
     * 전체삭제하기
     *
     * @param session
     * @return
     */
    @PostMapping("/wishlist/delete/all")
    public String deleteAllWishlist(HttpSession session) {
        Members member = (Members) session.getAttribute("memberInfo");
        if (member != null) {
            wishlistMapper.deleteAllByMemberId(member.getId());
        }
        return "redirect:/mypage/wishlist";
    }



    @GetMapping("/orders")
    @SessionCheckHelper(enable = true) // 로그인 상태에서만 접근 가능
    public String orderPage(@RequestParam(value = "period", required = false) Integer period,
            @RequestParam(value = "startDate",
                    required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate",
                    required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model, HttpSession session) {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }
        int memberId = loginMember.getId();
        if (startDate == null || endDate == null) {
            if (period == null)
                period = 7;
            endDate = LocalDate.now();
            startDate = endDate.minusDays(period);
        }

        List<OrderItem> orderList = orderService.getOrdersByDateRange(memberId, startDate, endDate);

        model.addAttribute("orderList", orderList);
        model.addAttribute("orderCount", orderList.size());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("period", period);

        return "mypage/orders";
    }


    @GetMapping("/returns")
    public String returnPage(
            @RequestParam(value = "orderItemId", required = false) Integer orderItemId,
            @RequestParam(value = "period", required = false) Integer period,
            @RequestParam(value = "startDate",
                    required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate",
                    required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model, HttpSession session) {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        int memberId = loginMember.getId();

        if (startDate == null || endDate == null) {
            if (period == null)
                period = 1;
            endDate = LocalDate.now();
            startDate = endDate.minusDays(period);
        }

        List<ReturnItem> returnList;


        if (orderItemId != null) {
            returnList = returnService.getReturnByOrderItemId(orderItemId, memberId);
        } else {
            returnList = returnService.getReturnsByDateRange(memberId, startDate, endDate);
        }
        // 중복된 상품제거
        Map<Integer, ReturnItem> uniqueMap = new HashMap<>();
        for (ReturnItem item : returnList) {
            uniqueMap.putIfAbsent(item.getOrderItemId(), item);
        }
        List<ReturnItem> uniqueReturnList = new ArrayList<>(uniqueMap.values());

        System.out.println("==== Return 조회 시작 ====");
        System.out.println("memberId: " + memberId);
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);
        System.out.println("orderItemId: " + orderItemId);
        System.out.println("returnList size: " + returnList.size());

        model.addAttribute("returnList", uniqueReturnList);
        model.addAttribute("returnCount", uniqueReturnList.size());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("period", period);
        model.addAttribute("orderItemId", orderItemId);

        return "mypage/returns";
    }

    @PostMapping("/returns")
    public String handleReturnPost(@RequestParam("orderItemId") int orderItemId,
            HttpSession session) {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        int memberId = loginMember.getId();

        // 주문 정보 가져오기
        OrderItem item = orderService.getOrderItemById(orderItemId);

        // ReturnItem 생성
        ReturnItem returnItem = new ReturnItem();
        returnItem.setOrderItemId(orderItemId);
        returnItem.setPaymentId(item.getPaymentId());
        returnItem.setOrderImg(item.getOrderImg());
        returnItem.setOrderBname(item.getOrderBname());
        returnItem.setOrderPname(item.getOrderPname());
        returnItem.setOrderCount(item.getOrderCount());
        returnItem.setOrderPrice(item.getOrderPrice());
        returnItem.setRegDate(LocalDate.now());

        // DB에 insert
        returnService.registerReturn(returnItem);

        orderService.updateOrderStatus(orderItemId, "반품요청");

        return "redirect:/mypage/returns";
    }

    @GetMapping("/refund")
    public String refundPage(Model model, HttpSession session) {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        int memberId = loginMember.getId(); // 아이디조회
        List<RefundItem> refundList = refundService.getListByMemberId(memberId);

        Map<Integer, RefundItem> uniqueMap = new HashMap<>();
        for (RefundItem item : refundList) {
            uniqueMap.putIfAbsent(item.getOrderItemId(), item);
        }
        List<RefundItem> uniqueRefundList = new ArrayList<>(uniqueMap.values());

        model.addAttribute("refundList", uniqueRefundList);
        model.addAttribute("refundCount", uniqueRefundList.size());

        return "mypage/refund";
    }

    @PostMapping("/refund-request")
    public String handleRefundRequest(@RequestParam("orderItemId") int orderItemId,
            HttpSession session) {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }
        int memberId = loginMember.getId();

        OrderItem item = orderService.getOrderItemById(orderItemId);
        if (item == null) {
            throw new IllegalArgumentException("주문 정보가 존재하지 않습니다.");
        }

        // 환불 데이터 구성
        RefundItem refundItem = new RefundItem();
        refundItem.setOrderItemId(orderItemId);
        refundItem.setPaymentId(item.getPaymentId());
        refundItem.setOrderImg(item.getOrderImg());
        refundItem.setOrderBname(item.getOrderBname());
        refundItem.setOrderPname(item.getOrderPname());
        refundItem.setOrderCount(item.getOrderCount());
        refundItem.setOrderPrice(item.getOrderPrice());
        refundItem.setRegDate(LocalDate.now());

        // 서비스 실행
        refundService.insert(refundItem);



        return "redirect:/mypage/refund";
    }


    @GetMapping("/withdraw")
    public String withdrawPage() {
        return "mypage/withdraw";
    }

    @GetMapping("/order-counts")
    @ResponseBody
    public Map<String, Integer> getOrderStatusCounts(
            @SessionAttribute("memberInfo") Members member) {
        int paymentDone = orderService.countOrderStatusByMember(member.getId(), "결제완료");
        int readyToShip = orderService.countOrderStatusByMember(member.getId(), "배송준비중");
        int shipped = orderService.countOrderStatusByMember(member.getId(), "배송완료");
        int confirmed = orderService.countOrderStatusByMember(member.getId(), "구매확정");

        Map<String, Integer> counts = new HashMap<>();
        counts.put("결제완료", paymentDone);
        counts.put("배송준비중", readyToShip);
        counts.put("배송완료", shipped);
        counts.put("구매확정", confirmed);
        return counts;
    }

    @PostMapping("/orders/confirm")

    public String confirmOrder(@RequestParam("orderItemId") int orderItemId) {
        orderService.updateOrderStatus(orderItemId, "배송완료");
        return "redirect:/mypage/orders";
    }

    @GetMapping("/orders/review-and-confirm")
    public String reviewAndConfirm(@RequestParam("orderItemId") int orderItemId,
            @RequestParam("productId") int productId,
            @RequestParam("orderPname") String orderPname) {
        orderService.updateOrderStatus(orderItemId, "구매확정");

        String encodedPname = URLEncoder.encode(orderPname, StandardCharsets.UTF_8);

        return "redirect:/reviews/register?productId=" + productId + "&orderPname=" + encodedPname;
    }

    @GetMapping("/my-page")
    @SessionCheckHelper(enable = true)
    public String showMyPage(HttpSession session, Model model) {

       Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        int memberId = loginMember.getId();
        LocalDate today = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        int period = 30;
        List<OrderItem> orderList = orderService.getOrdersByDateRange(memberId, startDate, endDate);

        model.addAttribute("orderList", orderList);
        model.addAttribute("orderCount", orderList.size());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("period", period);

        model.addAttribute("memberInfo", loginMember);


        // 오늘 날짜로만 주문내역조회
        List<OrderItem> todayOrders = orderService.getOrdersByDateRange(memberId, today, today);
        model.addAttribute("orderList", todayOrders);
        model.addAttribute("orderCount", todayOrders.size());
        model.addAttribute("startDate", today);
        model.addAttribute("endDate", today);

        return "mypage/my-page";
    }



}
