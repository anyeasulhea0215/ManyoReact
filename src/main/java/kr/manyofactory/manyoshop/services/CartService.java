package kr.manyofactory.manyoshop.services;

import kr.manyofactory.manyoshop.mappers.CartMapper;
import kr.manyofactory.manyoshop.models.Cart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartMapper cartMapper;

    public CartService(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    public List<Cart> getCartItems(int memberId) throws Exception {
        return cartMapper.selectId(memberId);
    }

    public int calculateTotalPrice(List<Cart> items) throws Exception {
    int total = 0;
    for (Cart item : items) {
            if (item.getProduct() != null) {
            total += item.getProduct().getSalePrice() * item.getQuantity();
        }

    }
    return total;
}


    public void addCartItem(Cart input) throws Exception {
        cartMapper.insert(input); }

    public void deleteCartItem(int basketId) throws Exception {
            cartMapper.delete(basketId);
        }

        public void updateCartQuantity(Cart input) throws Exception {
                cartMapper.updateQuantity(input);
            }
       public List<Cart> getCartItemsByBasketIds(Integer memberId, List<Integer> basketIds) throws Exception {
        return cartMapper.selectByIds(memberId, basketIds);
    }
        public void addOrUpdateCartItem(int memberId, int productId, int quantity) throws Exception {
        List<Cart> cartItems = getCartItems(memberId);
        for (Cart item : cartItems) {
            if (item.getProductId() == productId) {
                item.setQuantity(item.getQuantity() + quantity);
                updateCartQuantity(item);
                return;
            }
        }
        Cart newItem = new Cart();
        newItem.setMemberId(memberId);
        newItem.setProductId(productId);
        newItem.setQuantity(quantity);
        addCartItem(newItem);
    }

    //장바구니 수량 업데이트
    public void updateCartQuantity(int basketId, String action) {
        Cart cart = cartMapper.selectItem(basketId);
        if (cart == null) return;

        int quantity = cart.getQuantity();

        if ("increase".equals(action) && quantity < 10) {
            quantity++;
        } else if ("decrease".equals(action) && quantity > 1) {
            quantity--;
        } else if ("decrease".equals(action) && quantity <= 1) {
            return;
        }

        cart.setQuantity(quantity);
        cartMapper.updateCartQuantity(cart);
    }




}
