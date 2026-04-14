package de.mschwan.finance_tracker_dashboard.controller;

import de.mschwan.finance_tracker_dashboard.dto.TransactionRequest;
import de.mschwan.finance_tracker_dashboard.dto.TransactionResponse;
import de.mschwan.finance_tracker_dashboard.service.TransactionService;
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
}