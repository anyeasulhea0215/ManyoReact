package kr.manyofactory.manyoshop.models;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Wishlist {
    private int wishlistId;
    private int memberId;
    private int productId;
    private String optionInfo;
    private int quantity;
    private LocalDateTime addedAt;

    private String productName;
    private int productPrice;
    private String productImg;}

