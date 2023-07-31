package sample.howtopracticaltesting.spring.api.service.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sample.howtopracticaltesting.spring.IntegrationTestSupport;
import sample.howtopracticaltesting.spring.domain.product.ProductRepository;
import sample.howtopracticaltesting.spring.domain.product.entity.Product;
import sample.howtopracticaltesting.spring.domain.product.entity.ProductSellingStatus;
import sample.howtopracticaltesting.spring.domain.product.entity.ProductType;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.howtopracticaltesting.spring.domain.product.entity.ProductSellingStatus.SELLING;
import static sample.howtopracticaltesting.spring.domain.product.entity.ProductType.HANDMADE;

class ProductNumberFactoryTest extends IntegrationTestSupport {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductNumberFactory productNumberFactory;

    @DisplayName("새로 추가되는 상품의 주문번호는 1씩 증가한다.")
    @Test
    void createNextProductNumber() throws Exception {
        //given
        Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        productRepository.save(product);

        //when
        String nextProductNumber = productNumberFactory.createNextProductNumber();

        //then
        assertThat(nextProductNumber).isEqualTo("002");
    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}