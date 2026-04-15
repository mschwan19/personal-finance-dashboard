package de.mschwan.finance_tracker_dashboard.service;

import de.mschwan.finance_tracker_dashboard.dto.TransactionRequest;
import de.mschwan.finance_tracker_dashboard.dto.TransactionResponse;
import de.mschwan.finance_tracker_dashboard.model.Category;
import de.mschwan.finance_tracker_dashboard.model.Transaction;
import de.mschwan.finance_tracker_dashboard.repository.CategoryRepository;
import de.mschwan.finance_tracker_dashboard.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final CategoryRepository categoryRepository;

  public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
    this.transactionRepository = transactionRepository;
    this.categoryRepository = categoryRepository;
  }

  public List<TransactionResponse> getAllTransactions() {
    return transactionRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  public TransactionResponse createTransaction(TransactionRequest request) {
    Category category = categoryRepository.findById(request.categoryId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

    Transaction transaction = new Transaction();
    transaction.setAmount(request.amount());
    transaction.setDate(request.date());
    transaction.setDescription(request.description());
    transaction.setCategory(category);

    Transaction savedTransaction = transactionRepository.save(transaction);
    return mapToResponse(savedTransaction);
  }

  private TransactionResponse mapToResponse(Transaction transaction) {
    return new TransactionResponse(
            transaction.getId(),
            transaction.getAmount(),
            transaction.getDate(),
            transaction.getDescription(),
            transaction.getCategory().getId(),
            transaction.getCategory().getName(),
            transaction.getCategory().getColorHex()
    );
  }

  public void deleteTransaction(Long id) {
    if (!transactionRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TRANSACTION_NOT_FOUND");
    }
    transactionRepository.deleteById(id);
  }

  public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
    Transaction existingTransaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaktion nicht gefunden"));

    Category category = categoryRepository.findById(request.categoryId())
            .orElseThrow(() -> new RuntimeException("Kategorie nicht gefunden"));

    existingTransaction.setAmount(request.amount());
    existingTransaction.setDate(request.date());
    existingTransaction.setDescription(request.description());
    existingTransaction.setCategory(category);


    Transaction savedTransaction = transactionRepository.save(existingTransaction);
    return mapToResponse(savedTransaction);
  }
}