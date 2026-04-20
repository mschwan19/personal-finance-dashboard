package de.mschwan.finance_tracker_dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinanceTrackerDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceTrackerDashboardApplication.class, args);
	}

}
