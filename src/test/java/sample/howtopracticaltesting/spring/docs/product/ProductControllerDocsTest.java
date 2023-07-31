package sample.howtopracticaltesting.spring.docs.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.restdocs.payload.JsonFieldType;
import sample.howtopracticaltesting.spring.api.controller.product.ProductController;
import sample.howtopracticaltesting.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.howtopracticaltesting.spring.api.service.product.ProductResponse;
import sample.howtopracticaltesting.spring.api.service.product.ProductService;
import sample.howtopracticaltesting.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.howtopracticaltesting.spring.docs.RestDocsSupport;
import sample.howtopracticaltesting.spring.domain.product.entity.ProductSellingStatus;
import sample.howtopracticaltesting.spring.domain.product.entity.ProductType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.howtopracticaltesting.spring.domain.product.entity.ProductSellingStatus.SELLING;
import static sample.howtopracticaltesting.spring.domain.product.entity.ProductType.*;
import static sample.howtopracticaltesting.spring.domain.product.entity.ProductType.HANDMADE;

public class ProductControllerDocsTest extends RestDocsSupport {

    private final ProductService productService = mock(ProductService.class);

    @Override
    protected Object initController() {
        return new ProductController(productService);
    }

    @DisplayName("신규 상품을 등록하는 API")
    @Test
    void createProduct() throws Exception {
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        BDDMockito.given(productService.createProduct(any(ProductCreateServiceRequest.class)))
                .willReturn(ProductResponse.builder()
                        .id(1L)
                        .productNumber("001")
                        .type(HANDMADE)
                        .sellingStatus(SELLING)
                        .name("아메리카노")
                        .price(4000)
                        .build());

        mockMvc.perform(
                post("/api/v1/products/new")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-create",
                        requestFields(
                            fieldWithPath("type").type(JsonFieldType.STRING).
                                    description("상품 타입"),
                            fieldWithPath("sellingStatus").type(JsonFieldType.STRING)
                                    .description("상품 판매 상태"),
                            fieldWithPath("name").type(JsonFieldType.STRING)
                                    .description("상품 이름"),
                            fieldWithPath("price").type(JsonFieldType.NUMBER)
                                    .description("상품 가격")
                        ),
                        responseFields(
                            fieldWithPath("code").type(JsonFieldType.NUMBER)
                                    .description("코드"),
                            fieldWithPath("status").type(JsonFieldType.STRING)
                                    .description("상태"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("메세지"),
                            fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                            fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                    .description("상품 ID"),
                            fieldWithPath("data.productNumber").type(JsonFieldType.STRING)
                                    .description("상품 번호"),
                                fieldWithPath("data.type").type(JsonFieldType.STRING)
                                    .description("상품 타입"),
                            fieldWithPath("data.sellingStatus").type(JsonFieldType.STRING)
                                    .description("상품 판매 상태"),
                            fieldWithPath("data.name").type(JsonFieldType.STRING)
                                    .description("상품 이름"),
                            fieldWithPath("data.price").type(JsonFieldType.NUMBER)
                                    .description("상품 가격")
                )));
    }
}
