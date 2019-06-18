package webSpring.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class DownloadFormURL {

	@Scheduled(fixedRate = 10800000)
	private void doSomeShit() {
		
	}
	
}
