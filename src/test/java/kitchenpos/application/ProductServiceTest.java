package kitchenpos.application;

import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.math.BigDecimal;

import static kitchenpos.fixture.ProductFixture.바닐라라떼;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class ProductServiceTest {

    @Autowired
    private DataSource dataSource;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(new JdbcTemplateProductDao(dataSource));
    }

    @Test
    void 상품_등록시_가격은_비워둘_수_없다() {
        var product = 바닐라라떼();
        product.setPrice(null);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_등록시_가격은_0보다_작아선_안된다() {
        var product = 바닐라라떼();
        product.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_등록시_저장된_상품을_반환한다() {
        var product = 바닐라라떼();

        assertThat(productService.create(product))
                .usingRecursiveComparison()
                .isEqualTo(바닐라라떼());
    }

    @Test
    void 모든_상품들을_가져온다() {
        assertThat(productService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(ProductFixture.listAllInDatabase());
    }
}