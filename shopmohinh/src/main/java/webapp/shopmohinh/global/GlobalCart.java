package webapp.shopmohinh.global;

import java.util.ArrayList;
import java.util.List;

import webapp.shopmohinh.dto.CartItem;

public class GlobalCart {
    private List<CartItem> items;

    public GlobalCart() {
        this.items = new ArrayList<>();
    }

    public void addItem(CartItem item) {
        for (CartItem existingItem : items) {
            if (existingItem.getProduct().getId().equals(item.getProduct().getId())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public void clearItems() {
        this.items.clear();
        getTotalPrice();
    }
}
