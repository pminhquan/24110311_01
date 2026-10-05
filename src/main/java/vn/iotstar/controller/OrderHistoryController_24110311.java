package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.Orders_24110311;
import vn.iotstar.entity.User_24110311;
import vn.iotstar.service.OrderService_24110311;
import vn.iotstar.service.OrderServiceImpl_24110311;
import vn.iotstar.util.OrderStatusUtil_24110311;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/orders/history"})
public class OrderHistoryController_24110311 extends HttpServlet {

    private final OrderService_24110311 orderService = new OrderServiceImpl_24110311();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User_24110311 currentUser = (session != null) ? (User_24110311) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            if (session != null) {
                session.setAttribute("error", "Vui lòng đăng nhập để xem lịch sử đơn hàng.");
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Strict ownership: always use authenticated session user; ignore any ?userId= query parameter
        int userId = currentUser.getId();

        String rawStatus = request.getParameter("status");
        String normalizedStatus = OrderStatusUtil_24110311.normalizeStatus(rawStatus);

        List<Orders_24110311> orders;
        if (normalizedStatus != null) {
            orders = orderService.findByUserIdAndStatus(userId, normalizedStatus);
        } else {
            orders = orderService.findByUserId(userId);
        }

        request.setAttribute("orders", orders);
        request.setAttribute("statusList", OrderStatusUtil_24110311.getAllStatuses());
        request.setAttribute("statusMap", OrderStatusUtil_24110311.getStatusMap());
        request.setAttribute("selectedStatus", normalizedStatus != null ? normalizedStatus : "");

        request.getRequestDispatcher("/views/order/history.jsp").forward(request, response);
    }
}
