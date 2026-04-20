package de.mschwan.finance_tracker_dashboard.service;

import de.mschwan.finance_tracker_dashboard.model.Category;
import de.mschwan.finance_tracker_dashboard.model.RecurringTransaction;
import de.mschwan.finance_tracker_dashboard.model.Transaction;
import de.mschwan.finance_tracker_dashboard.repository.CategoryRepository;
import de.mschwan.finance_tracker_dashboard.repository.RecurringTransactionRepository;
import de.mschwan.finance_tracker_dashboard.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

  private final RecurringTransactionRepository recurringRepo;
  private final TransactionRepository transactionRepo;
  private final CategoryRepository categoryRepo;

  @Scheduled(cron = "0 0 1 * * ?")
  @Transactional
  public void processRecurringTransactions() {
    LocalDate today = LocalDate.now();

    List<RecurringTransaction> dueTransactions = recurringRepo.findAll().stream()
            .filter(rt -> !rt.getNextExecutionDate().isAfter(today))
            .toList();

    for (RecurringTransaction rt : dueTransactions) {
      Transaction transaction = new Transaction();
      transaction.setAmount(rt.getAmount());
      transaction.setDescription(rt.getDescription());
      transaction.setDate(rt.getNextExecutionDate());
      transaction.setUserId(rt.getUserId());

      if (rt.getCategoryId() != null) {
        Category category = categoryRepo.findById(rt.getCategoryId()).orElse(null);
        transaction.setCategory(category);
      }

      transactionRepo.save(transaction);

      LocalDate nextDate;
      if ("MONTHLY".equalsIgnoreCase(rt.getRecurrenceInterval())) {
        nextDate = rt.getNextExecutionDate().plusMonths(1);
      } else {
        nextDate = rt.getNextExecutionDate().plusYears(1);
      }
      rt.setNextExecutionDate(nextDate);

      recurringRepo.save(rt);
    }
  }

  public List<RecurringTransaction> getUpcomingTransactions(String userId) {
    LocalDate limit = LocalDate.now().plusDays(30);
    return recurringRepo.findByUserId(userId).stream()
            .filter(rt -> !rt.getNextExecutionDate().isAfter(limit))
            .toList();
  }
}