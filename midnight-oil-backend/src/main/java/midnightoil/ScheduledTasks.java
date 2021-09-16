package midnightoil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import midnightoil.service.MidnightOilService;

@Component
@EnableScheduling
public class ScheduledTasks {
	@Autowired
	private MidnightOilService service;
	@Scheduled(fixedRate=60000)
	public void scheduledPairing() {
		service.pairAll();
	}
	// should be less frequent than once an hour. 
	@Scheduled(fixedRate=1800000)
	public void scheduledCleanup() {
		service.cleanup();
	}
}
