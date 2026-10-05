<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>${book.title} - Book Details</title>
</head>
<body>
<div class="mb-3">
    <a href="${pageContext.request.contextPath}/books" class="btn btn-outline-secondary btn-sm">&larr; Back to Books</a>
    <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary btn-sm">Home</a>
    <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline-primary btn-sm ms-2">Xem giỏ hàng</a>
</div>

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

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <div class="row">
            <!-- Cover Image -->
            <div class="col-md-4 text-center mb-3 mb-md-0">
                <c:choose>
                    <c:when test="${not empty book.cover_image}">
                        <img src="${book.cover_image}" alt="${book.title}" class="img-fluid rounded shadow-sm" style="max-height: 350px; object-fit: contain;" onerror="this.src='https://via.placeholder.com/250x350?text=No+Cover';" />
                    </c:when>
                    <c:otherwise>
                        <img src="https://via.placeholder.com/250x350?text=No+Cover" alt="No Cover" class="img-fluid rounded shadow-sm" />
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Book Information -->
            <div class="col-md-8">
                <h2 class="text-primary mb-2">${book.title}</h2>
                <div class="mb-3">
                    <span class="badge bg-danger fs-6">$${book.price}</span>
                    <span class="badge ${book.quantity > 0 ? 'bg-success' : 'bg-danger'} fs-6">
                        ${book.quantity > 0 ? 'In Stock' : 'Out of Stock'} (${book.quantity})
                    </span>
                </div>

                <table class="table table-bordered table-sm mb-3">
                    <tbody>
                        <tr>
                            <th style="width: 150px;" class="bg-light">ISBN</th>
                            <td>${not empty book.isbn ? book.isbn : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th class="bg-light">Author(s)</th>
                            <td>
                                <c:forEach var="a" items="${book.authors}" varStatus="status">
                                    <span class="badge bg-secondary">${a.author_name}</span>${!status.last ? ' ' : ''}
                                </c:forEach>
                                <c:if test="${empty book.authors}">
                                    <span class="text-muted">N/A</span>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <th class="bg-light">Publisher</th>
                            <td>${not empty book.publisher ? book.publisher : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th class="bg-light">Publish Date</th>
                            <td>${not empty book.publish_date ? book.publish_date : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th class="bg-light">Quantity</th>
                            <td>${book.quantity}</td>
                        </tr>
                    </tbody>
                </table>

                <c:choose>
                    <c:when test="${book.quantity > 0}">
                        <form action="${pageContext.request.contextPath}/cart/add" method="post" class="d-flex align-items-center gap-2 mb-3">
                            <input type="hidden" name="bookId" value="${book.bookid}" />
                            <input type="hidden" name="redirect" value="/book/detail?id=${book.bookid}" />
                            <label for="cartQty" class="form-label mb-0 fw-semibold">Số lượng:</label>
                            <input type="number" id="cartQty" name="quantity" value="1" min="1" max="${book.quantity}" class="form-control form-control-sm" style="width: 80px;" required />
                            <button type="submit" class="btn btn-success btn-sm">+ Thêm vào giỏ hàng</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning py-2 mb-3">Sản phẩm hiện đang tạm hết hàng.</div>
                    </c:otherwise>
                </c:choose>

                <div class="mb-3">
                    <h5>Description</h5>
                    <p class="text-muted">${not empty book.description ? book.description : 'No description provided.'}</p>
                </div>

                <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.admin}">
                    <div>
                        <a href="${pageContext.request.contextPath}/books/edit?id=${book.bookid}" class="btn btn-warning btn-sm">Edit Book</a>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<!-- Reviews Section -->
<div class="row">
    <!-- Existing Reviews List -->
    <div class="col-md-7">
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Customer Reviews</h5>
                <span class="badge bg-info text-dark">${fn:length(ratings)} review(s)</span>
            </div>
            <div class="card-body">
                <c:forEach var="r" items="${ratings}">
                    <div class="border-bottom pb-2 mb-3">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="text-warning fw-bold">
                                    <c:forEach begin="1" end="${r.rating}">★</c:forEach>
                                </span>
                                <span class="text-muted small">(${r.rating}/5)</span>
                            </div>
                            <small class="text-muted"><c:out value="${not empty r.user ? r.user.fullname : 'User'}" /></small>
                        </div>
                        <p class="mb-0 mt-1 text-secondary"><c:out value="${not empty r.review_text ? r.review_text : 'No written review.'}" /></p>
                    </div>
                </c:forEach>
                <c:if test="${empty ratings}">
                    <p class="text-muted text-center py-4 mb-0">No reviews yet. Be the first to review this book!</p>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Submit Review Form -->
    <div class="col-md-5">
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">Write a Review</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <form action="${pageContext.request.contextPath}/book/detail" method="post">
                            <input type="hidden" name="bookId" value="${book.bookid}" />

                            <div class="mb-3">
                                <label for="rating" class="form-label">Rating</label>
                                <select class="form-select" id="rating" name="rating" required>
                                    <option value="5" selected>★★★★★ (5 - Excellent)</option>
                                    <option value="4">★★★★☆ (4 - Good)</option>
                                    <option value="3">★★★☆☆ (3 - Average)</option>
                                    <option value="2">★★☆☆☆ (2 - Poor)</option>
                                    <option value="1">★☆☆☆☆ (1 - Terrible)</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="review_text" class="form-label">Review</label>
                                <textarea class="form-control" id="review_text" name="review_text" rows="4" placeholder="Share your thoughts about this book..." required></textarea>
                            </div>

                            <button type="submit" class="btn btn-primary w-100">Submit Review</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-4">
                            <p class="text-muted mb-3">Vui lòng đăng nhập để gửi đánh giá về sách này.</p>
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary btn-sm">Đăng nhập</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
</body>
</html>
