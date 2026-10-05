<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Author</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <h4 class="mb-0">Add New Author</h4>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/authors/add" method="post">
                    <div class="mb-3">
                        <label for="author_name" class="form-label">Author Name <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="author_name" name="author_name" required autofocus />
                    </div>

                    <div class="mb-3">
                        <label for="date_of_birth" class="form-label">Date of Birth</label>
                        <input type="date" class="form-control" id="date_of_birth" name="date_of_birth" />
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-success">Save Author</button>
                        <a href="${pageContext.request.contextPath}/authors" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
