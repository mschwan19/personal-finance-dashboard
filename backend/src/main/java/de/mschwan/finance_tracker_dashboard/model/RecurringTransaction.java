package de.mschwan.finance_tracker_dashboard.model;

import de.mschwan.finance_tracker_dashboard.converter.BigDecimalCryptoConverter;
import de.mschwan.finance_tracker_dashboard.converter.StringCryptoConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "recurring_transactions")
@Getter
@Setter
@NoArgsConstructor
public class RecurringTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Convert(converter = StringCryptoConverter.class)
  @Column(nullable = false, columnDefinition = "varchar(255)")
  private String description;

  @Convert(converter = BigDecimalCryptoConverter.class)
  @Column(nullable = false, columnDefinition = "varchar(255)")
  private BigDecimal amount;

  @Column(name = "category_id")
  private Long categoryId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CategoryType type;

  @Column(nullable = false)
  private String recurrenceInterval;

  @Column(name = "next_execution_date", nullable = false)
  private LocalDate nextExecutionDate;
}