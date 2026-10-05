<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${empty sessionScope.currentUser or not sessionScope.currentUser.admin}">
    <c:redirect url="/books" />
</c:if>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Book</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow-sm">
            <div class="card-header bg-warning">
                <h4 class="mb-0">Edit Book</h4>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/books/edit" method="post">
                    <input type="hidden" name="bookid" value="${book.bookid}" />

                    <div class="mb-3">
                        <label class="form-label">Book ID</label>
                        <input type="text" class="form-control" value="${book.bookid}" disabled />
                    </div>

                    <div class="mb-3">
                        <label for="title" class="form-label">Title <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="title" name="title" value="${book.title}" required autofocus />
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="isbn" class="form-label">ISBN</label>
                            <input type="text" class="form-control" id="isbn" name="isbn" value="${book.isbn}" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="publisher" class="form-label">Publisher</label>
                            <input type="text" class="form-control" id="publisher" name="publisher" value="${book.publisher}" />
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label for="price" class="form-label">Price</label>
                            <input type="number" step="0.01" min="0" class="form-control" id="price" name="price" value="${book.price}" />
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="quantity" class="form-label">Quantity</label>
                            <input type="number" min="0" class="form-control" id="quantity" name="quantity" value="${book.quantity}" />
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="publish_date" class="form-label">Publish Date</label>
                            <input type="date" class="form-control" id="publish_date" name="publish_date" value="${book.publish_date}" />
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="authorIds" class="form-label">Authors</label>
                        <select class="form-select" id="authorIds" name="authorIds" multiple size="4">
                            <c:forEach var="author" items="${authors}">
                                <c:set var="isSelected" value="false" />
                                <c:forEach var="bookAuthor" items="${book.authors}">
                                    <c:if test="${bookAuthor.author_id == author.author_id}">
                                        <c:set var="isSelected" value="true" />
                                    </c:if>
                                </c:forEach>
                                <option value="${author.author_id}" ${isSelected ? 'selected' : ''}>${author.author_name}</option>
                            </c:forEach>
                        </select>
                        <div class="form-text">Hold Ctrl (or Cmd) to select multiple authors.</div>
                    </div>

                    <div class="mb-3">
                        <label for="cover_image" class="form-label">Cover Image (URL or Filename)</label>
                        <input type="text" class="form-control" id="cover_image" name="cover_image" value="${book.cover_image}" />
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <textarea class="form-control" id="description" name="description" rows="3">${book.description}</textarea>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-warning">Update Book</button>
                        <a href="${pageContext.request.contextPath}/books" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
