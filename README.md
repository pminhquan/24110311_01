# Java Web Servlet + JPA + JSP Project

A Java Web application developed using **Jakarta Servlet 6.0**, **JPA 3.1 / Hibernate 6.x**, **JSP/JSTL 3.x**, and **SiteMesh 3**.

The project includes user authentication, book/author management, ratings, shopping cart, COD checkout, order history, and status filtering.

Student MSSV: **24110311**

## Technology Stack

- Java 17
- Maven WAR project
- Jakarta Servlet 6.0
- Jakarta Server Pages (JSP) 3.1
- Jakarta Standard Tag Library (JSTL) 3.0
- Jakarta Persistence API (JPA) 3.1 & Hibernate ORM 6.5
- SiteMesh 3.2.x (Decorator & Layout management)
- Microsoft SQL Server JDBC Driver
- Target Server: Apache Tomcat 10.1+

## Database Schema & Entities

1. **User_24110311** (`[User]`): `id`, `email`, `fullname`, `phone`, `passwd`, `signup_date`, `last_login`, `is_admin`
2. **Book_24110311** (`Book`): `bookid`, `isbn`, `title`, `publisher`, `price`, `description`, `publish_date`, `cover_image`, `quantity`
3. **Author_24110311** (`Author`): `author_id`, `author_name`, `date_of_birth`
4. **Rating_24110311** (`Rating`): `rating_id`, `userid`, `bookid`, `rating`, `review_text`
5. **Orders_24110311** (`Orders`): `order_id`, `user_id`, `recipient_name`, `recipient_phone`, `shipping_address`, `payment_method`, `status`, `total_amount`, `order_date`
6. **OrderDetail_24110311** (`OrderDetail`): `detail_id`, `order_id`, `bookid`, `quantity`, `unit_price`

### Relationships
- **Author_24110311 <-> Book_24110311**: Many-to-Many via join table `book_author`.
- **User_24110311 -> Rating_24110311**: One-to-Many (`userid`).
- **Book_24110311 -> Rating_24110311**: One-to-Many (`bookid`).
- **User_24110311 -> Orders_24110311**: One-to-Many (`user_id`).
- **Orders_24110311 -> OrderDetail_24110311**: One-to-Many (`order_id`).
- **Book_24110311 -> OrderDetail_24110311**: Many-to-One (`bookid`).

## Architecture (3-Layer MVC)

```text
src/
└── main/
    ├── java/vn/iotstar/
    │   ├── config/
    │   │   └── JpaConfig.java                  # EntityManagerFactory & EntityManager helper
    │   ├── controller/
    │   │   ├── HomeController_24110311.java     # Home catalog controller
    │   │   ├── BookController_24110311.java     # Book catalog & admin CRUD controller
    │   │   ├── BookDetailController_24110311.java # Book detail & reviews controller
    │   │   ├── CartController_24110311.java     # Session shopping cart controller
    │   │   ├── CheckoutController_24110311.java # COD checkout & order confirmation controller
    │   │   ├── OrderHistoryController_24110311.java # Order history & status filtering controller
    │   │   └── ...
    │   ├── entity/
    │   │   ├── User_24110311.java              # User JPA Entity
    │   │   ├── Book_24110311.java              # Book JPA Entity
    │   │   ├── Author_24110311.java            # Author JPA Entity
    │   │   ├── Rating_24110311.java            # Rating JPA Entity
    │   │   ├── Orders_24110311.java            # Orders JPA Entity
    │   │   ├── OrderDetail_24110311.java       # OrderDetail JPA Entity
    │   │   ├── Cart_24110311.java              # Cart session model
    │   │   └── CartItem_24110311.java          # CartItem model
    │   ├── repository/                         # Repository layer (Data Access)
    │   ├── service/                            # Service layer (Business logic)
    │   └── util/
    │       └── OrderStatusUtil_24110311.java   # Order status mappings & helper
    ├── resources/
    │   └── META-INF/
    │       └── persistence.xml                 # JPA Persistence Unit (ExamDB)
    └── webapp/
        ├── WEB-INF/
        │   ├── sitemesh3.xml                   # SiteMesh 3 decorator rules
        │   └── web.xml                         # Servlet & Filter mappings
        └── views/
            ├── common/
            │   ├── header.jsp                  # Navigation bar component (Cart link + badge)
            │   └── footer.jsp                  # Footer component
            ├── decorator/
            │   └── web.jsp                     # SiteMesh master decorator template
            ├── cart/
            │   └── cart.jsp                    # Shopping cart view
            ├── checkout/
            │   ├── checkout.jsp                # COD checkout confirmation view
            │   └── success.jsp                 # Order success view
            ├── order/
            │   └── history.jsp                 # Order history & status filter view
            └── home.jsp
```

