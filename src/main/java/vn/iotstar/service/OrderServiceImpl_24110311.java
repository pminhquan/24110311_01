package vn.iotstar.service;

import vn.iotstar.entity.Cart_24110311;
import vn.iotstar.entity.Orders_24110311;
import vn.iotstar.repository.OrderRepository_24110311;
import vn.iotstar.repository.OrderRepositoryImpl_24110311;

public class OrderServiceImpl_24110311 implements OrderService_24110311 {

    private final OrderRepository_24110311 orderRepository = new OrderRepositoryImpl_24110311();

    @Override
    public Orders_24110311 findById(int orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Orders_24110311 processCheckout(int userId, String recipientName, String recipientPhone, String shippingAddress, Cart_24110311 cart) throws Exception {
        return orderRepository.createOrderWithTransaction(userId, recipientName, recipientPhone, shippingAddress, cart);
    }
}
