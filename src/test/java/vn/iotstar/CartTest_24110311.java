package vn.iotstar;

import vn.iotstar.entity.Book_24110311;
import vn.iotstar.entity.Cart_24110311;

import java.math.BigDecimal;

public class CartTest_24110311 {

    public static void main(String[] args) {
        System.out.println(">>> Running Cart and Business Logic Self-Check Tests with BigDecimal...");

        testAddToCartAndTotals();
        testSameBookIncrementNeverExceedsStock();
        testRejectInvalidQuantityAndOutOfStock();
        testUpdateCartQuantity();
        testRemoveAndClear();

        System.out.println(">>> All 5 Cart self-check test suites PASSED successfully with BigDecimal!");
    }

    private static void testAddToCartAndTotals() {
        Cart_24110311 cart = new Cart_24110311();
        assert cart.isEmpty() : "Cart should be empty initially";
        assert cart.getTotalQuantity() == 0 : "Initial total quantity should be 0";
        assert cart.getTotalAmount().compareTo(BigDecimal.ZERO) == 0 : "Initial total amount should be 0";

        Book_24110311 book1 = new Book_24110311(1, "ISBN1", "Clean Code", "Publisher", new BigDecimal("45.00"), "Desc", null, null, 10);
        cart.addItem(book1, 2);

        assert cart.getItemCount() == 1 : "Expected 1 item type";
        assert cart.getTotalQuantity() == 2 : "Expected total quantity 2";
        assert cart.getTotalAmount().compareTo(new BigDecimal("90.00")) == 0 : "Expected total amount 90.00";
        System.out.println("  [PASS] testAddToCartAndTotals");
    }

    private static void testSameBookIncrementNeverExceedsStock() {
        Cart_24110311 cart = new Cart_24110311();
        Book_24110311 book = new Book_24110311(2, "ISBN2", "Effective Java", "Publisher", new BigDecimal("50.00"), "Desc", null, null, 5);

        cart.addItem(book, 2);
        assert cart.getTotalQuantity() == 2;

        cart.addItem(book, 3);
        assert cart.getTotalQuantity() == 5;
        assert cart.getTotalAmount().compareTo(new BigDecimal("250.00")) == 0;

        // Adding 1 more should exceed stock of 5
        boolean rejected = false;
        try {
            cart.addItem(book, 1);
        } catch (IllegalArgumentException e) {
            rejected = true;
        }
        assert rejected : "Adding more than stock should have thrown IllegalArgumentException";
        assert cart.getTotalQuantity() == 5 : "Quantity should remain at 5 after rejected add";
        System.out.println("  [PASS] testSameBookIncrementNeverExceedsStock");
    }

    private static void testRejectInvalidQuantityAndOutOfStock() {
        Cart_24110311 cart = new Cart_24110311();
        Book_24110311 inStock = new Book_24110311(3, "ISBN3", "Book 3", "Pub", new BigDecimal("20.00"), "Desc", null, null, 5);
        Book_24110311 outOfStock = new Book_24110311(4, "ISBN4", "Book 4", "Pub", new BigDecimal("25.00"), "Desc", null, null, 0);

        // Quantity <= 0
        boolean zeroRejected = false;
        try {
            cart.addItem(inStock, 0);
        } catch (IllegalArgumentException e) {
            zeroRejected = true;
        }
        assert zeroRejected : "Quantity 0 must be rejected";

        // Out of stock
        boolean oosRejected = false;
        try {
            cart.addItem(outOfStock, 1);
        } catch (IllegalArgumentException e) {
            oosRejected = true;
        }
        assert oosRejected : "Out of stock book must be rejected";

        // Exceeding stock directly on first add
        boolean exceedRejected = false;
        try {
            cart.addItem(inStock, 6);
        } catch (IllegalArgumentException e) {
            exceedRejected = true;
        }
        assert exceedRejected : "Quantity exceeding stock on first add must be rejected";
        System.out.println("  [PASS] testRejectInvalidQuantityAndOutOfStock");
    }

    private static void testUpdateCartQuantity() {
        Cart_24110311 cart = new Cart_24110311();
        Book_24110311 book = new Book_24110311(5, "ISBN5", "Book 5", "Pub", new BigDecimal("30.00"), "Desc", null, null, 8);
        cart.addItem(book, 2);

        cart.updateItem(5, 4, book);
        assert cart.getTotalQuantity() == 4 : "Expected updated quantity 4";
        assert cart.getTotalAmount().compareTo(new BigDecimal("120.00")) == 0 : "Expected updated total 120.00";

        // Update to <= 0 rejected
        boolean zeroUpdateRejected = false;
        try {
            cart.updateItem(5, 0, book);
        } catch (IllegalArgumentException e) {
            zeroUpdateRejected = true;
        }
        assert zeroUpdateRejected : "Updating quantity to <= 0 must be rejected";

        // Update exceeding stock rejected
        boolean exceedUpdateRejected = false;
        try {
            cart.updateItem(5, 9, book);
        } catch (IllegalArgumentException e) {
            exceedUpdateRejected = true;
        }
        assert exceedUpdateRejected : "Updating quantity exceeding stock must be rejected";
        System.out.println("  [PASS] testUpdateCartQuantity");
    }

    private static void testRemoveAndClear() {
        Cart_24110311 cart = new Cart_24110311();
        Book_24110311 book1 = new Book_24110311(1, "ISBN1", "Book 1", "Pub", new BigDecimal("10.00"), "Desc", null, null, 5);
        Book_24110311 book2 = new Book_24110311(2, "ISBN2", "Book 2", "Pub", new BigDecimal("20.00"), "Desc", null, null, 5);

        cart.addItem(book1, 2);
        cart.addItem(book2, 1);
        assert cart.getItemCount() == 2;
        assert cart.getTotalQuantity() == 3;

        cart.removeItem(1);
        assert cart.getItemCount() == 1;
        assert cart.getTotalQuantity() == 1;
        assert cart.getTotalAmount().compareTo(new BigDecimal("20.00")) == 0;

        cart.clear();
        assert cart.isEmpty();
        assert cart.getTotalQuantity() == 0;
        assert cart.getTotalAmount().compareTo(BigDecimal.ZERO) == 0;
        System.out.println("  [PASS] testRemoveAndClear");
    }
}
