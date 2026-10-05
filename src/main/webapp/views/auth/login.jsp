<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-dark text-white text-center py-3">
                    <h4 class="mb-0">Đăng Nhập</h4>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty sessionScope.successMessage}">
                        <div class="alert alert-success" role="alert">${sessionScope.successMessage}</div>
                        <c:remove var="successMessage" scope="session" />
                    </c:if>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">${error}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="email" class="form-control" value="${email}" placeholder="name@example.com" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Mật khẩu</label>
                            <input type="password" name="passwd" class="form-control" placeholder="Nhập mật khẩu" required>
                        </div>
                        <div class="d-grid mt-4">
                            <button type="submit" class="btn btn-primary btn-lg">Đăng Nhập</button>
                        </div>
                    </form>
                    <div class="text-center mt-3">
                        <span>Chưa có tài khoản? </span>
                        <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
