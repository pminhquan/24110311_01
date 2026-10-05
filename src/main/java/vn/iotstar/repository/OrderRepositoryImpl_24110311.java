package vn.iotstar.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.Book_24110311;
import vn.iotstar.entity.CartItem_24110311;
import vn.iotstar.entity.Cart_24110311;
import vn.iotstar.entity.OrderDetail_24110311;
import vn.iotstar.entity.Orders_24110311;
import vn.iotstar.entity.User_24110311;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl_24110311 implements OrderRepository_24110311 {

    @Override
    public Orders_24110311 findById(int orderId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            Orders_24110311 order = em.find(Orders_24110311.class, orderId);
            if (order != null) {
                order.getOrderDetails().size();
                for (OrderDetail_24110311 d : order.getOrderDetails()) {
                    if (d.getBook() != null) {
                        d.getBook().getTitle();
                    }
                }
            }
            return order;
        } finally {
            em.close();
        }
    }

    @Override
    public Orders_24110311 createOrderWithTransaction(int userId, String recipientName, String recipientPhone, String shippingAddress, Cart_24110311 cart) throws Exception {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalStateException("Giỏ hàng đang trống, không thể thanh toán.");
        }
        if (recipientName == null || recipientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên người nhận không được để trống.");
        }
        if (recipientPhone == null || recipientPhone.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại nhận hàng không được để trống.");
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ nhận hàng không được để trống.");
        }

        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            User_24110311 user = em.find(User_24110311.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("Người dùng không tồn tại (ID: " + userId + ").");
            }

            Orders_24110311 order = new Orders_24110311();
            order.setUser(user);
            order.setRecipientName(recipientName.trim());
            order.setRecipientPhone(recipientPhone.trim());
            order.setShippingAddress(shippingAddress.trim());
            order.setPaymentMethod("COD");
            order.setStatus("NEW");
            order.setOrderDate(LocalDateTime.now());

            BigDecimal totalAmount = BigDecimal.ZERO;
            List<OrderDetail_24110311> orderDetails = new ArrayList<>();

            for (CartItem_24110311 item : cart.getItems().values()) {
                // Re-query every Book directly from the database with a pessimistic write lock within the active transaction
                Book_24110311 dbBook = em.find(Book_24110311.class, item.getBookId(), LockModeType.PESSIMISTIC_WRITE);
                if (dbBook == null) {
                    throw new IllegalStateException("Sách với mã ID " + item.getBookId() + " không còn tồn tại trong hệ thống.");
                }

                if (item.getQuantity() <= 0) {
                    throw new IllegalArgumentException("Số lượng đặt mua cho cuốn '" + dbBook.getTitle() + "' không hợp lệ (phải > 0).");
                }

                // Validate stock again against current DB state
                if (dbBook.getQuantity() < item.getQuantity()) {
                    throw new IllegalStateException("Sách '" + dbBook.getTitle() + "' không đủ số lượng trong kho (Hiện còn: " 
                            + dbBook.getQuantity() + ", yêu cầu: " + item.getQuantity() + ").");
                }

                // Decrement Book.quantity only in the active transaction
                dbBook.setQuantity(dbBook.getQuantity() - item.getQuantity());
                em.merge(dbBook);

                // Use DB price
                BigDecimal unitPrice = dbBook.getPrice() != null ? dbBook.getPrice() : BigDecimal.ZERO;
                BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                totalAmount = totalAmount.add(lineTotal);

                OrderDetail_24110311 detail = new OrderDetail_24110311();
                detail.setOrder(order);
                detail.setBook(dbBook);
                detail.setQuantity(item.getQuantity());
                detail.setUnitPrice(unitPrice);
                orderDetails.add(detail);
            }

            order.setTotalAmount(totalAmount);
            order.setOrderDetails(orderDetails);

            em.persist(order);
            for (OrderDetail_24110311 detail : orderDetails) {
                em.persist(detail);
            }

            tx.commit();
            return order;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Orders_24110311> findByUserId(int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT o FROM Orders_24110311 o " +
                          "LEFT JOIN FETCH o.orderDetails od " +
                          "LEFT JOIN FETCH od.book b " +
                          "WHERE o.user.id = :userId " +
                          "ORDER BY o.orderDate DESC, o.orderId DESC";
            List<Orders_24110311> list = em.createQuery(jpql, Orders_24110311.class)
                    .setParameter("userId", userId)
                    .getResultList();
            initializeOrderCollections(list);
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Orders_24110311> findByUserIdAndStatus(int userId, String status) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT o FROM Orders_24110311 o " +
                          "LEFT JOIN FETCH o.orderDetails od " +
                          "LEFT JOIN FETCH od.book b " +
                          "WHERE o.user.id = :userId AND o.status = :status " +
                          "ORDER BY o.orderDate DESC, o.orderId DESC";
            List<Orders_24110311> list = em.createQuery(jpql, Orders_24110311.class)
                    .setParameter("userId", userId)
                    .setParameter("status", status)
                    .getResultList();
            initializeOrderCollections(list);
            return list;
        } finally {
            em.close();
        }
    }

    private void initializeOrderCollections(List<Orders_24110311> orders) {
        if (orders != null) {
            for (Orders_24110311 order : orders) {
                if (order.getOrderDetails() != null) {
                    order.getOrderDetails().size();
                    for (OrderDetail_24110311 d : order.getOrderDetails()) {
                        if (d.getBook() != null) {
                            d.getBook().getTitle();
                        }
                    }
                }
            }
        }
    }
}

