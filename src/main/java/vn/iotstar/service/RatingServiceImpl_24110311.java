package vn.iotstar.service;

import vn.iotstar.entity.Rating_24110311;
import vn.iotstar.repository.RatingRepository_24110311;
import vn.iotstar.repository.RatingRepositoryImpl_24110311;

import java.util.List;

public class RatingServiceImpl_24110311 implements RatingService_24110311 {

    private final RatingRepository_24110311 ratingRepository = new RatingRepositoryImpl_24110311();

    @Override
    public List<Rating_24110311> findRatingsByBook(int bookId) {
        return ratingRepository.findRatingsByBook(bookId);
    }

    @Override
    public void addRating(Rating_24110311 rating) {
        ratingRepository.addRating(rating);
    }
}
