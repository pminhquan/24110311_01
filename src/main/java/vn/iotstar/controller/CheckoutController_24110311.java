package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.CartItem_24110311;
import vn.iotstar.entity.Cart_24110311;
import vn.iotstar.entity.Orders_24110311;
import vn.iotstar.entity.User_24110311;
import vn.iotstar.service.BookService_24110311;
import vn.iotstar.service.BookServiceImpl_24110311;
import vn.iotstar.service.OrderService_24110311;
import vn.iotstar.service.OrderServiceImpl_24110311;

import java.io.IOException;

@WebServlet(urlPatterns = {
    "/checkout", "/checkout/success"
})
public class CheckoutController_24110311 extends HttpServlet {

    private final BookService_24110311 bookService = new BookServiceImpl_24110311();
    private final OrderService_24110311 orderService = new OrderServiceImpl_24110311();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        HttpSession session = request.getSession();

        User_24110311 currentUser = (User_24110311) session.getAttribute("currentUser");
        if (currentUser == null) {
            session.setAttribute("error", "Vui lòng đăng nhập trước khi tiến hành thanh toán.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if ("/checkout/success".equals(path)) {
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam != null && !orderIdParam.trim().isEmpty()) {
                try {
                    int orderId = Integer.parseInt(orderIdParam.trim());
                    Orders_24110311 order = orderService.findById(orderId);
                    if (order != null && order.getUser() != null && order.getUser().getId() == currentUser.getId()) {
                        request.setAttribute("order", order);
                        request.getRequestDispatcher("/views/checkout/success.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            response.sendRedirect(request.getContextPath() + "/books");
            return;
        }

        Cart_24110311 cart = (Cart_24110311) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            session.setAttribute("cartError", "Giỏ hàng của bạn đang trống. Vui lòng chọn sách trước khi thanh toán.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // Re-query latest book information from DB to verify stock and price
        cart.reloadBooks(bookService);

        boolean hasStockIssue = false;
        StringBuilder stockWarning = new StringBuilder();
        for (CartItem_24110311 item : cart.getItemList()) {
            if (item.getBook() == null || item.getQuantity() > item.getBook().getQuantity()) {
                hasStockIssue = true;
                int currentQty = item.getBook() != null ? item.getBook().getQuantity() : 0;
                stockWarning.append("Sách '").append(item.getBook() != null ? item.getBook().getTitle() : "ID " + item.getBookId())
                        .append("' hiện chỉ còn ").append(currentQty).append(" quyển (bạn chọn ")
                        .append(item.getQuantity()).append(" quyển). ");
            }
        }

        if (hasStockIssue) {
            request.setAttribute("stockWarning", stockWarning.toString() + "Vui lòng cập nhật lại giỏ hàng.");
        }

        request.setAttribute("cart", cart);
        request.setAttribute("currentUser", currentUser);
        request.getRequestDispatcher("/views/checkout/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        User_24110311 currentUser = (User_24110311) session.getAttribute("currentUser");
        if (currentUser == null) {
            session.setAttribute("error", "Vui lòng đăng nhập trước khi tiến hành thanh toán.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Cart_24110311 cart = (Cart_24110311) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            session.setAttribute("cartError", "Giỏ hàng đang trống, không thể thanh toán.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        String recipientName = request.getParameter("recipient_name");
        String recipientPhone = request.getParameter("recipient_phone");
        String shippingAddress = request.getParameter("shipping_address");

        if (recipientName == null || recipientName.trim().isEmpty() ||
            recipientPhone == null || recipientPhone.trim().isEmpty() ||
            shippingAddress == null || shippingAddress.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ họ tên, số điện thoại và địa chỉ nhận hàng.");
            request.setAttribute("cart", cart);
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("recipient_name", recipientName);
            request.setAttribute("recipient_phone", recipientPhone);
            request.setAttribute("shipping_address", shippingAddress);
            request.getRequestDispatcher("/views/checkout/checkout.jsp").forward(request, response);
            return;
        }

        try {
            // Process order in a single RESOURCE_LOCAL transaction (re-query books, check stock, decrement quantity, persist order and details)
            Orders_24110311 order = orderService.processCheckout(currentUser.getId(), recipientName, recipientPhone, shippingAddress, cart);

            // Clear cart ONLY AFTER successful transaction commit
            cart.clear();

            response.sendRedirect(request.getContextPath() + "/checkout/success?orderId=" + order.getOrderId());
        } catch (Exception e) {
            cart.reloadBooks(bookService);
            request.setAttribute("error", "Đặt hàng thất bại: " + e.getMessage());
            request.setAttribute("cart", cart);
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("recipient_name", recipientName);
            request.setAttribute("recipient_phone", recipientPhone);
            request.setAttribute("shipping_address", shippingAddress);
            request.getRequestDispatcher("/views/checkout/checkout.jsp").forward(request, response);
        }
    }
}
