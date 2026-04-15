package de.mschwan.finance_tracker_dashboard.controller;

import de.mschwan.finance_tracker_dashboard.dto.TransactionRequest;
import de.mschwan.finance_tracker_dashboard.dto.TransactionResponse;
import de.mschwan.finance_tracker_dashboard.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
  public List<TransactionResponse> getAllTransactions(@AuthenticationPrincipal Jwt jwt) {
    return transactionService.getTransactionsByUserId(jwt.getSubject());
  }

  @PostMapping
  public TransactionResponse createTransaction(
          @RequestBody TransactionRequest request,
          @AuthenticationPrincipal Jwt jwt) {
    return transactionService.createTransaction(request, jwt.getSubject());
  }

  @PutMapping("/{id}")
  public TransactionResponse updateTransaction(
          @PathVariable Long id,
          @RequestBody TransactionRequest request,
          @AuthenticationPrincipal Jwt jwt) {
    return transactionService.updateTransaction(id, request, jwt.getSubject());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTransaction(
          @PathVariable Long id,
          @AuthenticationPrincipal Jwt jwt) {
    transactionService.deleteTransaction(id, jwt.getSubject());
  }
}