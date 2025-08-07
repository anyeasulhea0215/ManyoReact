package kr.manyofactory.manyoshop.controller;

import jakarta.servlet.http.HttpSession;
import kr.manyofactory.manyoshop.models.Cart;
import kr.manyofactory.manyoshop.models.Members;
import kr.manyofactory.manyoshop.models.Product;
import kr.manyofactory.manyoshop.services.BestProductService;
import kr.manyofactory.manyoshop.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private BestProductService bestProductService;

    /** 장바구니에 상품을 추가하는 기능입니다
     *
     * @param productId
     * @param quantity
     * @param session
     * @return
     * @throws Exception
     */
    @PostMapping("/orders/cart")
    public String addToCart(@RequestParam("productId") int productId,
                            @RequestParam("quantity") int quantity,
                            HttpSession session) throws Exception {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        Integer memberId = (loginMember != null) ? loginMember.getId() : null;

        if (memberId == null) {
            return "redirect:/login/login";
        }

        List<Cart> existingItems = cartService.getCartItems(memberId);
        for (Cart item : existingItems) {
            if (item.getProductId() == productId) {
                item.setQuantity(item.getQuantity() + quantity);
                cartService.updateCartQuantity(item);
                return "redirect:/cart/list";
            }
        }

        Cart cartItem = new Cart();
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        cartItem.setMemberId(memberId);

        Product product = bestProductService.getProductById(productId);
        cartItem.setProduct(product);

        cartService.addCartItem(cartItem);

        return "redirect:/cart/list";
    }


    /** 장바구니 목록 조회하는 기능입니다
     *
     * @param session
     * @param model
     * @return
     * @throws Exception
     */

    @GetMapping("/cart/list")
    public String showCart(HttpSession session, Model model) throws Exception {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        Integer memberId = (loginMember != null) ? loginMember.getId() : null;

        if (memberId == null) {
            return "redirect:/login/login";
        }

        List<Cart> items = cartService.getCartItems(memberId);
        int totalPrice = cartService.calculateTotalPrice(items);
        int deliveryFee = (totalPrice >= 50000) ? 0 : 3000;

        model.addAttribute("items", items);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("deliveryFee", deliveryFee);

        return "orders/cart";
    }

    /**장바구니 상품 삭제하는 기능입니다
     *
     * @param basketId
     * @param session
     * @return
     * @throws Exception
     */
    @PostMapping("/cart/delete")
    public String deleteCartItem(@RequestParam("basketId") int basketId, HttpSession session) throws Exception {
        Members loginMember = (Members) session.getAttribute("memberInfo");
        Integer memberId = (loginMember != null) ? loginMember.getId() : null;

        if (memberId == null) {
            return "redirect:/login/login";
        }

        cartService.deleteCartItem(basketId);
        return "redirect:/cart/list";
    }

    /** 체크한것만 결제페이지로 이동하는 기능입니다
     *
     * @param basketIds
     * @param session
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/order/checkout")
    public String checkout(@RequestParam("basketIds") List<Integer> basketIds,
                           HttpSession session,
                           Model model) throws Exception {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        Integer memberId = (loginMember != null) ? loginMember.getId() : null;

        if (memberId == null) {
            return "redirect:/login/login";
        }

        List<Cart> selectedItems = cartService.getCartItemsByBasketIds(memberId, basketIds);
        int totalPrice = cartService.calculateTotalPrice(selectedItems);
        int deliveryFee = (totalPrice >= 30000) ? 0 : 3000;
        int finalPrice = totalPrice + deliveryFee;
        model.addAttribute("basketList", selectedItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("deliveryFee", deliveryFee);
        model.addAttribute("finalPrice", finalPrice);
        model.addAttribute("member", loginMember);

        return "orders/order";
    }
}