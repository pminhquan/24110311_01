package vn.iotstar.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.Book_24110311;
import vn.iotstar.entity.Rating_24110311;
import vn.iotstar.entity.User_24110311;

import java.util.List;

public class RatingRepositoryImpl_24110311 implements RatingRepository_24110311 {

    @Override
    public List<Rating_24110311> findRatingsByBook(int bookId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("FROM Rating_24110311 r WHERE r.book.bookid = :bookId ORDER BY r.rating_id DESC", Rating_24110311.class)
                    .setParameter("bookId", bookId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void addRating(Rating_24110311 rating) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (rating.getUser() != null && rating.getUser().getId() > 0) {
                rating.setUser(em.find(User_24110311.class, rating.getUser().getId()));
            }
            if (rating.getBook() != null) {
                rating.setBook(em.find(Book_24110311.class, rating.getBook().getBookid()));
            }
            em.persist(rating);
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
