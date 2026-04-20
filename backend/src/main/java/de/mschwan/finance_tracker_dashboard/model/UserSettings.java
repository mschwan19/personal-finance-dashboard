package de.mschwan.finance_tracker_dashboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "user_settings")
@Getter
@Setter
@NoArgsConstructor
public class UserSettings {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "initial_balance", nullable = false)
  private BigDecimal initialBalance = BigDecimal.ZERO;
}