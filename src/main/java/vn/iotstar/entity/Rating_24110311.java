package vn.iotstar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "Rating")
public class Rating_24110311 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private int rating_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid", nullable = false)
    private User_24110311 user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookid", nullable = false)
    private Book_24110311 book;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "review_text", columnDefinition = "NVARCHAR(MAX)")
    private String review_text;

    public Rating_24110311() {
    }

    public Rating_24110311(User_24110311 user, Book_24110311 book, int rating, String review_text) {
        this.user = user;
        this.book = book;
        this.rating = rating;
        this.review_text = review_text;
    }

    public Rating_24110311(int rating_id, User_24110311 user, Book_24110311 book, int rating, String review_text) {
        this.rating_id = rating_id;
        this.user = user;
        this.book = book;
        this.rating = rating;
        this.review_text = review_text;
    }

    public int getRating_id() {
        return rating_id;
    }

    public void setRating_id(int rating_id) {
        this.rating_id = rating_id;
    }

    public int getRatingId() {
        return rating_id;
    }

    public void setRatingId(int ratingId) {
        this.rating_id = ratingId;
    }

    public User_24110311 getUser() {
        return user;
    }

    public void setUser(User_24110311 user) {
        this.user = user;
    }

    public Book_24110311 getBook() {
        return book;
    }

    public void setBook(Book_24110311 book) {
        this.book = book;
    }

    public int getUserid() {
        return user != null ? user.getId() : 0;
    }

    public int getBookid() {
        return book != null ? book.getBookid() : 0;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public String getReviewText() {
        return review_text;
    }

    public void setReviewText(String reviewText) {
        this.review_text = reviewText;
    }

    @Override
    public String toString() {
        return "Rating_24110311{" +
                "rating_id=" + rating_id +
                ", userid=" + getUserid() +
                ", bookid=" + getBookid() +
                ", rating=" + rating +
                ", review_text='" + review_text + '\'' +
                '}';
    }
}
