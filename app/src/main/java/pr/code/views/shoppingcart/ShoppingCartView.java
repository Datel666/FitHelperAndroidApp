package pr.code.views.shoppingcart;


import java.util.List;


import pr.code.models.CartItems;

/**
 * Implements data display (from the Model), contacts the Presenter for updates, redirects events from the user to the Presenter
 */
public interface ShoppingCartView {
    void setCartItems(List<CartItems.CartItem> cartItems);
}
