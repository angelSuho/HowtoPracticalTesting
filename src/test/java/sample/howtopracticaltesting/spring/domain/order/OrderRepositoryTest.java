package sample.howtopracticaltesting.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sample.howtopracticaltesting.spring.domain.orderproduct.OrderProduct;
import sample.howtopracticaltesting.spring.domain.product.ProductRepository;
import sample.howtopracticaltesting.spring.domain.product.entity.Product;
import sample.howtopracticaltesting.spring.domain.product.entity.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.howtopracticaltesting.spring.domain.product.entity.ProductSellingStatus.SELLING;
import static sample.howtopracticaltesting.spring.domain.product.entity.ProductType.HANDMADE;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("선택한 주문시기동안의 주문목록을 조회한다.")
    @Test
    void findOrderList() throws Exception {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        LocalDateTime yesterdayTime = registeredDateTime.minusDays(1);

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Order order1 = Order.create(List.of(product1, product2), yesterdayTime);
        Order order2 = Order.create(List.of(product2, product3), registeredDateTime);
        orderRepository.saveAll(List.of(order1, order2));

        //when
        List<Order> orders = orderRepository.findOrdersBy(yesterdayTime.toLocalDate().atStartOfDay(),
                yesterdayTime.toLocalDate().plusDays(1).atStartOfDay(),
                OrderStatus.INIT);

        List<OrderProduct> orderProducts = orders.get(0).getOrderProducts();

        //then
        assertThat(orderProducts).hasSize(2)
                .extracting(productNumber -> productNumber.getProduct().getProductNumber())
        .containsExactlyInAnyOrder("001", "002");

        assertThat(orderProducts).extracting(OrderProduct::getProduct)
                .extracting(Product::getPrice)
                .containsExactlyInAnyOrder(1000, 3000);
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}