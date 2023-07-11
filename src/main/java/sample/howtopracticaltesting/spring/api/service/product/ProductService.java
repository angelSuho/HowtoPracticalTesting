package sample.howtopracticaltesting.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.howtopracticaltesting.spring.domain.product.ProductRepository;
import sample.howtopracticaltesting.spring.domain.product.entity.Product;
import sample.howtopracticaltesting.spring.domain.product.entity.ProductSellingStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
        return products.stream()
                .map(ProductResponse::of)
                .toList();
    }
}
