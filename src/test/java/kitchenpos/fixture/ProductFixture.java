package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.FixtureUtil.listAllInDatabaseFrom;
import static kitchenpos.fixture.FixtureUtil.pushing;

@SuppressWarnings("NonAsciiCharacters")
public abstract class ProductFixture {

    @InDatabase
    public static Product 후라이드() {
        return pushing(new Product(), 1L, "후라이드", new BigDecimal("16000.00"));
    }

    @InDatabase
    public static Product 양념치킨() {
        return pushing(new Product(), 2L, "양념치킨", new BigDecimal("16000.00"));
    }

    @InDatabase
    public static Product 반반치킨() {
        return pushing(new Product(), 3L, "반반치킨", new BigDecimal("16000.00"));
    }

    @InDatabase
    public static Product 통구이() {
        return pushing(new Product(), 4L, "통구이", new BigDecimal("16000.00"));
    }

    @InDatabase
    public static Product 간장치킨() {
        return pushing(new Product(), 5L, "간장치킨", new BigDecimal("17000.00"));
    }

    @InDatabase
    public static Product 순살치킨() {
        return pushing(new Product(), 6L, "순살치킨", new BigDecimal("17000.00"));
    }

    public static Product 바닐라라떼() {
        return pushing(new Product(), 7L, "바닐라라떼", new BigDecimal("3700.00"));
    }

    public static List<Product> listAllInDatabase() {
        return listAllInDatabaseFrom(ProductFixture.class, Product.class);
    }
}