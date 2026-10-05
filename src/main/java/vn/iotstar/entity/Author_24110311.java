package vn.iotstar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Author")
public class Author_24110311 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private int author_id;

    @Column(name = "author_name", length = 255, nullable = false, columnDefinition = "NVARCHAR(255)")
    private String author_name;

    @Column(name = "date_of_birth")
    private LocalDate date_of_birth;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private List<Book_24110311> books = new ArrayList<>();

    public Author_24110311() {
    }

    public Author_24110311(String author_name, LocalDate date_of_birth) {
        this.author_name = author_name;
        this.date_of_birth = date_of_birth;
    }

    public Author_24110311(int author_id, String author_name, LocalDate date_of_birth) {
        this.author_id = author_id;
        this.author_name = author_name;
        this.date_of_birth = date_of_birth;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getAuthorId() {
        return author_id;
    }

    public void setAuthorId(int authorId) {
        this.author_id = authorId;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthorName() {
        return author_name;
    }

    public void setAuthorName(String authorName) {
        this.author_name = authorName;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public LocalDate getDateOfBirth() {
        return date_of_birth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.date_of_birth = dateOfBirth;
    }

    public List<Book_24110311> getBooks() {
        return books;
    }

    public void setBooks(List<Book_24110311> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Author_24110311{" +
                "author_id=" + author_id +
                ", author_name='" + author_name + '\'' +
                ", date_of_birth=" + date_of_birth +
                '}';
    }
}
