package midnightoil;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import midnightoil.service.MidnightOilService;

@Component
public class ScheduledTasks {
	private MidnightOilService service;
	@Scheduled(fixedRate=60000)
	public void scheduledPairing() {
		service.pairAll();
	}
	@Scheduled(fixedRate=86400000)
	public void scheduledCleanup() {
		service.cleanup();
	}
}
