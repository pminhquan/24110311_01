package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.BookService_24110311;
import vn.iotstar.service.BookServiceImpl_24110311;

import java.io.IOException;

@WebServlet(urlPatterns = {"/", "/home"})
public class HomeController_24110311 extends HttpServlet {

    private static final String HOME_VIEW = "/views/home.jsp";
    private final BookService_24110311 bookService = new BookServiceImpl_24110311();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        int size = 6; // 6 books per page as required

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        long totalItems = bookService.count();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        if (totalPages == 0) {
            totalPages = 1;
        }
        if (page > totalPages) {
            page = totalPages;
        }

        request.setAttribute("books", bookService.findPage(page, size));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher(HOME_VIEW).forward(request, response);
    }
}
