package webapp.shopmohinh.repository;

import webapp.shopmohinh.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByStatus(Integer status);
}
