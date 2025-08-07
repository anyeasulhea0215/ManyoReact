package kr.manyofactory.manyoshop.controller;

import jakarta.servlet.http.HttpSession;
import kr.manyofactory.manyoshop.helpers.PageHelper;
import kr.manyofactory.manyoshop.mappers.ProductReviewMapper;
import kr.manyofactory.manyoshop.models.*;
import kr.manyofactory.manyoshop.services.BestProductService;
import kr.manyofactory.manyoshop.services.ProductInquiryService;
import kr.manyofactory.manyoshop.services.ProductReviewService;
import kr.manyofactory.manyoshop.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${upload.url}")
    private String uploadUrl;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductReviewMapper productReviewMapper;


    @Autowired
    private ProductReviewService reviewService;

    @Autowired
    private ProductInquiryService productInquiryService;


    @Autowired
    private BestProductService bestProductService;

    @GetMapping("")
    public String showAllProducts(@RequestParam(defaultValue = "") String keyword,

            HttpSession session, @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) Integer categoryId, Model model) {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        int memberId = loginMember.getId();
        /* ========================================== */

        int listCount = 20;
        int totalCount;
        List<Product> products;
        if (categoryId != null) {
            totalCount = productService.getProductCount(keyword, categoryId);
            products = productService.getProducts(keyword, categoryId, (page - 1) * listCount,
                    listCount);
        } else {
            totalCount = productService.getProductCount(keyword);
            products = productService.getProducts(keyword, (page - 1) * listCount, listCount);
        }

        PageHelper pageHelper = new PageHelper(page, totalCount, listCount, 5);


        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("pageHelper", pageHelper);

        model.addAttribute("memberInfo", loginMember);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d(EEE)", Locale.KOREAN);
        model.addAttribute("tomorrowDate", tomorrow.format(formatter));

        return "manyo/all";
    }

    @GetMapping("/detail")
    public String showProductDetail(@RequestParam int productId,
            @RequestParam(value = "tab", required = false, defaultValue = "detail") String tab,
            @RequestParam(value = "inquiryId", required = false) Integer inquiryId,
            HttpSession session, Model model) {
        Product product = bestProductService.getProductById(productId);

        model.addAttribute("products", product);
        model.addAttribute("activeTab", tab);

        if ("inquiry".equals(tab)) {
            Members loginMember = (Members) session.getAttribute("memberInfo");
            if (loginMember == null) {
                return "redirect:/login";
            }
            int memberId = loginMember.getId();

            List<ProductInquiry> inquiries =
                    productInquiryService.getInquiriesByProductIdAndMemberId(productId, memberId);
            model.addAttribute("inquiries", inquiries);
        }

        if (inquiryId != null) {
            model.addAttribute("highlightInquiryId", inquiryId);
        }

        return "products/detail";
    }


    @PostMapping("/review-like")
    public String likeReview(@RequestParam int reviewId,
            @SessionAttribute(value = "memberInfo", required = false) Members member) {
        if (member == null) {
            return "redirect:/login";
        }

        reviewService.addLike(member.getId(), reviewId);
        return "redirect:/products/product-review?productId="
                + reviewService.getProductIdByReview(reviewId);
    }


    @GetMapping("/product-review")
    public String viewProductReviews(@RequestParam("productId") int productId, Model model) {

        List<ProductReview> reviews = reviewService.getReviewsByProductId(productId); // 리뷰 목록 가져오기

        for (ProductReview review : reviews) {
            if (review.getImageUrl() != null && !review.getImageUrl().isEmpty()) {
                review.setImageUrl(uploadUrl + review.getImageUrl());
            }
            int likeCount = reviewService.getLikeCount(review.getReviewId());
            review.setLikeCount(likeCount);
        }


        double avgRating = reviewService.getAverageRating(productId); // 평균 별점 계산

        Map<Integer, Integer> ratingCountMap = reviewService.getRatingDistribution(productId); // 별점분포
        // 가져오기

        Product product = productService.getProductById(productId);


        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("ratingCountMap", ratingCountMap);
        model.addAttribute("activeTab", "review");

        return "products/product-review";
    }

    @PostMapping("/review-comment")
    public String postComment(@RequestParam int reviewId, @RequestParam String content,
            @SessionAttribute(value = "memberInfo", required = false) Members member) {
        if (member == null) {
            return "redirect:/login";
        }

        ReviewComment comment = new ReviewComment();
        comment.setReviewId(reviewId);
        comment.setMemberId(member.getId());
        comment.setContent(content);

        reviewService.addComment(comment);
        return "redirect:/products/product-review?productId="
                + reviewService.getProductIdByReview(reviewId);
    }

    @GetMapping("/product-inquiry")
    public String productInquiryTab(
            @RequestParam(value = "productId", required = false) Integer productId,
            HttpSession session, Model model) {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        int memberId = loginMember.getId();

        List<ProductInquiry> inquiries;
        if (productId != null) {
            inquiries =
                    productInquiryService.getInquiriesByProductIdAndMemberId(productId, memberId);
            Product product = productService.getProductById(productId);
            model.addAttribute("product", product);
        } else {
            inquiries = productInquiryService.getMyInquiries(memberId);
        }

        model.addAttribute("myInquiries", inquiries);
        model.addAttribute("productId", productId);
        model.addAttribute("activeTab", "inquiry");

        return "products/product-inquiry";
    }


    @GetMapping("/product-inquiry-write")
    public String showInquiryForm(@RequestParam("productId") int productId, HttpSession session,
            Model model) {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        Product product = productService.getProductById(productId);


        ProductInquiry emptyInquiry = new ProductInquiry();
        emptyInquiry.setProductId(productId);

        model.addAttribute("product", product);
        model.addAttribute("productId", productId);
        model.addAttribute("inquiry", emptyInquiry);

        return "products/product-inquiry-write";
    }

    @PostMapping("/product-inquiry/save")
    public String saveInquiry(@ModelAttribute ProductInquiry inquiry, HttpSession session)
            throws Exception {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }


        inquiry.setMemberId(loginMember.getId());
        inquiry.setIsAnswered("N");
        inquiry.setRegDate(LocalDateTime.now());

        productInquiryService.add(inquiry);

        return "redirect:/products/product-inquiry?productId=" + inquiry.getProductId();
    }

    @GetMapping("/product-inquiry-detail")
    public String showInquiryDetail(@RequestParam int inquiryId,
            @RequestParam(required = false) Integer productId, HttpSession session, Model model)
            throws Exception {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        ProductInquiry inquiry = productInquiryService.getInquiryById(inquiryId);
        if (inquiry == null || inquiry.getMemberId() != loginMember.getId()) {
            return "redirect:/products/product-inquiry?productId=" + productId;
        }

        Product product = null;
        if (productId != null) {
            product = productService.getProductById(productId);
        }

        model.addAttribute("inquiry", inquiry);
        model.addAttribute("product", product);
        model.addAttribute("productId", productId);
        model.addAttribute("activeTab", "inquiry");

        return "products/product-inquiry-detail";
    }

    @PostMapping("/product-inquiry-answer")
    public String answerInquiry(@RequestParam int inquiryId, @RequestParam String answer,
            HttpSession session) throws Exception {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        ProductInquiry inquiry = productInquiryService.getInquiryById(inquiryId);
        if (inquiry == null) {
            return "redirect:/products/product-inquiry";
        }
        inquiry.setAnswer(answer);
        inquiry.setIsAnswered("Y");

        productInquiryService.update(inquiry);

        return "redirect:/products/product-inquiry-detail?inquiryId=" + inquiryId;
    }

    @PostMapping("/product-inquiry/delete")
    public String deleteInquiry(@RequestParam int inquiryId, @RequestParam int productId,
            HttpSession session) throws Exception {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        ProductInquiry inquiry = productInquiryService.getInquiryById(inquiryId);
        if (inquiry != null && inquiry.getMemberId() == loginMember.getId()) {
            productInquiryService.delete(inquiry);
        }
        return "redirect:/products/product-inquiry?productId=" + productId;
    }
}
