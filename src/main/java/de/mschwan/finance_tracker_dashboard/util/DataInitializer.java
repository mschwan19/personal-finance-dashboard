package de.mschwan.finance_tracker_dashboard.util;

import de.mschwan.finance_tracker_dashboard.model.Category;
import de.mschwan.finance_tracker_dashboard.model.CategoryType;
import de.mschwan.finance_tracker_dashboard.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  private final CategoryRepository categoryRepository;

  public DataInitializer(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public void run(String... args) {
    if (categoryRepository.count() == 0) {
      System.out.println("Erstelle Test-Kategorien...");

      Category gehalt = new Category();
      gehalt.setName("Gehalt");
      gehalt.setType(CategoryType.INCOME);
      gehalt.setColorHex("#4CAF50");

      Category miete = new Category();
      miete.setName("Miete");
      miete.setType(CategoryType.EXPENSE);
      miete.setColorHex("#F44336");

      categoryRepository.save(gehalt);
      categoryRepository.save(miete);

      System.out.println("Test-Kategorien erfolgreich gespeichert!");
    }
  }
}