-- =============================================
-- SQL Server Database Script for Exam
-- Entities: User, Book, Author, Rating
-- =============================================

CREATE DATABASE ExamDB;
GO

USE ExamDB;
GO

-- 1. Create [User] Table
CREATE TABLE [User] (
    id INT IDENTITY(1,1) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    fullname NVARCHAR(255),
    phone VARCHAR(20),
    passwd VARCHAR(255) NOT NULL,
    signup_date DATE,
    last_login DATETIME,
    is_admin BIT NOT NULL DEFAULT 0
);
GO

-- 2. Create Author Table
CREATE TABLE Author (
    author_id INT IDENTITY(1,1) PRIMARY KEY,
    author_name NVARCHAR(255) NOT NULL,
    date_of_birth DATE
);
GO

-- 3. Create Book Table
CREATE TABLE Book (
    bookid INT IDENTITY(1,1) PRIMARY KEY,
    isbn VARCHAR(50),
    title NVARCHAR(255) NOT NULL,
    publisher NVARCHAR(255),
    price DECIMAL(18,2) DEFAULT 0 CHECK (price >= 0),
    description NVARCHAR(MAX),
    publish_date DATE,
    cover_image NVARCHAR(255),
    quantity INT DEFAULT 0
);
GO

-- 4. Create book_author Join Table (Author <-> Book Many-to-Many)
CREATE TABLE book_author (
    bookid INT NOT NULL,
    author_id INT NOT NULL,
    PRIMARY KEY (bookid, author_id),
    CONSTRAINT FK_book_author_book FOREIGN KEY (bookid) REFERENCES Book(bookid) ON DELETE CASCADE,
    CONSTRAINT FK_book_author_author FOREIGN KEY (author_id) REFERENCES Author(author_id) ON DELETE CASCADE
);
GO

-- 5. Create Rating Table (User -> Rating, Book -> Rating One-to-Many)
CREATE TABLE Rating (
    rating_id INT IDENTITY(1,1) PRIMARY KEY,
    userid INT NOT NULL,
    bookid INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text NVARCHAR(MAX),
    CONSTRAINT FK_rating_user FOREIGN KEY (userid) REFERENCES [User](id) ON DELETE CASCADE,
    CONSTRAINT FK_rating_book FOREIGN KEY (bookid) REFERENCES Book(bookid) ON DELETE CASCADE
);
GO

-- 6. Create Orders Table (User -> Orders One-to-Many)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Orders' AND xtype='U')
BEGIN
    CREATE TABLE Orders (
        order_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        recipient_name NVARCHAR(255) NOT NULL,
        recipient_phone VARCHAR(20) NOT NULL,
        shipping_address NVARCHAR(MAX) NOT NULL,
        payment_method VARCHAR(50) NOT NULL DEFAULT 'COD',
        status VARCHAR(50) NOT NULL DEFAULT 'NEW',
        total_amount DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK (total_amount >= 0),
        order_date DATETIME DEFAULT GETDATE(),
        CONSTRAINT FK_orders_user FOREIGN KEY (user_id) REFERENCES [User](id) ON DELETE CASCADE
    );
END
GO

-- 7. Create OrderDetail Table (Orders -> OrderDetail One-to-Many, Book -> OrderDetail Many-to-One)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='OrderDetail' AND xtype='U')
BEGIN
    CREATE TABLE OrderDetail (
        detail_id INT IDENTITY(1,1) PRIMARY KEY,
        order_id INT NOT NULL,
        bookid INT NOT NULL,
        quantity INT NOT NULL CHECK (quantity > 0),
        unit_price DECIMAL(18,2) NOT NULL CHECK (unit_price >= 0),
        CONSTRAINT FK_orderdetail_orders FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
        CONSTRAINT FK_orderdetail_book FOREIGN KEY (bookid) REFERENCES Book(bookid)
    );
END
GO

-- Sample Data
-- 1. Users (at least 2 users, matching login password format)
INSERT INTO [User] (email, fullname, phone, passwd, signup_date, last_login, is_admin)
VALUES 
('admin@example.com', N'System Administrator', '0901234567', '123456', '2026-01-01', GETDATE(), 1),
('user1@example.com', N'Nguyen Van A', '0912345678', '123456', '2026-02-15', GETDATE(), 0),
('quan.pm@example.com', N'Phạm Minh Quân', '0987654321', '123456', '2026-03-01', GETDATE(), 0);
GO

-- 2. Authors (at least 8 authors)
INSERT INTO Author (author_name, date_of_birth)
VALUES 
(N'Robert C. Martin', '1952-12-05'),
(N'Joshua Bloch', '1961-08-28'),
(N'Martin Fowler', '1963-12-18'),
(N'Kent Beck', '1961-03-31'),
(N'Erich Gamma', '1961-03-13'),
(N'Richard Helm', '1962-09-08'),
(N'Ralph Johnson', '1955-10-07'),
(N'John Vlissides', '1961-08-02'),
(N'Andrew Hunt', '1964-05-15'),
(N'David Thomas', '1956-10-24');
GO

