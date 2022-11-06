package hu.otpbank.omnichannel.phishingfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PhishingFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhishingFinderApplication.class, args);
	}
}
