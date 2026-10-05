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
import vn.iotstar.util.MailUtil_24110311;

import java.io.IOException;
import java.util.Random;

@WebServlet(urlPatterns = {"/register"})
public class RegisterController_24110311 extends HttpServlet {

    private final UserService_24110311 userService = new UserServiceImpl_24110311();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String fullname = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String passwd = request.getParameter("passwd");

        if (email == null || email.trim().isEmpty() || passwd == null || passwd.trim().isEmpty()) {
            request.setAttribute("error", "Email và mật khẩu không được để trống!");
            request.setAttribute("email", email);
            request.setAttribute("fullname", fullname);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }

        email = email.trim();
        if (userService.checkEmailExist(email)) {
            request.setAttribute("error", "Email đã tồn tại trên hệ thống!");
            request.setAttribute("fullname", fullname);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }

        User_24110311 user = new User_24110311();
        user.setEmail(email);
        user.setFullname(fullname != null ? fullname.trim() : "");
        user.setPhone(phone != null ? phone.trim() : "");
        user.setPasswd(passwd);

        // Generate simple 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(1000000));
        System.out.println(">>> [EXAM OTP] OTP for " + email + ": " + otp);

        HttpSession session = request.getSession();
        session.setAttribute("otp", otp);
        session.setAttribute("registerUser", user);

        // Send OTP via email
        boolean sent = MailUtil_24110311.sendOtp(email, otp);
        if (sent) {
            System.out.println(">>> [OTP STATUS] Email sent successfully to " + email);
        } else {
            System.out.println(">>> [OTP STATUS] Email sending failed or not configured. OTP remains in session for exam verification.");
        }

        response.sendRedirect(request.getContextPath() + "/verify-otp");
    }
}
