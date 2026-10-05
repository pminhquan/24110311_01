package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User_24110311;
import vn.iotstar.service.UserService_24110311;
import vn.iotstar.service.UserServiceImpl_24110311;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(urlPatterns = {"/verify-otp"})
public class VerifyOtpController_24110311 extends HttpServlet {

    private final UserService_24110311 userService = new UserServiceImpl_24110311();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User_24110311 registerUser = (User_24110311) session.getAttribute("registerUser");
        if (registerUser == null) {
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }
        request.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String enteredOtp = request.getParameter("otp");

        HttpSession session = request.getSession();
        String sessionOtp = (String) session.getAttribute("otp");
        User_24110311 registerUser = (User_24110311) session.getAttribute("registerUser");

        if (sessionOtp == null || registerUser == null) {
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        if (enteredOtp != null && enteredOtp.trim().equals(sessionOtp)) {
            registerUser.setSignup_date(LocalDate.now());
            registerUser.setIs_admin(false);

            boolean success = userService.register(registerUser);
            if (success) {
                session.removeAttribute("otp");
                session.removeAttribute("registerUser");
                session.removeAttribute("otpMessage");
                session.setAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                request.setAttribute("error", "Lỗi lưu tài khoản hoặc email đã tồn tại!");
                request.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Mã OTP không chính xác!");
            request.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(request, response);
        }
    }
}
