<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thanh toán đơn hàng - Checkout</title>
</head>
<body>
<div class="mb-3">
    <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline-secondary btn-sm">&larr; Quay lại giỏ hàng</a>
</div>

<h2 class="mb-3">Xác nhận thanh toán (COD)</h2>

<c:if test="${not empty error}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <strong>Lỗi:</strong> ${error}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<c:if test="${not empty stockWarning}">
    <div class="alert alert-warning alert-dismissible fade show" role="alert">
        <strong>Cảnh báo tồn kho:</strong> ${stockWarning}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<div class="row">
    <!-- Customer & Delivery Form -->
    <div class="col-lg-6 mb-4">
        <div class="card shadow-sm h-100">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">Thông tin giao hàng</h5>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/checkout" method="post" id="checkoutForm">
                    <div class="mb-3">
                        <label for="recipient_name" class="form-label fw-semibold">Họ và tên người nhận <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="recipient_name" name="recipient_name" 
                               value="${not empty recipient_name ? recipient_name : sessionScope.currentUser.fullname}" 
                               placeholder="Ví dụ: Nguyễn Văn A" required />
                    </div>

                    <div class="mb-3">
                        <label for="recipient_phone" class="form-label fw-semibold">Số điện thoại nhận hàng <span class="text-danger">*</span></label>
                        <input type="tel" class="form-control" id="recipient_phone" name="recipient_phone" 
                               value="${not empty recipient_phone ? recipient_phone : sessionScope.currentUser.phone}" 
                               placeholder="Ví dụ: 0912345678" required />
                    </div>

                    <div class="mb-3">
                        <label for="shipping_address" class="form-label fw-semibold">Địa chỉ giao hàng chi tiết <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="shipping_address" name="shipping_address" rows="3" 
                                  placeholder="Số nhà, tên đường, phường/xã, quận/huyện, tỉnh/thành phố..." required>${not empty shipping_address ? shipping_address : ''}</textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Phương thức thanh toán</label>
                        <div class="form-check p-3 border rounded bg-light">
                            <input class="form-check-input" type="radio" name="payment_method" id="payment_cod" value="COD" checked>
                            <label class="form-check-label fw-bold text-success" for="payment_cod">
                                COD (Cash on Delivery) - Thanh toán khi nhận hàng
                            </label>
                            <div class="small text-muted mt-1">Bạn sẽ thanh toán tiền mặt trực tiếp cho nhân viên giao hàng khi nhận được sách.</div>
                        </div>
                    </div>

                    <div class="mt-4">
                        <button type="submit" class="btn btn-success btn-lg w-100" id="btnSubmitOrder">
                            Xác nhận đặt hàng (COD)
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Order Summary -->
    <div class="col-lg-6 mb-4">
        <div class="card shadow-sm h-100">
            <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Đơn hàng của bạn</h5>
                <span class="badge bg-warning text-dark">${cart.totalQuantity} quyển</span>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-striped align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Sách</th>
                                <th class="text-center" style="width: 70px;">SL</th>
                                <th class="text-end" style="width: 90px;">Đơn giá</th>
                                <th class="text-end" style="width: 110px;">Thành tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cart.itemList}">
                                <tr>
                                    <td>
                                        <div class="fw-semibold">${not empty item.book ? item.book.title : 'Sách #' + item.bookId}</div>
                                        <div class="small text-muted">Mã sách: ${item.bookId}</div>
                                        <c:if test="${not empty item.book and item.book.quantity < item.quantity}">
                                            <div class="badge bg-danger">Chỉ còn ${item.book.quantity} quyển trong kho!</div>
                                        </c:if>
                                    </td>
                                    <td class="text-center fw-bold">${item.quantity}</td>
                                    <td class="text-end">$${item.unitPrice}</td>
                                    <td class="text-end fw-bold text-success">$${item.subtotal}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot class="table-light">
                            <tr>
                                <th colspan="3" class="text-end">Tổng số lượng:</th>
                                <th class="text-end text-primary">${cart.totalQuantity} quyển</th>
                            </tr>
                            <tr>
                                <th colspan="3" class="text-end fs-5">Tổng tiền phải trả:</th>
                                <th class="text-end fs-5 text-danger fw-bold">$${cart.totalAmount}</th>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
            <div class="card-footer bg-light text-muted small">
                * Giá sách và số lượng tồn kho được xác thực tự động từ cơ sở dữ liệu khi xác nhận đơn hàng.
            </div>
        </div>
    </div>
</div>
</body>
</html>
