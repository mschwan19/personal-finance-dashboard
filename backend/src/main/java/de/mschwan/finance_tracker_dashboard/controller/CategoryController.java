package de.mschwan.finance_tracker_dashboard.controller;

import de.mschwan.finance_tracker_dashboard.dto.CategoryResponse;
import de.mschwan.finance_tracker_dashboard.model.Category;
import de.mschwan.finance_tracker_dashboard.service.CategoryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public List<CategoryResponse> getAllCategories(@AuthenticationPrincipal Jwt jwt) {
    return categoryService.getAllCategoriesForUser(jwt.getSubject());
  }

  @PostMapping
  public CategoryResponse createPrivateCategory(@RequestBody Category request, @AuthenticationPrincipal Jwt jwt) {
    return categoryService.createPrivateCategory(request, jwt.getSubject());
  }
}