package de.mschwan.finance_tracker_dashboard.controller;

import de.mschwan.finance_tracker_dashboard.dto.CategoryResponse;
import de.mschwan.finance_tracker_dashboard.model.Category;
import de.mschwan.finance_tracker_dashboard.service.CategoryService;
import org.springframework.http.ResponseEntity;
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

  @GetMapping("/custom")
  public List<CategoryResponse> getCustomCategories(@AuthenticationPrincipal Jwt jwt) {
    return categoryService.getCustomCategoriesForUser(jwt.getSubject());
  }

  @PostMapping("/custom")
  public CategoryResponse createCustomCategory(@RequestBody Category request, @AuthenticationPrincipal Jwt jwt) {
    return categoryService.createCustomCategory(request, jwt.getSubject());
  }

  @DeleteMapping("/custom/{id}")
  public ResponseEntity<Void> deleteCustomCategory(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
    categoryService.deleteCustomCategory(id, jwt.getSubject());
    return ResponseEntity.noContent().build();
  }
}