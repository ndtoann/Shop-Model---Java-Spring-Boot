package webapp.shopmohinh.repository;

import webapp.shopmohinh.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByStatus(Integer status);
}
