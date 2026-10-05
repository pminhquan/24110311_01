<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Giỏ hàng - Shopping Cart</title>
</head>
<body>
<div class="mb-3">
    <a href="${pageContext.request.contextPath}/books" class="btn btn-outline-secondary btn-sm">&larr; Tiếp tục chọn sách</a>
</div>

<h2 class="mb-3">Giỏ hàng của bạn</h2>

<c:if test="${not empty sessionScope.cartSuccess}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        ${sessionScope.cartSuccess}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="cartSuccess" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.cartError}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        ${sessionScope.cartError}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="cartError" scope="session"/>
</c:if>

<c:choose>
    <c:when test="${empty cart or cart.isEmpty()}">
        <div class="card text-center p-5 shadow-sm">
            <div class="card-body">
                <h4 class="card-title text-muted">Giỏ hàng đang trống</h4>
                <p class="card-text text-secondary">Bạn chưa có sản phẩm nào trong giỏ hàng. Hãy khám phá danh mục sách ngay!</p>
                <a href="${pageContext.request.contextPath}/books" class="btn btn-primary mt-2">Xem danh sách sách</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-responsive shadow-sm rounded mb-4">
            <table class="table table-bordered table-hover align-middle mb-0">
                <thead class="table-dark">
                    <tr>
                        <th style="width: 50px;" class="text-center">#</th>
                        <th style="width: 80px;" class="text-center">Ảnh</th>
                        <th>Tên sách</th>
                        <th style="width: 110px;" class="text-end">Đơn giá</th>
                        <th style="width: 180px;" class="text-center">Số lượng</th>
                        <th style="width: 120px;" class="text-end">Thành tiền</th>
                        <th style="width: 100px;" class="text-center">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${cart.itemList}" varStatus="status">
                        <tr>
                            <td class="text-center">${status.count}</td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${not empty item.book and not empty item.book.cover_image}">
                                        <img src="${item.book.cover_image}" alt="${item.book.title}" style="max-height: 50px; max-width: 50px;" class="img-thumbnail" onerror="this.src='https://via.placeholder.com/50x70?text=Book';" />
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted small">No Image</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <strong>
                                    <a href="${pageContext.request.contextPath}/book/detail?id=${item.bookId}" class="text-decoration-none text-dark">
                                        ${not empty item.book ? item.book.title : 'Sách #' + item.bookId}
                                    </a>
                                </strong>
                                <c:if test="${not empty item.book}">
                                    <div class="small text-muted">
                                        Tồn kho: <span class="${item.book.quantity < item.quantity ? 'text-danger fw-bold' : ''}">${item.book.quantity}</span> quyển
                                    </div>
                                    <c:if test="${item.book.quantity < item.quantity}">
                                        <span class="badge bg-danger">Vượt quá tồn kho (${item.book.quantity})</span>
                                    </c:if>
                                </c:if>
                            </td>
                            <td class="text-end text-danger fw-bold">$${item.unitPrice}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart/update" method="post" class="d-flex justify-content-center align-items-center gap-2">
                                    <input type="hidden" name="bookId" value="${item.bookId}" />
                                    <input type="number" name="quantity" value="${item.quantity}" min="1" max="${not empty item.book ? item.book.quantity : 999}" class="form-control form-control-sm text-center" style="width: 70px;" required />
                                    <button type="submit" class="btn btn-sm btn-outline-primary" title="Cập nhật">Lưu</button>
                                </form>
                            </td>
                            <td class="text-end fw-bold text-success">$${item.subtotal}</td>
                            <td class="text-center">
                                <form action="${pageContext.request.contextPath}/cart/remove" method="post" onsubmit="return confirm('Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?');">
                                    <input type="hidden" name="bookId" value="${item.bookId}" />
                                    <button type="submit" class="btn btn-sm btn-outline-danger">Xóa</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot class="table-light">
                    <tr>
                        <th colspan="4" class="text-end">Tổng số lượng sản phẩm:</th>
                        <th class="text-center fs-6 text-primary">${cart.totalQuantity}</th>
                        <th colspan="2"></th>
                    </tr>
                    <tr>
                        <th colspan="5" class="text-end fs-5">Tổng tiền thanh toán:</th>
                        <th class="text-end fs-5 text-danger fw-bold">$${cart.totalAmount}</th>
                        <th></th>
                    </tr>
                </tfoot>
            </table>
        </div>

        <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
            <form action="${pageContext.request.contextPath}/cart/clear" method="post" onsubmit="return confirm('Bạn có chắc muốn xóa sạch toàn bộ giỏ hàng?');">
                <button type="submit" class="btn btn-outline-danger">Xóa toàn bộ giỏ hàng</button>
            </form>
            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/books" class="btn btn-outline-secondary">Mua thêm sách khác</a>
                <a href="${pageContext.request.contextPath}/checkout" class="btn btn-success btn-lg">Tiến hành thanh toán (COD) &rarr;</a>
            </div>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>
