package vn.iotstar.repository;

import vn.iotstar.entity.Book_24110311;

import java.util.List;

public interface BookRepository_24110311 {

    List<Book_24110311> findAll();

    List<Book_24110311> findPage(int page, int size);

    long count();

    Book_24110311 findById(int id);

    void insert(Book_24110311 book);

    void update(Book_24110311 book);

    void delete(int id);
}
