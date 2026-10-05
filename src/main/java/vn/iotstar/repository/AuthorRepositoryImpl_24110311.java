package vn.iotstar.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.Author_24110311;

import java.util.List;

public class AuthorRepositoryImpl_24110311 implements AuthorRepository_24110311 {

    @Override
    public List<Author_24110311> findAll() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("FROM Author_24110311", Author_24110311.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Author_24110311> findPage(int page, int size) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("FROM Author_24110311 a ORDER BY a.author_id", Author_24110311.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(a) FROM Author_24110311 a", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public Author_24110311 findById(int id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.find(Author_24110311.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void insert(Author_24110311 author) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(author);
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
    public void update(Author_24110311 author) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(author);
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
            Author_24110311 author = em.find(Author_24110311.class, id);
            if (author != null) {
                em.remove(author);
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
