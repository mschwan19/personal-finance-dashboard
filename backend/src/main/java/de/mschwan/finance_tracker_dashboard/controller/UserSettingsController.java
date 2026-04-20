package de.mschwan.finance_tracker_dashboard.controller;

import de.mschwan.finance_tracker_dashboard.dto.BalanceRequest;
import de.mschwan.finance_tracker_dashboard.model.UserSettings;
import de.mschwan.finance_tracker_dashboard.repository.UserSettingsRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/settings")
public class UserSettingsController {

  private final UserSettingsRepository settingsRepository;

  public UserSettingsController(UserSettingsRepository settingsRepository) {
    this.settingsRepository = settingsRepository;
  }

  @GetMapping("/balance")
  public BigDecimal getInitialBalance(@AuthenticationPrincipal Jwt jwt) {
    return settingsRepository.findById(jwt.getSubject())
            .map(UserSettings::getInitialBalance)
            .orElse(BigDecimal.ZERO);
  }

  @PutMapping("/balance")
  public void updateInitialBalance(@RequestBody BalanceRequest request, @AuthenticationPrincipal Jwt jwt) {
    UserSettings settings = settingsRepository.findById(jwt.getSubject())
            .orElseGet(() -> {
              UserSettings newSettings = new UserSettings();
              newSettings.setId(jwt.getSubject());
              return newSettings;
            });

    settings.setInitialBalance(request.initialBalance());
    settingsRepository.save(settings);
  }
}