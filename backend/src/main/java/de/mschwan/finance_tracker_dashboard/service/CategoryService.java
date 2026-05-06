package de.mschwan.finance_tracker_dashboard.service;

import de.mschwan.finance_tracker_dashboard.dto.CategoryResponse;
import de.mschwan.finance_tracker_dashboard.model.Category;
import de.mschwan.finance_tracker_dashboard.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<CategoryResponse> getAllCategoriesForUser(String userId) {
    return categoryRepository.findByUserIdOrUserIdIsNull(userId).stream()
            .map(this::mapToResponse)
            .toList();
  }

  public List<CategoryResponse> getCustomCategoriesForUser(String userId) {
    return categoryRepository.findByUserId(userId).stream()
            .map(this::mapToResponse)
            .toList();
  }

  public CategoryResponse createCustomCategory(Category request, String userId) {
    request.setUserId(userId);
    Category saved = categoryRepository.save(request);
    return mapToResponse(saved);
  }

  public void deleteCustomCategory(Long categoryId, String userId) {
    Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
            .orElseThrow(() -> new RuntimeException("Category not found or permission denied."));

    categoryRepository.delete(category);
  }

  private CategoryResponse mapToResponse(Category cat) {
    return new CategoryResponse(cat.getId(), cat.getName(), cat.getType(), cat.getColorHex());
  }
}