package sample.howtopracticaltesting.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.howtopracticaltesting.spring.api.controller.order.request.OrderCreateRequest;
import sample.howtopracticaltesting.spring.api.service.order.response.OrderResponse;
import sample.howtopracticaltesting.spring.domain.order.Order;
import sample.howtopracticaltesting.spring.domain.order.OrderRepository;
import sample.howtopracticaltesting.spring.domain.product.ProductRepository;
import sample.howtopracticaltesting.spring.domain.product.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

}
