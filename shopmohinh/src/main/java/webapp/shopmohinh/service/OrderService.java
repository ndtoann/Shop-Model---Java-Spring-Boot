package webapp.shopmohinh.service;

import webapp.shopmohinh.dto.OrderDto;
import webapp.shopmohinh.model.Order;
import webapp.shopmohinh.model.OrderDetail;

import java.util.List;

public interface OrderService {
    List<Order> getAll();
    
    Order getById(Long id);
    
    void save(OrderDto orderDto, List<OrderDetail> orderDetail, Double totalMoney);

    void updateStatus(Long id);

    void deleteById(Long id);
}
