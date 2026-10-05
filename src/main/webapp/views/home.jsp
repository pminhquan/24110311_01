<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home - Book Catalog</title>
</head>
<body>
<div class="p-4 mb-4 bg-light rounded-3 shadow-sm text-center">
    <h1 class="display-6 fw-bold">Book Catalog</h1>
    <p class="text-muted mb-0">Practical Exam - Servlet + JPA + JSP</p>
</div>

<!-- Books Grid -->
<div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
    <c:forEach var="item" items="${books}">
        <div class="col">
            <div class="card h-100 shadow-sm">
                <div class="text-center pt-3 bg-light">
                    <a href="${pageContext.request.contextPath}/book/detail?id=${item.bookid}">
                        <c:choose>
                            <c:when test="${not empty item.cover_image}">
                                <img src="${item.cover_image}" alt="${item.title}" class="img-fluid rounded" style="max-height: 180px; object-fit: contain;" onerror="this.src='https://via.placeholder.com/140x180?text=No+Cover';" />
                            </c:when>
                            <c:otherwise>
                                <img src="https://via.placeholder.com/140x180?text=No+Cover" alt="No Cover" class="img-fluid rounded" style="max-height: 180px;" />
                            </c:otherwise>
                        </c:choose>
                    </a>
                </div>
                <div class="card-body d-flex flex-column">
                    <h5 class="card-title">
                        <a href="${pageContext.request.contextPath}/book/detail?id=${item.bookid}" class="text-decoration-none text-primary">${item.title}</a>
                    </h5>
                    <p class="card-text text-muted small mb-2">
                        <strong>ISBN:</strong> ${not empty item.isbn ? item.isbn : 'N/A'}<br/>
                        <strong>Publisher:</strong> ${not empty item.publisher ? item.publisher : 'N/A'}<br/>
                        <strong>Publish Date:</strong> ${not empty item.publish_date ? item.publish_date : 'N/A'}<br/>
                        <strong>Quantity:</strong> <span class="badge ${item.quantity > 0 ? 'bg-success' : 'bg-danger'}">${item.quantity} in stock</span><br/>
                        <strong>Price:</strong> <span class="text-danger fw-bold">$${item.price}</span>
                    </p>

                    <div class="mb-2">
                        <strong>Author(s):</strong><br/>
                        <c:forEach var="a" items="${item.authors}" varStatus="status">
                            <span class="badge bg-secondary">${a.author_name}</span>${!status.last ? ' ' : ''}
                        </c:forEach>
                        <c:if test="${empty item.authors}">
                            <span class="text-muted small">N/A</span>
                        </c:if>
                    </div>

                    <c:if test="${not empty item.description}">
                        <p class="card-text small text-secondary text-truncate mb-2" title="${item.description}">${item.description}</p>
                    </c:if>

                    <div class="mt-auto pt-2 border-top">
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="badge bg-info text-dark">
                                <c:choose>
                                    <c:when test="${not empty item.ratings}">
                                        ${fn:length(item.ratings)} review(s)
                                    </c:when>
                                    <c:otherwise>
                                        0 reviews
                                    </c:otherwise>
                                </c:choose>
                            </span>
                            <div class="btn-group">
                                <a href="${pageContext.request.contextPath}/book/detail?id=${item.bookid}" class="btn btn-sm btn-outline-primary">Details</a>
                                <c:choose>
                                    <c:when test="${item.quantity > 0}">
                                        <form action="${pageContext.request.contextPath}/cart/add" method="post" class="d-inline">
                                            <input type="hidden" name="bookId" value="${item.bookid}" />
                                            <input type="hidden" name="quantity" value="1" />
                                            <input type="hidden" name="redirect" value="/home?page=${currentPage}" />
                                            <button type="submit" class="btn btn-sm btn-success rounded-start-0">+ Giỏ hàng</button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-sm btn-secondary rounded-start-0" disabled>Hết hàng</button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
    <c:if test="${empty books}">
        <div class="col-12 text-center text-muted py-5">
            <p class="fs-4">No books found in catalog.</p>
        </div>
    </c:if>
</div>

<!-- Pagination Controls -->
<div class="d-flex justify-content-between align-items-center mt-4">
    <div>
        Page <strong>${currentPage}</strong> of <strong>${totalPages}</strong>
    </div>
    <nav>
        <ul class="pagination mb-0">
            <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                <a class="page-link" href="${pageContext.request.contextPath}/home?page=${currentPage - 1}">Previous</a>
            </li>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <li class="page-item ${currentPage == i ? 'active' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/home?page=${i}">${i}</a>
                </li>
            </c:forEach>
            <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                <a class="page-link" href="${pageContext.request.contextPath}/home?page=${currentPage + 1}">Next</a>
            </li>
        </ul>
    </nav>
</div>
</body>
</html>
