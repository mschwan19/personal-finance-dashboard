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

  public List<CategoryResponse> getAllCategoriesForUser(String userId) {
    return categoryRepository.findByUserIdOrUserIdIsNull(userId).stream()
            .map(cat -> new CategoryResponse(cat.getId(), cat.getName(), cat.getType(), cat.getColorHex()))
            .toList();
  }

  public CategoryResponse createPrivateCategory(Category request, String userId) {
    request.setUserId(userId);
    Category saved = categoryRepository.save(request);
    return new CategoryResponse(saved.getId(), saved.getName(), saved.getType(), saved.getColorHex());
  }
}