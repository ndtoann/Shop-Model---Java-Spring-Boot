package webapp.shopmohinh.service.impl;

import webapp.shopmohinh.dto.ReviewDto;
import webapp.shopmohinh.model.Product;
import webapp.shopmohinh.model.Review;
import webapp.shopmohinh.repository.ReviewRepository;
import webapp.shopmohinh.repository.ProductRepository;
import webapp.shopmohinh.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Review> getAll() {
        List<Review> Reviews = reviewRepository.findAll();
        return Reviews;
    }

    @Override
    public List<Review> getByProductId(Long productId) {
        List<Review> Reviews = reviewRepository.findByProductId(productId);
        return Reviews;
    }

    @Override
    public Review getById(Long id) {
        Review Review = null;

        Optional<Review> optional = reviewRepository.findById(id);

        if (optional.isPresent()) {
            Review = optional.get();
        } else {
            throw new RuntimeException("Không tìm thấy sản phẩm!");
        }

        return Review;

    }

    @Override
    public void save(ReviewDto reviewDto) {
        Review review = new Review();
        review.setName_user(reviewDto.getName_user());
        review.setEmail(reviewDto.getEmail());
        review.setRated(reviewDto.getRated());
        review.setContent(reviewDto.getComment());
        review.setStatus(0);

        Optional<Product> optionalProduct = productRepository.findById(reviewDto.getProduct_id());
        if (optionalProduct.isPresent()) {
            review.setProduct(optionalProduct.get());
        } else {
            throw new RuntimeException("Không tìm thấy sản phẩm!");
        }

        reviewRepository.save(review);
    }

    @Override
    public void updateStatus(Long id) {
        Optional<Review> optional = reviewRepository.findById(id);

        if (optional.isPresent()) {
            Review review = new Review();
            review.setId(id);
            review.setName_user(optional.get().getName_user());
            review.setEmail(optional.get().getEmail());
            review.setContent(optional.get().getContent());
            review.setRated(optional.get().getRated());
            review.setProduct(optional.get().getProduct());
            review.setStatus(1);

            reviewRepository.save(review);
        } else {
            throw new RuntimeException("Không tìm thấy sản phẩm!");
        }

    }

    @Override
    public void deleteById(Long id) {
        Optional<Review> optional = reviewRepository.findById(id);

        if (optional.isPresent()) {
            reviewRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy sản phẩm!");
        }
    }
}
