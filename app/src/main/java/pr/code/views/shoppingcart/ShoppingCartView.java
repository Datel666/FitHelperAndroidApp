package pr.code.views.shoppingcart;

import java.util.List;

import pr.code.models.CartItems;

public interface ShoppingCartView {
    void setCartItems(List<CartItems> cartItems);
}
