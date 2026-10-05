package vn.iotstar.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.Author_24110311;
import vn.iotstar.entity.Book_24110311;

import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl_24110311 implements BookRepository_24110311 {

    @Override
    public List<Book_24110311> findAll() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Book_24110311> list = em.createQuery("FROM Book_24110311", Book_24110311.class).getResultList();
            for (Book_24110311 b : list) {
                b.getRatings().size();
            }
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Book_24110311> findPage(int page, int size) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Book_24110311> list = em.createQuery("FROM Book_24110311 b ORDER BY b.bookid", Book_24110311.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
            for (Book_24110311 b : list) {
                b.getRatings().size();
            }
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(b) FROM Book_24110311 b", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public Book_24110311 findById(int id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            Book_24110311 book = em.find(Book_24110311.class, id);
            if (book != null) {
                book.getRatings().size();
            }
            return book;
        } finally {
            em.close();
        }
    }

    @Override
    public void insert(Book_24110311 book) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                List<Author_24110311> managedAuthors = new ArrayList<>();
                for (Author_24110311 a : book.getAuthors()) {
                    Author_24110311 managed = em.find(Author_24110311.class, a.getAuthor_id());
                    if (managed != null) {
                        managedAuthors.add(managed);
                    }
                }
                book.setAuthors(managedAuthors);
            }
            em.persist(book);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Book_24110311 book) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Book_24110311 existing = em.find(Book_24110311.class, book.getBookid());
            if (existing == null) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                return;
            }
            if (book.getAuthors() != null) {
                List<Author_24110311> managedAuthors = new ArrayList<>();
                for (Author_24110311 a : book.getAuthors()) {
                    Author_24110311 managed = em.find(Author_24110311.class, a.getAuthor_id());
                    if (managed != null) {
                        managedAuthors.add(managed);
                    }
                }
                book.setAuthors(managedAuthors);
            }
            em.merge(book);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Book_24110311 book = em.find(Book_24110311.class, id);
            if (book != null) {
                em.remove(book);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
