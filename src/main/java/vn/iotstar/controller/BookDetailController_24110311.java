package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Book_24110311;
import vn.iotstar.entity.Rating_24110311;
import vn.iotstar.entity.User_24110311;
import vn.iotstar.service.BookService_24110311;
import vn.iotstar.service.BookServiceImpl_24110311;
import vn.iotstar.service.RatingService_24110311;
import vn.iotstar.service.RatingServiceImpl_24110311;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/book/detail", "/books/detail"})
public class BookDetailController_24110311 extends HttpServlet {

    private final BookService_24110311 bookService = new BookServiceImpl_24110311();
    private final RatingService_24110311 ratingService = new RatingServiceImpl_24110311();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            idParam = request.getParameter("bookId");
        }

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/books");
            return;
        }

        try {
            int bookId = Integer.parseInt(idParam.trim());
            Book_24110311 book = bookService.findById(bookId);
            if (book == null) {
                response.sendRedirect(request.getContextPath() + "/books");
                return;
            }

            List<Rating_24110311> ratings = ratingService.findRatingsByBook(bookId);
            request.setAttribute("book", book);
            request.setAttribute("ratings", ratings);
            request.getRequestDispatcher("/views/book/detail.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/books");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        User_24110311 currentUser = (User_24110311) request.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String bookIdStr = request.getParameter("bookId");
        String ratingStr = request.getParameter("rating");
        String reviewText = request.getParameter("review_text");

        if (bookIdStr != null && !bookIdStr.trim().isEmpty()) {
            try {
                int bookId = Integer.parseInt(bookIdStr.trim());
                int ratingVal = 5;
                if (ratingStr != null && !ratingStr.trim().isEmpty()) {
                    ratingVal = Integer.parseInt(ratingStr.trim());
                }

                Book_24110311 book = new Book_24110311();
                book.setBookid(bookId);

                Rating_24110311 rating = new Rating_24110311();
                rating.setBook(book);
                rating.setRating(ratingVal);
                rating.setReview_text(reviewText != null ? reviewText.trim() : "");
                rating.setUser(currentUser);

                ratingService.addRating(rating);

                response.sendRedirect(request.getContextPath() + "/book/detail?id=" + bookId);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect(request.getContextPath() + "/books");
    }
}
