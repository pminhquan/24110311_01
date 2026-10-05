package vn.iotstar.service;

import vn.iotstar.entity.Book_24110311;
import vn.iotstar.repository.BookRepository_24110311;
import vn.iotstar.repository.BookRepositoryImpl_24110311;

import java.util.List;

public class BookServiceImpl_24110311 implements BookService_24110311 {

    private final BookRepository_24110311 bookRepository = new BookRepositoryImpl_24110311();

    @Override
    public List<Book_24110311> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book_24110311> findPage(int page, int size) {
        return bookRepository.findPage(page, size);
    }

    @Override
    public long count() {
        return bookRepository.count();
    }

    @Override
    public Book_24110311 findById(int id) {
        return bookRepository.findById(id);
    }

    @Override
    public void insert(Book_24110311 book) {
        bookRepository.insert(book);
    }

    @Override
    public void update(Book_24110311 book) {
        bookRepository.update(book);
    }

    @Override
    public void delete(int id) {
        bookRepository.delete(id);
    }
}
