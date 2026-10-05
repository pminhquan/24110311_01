<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Xác thực OTP</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-success text-white text-center py-3">
                    <h4 class="mb-0">Xác Thực Mã OTP</h4>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">${error}</div>
                    </c:if>

                    <p class="text-muted text-center">
                        Vui lòng nhập mã OTP 6 chữ số để kích hoạt tài khoản của bạn.
                    </p>

                    <form action="${pageContext.request.contextPath}/verify-otp" method="post">
                        <div class="mb-3">
                            <label class="form-label text-center d-block">Mã OTP</label>
                            <input type="text" name="otp" class="form-control form-control-lg text-center" maxlength="6" placeholder="000000" style="letter-spacing: 6px; font-size: 1.5rem; font-weight: bold;" required autofocus>
                        </div>
                        <div class="d-grid mt-4">
                            <button type="submit" class="btn btn-success btn-lg">Xác Nhận Kích Hoạt</button>
                        </div>
                    </form>
                    <div class="text-center mt-3">
                        <a href="${pageContext.request.contextPath}/register">Quay lại Đăng ký</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
