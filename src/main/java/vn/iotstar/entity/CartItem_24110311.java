package vn.iotstar.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem_24110311 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookId;
    private Book_24110311 book;
    private int quantity;

    public CartItem_24110311() {
    }

    public CartItem_24110311(Book_24110311 book, int quantity) {
        if (book != null) {
            this.bookId = book.getBookid();
        }
        this.book = book;
        this.quantity = quantity;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Book_24110311 getBook() {
        return book;
    }

    public void setBook(Book_24110311 book) {
        this.book = book;
        if (book != null) {
            this.bookId = book.getBookid();
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return (book != null && book.getPrice() != null) ? book.getPrice() : BigDecimal.ZERO;
    }

    public BigDecimal getSubtotal() {
        return getUnitPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
