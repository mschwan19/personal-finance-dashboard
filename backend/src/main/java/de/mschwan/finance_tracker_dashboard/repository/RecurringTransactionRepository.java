package de.mschwan.finance_tracker_dashboard.repository;

import de.mschwan.finance_tracker_dashboard.model.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {

  List<RecurringTransaction> findByUserId(String userId);
}