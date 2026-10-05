package vn.iotstar.service;

import vn.iotstar.entity.Author_24110311;
import vn.iotstar.repository.AuthorRepository_24110311;
import vn.iotstar.repository.AuthorRepositoryImpl_24110311;

import java.util.List;

public class AuthorServiceImpl_24110311 implements AuthorService_24110311 {

    private final AuthorRepository_24110311 authorRepository = new AuthorRepositoryImpl_24110311();

    @Override
    public List<Author_24110311> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author_24110311> findPage(int page, int size) {
        return authorRepository.findPage(page, size);
    }

    @Override
    public long count() {
        return authorRepository.count();
    }

    @Override
    public Author_24110311 findById(int id) {
        return authorRepository.findById(id);
    }

    @Override
    public void insert(Author_24110311 author) {
        authorRepository.insert(author);
    }

    @Override
    public void update(Author_24110311 author) {
        authorRepository.update(author);
    }

    @Override
    public void delete(int id) {
        authorRepository.delete(id);
    }
}
