package pr.code.models;

import java.util.List;

/**
 * This class describes Cart item(s) model
 */
public class CartItems {

    private List<CartItem> cartItems;

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public static class CartItem {

        private String itemid;
        private String itemname;
        private String itemquantity;

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getItemquantity() {
            return itemquantity;
        }

        public void setItemquantity(String itemquantity) {
            this.itemquantity = itemquantity;
        }
    }
}
