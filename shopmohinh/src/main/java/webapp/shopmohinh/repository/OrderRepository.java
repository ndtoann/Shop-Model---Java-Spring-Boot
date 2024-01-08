package webapp.shopmohinh.repository;

import webapp.shopmohinh.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    
}
