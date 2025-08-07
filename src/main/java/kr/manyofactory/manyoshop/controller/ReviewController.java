package kr.manyofactory.manyoshop.controller;

import jakarta.servlet.http.HttpSession;
import kr.manyofactory.manyoshop.helpers.FileHelper;
import kr.manyofactory.manyoshop.helpers.RegexHelper;
import kr.manyofactory.manyoshop.helpers.RestHelper;
import kr.manyofactory.manyoshop.mappers.ProductReviewMapper;
import kr.manyofactory.manyoshop.models.Members;
import kr.manyofactory.manyoshop.models.Product;
import kr.manyofactory.manyoshop.models.ProductReview;
import kr.manyofactory.manyoshop.models.UploadItem;
import kr.manyofactory.manyoshop.services.ProductReviewService;
import kr.manyofactory.manyoshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${upload.url}")
    private String uploadUrl;

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private RegexHelper regexHelper;


    @Autowired
    private RestHelper restHelper;

    @Autowired
    private ProductReviewMapper productReviewMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductReviewService reviewService;

    @GetMapping("/register")
    public String showReviewForm(
            @RequestParam(value = "productId", required = false) Integer productId,
            @RequestParam(value = "orderPname", required = false) String orderPname, Model model) {
        if (productId == null && orderPname != null && !orderPname.isEmpty()) {
            Product product = productService.getProductByNameLike(orderPname);
            if (product != null) {
                productId = product.getProductId();
            }
        }

        model.addAttribute("productId", productId);
        model.addAttribute("orderPname", orderPname);
        return "reviews/review_register";
    }



    @PostMapping("")
    public String insertReview(@ModelAttribute ProductReview review,
            @RequestParam(value = "productId", required = false) Integer productId,
            @RequestParam(value = "orderPname", required = false) String orderPname,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            HttpSession session) throws IOException {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login/login";
        }
        review.setMemberId(loginMember.getId());

        if (productId != null) {
            review.setProductId(productId);
        } else if (orderPname != null && !orderPname.isEmpty()) {
            Product product = productService.getProductByNameLike(orderPname);
            if (product != null) {
                review.setProductId(product.getProductId());
            } else {

            }
        }

        if (photo != null && !photo.isEmpty()) {
            try {
                UploadItem uploadItem = fileHelper.saveMultipartFile(photo);

                review.setImageUrl(uploadItem.getFilePath());
                // review.setImageUrl(uploadUrl + uploadItem.getFilePath()); // 리뷰 사진 조회가 안되서 사진
                // 저장경로
                // 수정해봤어요 ㅠ..

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        productReviewMapper.insert(review);

        return "redirect:/reviews/mine";
    }



    @GetMapping("/mine")
    public String myReviews(HttpSession session, Model model) {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login/login";
        }
        List<ProductReview> myReviews = productReviewMapper.selectByMemberId(loginMember.getId());

        for (ProductReview review : myReviews) {
            if (review.getImageUrl() != null && !review.getImageUrl().isEmpty()) {
                review.setImageUrl(uploadUrl + review.getImageUrl());
            }
        }

        model.addAttribute("myReviews", myReviews);

        return "reviews/review_mine";
    }

    @DeleteMapping("/{reviewId}")
    public String deleteReview(@PathVariable("reviewId") int reviewId) {
        productReviewMapper.delete(reviewId);
        return "redirect:/reviews/mine";
    }

    @PostMapping("/edit/{reviewId}")
    public String editReview(@PathVariable("reviewId") int reviewId,
            @ModelAttribute ProductReview review,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            HttpSession session) throws Exception {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login/login";
        }
        Integer memberId = loginMember.getId();

        ProductReview originalReview = productReviewMapper.selectItem(new ProductReview() {
            {
                setReviewId(reviewId);
            }
        });

        if (originalReview == null || originalReview.getMemberId() != memberId) {
            return "redirect:/reviews/mine";
        }

        if (photo != null && !photo.isEmpty()) {
            if (originalReview.getImageUrl() != null && !originalReview.getImageUrl().isEmpty()) {
                fileHelper.deleteFile(originalReview.getImageUrl());
            }
            UploadItem uploadItem = fileHelper.saveMultipartFile(photo);
            review.setImageUrl(uploadItem.getFilePath());
        } else {
            review.setImageUrl(originalReview.getImageUrl());
        }

        review.setReviewId(reviewId);
        review.setMemberId(memberId);

        productReviewMapper.update(review);

        return "redirect:/reviews/mine";
    }

    @GetMapping("/edit/{reviewId}")
    public String showEditForm(@PathVariable("reviewId") int reviewId, Model model,
            HttpSession session) {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login/login";
        }
        Integer memberId = loginMember.getId();

        ProductReview review = productReviewMapper.selectItem(new ProductReview() {
            {
                setReviewId(reviewId);
            }
        });

        if (review == null || review.getMemberId() != memberId) {
            return "redirect:/reviews/mine";
        }

        if (review.getImageUrl() != null && !review.getImageUrl().isEmpty()) {
            review.setImageUrl(uploadUrl + review.getImageUrl());
        }

        model.addAttribute("review", review);
        return "reviews/review_edit";
    }



}
