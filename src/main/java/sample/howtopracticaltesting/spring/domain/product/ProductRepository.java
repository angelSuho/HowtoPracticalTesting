package sample.howtopracticaltesting.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.howtopracticaltesting.spring.domain.product.entity.Product;
import sample.howtopracticaltesting.spring.domain.product.entity.ProductSellingStatus;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * select *
     * from product
     * where selling_status in ('SELLING', 'HOLD');
     * */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

    List<Product> findAllByProductNumberIn(List<String> productNumbers);


}
