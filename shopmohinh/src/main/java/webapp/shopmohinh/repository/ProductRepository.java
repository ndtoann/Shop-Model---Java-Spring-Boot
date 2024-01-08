package webapp.shopmohinh.repository;

import webapp.shopmohinh.model.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long cateId);

    List<Product> findByProductNameContaining(String name);

    List<Product> findByStatus(Integer status);
}