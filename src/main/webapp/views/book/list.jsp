<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Book List</title>
</head>
<body>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2>Book List</h2>
    <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.admin}">
        <a href="${pageContext.request.contextPath}/books/add" class="btn btn-primary">+ Add New Book</a>
    </c:if>
</div>

<table class="table table-bordered table-hover align-middle">
    <thead class="table-dark">
        <tr>
            <th style="width: 60px;">ID</th>
            <th style="width: 80px;">Cover</th>
            <th>Title</th>
            <th style="width: 150px;">Authors</th>
            <th style="width: 140px;">ISBN</th>
            <th style="width: 140px;">Publisher</th>
            <th style="width: 90px;">Price</th>
            <th style="width: 80px;">Quantity</th>
            <th style="width: 120px;">Publish Date</th>
            <th style="width: 200px;">Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="item" items="${books}">
            <tr>
                <td>${item.bookid}</td>
                <td class="text-center">
                    <c:choose>
                        <c:when test="${not empty item.cover_image}">
                            <img src="${item.cover_image}" alt="${item.title}" style="max-height: 50px; max-width: 50px;" class="img-thumbnail" onerror="this.src='https://via.placeholder.com/50x70?text=Book';"/>
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted small">No Image</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <strong>${item.title}</strong>
                    <c:if test="${not empty item.description}">
                        <div class="text-muted small text-truncate" style="max-width: 250px;">${item.description}</div>
                    </c:if>
                </td>
                <td>
                    <c:forEach var="a" items="${item.authors}" varStatus="status">
                        <span class="badge bg-secondary">${a.author_name}</span>${!status.last ? ' ' : ''}
                    </c:forEach>
                    <c:if test="${empty item.authors}">
                        <span class="text-muted small">None</span>
                    </c:if>
                </td>
                <td>${item.isbn}</td>
                <td>${item.publisher}</td>
                <td>$${item.price}</td>
                <td>${item.quantity}</td>
                <td>${item.publish_date}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/book/detail?id=${item.bookid}" class="btn btn-sm btn-info text-white">Detail</a>
                    <c:choose>
                        <c:when test="${item.quantity > 0}">
                            <form action="${pageContext.request.contextPath}/cart/add" method="post" class="d-inline">
                                <input type="hidden" name="bookId" value="${item.bookid}" />
                                <input type="hidden" name="quantity" value="1" />
                                <input type="hidden" name="redirect" value="/books?page=${currentPage}" />
                                <button type="submit" class="btn btn-sm btn-success">+ Cart</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-sm btn-secondary" disabled>Hết</button>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.admin}">
                        <a href="${pageContext.request.contextPath}/books/edit?id=${item.bookid}" class="btn btn-sm btn-warning">Edit</a>
                        <a href="${pageContext.request.contextPath}/books/delete?id=${item.bookid}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this book?');">Delete</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty books}">
            <tr>
                <td colspan="10" class="text-center text-muted py-4">No books found.</td>
            </tr>
        </c:if>
    </tbody>
</table>

<!-- Pagination Controls -->
<div class="d-flex justify-content-between align-items-center mt-3">
    <div>
        Page <strong>${currentPage}</strong> of <strong>${totalPages}</strong>
    </div>
    <nav>
        <ul class="pagination mb-0">
            <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                <a class="page-link" href="${pageContext.request.contextPath}/books?page=${currentPage - 1}">Previous</a>
            </li>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <li class="page-item ${currentPage == i ? 'active' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/books?page=${i}">${i}</a>
                </li>
            </c:forEach>
            <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                <a class="page-link" href="${pageContext.request.contextPath}/books?page=${currentPage + 1}">Next</a>
            </li>
        </ul>
    </nav>
</div>
</body>
</html>