-- 3. Books (at least 12 books for pagination testing: 6 per page)
INSERT INTO Book (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
VALUES 
('978-0132350884', N'Clean Code', N'Prentice Hall', 45.0, N'A Handbook of Agile Software Craftsmanship', '2008-08-01', 'https://images.unsplash.com/photo-1532012164546-f432f2e37b73?w=400', 10),
('978-0134685991', N'Effective Java', N'Addison-Wesley', 50.0, N'Best practices for the Java platform', '2018-01-06', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=400', 15),
('978-0201633610', N'Design Patterns: Elements of Reusable Object-Oriented Software', N'Addison-Wesley', 55.0, N'Captures a wealth of experience in object-oriented design', '1994-11-10', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400', 8),
('978-0201485677', N'Refactoring: Improving the Design of Existing Code', N'Addison-Wesley', 48.0, N'Improving the design of existing code without changing its behavior', '1999-07-08', 'https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=400', 12),
('978-0137081073', N'The Clean Coder', N'Prentice Hall', 40.0, N'A Code of Conduct for Professional Programmers', '2011-05-13', 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=400', 20),
('978-0134494166', N'Clean Architecture', N'Prentice Hall', 42.0, N'A Craftsman''s Guide to Software Structure and Design', '2017-09-17', 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=400', 14),
('978-0201616224', N'The Pragmatic Programmer', N'Addison-Wesley', 49.99, N'Your journey to mastery in software development', '1999-10-30', 'https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=400', 18),
('978-0321146533', N'Test Driven Development: By Example', N'Addison-Wesley', 38.5, N'Clean code that works using automated test-first development', '2002-11-08', 'https://images.unsplash.com/photo-1506880018603-83d5b814b5a6?w=400', 9),
('978-0321125217', N'Domain-Driven Design', N'Addison-Wesley', 58.0, N'Tackling Complexity in the Heart of Software', '2003-08-20', 'https://images.unsplash.com/photo-1519682337058-a94d519337bc?w=400', 7),
('978-0135957059', N'The Pragmatic Programmer (20th Anniversary Edition)', N'Addison-Wesley', 52.0, N'Updated for modern computing practices', '2019-09-13', 'https://images.unsplash.com/photo-1457369804613-52c61a468e7d?w=400', 25),
('978-0131177055', N'Working Effectively with Legacy Code', N'Prentice Hall', 46.0, N'Strategies for working with large, untested code bases', '2004-09-22', 'https://images.unsplash.com/photo-1495640388908-05fa85288e61?w=400', 11),
('978-0321278654', N'Extreme Programming Explained', N'Addison-Wesley', 35.0, N'Embrace change and agile software development principles', '2004-11-19', 'https://images.unsplash.com/photo-1491841573634-28140fc7ced7?w=400', 16);
GO

-- 4. book_author (ManyToMany join table: every book has >= 1 author, multiple books with multiple authors)
INSERT INTO book_author (bookid, author_id)
VALUES 
-- Book 1: Clean Code -> Robert C. Martin
(1, 1),
-- Book 2: Effective Java -> Joshua Bloch
(2, 2),
-- Book 3: Design Patterns -> Gang of Four (4 authors)
(3, 5), (3, 6), (3, 7), (3, 8),
-- Book 4: Refactoring -> Martin Fowler & Kent Beck (2 authors)
(4, 3), (4, 4),
-- Book 5: The Clean Coder -> Robert C. Martin
(5, 1),
-- Book 6: Clean Architecture -> Robert C. Martin
(6, 1),
-- Book 7: The Pragmatic Programmer -> Andrew Hunt & David Thomas (2 authors)
(7, 9), (7, 10),
-- Book 8: TDD By Example -> Kent Beck
(8, 4),
-- Book 9: Domain-Driven Design -> Martin Fowler
(9, 3),
-- Book 10: Pragmatic Programmer 20th -> Andrew Hunt & David Thomas (2 authors)
(10, 9), (10, 10),
-- Book 11: Working Effectively with Legacy Code -> Robert C. Martin
(11, 1),
-- Book 12: Extreme Programming Explained -> Kent Beck
(12, 4);
GO

-- 5. Rating (Linked to valid users and books)
INSERT INTO Rating (userid, bookid, rating, review_text)
VALUES 
(1, 1, 5, N'Must-read for every professional programmer.'),
(2, 1, 4, N'Great insights on clean coding principles.'),
(3, 1, 5, N'Completely changed the way I write code.'),
(2, 2, 5, N'The ultimate guide to Java idioms and best practices.'),
(3, 2, 5, N'Essential reading for every Java developer.'),
(1, 3, 5, N'Timeless classic on software design patterns.'),
(2, 3, 4, N'A bit dense, but foundational.'),
(3, 4, 5, N'Helped me refactor legacy code with confidence.'),
(1, 7, 5, N'Pragmatic, practical, and highly recommended.'),
(2, 7, 5, N'Every software engineer should own a copy.');
GO
