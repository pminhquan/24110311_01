package vn.iotstar.entity;

import vn.iotstar.service.BookService_24110311;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart_24110311 implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<Integer, CartItem_24110311> items = new LinkedHashMap<>();

    public Map<Integer, CartItem_24110311> getItems() {
        return items;
    }

    public Collection<CartItem_24110311> getItemList() {
        return items.values();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemCount() {
        return items.size();
    }

    public int getTotalQuantity() {
        int total = 0;
        for (CartItem_24110311 item : items.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem_24110311 item : items.values()) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public void addItem(Book_24110311 book, int quantity) throws IllegalArgumentException {
        if (book == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng thêm vào phải lớn hơn 0");
        }
        if (book.getQuantity() <= 0) {
            throw new IllegalArgumentException("Sách '" + book.getTitle() + "' đã hết hàng");
        }

        int bookId = book.getBookid();
        CartItem_24110311 existing = items.get(bookId);
        int currentQty = (existing != null) ? existing.getQuantity() : 0;
        int newQty = currentQty + quantity;

        if (newQty > book.getQuantity()) {
            throw new IllegalArgumentException("Không thể thêm vượt quá số lượng tồn kho (hiện còn " 
                    + book.getQuantity() + " quyển, trong giỏ đã có " + currentQty + " quyển)");
        }

        if (existing != null) {
            existing.setBook(book);
            existing.setQuantity(newQty);
        } else {
            items.put(bookId, new CartItem_24110311(book, quantity));
        }
    }

    public void updateItem(int bookId, int quantity, Book_24110311 currentBook) throws IllegalArgumentException {
        if (!items.containsKey(bookId)) {
            throw new IllegalArgumentException("Sản phẩm không có trong giỏ hàng");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        if (currentBook == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại");
        }
        if (quantity > currentBook.getQuantity()) {
            throw new IllegalArgumentException("Số lượng yêu cầu (" + quantity + ") vượt quá tồn kho (hiện còn " 
                    + currentBook.getQuantity() + " quyển)");
        }

        CartItem_24110311 item = items.get(bookId);
        item.setBook(currentBook);
        item.setQuantity(quantity);
    }

    public void removeItem(int bookId) {
        items.remove(bookId);
    }

    public void clear() {
        items.clear();
    }

    public void reloadBooks(BookService_24110311 bookService) {
        if (bookService == null) return;
        for (CartItem_24110311 item : items.values()) {
            Book_24110311 latest = bookService.findById(item.getBookId());
            if (latest != null) {
                item.setBook(latest);
            }
        }
    }
}
