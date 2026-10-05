package vn.iotstar.service;

import vn.iotstar.entity.Author_24110311;

import java.util.List;

public interface AuthorService_24110311 {

    List<Author_24110311> findAll();

    List<Author_24110311> findPage(int page, int size);

    long count();

    Author_24110311 findById(int id);

    void insert(Author_24110311 author);

    void update(Author_24110311 author);

    void delete(int id);
}
