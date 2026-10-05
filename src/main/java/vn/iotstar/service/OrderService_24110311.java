package vn.iotstar.service;

import vn.iotstar.entity.Cart_24110311;
import vn.iotstar.entity.Orders_24110311;

public interface OrderService_24110311 {

    Orders_24110311 findById(int orderId);

    Orders_24110311 processCheckout(int userId, String recipientName, String recipientPhone, String shippingAddress, Cart_24110311 cart) throws Exception;
}
