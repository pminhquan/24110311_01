<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Author List</title>
</head>
<body>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2>Author List</h2>
    <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.admin}">
        <a href="${pageContext.request.contextPath}/authors/add" class="btn btn-primary">+ Add New Author</a>
    </c:if>
</div>

<table class="table table-bordered table-hover align-middle">
    <thead class="table-dark">
        <tr>
            <th style="width: 80px;">ID</th>
            <th>Author Name</th>
            <th style="width: 200px;">Date of Birth</th>
            <th style="width: 180px;">Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="item" items="${authors}">
            <tr>
                <td>${item.author_id}</td>
                <td><strong>${item.author_name}</strong></td>
                <td>${item.date_of_birth}</td>
                <td>
                    <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.admin}">
                        <a href="${pageContext.request.contextPath}/authors/edit?id=${item.author_id}" class="btn btn-sm btn-warning">Edit</a>
                        <a href="${pageContext.request.contextPath}/authors/delete?id=${item.author_id}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this author?');">Delete</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty authors}">
            <tr>
                <td colspan="4" class="text-center text-muted py-4">No authors found.</td>
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
                <a class="page-link" href="${pageContext.request.contextPath}/authors?page=${currentPage - 1}">Previous</a>
            </li>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <li class="page-item ${currentPage == i ? 'active' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/authors?page=${i}">${i}</a>
                </li>
            </c:forEach>
            <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                <a class="page-link" href="${pageContext.request.contextPath}/authors?page=${currentPage + 1}">Next</a>
            </li>
        </ul>
    </nav>
</div>
</body>
</html>
