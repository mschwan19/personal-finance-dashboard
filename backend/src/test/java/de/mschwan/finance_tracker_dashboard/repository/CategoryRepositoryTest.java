package de.mschwan.finance_tracker_dashboard.repository;

import de.mschwan.finance_tracker_dashboard.model.Category;
import de.mschwan.finance_tracker_dashboard.model.CategoryType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  void shouldSaveAndRetrieveCategory() {
    Category category = new Category();
    category.setName("Test-Einkauf");
    category.setType(CategoryType.EXPENSE);

    Category savedCategory = categoryRepository.save(category);

    assertThat(savedCategory.getId()).isNotNull();
    assertThat(savedCategory.getName()).isEqualTo("Test-Einkauf");
  }
}