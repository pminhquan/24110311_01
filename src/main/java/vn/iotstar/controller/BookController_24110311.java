package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.Author_24110311;
import vn.iotstar.entity.Book_24110311;
import vn.iotstar.entity.User_24110311;
import vn.iotstar.service.AuthorService_24110311;
import vn.iotstar.service.AuthorServiceImpl_24110311;
import vn.iotstar.service.BookService_24110311;
import vn.iotstar.service.BookServiceImpl_24110311;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {
    "/books", "/books/add", "/books/edit", "/books/delete",
    "/book", "/book/add", "/book/edit", "/book/delete"
})
public class BookController_24110311 extends HttpServlet {

    private final BookService_24110311 bookService = new BookServiceImpl_24110311();
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
            path = "/books/" + action;
        }

        switch (path) {
            case "/books/add":
            case "/book/add":
                if (!isAdmin(request)) {
                    response.sendRedirect(request.getContextPath() + "/books");
                    return;
                }
                request.setAttribute("authors", authorService.findAll());
                request.getRequestDispatcher("/WEB-INF/views/book/add.jsp").forward(request, response);
                break;
            case "/books/edit":
            case "/book/edit":
                if (!isAdmin(request)) {
                    response.sendRedirect(request.getContextPath() + "/books");
                    return;
                }
                try {
                    int editId = Integer.parseInt(request.getParameter("id"));
                    Book_24110311 book = bookService.findById(editId);
                    request.setAttribute("book", book);
                    request.setAttribute("authors", authorService.findAll());
                    request.getRequestDispatcher("/WEB-INF/views/book/edit.jsp").forward(request, response);
                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/books");
                }
                break;
            case "/books/delete":
            case "/book/delete":
                if (!isAdmin(request)) {
                    response.sendRedirect(request.getContextPath() + "/books");
                    return;
                }
                try {
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    bookService.delete(deleteId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.sendRedirect(request.getContextPath() + "/books");
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
                long totalItems = bookService.count();
                int totalPages = (int) Math.ceil((double) totalItems / size);
                if (totalPages == 0) totalPages = 1;
                if (page > totalPages) page = totalPages;

                request.setAttribute("books", bookService.findPage(page, size));
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.getRequestDispatcher("/views/book/list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/books");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String path = request.getServletPath();

        if (action != null) {
            path = "/books/" + action;
        }

        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String publisher = request.getParameter("publisher");
        String priceStr = request.getParameter("price");
        String description = request.getParameter("description");
        String publishDateStr = request.getParameter("publish_date");
        String coverImage = request.getParameter("cover_image");
        String quantityStr = request.getParameter("quantity");
        String[] authorIds = request.getParameterValues("authorIds");

        BigDecimal price = BigDecimal.ZERO;
        try {
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                price = new BigDecimal(priceStr.trim());
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    price = BigDecimal.ZERO;
                }
            }
        } catch (Exception ignored) {
        }

        int quantity = 0;
        try {
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                quantity = Integer.parseInt(quantityStr.trim());
            }
        } catch (NumberFormatException ignored) {
        }

        LocalDate publishDate = null;
        try {
            if (publishDateStr != null && !publishDateStr.trim().isEmpty()) {
                publishDate = LocalDate.parse(publishDateStr.trim());
            }
        } catch (Exception ignored) {
        }

        List<Author_24110311> selectedAuthors = new ArrayList<>();
        if (authorIds != null) {
            for (String idStr : authorIds) {
                try {
                    int authorId = Integer.parseInt(idStr);
                    Author_24110311 author = authorService.findById(authorId);
                    if (author != null) {
                        selectedAuthors.add(author);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if ("/books/edit".equals(path) || "/book/edit".equals(path) || "update".equalsIgnoreCase(action)) {
            try {
                int bookid = Integer.parseInt(request.getParameter("bookid"));
                Book_24110311 book = new Book_24110311(bookid, isbn, title, publisher, price, description, publishDate, coverImage, quantity);
                book.setAuthors(selectedAuthors);
                bookService.update(book);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Book_24110311 book = new Book_24110311(isbn, title, publisher, price, description, publishDate, coverImage, quantity);
            book.setAuthors(selectedAuthors);
            bookService.insert(book);
        }

        response.sendRedirect(request.getContextPath() + "/books");
    }
}
