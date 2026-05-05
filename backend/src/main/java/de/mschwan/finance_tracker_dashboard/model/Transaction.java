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
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Convert(converter = BigDecimalCryptoConverter.class)
  @Column(nullable = false, columnDefinition = "varchar(255)")
  private BigDecimal amount;

  @Column(nullable = false)
  private LocalDate date;

  @Convert(converter = StringCryptoConverter.class)
  @Column(columnDefinition = "varchar(255)")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;
}