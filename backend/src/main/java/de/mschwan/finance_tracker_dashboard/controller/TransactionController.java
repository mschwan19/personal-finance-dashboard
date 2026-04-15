package de.mschwan.finance_tracker_dashboard.controller;

import de.mschwan.finance_tracker_dashboard.dto.TransactionRequest;
import de.mschwan.finance_tracker_dashboard.dto.TransactionResponse;
import de.mschwan.finance_tracker_dashboard.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @GetMapping
  public List<TransactionResponse> getAllTransactions() {
    return transactionService.getAllTransactions();
  }

  @PostMapping
  public TransactionResponse createTransaction(@RequestBody TransactionRequest request) {
    return transactionService.createTransaction(request);
  }

  @PutMapping("/{id}")
  public TransactionResponse updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest request) {
    return transactionService.updateTransaction(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTransaction(@PathVariable Long id) {
    transactionService.deleteTransaction(id);
  }
}