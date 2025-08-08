package kr.manyofactory.manyoshop.controller.apis;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

import kr.manyofactory.manyoshop.models.PopularProduct;
import kr.manyofactory.manyoshop.services.PopularProductService;

@RestController
@RequestMapping("/api/stat")
@RequiredArgsConstructor
public class PopularProductController {

    private final PopularProductService popularProductService;


    @GetMapping("/popular-products/wishlist")
    public List<PopularProduct> getPopularProductsByWishlist() {
        List<PopularProduct> list = popularProductService.getTopProductsByWishlist();
        return list != null ? list : List.of();
    }

}
