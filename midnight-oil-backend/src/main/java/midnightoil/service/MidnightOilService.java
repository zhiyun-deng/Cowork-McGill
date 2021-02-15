package midnightoil.service;
import java.sql.Date;
import java.util.Set;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import midnightoil.model.*;
import midnightoil.dao.*;
import midnightoil.util.*;
import reactor.core.publisher.Mono;

@Service
public class MidnightOilService {
	@Autowired
	PairingRepository pairingRepo;
	@Autowired
	RequestRepository requestRepo;
	@Autowired
	TimeSlotRepository timeSlotRepo;
	
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
	public Request createRequest(TimeSlot timeslot, String token) {
		if(!verifyToken(token)) return null;
		Request r = new Request();
		Set<TimeSlot> set = new HashSet<TimeSlot>();
		boolean success = set.add(timeslot);
		r.setTimeSlot(set);
		if(success) {
			timeslot.setNumRequests(timeslot.getNumRequests()+1);
			requestRepo.save(r);
			return r;
		}
		return null;
	}
	
	@Transactional
	public TimeSlot getTimeSlot(Date startDate, Time startTime) {
		return timeSlotRepo.findByStartTimeAndStartDate(startTime, startDate).get(0);
	}
	@Transactional
	public TimeSlot createTimeSlot(Date startDate, Time startTime) {
		TimeSlot t = new TimeSlot();
		t.setStartDate(startDate);
		t.setStartTime(startTime);
		t.setNumPairs(0);
		t.setNumRequests(0);
		timeSlotRepo.save(t);
		return t;
	}
	@Transactional
	public boolean hasUnpairedRequest(TimeSlot t) {
		return (t.getNumRequests()>t.getNumPairs()*2);
	}
	public boolean verifyToken(String token) {
		try {
			ZoomUserInfo info = webClient.get()
		            .uri("https://api.zoom.us/v2/users/me")
		            .header("Authorization", "Bearer " + token)
		            .retrieve()
		            .bodyToMono(ZoomUserInfo.class)
		            .block();
			if(info.email.indexOf("gmail.com")!=-1) {
				return true;
			}
			return false;
		}
		catch(Exception e) {
			return false;
		}

	}
	public Request getRequest(Integer requestId) {
		Optional<Request> r = requestRepo.findById(requestId);
		if(!r.isPresent()) return null;
		
		return r.get();
	}
	public String getToken(String code) {
		return null;
	}
	
	private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
