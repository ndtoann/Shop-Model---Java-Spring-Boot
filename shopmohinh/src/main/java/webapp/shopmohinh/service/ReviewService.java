package webapp.shopmohinh.service;

import webapp.shopmohinh.dto.ReviewDto;
import webapp.shopmohinh.model.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getAll();
    
    List<Review> getByProductId(Long id);

    Review getById(Long id);
    
    void save(ReviewDto ReviewDto);

    void updateStatus(Long id);

    void deleteById(Long id);
}
