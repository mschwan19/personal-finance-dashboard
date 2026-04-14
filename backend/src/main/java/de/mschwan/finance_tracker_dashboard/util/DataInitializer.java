package de.mschwan.finance_tracker_dashboard.util;

import de.mschwan.finance_tracker_dashboard.model.Category;
import de.mschwan.finance_tracker_dashboard.model.CategoryType;
import de.mschwan.finance_tracker_dashboard.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  private static final String COLOR_INCOME = "#4CAF50";
  private static final String COLOR_HOUSING = "#F44336";
  private static final String COLOR_GROCERIES = "#FF9800";
  private static final String COLOR_LEISURE = "#9C27B0";
  private static final String COLOR_MOBILITY = "#2196F3";
  private static final String COLOR_SHOPPING = "#E91E63";
  private static final String COLOR_INVESTMENT = "#00BCD4";

  private final CategoryRepository categoryRepository;

  public DataInitializer(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public void run(String... args) {
    if (categoryRepository.count() == 0) {
      // INCOME
      createCategory("SALARY", CategoryType.INCOME, COLOR_INCOME);
      createCategory("GIFTS", CategoryType.INCOME, COLOR_INCOME);
      createCategory("REFUNDS", CategoryType.INCOME, COLOR_INCOME);

      // EXPENSE
      createCategory("HOUSING", CategoryType.EXPENSE, COLOR_HOUSING);
      createCategory("GROCERIES", CategoryType.EXPENSE, COLOR_GROCERIES);
      createCategory("LEISURE", CategoryType.EXPENSE, COLOR_LEISURE);
      createCategory("MOBILITY", CategoryType.EXPENSE, COLOR_MOBILITY);
      createCategory("SHOPPING", CategoryType.EXPENSE, COLOR_SHOPPING);
      createCategory("INVESTMENTS", CategoryType.EXPENSE, COLOR_INVESTMENT);
    }
  }

  private void createCategory(String name, CategoryType type, String color) {
    Category category = new Category();
    category.setName(name);
    category.setType(type);
    category.setColorHex(color);
    categoryRepository.save(category);
  }
}