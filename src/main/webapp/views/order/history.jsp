<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lịch sử đơn hàng</title>
</head>
<body>
<div class="row mb-4">
    <div class="col-12">
        <h2 class="border-bottom pb-2">Lịch sử đơn hàng của bạn</h2>
    </div>
</div>

<!-- Filter Section -->
<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form method="get" action="${pageContext.request.contextPath}/orders/history" class="row g-3 align-items-center">
            <div class="col-auto">
                <label for="statusFilter" class="col-form-label fw-bold">Trạng thái:</label>
            </div>
            <div class="col-auto">
                <select id="statusFilter" name="status" class="form-select form-select-sm" onchange="this.form.submit()">
                    <option value="" ${empty selectedStatus ? 'selected' : ''}>-- Tất cả trạng thái --</option>
                    <c:forEach var="st" items="${statusList}">
                        <option value="${st}" ${selectedStatus eq st ? 'selected' : ''}>
                            <c:out value="${statusMap[st]}" /> (<c:out value="${st}" />)
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary btn-sm">Lọc</button>
                <c:if test="${not empty selectedStatus}">
                    <a href="${pageContext.request.contextPath}/orders/history" class="btn btn-outline-secondary btn-sm">Xóa lọc</a>
                </c:if>
            </div>
        </form>

        <div class="mt-3 d-flex flex-wrap gap-1 align-items-center">
            <small class="text-muted me-2">Lọc nhanh:</small>
            <a href="${pageContext.request.contextPath}/orders/history" 
               class="btn btn-sm ${empty selectedStatus ? 'btn-dark' : 'btn-outline-secondary'}">
                Tất cả
            </a>
            <c:forEach var="st" items="${statusList}">
                <a href="${pageContext.request.contextPath}/orders/history?status=${st}" 
                   class="btn btn-sm ${selectedStatus eq st ? 'btn-primary' : 'btn-outline-primary'}">
                    <c:out value="${statusMap[st]}" />
                </a>
            </c:forEach>
        </div>
    </div>
</div>

<!-- Orders List -->
<c:choose>
    <c:when test="${empty orders}">
        <div class="alert alert-info text-center py-4">
            <h5 class="alert-heading">Không tìm thấy đơn hàng nào</h5>
            <c:choose>
                <c:when test="${not empty selectedStatus}">
                    <p class="mb-2">Bạn không có đơn hàng nào ở trạng thái <strong><c:out value="${statusMap[selectedStatus]}" /> (<c:out value="${selectedStatus}" />)</strong>.</p>
                    <a href="${pageContext.request.contextPath}/orders/history" class="btn btn-sm btn-outline-primary">Xem tất cả đơn hàng</a>
                </c:when>
                <c:otherwise>
                    <p class="mb-2">Bạn chưa thực hiện đơn đặt hàng nào.</p>
                    <a href="${pageContext.request.contextPath}/books" class="btn btn-sm btn-primary">Bắt đầu mua sắm</a>
                </c:otherwise>
            </c:choose>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach var="order" items="${orders}">
            <div class="card shadow-sm mb-4 border-secondary">
                <div class="card-header bg-light d-flex justify-content-between align-items-center flex-wrap">
                    <div>
                        <span class="fw-bold me-2">Mã đơn hàng: #<c:out value="${order.orderId}" /></span>
                        <span class="text-muted small">| Ngày đặt: <c:out value="${order.formattedOrderDate}" /></span>
                    </div>
                    <div>
                        <span class="badge ${order.statusBadgeClass} fs-6">
                            <c:out value="${order.statusVietnamese}" /> (<c:out value="${order.status}" />)
                        </span>
                    </div>
                </div>
                <div class="card-body">
                    <!-- Recipient Summary -->
                    <div class="row mb-3 pb-2 border-bottom text-muted small">
                        <div class="col-md-4">
                            <strong>Người nhận:</strong> <c:out value="${order.recipientName}" />
                        </div>
                        <div class="col-md-3">
                            <strong>Điện thoại:</strong> <c:out value="${order.recipientPhone}" />
                        </div>
                        <div class="col-md-5">
                            <strong>Địa chỉ:</strong> <c:out value="${order.shippingAddress}" />
                        </div>
                        <div class="col-12 mt-1">
                            <strong>Phương thức thanh toán:</strong> <c:out value="${order.paymentMethod}" /> (Thanh toán khi nhận hàng)
                        </div>
                    </div>

                    <!-- Order Details Table -->
                    <div class="table-responsive">
                        <table class="table table-sm table-bordered align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th style="width: 40px;" class="text-center">#</th>
                                    <th>Tên sách</th>
                                    <th style="width: 130px;" class="text-end">Đơn giá mua</th>
                                    <th style="width: 90px;" class="text-center">Số lượng</th>
                                    <th style="width: 140px;" class="text-end">Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="detail" items="${order.orderDetails}" varStatus="loop">
                                    <tr>
                                        <td class="text-center">${loop.count}</td>
                                        <td>
                                            <strong>
                                                <c:choose>
                                                    <c:when test="${not empty detail.book}">
                                                        <c:out value="${detail.book.title}" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        Sách #<c:out value="${detail.book != null ? detail.book.bookid : ''}" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </strong>
                                        </td>
                                        <td class="text-end">$<c:out value="${detail.unitPrice}" /></td>
                                        <td class="text-center"><c:out value="${detail.quantity}" /></td>
                                        <td class="text-end fw-bold text-success">$<c:out value="${detail.subtotal}" /></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot class="table-light">
                                <tr>
                                    <th colspan="4" class="text-end fs-6">Tổng tiền:</th>
                                    <th class="text-end fs-6 text-danger fw-bold">$<c:out value="${order.totalAmount}" /></th>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

</body>
</html>
