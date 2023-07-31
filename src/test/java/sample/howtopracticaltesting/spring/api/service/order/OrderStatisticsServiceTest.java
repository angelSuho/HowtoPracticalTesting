package sample.howtopracticaltesting.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import sample.howtopracticaltesting.spring.IntegrationTestSupport;
import sample.howtopracticaltesting.spring.client.mail.MailSendClient;
import sample.howtopracticaltesting.spring.domain.history.mail.MailSendHistory;
import sample.howtopracticaltesting.spring.domain.history.mail.MailSendHistoryRepository;
import sample.howtopracticaltesting.spring.domain.order.Order;
import sample.howtopracticaltesting.spring.domain.order.OrderRepository;
import sample.howtopracticaltesting.spring.domain.order.OrderStatus;
import sample.howtopracticaltesting.spring.domain.orderproduct.OrderProductRepository;
import sample.howtopracticaltesting.spring.domain.product.ProductRepository;
import sample.howtopracticaltesting.spring.domain.product.entity.Product;
import sample.howtopracticaltesting.spring.domain.product.entity.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sample.howtopracticaltesting.spring.domain.product.entity.ProductSellingStatus.SELLING;
import static sample.howtopracticaltesting.spring.domain.product.entity.ProductType.HANDMADE;

class OrderStatisticsServiceTest extends IntegrationTestSupport {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @AfterEach
    void setUp() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() throws Exception {
        //given
//        LocalDateTime now = LocalDateTime.of(2023, 7, 30, 12, 36);
        LocalDateTime now = LocalDateTime.of(2023, 7, 30, 0, 0);

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 2000);
        Product product3 = createProduct(HANDMADE, "003", 3000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 7, 29, 23, 59, 59), products);
        Order order2 = createPaymentCompletedOrder(now, products);
        Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 7, 30, 23, 59, 59), products);
        Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 7, 31, 0, 0), products);

        //stubbing
        when(mailSendClient.sendMail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        //when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 7, 30), "sooho@test.com");

        //then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .containsExactly("총 매출 합계는 12000원입니다.");
    }

    private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);
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