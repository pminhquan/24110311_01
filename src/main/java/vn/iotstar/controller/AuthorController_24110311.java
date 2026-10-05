package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.Author_24110311;
import vn.iotstar.entity.User_24110311;
import vn.iotstar.service.AuthorService_24110311;
import vn.iotstar.service.AuthorServiceImpl_24110311;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(urlPatterns = {
    "/authors", "/authors/add", "/authors/edit", "/authors/delete",
    "/author", "/author/add", "/author/edit", "/author/delete"
})
public class AuthorController_24110311 extends HttpServlet {

    private final AuthorService_24110311 authorService = new AuthorServiceImpl_24110311();

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User_24110311 user = (User_24110311) session.getAttribute("currentUser");
            return user != null && user.isAdmin();
        }
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String path = request.getServletPath();

        if (action != null) {
            path = "/authors/" + action;
        }

        switch (path) {
            case "/authors/add":
            case "/author/add":
                if (!isAdmin(request)) {
                    response.sendRedirect(request.getContextPath() + "/authors");
                    return;
                }
                request.getRequestDispatcher("/views/author/add.jsp").forward(request, response);
                break;
            case "/authors/edit":
            case "/author/edit":
                if (!isAdmin(request)) {
                    response.sendRedirect(request.getContextPath() + "/authors");
                    return;
                }
                try {
                    int editId = Integer.parseInt(request.getParameter("id"));
                    Author_24110311 author = authorService.findById(editId);
                    request.setAttribute("author", author);
                    request.getRequestDispatcher("/views/author/edit.jsp").forward(request, response);
                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/authors");
                }
                break;
            case "/authors/delete":
            case "/author/delete":
                if (!isAdmin(request)) {
                    response.sendRedirect(request.getContextPath() + "/authors");
                    return;
                }
                try {
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    authorService.delete(deleteId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.sendRedirect(request.getContextPath() + "/authors");
                break;
            default:
                int page = 1;
                int size = 5;
                String pageParam = request.getParameter("page");
                if (pageParam != null) {
                    try {
                        page = Integer.parseInt(pageParam);
                        if (page < 1) page = 1;
                    } catch (NumberFormatException ignored) {
                    }
                }
                long totalItems = authorService.count();
                int totalPages = (int) Math.ceil((double) totalItems / size);
                if (totalPages == 0) totalPages = 1;
                if (page > totalPages) page = totalPages;

                request.setAttribute("authors", authorService.findPage(page, size));
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.getRequestDispatcher("/views/author/list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/authors");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String path = request.getServletPath();

        if (action != null) {
            path = "/authors/" + action;
        }

        if ("/authors/delete".equals(path) || "/author/delete".equals(path) || "delete".equalsIgnoreCase(action)) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
            return;
        }

        String authorName = request.getParameter("author_name");
        String dateOfBirthStr = request.getParameter("date_of_birth");

        LocalDate dateOfBirth = null;
        try {
            if (dateOfBirthStr != null && !dateOfBirthStr.trim().isEmpty()) {
                dateOfBirth = LocalDate.parse(dateOfBirthStr.trim());
            }
        } catch (Exception ignored) {
        }

        if ("/authors/edit".equals(path) || "/author/edit".equals(path) || "update".equalsIgnoreCase(action)) {
            try {
                int authorId = Integer.parseInt(request.getParameter("author_id"));
                Author_24110311 existing = authorService.findById(authorId);
                if (existing != null) {
                    Author_24110311 author = new Author_24110311(authorId, authorName, dateOfBirth);
                    authorService.update(author);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Author_24110311 author = new Author_24110311(authorName, dateOfBirth);
            authorService.insert(author);
        }

        response.sendRedirect(request.getContextPath() + "/authors");
    }
}
