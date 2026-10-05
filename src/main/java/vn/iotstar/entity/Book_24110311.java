package vn.iotstar.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Book")
public class Book_24110311 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookid")
    private int bookid;

    @Column(name = "isbn", length = 50)
    private String isbn;

    @Column(name = "title", length = 255, nullable = false, columnDefinition = "NVARCHAR(255)")
    private String title;

    @Column(name = "publisher", length = 255, columnDefinition = "NVARCHAR(255)")
    private String publisher;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "publish_date")
    private LocalDate publish_date;

    @Column(name = "cover_image", length = 255)
    private String cover_image;

    @Column(name = "quantity")
    private int quantity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "bookid"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author_24110311> authors = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rating_24110311> ratings = new ArrayList<>();

    public Book_24110311() {
    }

    public Book_24110311(String isbn, String title, String publisher, BigDecimal price, String description, LocalDate publish_date, String cover_image, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.price = price;
        this.description = description;
        this.publish_date = publish_date;
        this.cover_image = cover_image;
        this.quantity = quantity;
    }

    public Book_24110311(String isbn, String title, String publisher, double price, String description, LocalDate publish_date, String cover_image, int quantity) {
        this(isbn, title, publisher, BigDecimal.valueOf(price), description, publish_date, cover_image, quantity);
    }

    public Book_24110311(int bookid, String isbn, String title, String publisher, BigDecimal price, String description, LocalDate publish_date, String cover_image, int quantity) {
        this.bookid = bookid;
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.price = price;
        this.description = description;
        this.publish_date = publish_date;
        this.cover_image = cover_image;
        this.quantity = quantity;
    }

    public Book_24110311(int bookid, String isbn, String title, String publisher, double price, String description, LocalDate publish_date, String cover_image, int quantity) {
        this(bookid, isbn, title, publisher, BigDecimal.valueOf(price), description, publish_date, cover_image, quantity);
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setPrice(double price) {
        this.price = BigDecimal.valueOf(price);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(LocalDate publish_date) {
        this.publish_date = publish_date;
    }

    public LocalDate getPublishDate() {
        return publish_date;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publish_date = publishDate;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getCoverImage() {
        return cover_image;
    }

    public void setCoverImage(String coverImage) {
        this.cover_image = coverImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Author_24110311> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author_24110311> authors) {
        this.authors = authors;
    }

    public List<Rating_24110311> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating_24110311> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Book_24110311{" +
                "bookid=" + bookid +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", price=" + price +
                ", publish_date=" + publish_date +
                ", cover_image='" + cover_image + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
