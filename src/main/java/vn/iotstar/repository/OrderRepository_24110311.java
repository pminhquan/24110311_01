package vn.iotstar.repository;

import vn.iotstar.entity.Cart_24110311;
import vn.iotstar.entity.Orders_24110311;

public interface OrderRepository_24110311 {

    Orders_24110311 findById(int orderId);

    Orders_24110311 createOrderWithTransaction(int userId, String recipientName, String recipientPhone, String shippingAddress, Cart_24110311 cart) throws Exception;
}
