<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đặt hàng thành công - Order Confirmation</title>
</head>
<body>
<div class="card shadow-sm border-success mb-4">
    <div class="card-header bg-success text-white py-3 text-center">
        <h3 class="mb-1">&#10004; Đặt hàng thành công!</h3>
        <p class="mb-0">Cảm ơn bạn đã đặt mua sách tại hệ thống. Đơn hàng của bạn đã được ghi nhận.</p>
    </div>
    <div class="card-body">
        <div class="row mb-4">
            <div class="col-md-6 border-end">
                <h5 class="text-primary border-bottom pb-2">Thông tin đơn hàng</h5>
                <p class="mb-1"><strong>Mã đơn hàng:</strong> <span class="badge bg-dark fs-6">#${order.orderId}</span></p>
                <p class="mb-1"><strong>Ngày đặt hàng:</strong> ${order.orderDate}</p>
                <p class="mb-1"><strong>Trạng thái đơn hàng:</strong> <span class="badge bg-info text-dark">${order.status}</span></p>
                <p class="mb-1"><strong>Phương thức thanh toán:</strong> <span class="badge bg-secondary">${order.paymentMethod} (Thanh toán khi nhận hàng)</span></p>
            </div>
            <div class="col-md-6 ps-md-4">
                <h5 class="text-primary border-bottom pb-2">Thông tin người nhận</h5>
                <p class="mb-1"><strong>Họ và tên:</strong> ${order.recipientName}</p>
                <p class="mb-1"><strong>Số điện thoại:</strong> ${order.recipientPhone}</p>
                <p class="mb-1"><strong>Địa chỉ giao hàng:</strong> ${order.shippingAddress}</p>
                <p class="mb-1"><strong>Tài khoản đặt:</strong> ${order.user.fullname} (${order.user.email})</p>
            </div>
        </div>

        <h5 class="text-primary border-bottom pb-2">Danh sách sản phẩm</h5>
        <div class="table-responsive mb-3">
            <table class="table table-bordered table-striped align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th style="width: 50px;" class="text-center">#</th>
                        <th>Tên sách</th>
                        <th style="width: 120px;" class="text-end">Đơn giá</th>
                        <th style="width: 100px;" class="text-center">Số lượng</th>
                        <th style="width: 140px;" class="text-end">Thành tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detail" items="${order.orderDetails}" varStatus="status">
                        <tr>
                            <td class="text-center">${status.count}</td>
                            <td>
                                <strong>${not empty detail.book ? detail.book.title : 'Sách #' + detail.book.bookid}</strong>
                            </td>
                            <td class="text-end">$${detail.unitPrice}</td>
                            <td class="text-center">${detail.quantity}</td>
                            <td class="text-end fw-bold text-success">$${detail.subtotal}</td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot class="table-light">
                    <tr>
                        <th colspan="4" class="text-end fs-5">Tổng tiền thanh toán (COD):</th>
                        <th class="text-end fs-5 text-danger fw-bold">$${order.totalAmount}</th>
                    </tr>
                </tfoot>
            </table>
        </div>

        <div class="text-center mt-4">
            <a href="${pageContext.request.contextPath}/books" class="btn btn-primary btn-lg">Tiếp tục mua sắm</a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary btn-lg ms-2">Về trang chủ</a>
        </div>
    </div>
</div>
</body>
</html>
