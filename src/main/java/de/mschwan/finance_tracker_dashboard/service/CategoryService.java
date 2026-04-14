package de.mschwan.finance_tracker_dashboard.service;

import de.mschwan.finance_tracker_dashboard.dto.CategoryResponse;
import de.mschwan.finance_tracker_dashboard.model.Category;
import de.mschwan.finance_tracker_dashboard.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<CategoryResponse> getAllCategories() {
    return categoryRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  private CategoryResponse mapToResponse(Category category) {
    return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getType(),
            category.getColorHex()
    );
  }
}