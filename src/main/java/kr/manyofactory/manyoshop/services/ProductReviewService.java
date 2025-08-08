package kr.manyofactory.manyoshop.services;

import java.util.List;
import java.util.Map;
import kr.manyofactory.manyoshop.models.ProductReview;
import kr.manyofactory.manyoshop.models.ReviewComment;

public interface ProductReviewService {

    List<ProductReview> getReviewsByProductId(int productId);

    double getAverageRating(int productId);

    Map<Integer, Integer> getRatingDistribution(int productId);

    boolean hasUserReviewedProduct(int memberId, int productId);

    void addLike(int memberId, int reviewId);

    int getLikeCount(int reviewId);

    int getProductIdByReview(int reviewId);

    List<ReviewComment> getCommentsByReviewId(int reviewId);
    void addComment(ReviewComment comment);


}
