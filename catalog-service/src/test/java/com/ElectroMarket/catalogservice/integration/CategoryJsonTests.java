package com.ElectroMarket.catalogservice.integration;

import com.ElectroMarket.catalogservice.models.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CategoryJsonTests {
    @Autowired
    private JacksonTester<Category> json;

    @Test
    void testSerialize() throws Exception {
        var category = new Category(123L, "category", 2L);
        var jsonContent = json.write(category);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .isEqualTo(category.id().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.name")
                .isEqualTo(category.name());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.parent_id")
                .isEqualTo(category.parent_id().intValue());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
                    {
                        "id": 242,
                        "name": "category",
                        "parent_id": "2"
                    }
                    """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new Category(242L, "category", 2L));

    }
}
