package de.mschwan.finance_tracker_dashboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**") // Gilt für alle Endpunkte, die mit /api/ anfangen
            .allowedOrigins("http://localhost:5173") // Dein Vue-Frontend
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // Erlaubte Befehle
  }
}