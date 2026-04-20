package de.mschwan.finance_tracker_dashboard.controller;

import de.mschwan.finance_tracker_dashboard.model.RecurringTransaction;
import de.mschwan.finance_tracker_dashboard.repository.RecurringTransactionRepository;
import de.mschwan.finance_tracker_dashboard.service.RecurringTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring")
@RequiredArgsConstructor
public class RecurringTransactionController {

  private final RecurringTransactionRepository repository;
  private final RecurringTransactionService service;

  @GetMapping
  public List<RecurringTransaction> getAll(@AuthenticationPrincipal Jwt jwt) {
    return repository.findByUserId(jwt.getSubject());
  }

  @GetMapping("/upcoming")
  public List<RecurringTransaction> getUpcoming(@AuthenticationPrincipal Jwt jwt) {
    return service.getUpcomingTransactions(jwt.getSubject());
  }

  @PostMapping
  public RecurringTransaction create(@RequestBody RecurringTransaction rt, @AuthenticationPrincipal Jwt jwt) {
    rt.setUserId(jwt.getSubject());
    return repository.save(rt);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
    repository.findById(id).ifPresent(rt -> {
      if (rt.getUserId().equals(jwt.getSubject())) {
        repository.deleteById(id);
      }
    });
  }
}