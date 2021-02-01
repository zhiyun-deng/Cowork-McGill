package midnightoil.service;
import java.sql.Date;
import java.util.Set;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import midnightoil.model.*;
import midnightoil.dao.*;

@Service
public class MidnightOilService {
	@Autowired
	PairingRepository pairingRepo;
	@Autowired
	RequestRepository requestRepo;
	@Autowired
	TimeSlotRepository timeSlotRepo;
	@Autowired
	WebClient webClient;
	private static final String INSTALL_LINK = "https://zoom.us/oauth/authorize?response_type=code&client_id=CpVB04MsSzyrqxe6kYPzNw&redirect_uri=https%3A%2F%2Fmidnight-oil.herokuapp.com%2F";
	/* What do I hope this app accomplish? */
	//submit a request
	//verify student
	//display timeslots
	//match a request and pair
	//display timeslot with outstanding request
	public MidnightOilService() {
		webClient = WebClient.create();
	}
	@Transactional
	public Request createRequest(TimeSlot timeslot) {
		
		Request r = new Request();
		Set<TimeSlot> set = new HashSet<TimeSlot>();
		boolean success = set.add(timeslot);
		r.setTimeSlot(set);
		if(success) {
			requestRepo.save(r);
			return r;
		}
		return null;
	}
	@Transactional
	public TimeSlot getTimeSlot(Date startDate, Time startTime) {
		return timeSlotRepo.findByStartTimeAndStartDate(startTime, startDate).get(0);
	}
	public String getToken() {
		WebClient.UriSpec<WebClient.RequestBodySpec> request2 = webClient.post();
		WebClient.RequestBodySpec uri1 = webClient
				  .method(HttpMethod.POST)
				  .uri("/resource");
		return "";
	}
	private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
