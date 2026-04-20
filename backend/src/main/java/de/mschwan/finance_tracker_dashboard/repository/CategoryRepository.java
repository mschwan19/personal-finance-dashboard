package de.mschwan.finance_tracker_dashboard.repository;

import de.mschwan.finance_tracker_dashboard.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findByUserIdOrUserIdIsNull(String userId);
}