## Database Configuration

Base connection settings are defined in:
`src/main/resources/META-INF/persistence.xml`

For security, no credentials/passwords are hardcoded in source. Configure database connection parameters via environment variables or JVM system properties:

### Supported Environment Variables
- `DB_URL`: JDBC URL (e.g., `jdbc:sqlserver://localhost:1433;databaseName=ExamDB;encrypt=true;trustServerCertificate=true;`)
- `DB_USER`: Database username (e.g., `sa`)
- `DB_PASSWORD`: Database password

### Supported JVM System Properties
- `-Ddb.url=...`
- `-Ddb.user=...`
- `-Ddb.password=...` (or `-Djakarta.persistence.jdbc.password=...`)

To initialize the database schema:
Run the script in `database.sql` inside SQL Server Management Studio (SSMS).

## Shopping Cart & COD Checkout

### Shopping Cart
- Stored in HTTP session (`sessionScope.cart`) using stable `bookid`.
- Add to cart available from Home page, Book List, and Book Detail views.
- Quantity validation: rejects `quantity <= 0`, out-of-stock items, and prevents exceeding available stock.
- Dynamic book entity reloading from DB to reflect current prices and quantities.
- Cart endpoints: `/cart`, `/cart/add`, `/cart/update`, `/cart/remove`, `/cart/clear`.

### COD Checkout
- Restricted to authenticated users (`sessionScope.currentUser`).
- Re-queries each book with pessimistic write locking and verifies stock in an active transaction.
- Decrements `Book.quantity` and persists `Orders` + `OrderDetail` entities in a single `RESOURCE_LOCAL` transaction.
- Status initialized to `NEW` and payment method set to `COD`.
- Cart is cleared strictly upon successful transaction commit.
- Order confirmation displays generated Order ID, delivery details, and items summary. Only the purchasing user can view their order success page.

### Order History & Status Filtering
- Access endpoint `/orders/history` for logged-in users (`sessionScope.currentUser`).
- Ownership isolation: users can only view orders belonging to their own account.
- Displays current SQL Server order data with status filtering support.
- Supported order statuses and Vietnamese labels:
  - `NEW` — Đơn hàng mới
  - `CONFIRMED` — Đã xác nhận
  - `PREPARING` — Chuẩn bị hàng
  - `SHIPPING` — Vận chuyển
  - `DELIVERING` — Giao hàng
  - `DELIVERED` — Đã giao
  - `CANCELLED` — Đơn hàng hủy
  - `RETURNED` — Đơn hàng hoàn
- Direct SQL `Orders.status` changes are reflected after refresh.
- Details use `OrderDetail` purchase-time unit prices rather than current `Book` prices.

## Build and Run

1. Ensure JDK 17, Maven, and Apache Tomcat 10.1+ are installed.
2. Initialize database schema using `database.sql`.
3. Set database credentials via environment variables:
   ```bash
   export DB_USER=sa
   export DB_PASSWORD=your_actual_password
   # Windows PowerShell:
   # $env:DB_USER="sa"
   # $env:DB_PASSWORD="your_actual_password"
   ```
4. Build the WAR package:
   ```bash
   mvn clean package
   ```
5. Deploy `target/ktra-23-09.war` to Tomcat's `webapps` folder and start Tomcat.
6. Access the application:
   ```text
   http://localhost:8080/ktra-23-09/
   ```
