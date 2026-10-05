package vn.iotstar.service;

import vn.iotstar.entity.Rating_24110311;

import java.util.List;

public interface RatingService_24110311 {

    List<Rating_24110311> findRatingsByBook(int bookId);

    void addRating(Rating_24110311 rating);
}
