package kr.manyofactory.manyoshop.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import kr.manyofactory.manyoshop.models.ReviewComment;

import kr.manyofactory.manyoshop.mappers.LikesMapper;
import kr.manyofactory.manyoshop.mappers.ReviewCommentMapper;
import kr.manyofactory.manyoshop.models.Likes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.manyofactory.manyoshop.mappers.ProductReviewMapper;
import kr.manyofactory.manyoshop.models.ProductReview;
import kr.manyofactory.manyoshop.services.ProductReviewService;

@Service
public class ProductReviewSerciceImpl implements ProductReviewService {

    @Autowired
    private ProductReviewMapper productReviewMapper;

    @Autowired
    private LikesMapper likesMapper;

    @Autowired
    private ReviewCommentMapper reviewCommentMapper;

    /**
     * 상품 아이디 별로 리뷰 목록 가져오기
     */
    @Override
    public List<ProductReview> getReviewsByProductId(int productId) {
        List<ProductReview> reviews = productReviewMapper.selectByProductId(productId);

        for (ProductReview review : reviews) {
            List<ReviewComment> comments = reviewCommentMapper.selectByReviewId(review.getReviewId());
            review.setComments(comments);

            int likeCount = likesMapper.countByReviewId(review.getReviewId());
            review.setLikeCount(likeCount);
        }

        return reviews;
    }


    /**
     * 평균 별점 계산 메서드
     */
    @Override
    public double getAverageRating(int productId) {

        List<ProductReview> reviews = productReviewMapper.selectByProductId(productId);
        if (reviews.isEmpty())
            return 0.0; // 리뷰가 없다면 0.0

        int total = reviews.stream().mapToInt(ProductReview::getRating).sum();
        return (double) total / reviews.size();
    }

    /**
     * 별점 분포를 계산하는 메서드
     */
    @Override
    public Map<Integer, Integer> getRatingDistribution(int productId) {

        List<ProductReview> reviews = productReviewMapper.selectByProductId(productId);
        Map<Integer, Integer> result = new TreeMap<>(Collections.reverseOrder());

        for (int i = 1; i <= 5; i++) {
            result.put(i, 0);
        }

        for (ProductReview review : reviews) {
            result.put(review.getRating(), result.get(review.getRating()) + 1);
        }

        return result;
    }

    @Override
    public boolean hasUserReviewedProduct(int memberId, int productId) {
        return productReviewMapper.existsByMemberIdAndProductId(memberId, productId);
    }

    @Override
    public void addLike(int memberId, int reviewId) {
        Likes like = new Likes();
        like.setReviewId(reviewId);
        likesMapper.insert(like);
    }

    @Override
    public int getLikeCount(int reviewId) {
        return likesMapper.countByReviewId(reviewId);
    }

    @Override
    public int getProductIdByReview(int reviewId) {
        return productReviewMapper.getProductIdByReviewId(reviewId);
    }



    @Override
    public List<ReviewComment> getCommentsByReviewId(int reviewId) {
        return reviewCommentMapper.selectByReviewId(reviewId);
    }

    @Override
    public void addComment(ReviewComment comment) {
        reviewCommentMapper.insert(comment);
    }

}
