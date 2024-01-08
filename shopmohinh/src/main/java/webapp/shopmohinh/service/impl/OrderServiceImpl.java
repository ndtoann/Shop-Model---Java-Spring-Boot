package webapp.shopmohinh.service.impl;

import webapp.shopmohinh.dto.*;
import webapp.shopmohinh.model.*;
import webapp.shopmohinh.repository.*;
import webapp.shopmohinh.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getAll() {
        List<Order> orders = orderRepository.findAll();
        return orders;
    }

    @Override
    public Order getById(Long id) {
        Order order = null;

        Optional<Order> optional = orderRepository.findById(id);

        if (optional.isPresent()) {
            order = optional.get();
        } else {
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }

        return order;

    }

    @Override
    public void save(OrderDto orderDto, List<OrderDetail> orderDetail, Double totalMoney) {
        Order order = new Order();
        order.setName_customer(orderDto.getName_customer());
        order.setTel(orderDto.getTel());
        order.setEmail(orderDto.getEmail());
        order.setCity_id(orderDto.getCity_id());
        order.setDistrict_id(orderDto.getDistrict_id());
        order.setWard_id(orderDto.getWard_id());
        order.setAddress(orderDto.getAddress());
        order.setMethod_pack(orderDto.getMethod_pack());
        order.setMethod_checkout(orderDto.getMethod_checkout());
        order.setNote(orderDto.getNote());
        order.setTotal_money(totalMoney);
        order.setOrderDetails(orderDetail);
        order.setStatus(0);

        for (OrderDetail orderItem : orderDetail) {
            orderItem.setOrder(order);
        }

        orderRepository.save(order);
    }

    @Override
    public void updateStatus(Long id) {
        Order oldOrder = null;
        Optional<Order> optional = orderRepository.findById(id);

        if (optional.isPresent()) {
            oldOrder = optional.get();
            Order order = new Order();
            order.setId(id);
            order.setName_customer(oldOrder.getName_customer());
            order.setTel(oldOrder.getTel());
            order.setEmail(oldOrder.getEmail());
            order.setCity_id(oldOrder.getCity_id());
            order.setDistrict_id(oldOrder.getDistrict_id());
            order.setWard_id(oldOrder.getWard_id());
            order.setAddress(oldOrder.getAddress());
            order.setMethod_pack(oldOrder.getMethod_pack());
            order.setMethod_checkout(oldOrder.getMethod_checkout());
            order.setNote(oldOrder.getNote());
            order.setTotal_money(oldOrder.getTotal_money());
            order.setOrderDetails(oldOrder.getOrderDetails());
            if (oldOrder.getStatus() == 0) {
                order.setStatus(1);
            } else if (oldOrder.getStatus() == 1) {
                order.setStatus(2);
            }

            orderRepository.save(order);
        } else {
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Order> optional = orderRepository.findById(id);

        if (optional.isPresent()) {
            orderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }
    }
}
