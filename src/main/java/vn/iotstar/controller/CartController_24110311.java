package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.Book_24110311;
import vn.iotstar.entity.Cart_24110311;
import vn.iotstar.service.BookService_24110311;
import vn.iotstar.service.BookServiceImpl_24110311;

import java.io.IOException;

@WebServlet(urlPatterns = {
    "/cart", "/cart/add", "/cart/update", "/cart/remove", "/cart/clear"
})
public class CartController_24110311 extends HttpServlet {

    private final BookService_24110311 bookService = new BookServiceImpl_24110311();

    private Cart_24110311 getOrCreateCart(HttpSession session) {
        Cart_24110311 cart = (Cart_24110311) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart_24110311();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        Cart_24110311 cart = getOrCreateCart(session);

        switch (path) {
            case "/cart/remove":
                handleRemove(request, session, cart);
                response.sendRedirect(request.getContextPath() + "/cart");
                break;
            case "/cart/clear":
                cart.clear();
                session.setAttribute("cartSuccess", "Đã xóa toàn bộ giỏ hàng.");
                response.sendRedirect(request.getContextPath() + "/cart");
                break;
            default:
                // Reload current books to ensure latest prices and stock levels
                cart.reloadBooks(bookService);
                request.setAttribute("cart", cart);
                request.getRequestDispatcher("/views/cart/cart.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        Cart_24110311 cart = getOrCreateCart(session);

        switch (path) {
            case "/cart/add":
                handleAdd(request, session, cart);
                String redirect = request.getParameter("redirect");
                if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("://")) {
                    response.sendRedirect(request.getContextPath() + redirect);
                } else {
                    response.sendRedirect(request.getContextPath() + "/cart");
                }
                break;
            case "/cart/update":
                handleUpdate(request, session, cart);
                response.sendRedirect(request.getContextPath() + "/cart");
                break;
            case "/cart/remove":
                handleRemove(request, session, cart);
                response.sendRedirect(request.getContextPath() + "/cart");
                break;
            case "/cart/clear":
                cart.clear();
                session.setAttribute("cartSuccess", "Đã làm trống giỏ hàng.");
                response.sendRedirect(request.getContextPath() + "/cart");
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/cart");
                break;
        }
    }

    private void handleAdd(HttpServletRequest request, HttpSession session, Cart_24110311 cart) {
        String bookIdParam = request.getParameter("bookId");
        String qtyParam = request.getParameter("quantity");

        if (bookIdParam == null || bookIdParam.trim().isEmpty()) {
            session.setAttribute("cartError", "Mã sách không hợp lệ.");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdParam.trim());
            int quantity = 1;
            if (qtyParam != null && !qtyParam.trim().isEmpty()) {
                quantity = Integer.parseInt(qtyParam.trim());
            }

            if (quantity <= 0) {
                session.setAttribute("cartError", "Số lượng thêm vào giỏ phải lớn hơn 0.");
                return;
            }

            Book_24110311 book = bookService.findById(bookId);
            if (book == null) {
                session.setAttribute("cartError", "Sách không tồn tại.");
                return;
            }

            if (book.getQuantity() <= 0) {
                session.setAttribute("cartError", "Sách '" + book.getTitle() + "' đã hết hàng.");
                return;
            }

            cart.addItem(book, quantity);
            session.setAttribute("cartSuccess", "Đã thêm '" + book.getTitle() + "' vào giỏ hàng thành công!");
        } catch (NumberFormatException e) {
            session.setAttribute("cartError", "Định dạng số lượng hoặc mã sách không hợp lệ.");
        } catch (IllegalArgumentException e) {
            session.setAttribute("cartError", e.getMessage());
        } catch (Exception e) {
            session.setAttribute("cartError", "Có lỗi xảy ra khi thêm vào giỏ hàng: " + e.getMessage());
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpSession session, Cart_24110311 cart) {
        String bookIdParam = request.getParameter("bookId");
        String qtyParam = request.getParameter("quantity");

        if (bookIdParam == null || qtyParam == null) {
            session.setAttribute("cartError", "Dữ liệu cập nhật không đầy đủ.");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdParam.trim());
            int quantity = Integer.parseInt(qtyParam.trim());

            if (quantity <= 0) {
                session.setAttribute("cartError", "Số lượng cập nhật phải lớn hơn 0. Nếu muốn xóa, hãy bấm nút Xóa.");
                return;
            }

            Book_24110311 book = bookService.findById(bookId);
            if (book == null) {
                session.setAttribute("cartError", "Sách không tồn tại.");
                return;
            }

            cart.updateItem(bookId, quantity, book);
            session.setAttribute("cartSuccess", "Cập nhật số lượng sách '" + book.getTitle() + "' thành công!");
        } catch (NumberFormatException e) {
            session.setAttribute("cartError", "Số lượng không đúng định dạng số.");
        } catch (IllegalArgumentException e) {
            session.setAttribute("cartError", e.getMessage());
        } catch (Exception e) {
            session.setAttribute("cartError", "Có lỗi xảy ra khi cập nhật giỏ hàng: " + e.getMessage());
        }
    }

    private void handleRemove(HttpServletRequest request, HttpSession session, Cart_24110311 cart) {
        String bookIdParam = request.getParameter("bookId");
        if (bookIdParam != null && !bookIdParam.trim().isEmpty()) {
            try {
                int bookId = Integer.parseInt(bookIdParam.trim());
                cart.removeItem(bookId);
                session.setAttribute("cartSuccess", "Đã xóa sản phẩm khỏi giỏ hàng.");
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
