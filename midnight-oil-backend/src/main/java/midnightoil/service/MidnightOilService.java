package midnightoil.service;
import java.sql.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
	private Date windowStartDate;// start of the current booking window
	private Time windowStartTime;
	private Date windowEndDate;
	private Time windowEndTime;
	private boolean initialized = false;
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
	public List<TimeSlot> getAllTimeSlot() {
		return toList(timeSlotRepo.findAllByOrderByStartDateAscStartTimeAsc());
	}
	@Transactional
	public Request createRequest(TimeSlot timeslot, String token) {
		String start_time = "";
		System.out.println(11);
		start_time+=timeslot.getStartDate().toString();
		start_time+="T";
		start_time+=timeslot.getStartTime().toString();
		Integer duration = 60;
		String joinUrl = scheduleMeeting(token,start_time,duration);
		System.out.println(22);
		if(joinUrl==null) return null;
		System.out.println(33);
		Request r = new Request();
		TimeSlot retrievedTimeSlot = getTimeSlot(timeslot.getStartDate(), timeslot.getStartTime());
		Set<TimeSlot> set = new HashSet<TimeSlot>();
		boolean success = set.add(retrievedTimeSlot);
		System.out.println(44);
		r.setTimeSlot(set);
		if(success) {
			r.setLink(joinUrl);
			retrievedTimeSlot.setNumRequests(retrievedTimeSlot.getNumRequests()+1);
			Set<Request> requestSet = retrievedTimeSlot.getRequest();
			if(requestSet==null) {
				requestSet = new HashSet<Request>();
			}
			requestSet.add(r);
			retrievedTimeSlot.setRequest(requestSet);
			requestRepo.save(r);
			timeSlotRepo.save(retrievedTimeSlot);
			System.out.println(retrievedTimeSlot.getNumRequests());
			System.out.println(55);
			return r;
		}
		System.out.println(66);
		return null;
	}
	
	@Transactional
	public TimeSlot getTimeSlot(Date startDate, Time startTime) {
		if(timeSlotRepo.findByStartTimeAndStartDate(startTime, startDate).isEmpty()) return null;
		return timeSlotRepo.findByStartTimeAndStartDate(startTime, startDate).get(0);
	}
	@Transactional
	public TimeSlot createTimeSlot(Date startDate, Time startTime) {
		TimeSlot t = new TimeSlot();
		t.setStartDate(startDate);
		t.setStartTime(startTime);
		t.setEndDate(startDate);
		t.setEndTime(startTime);
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
			/*if(info.email.indexOf("gmail.com")!=-1) {
				return true;
			}*/
			if(info.email.indexOf("mail.mcgill.ca")!=-1) {
				return true;
			}
			return false;
		}
		catch(Exception e) {
			return false;
		}

	}
	public String scheduleMeeting(String token, String start_time, Integer duration) {
		try {
			MeetingRequest req = new MeetingRequest();
			req.topic="Random Co-working Pairing";
			req.timezone="America/Montreal";
			req.type = 2;
			req.start_time = start_time;
			req.duration = duration;
			System.out.println(token);
			MeetingResponse response = webClient.post()
		            .uri("https://api.zoom.us/v2/users/me/meetings")
		            .header("Content-Type","application/json")
		            .header("Authorization", "Bearer " + token)
		            .body(Mono.just(req),MeetingRequest.class)
		            .retrieve()
		            .bodyToMono(MeetingResponse.class)
		            .block();
			System.out.println(111);
			/*
			if(response.host_email.indexOf("gmail.com")!=-1) {
				System.out.println(222);
				return response.join_url;
			}*/
			if(response.host_email.indexOf("mail.mcgill.ca")!=-1) {
				System.out.println(222);
				return response.join_url;
			}
			System.out.println(response.toString());
			System.out.println(333);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	@Transactional
	public Request getRequest(String requestId) {
		Optional<Request> r = requestRepo.findById(requestId);
		if(!r.isPresent()) return null;
		
		return r.get();
	}
	public String getToken(String code) {
		try {
			//development credentials are included. Even if they were production credentials, the security risk would be 
			//minimal since they are credentials of a PUBLIC client, whose behaviour is restricted by the developer Zoom account. 
			//See https://stackoverflow.com/questions/19615372/client-secret-in-oauth-2-0
			//EDIT: credentials are no longer included
			WebClient webClient = WebClient.create("https://zoom.us");
			System.out.println("!!!!!!!!!!!!!!!!!!!"+ System.getenv("ZOOM_CREDENTIAL"));
			TokenResponse response = webClient.post()
		            .uri(uriBuilder -> uriBuilder.path("/oauth/token")
		                    .queryParam("grant_type", "authorization_code")
		                    .queryParam("code", code)
		                    .queryParam("redirect_uri","https://cowork-mcgill.herokuapp.com/#/app")
		                    .build())
		            .header("Authorization","Basic " + System.getenv("ZOOM_CREDENTIAL")) // original is Q3BWQjA0TXNTenlycXhlNmtZUHpOdzozb2F3Z2pCUTFhM3lLQUdXNktxQmM4a1FyZWRsMEpBWA==
		            .retrieve()
		            .bodyToMono(TokenResponse.class)
		            .block();
			return response.access_token;
		}
		catch(Exception e) {
			System.out.println(e.toString());
			return null;
		}
		
	}
	@Transactional
	public void pairAll() {
		//for every timeslot, pair all requests
		Long current = System.currentTimeMillis();
		windowStartDate = new java.sql.Date(current);
		windowStartTime = new java.sql.Time(current);
		windowEndDate = new java.sql.Date(current+TimeUnit.DAYS.toMillis(5)); //the system will look at all timeslots between now 
		windowEndTime = new java.sql.Time(current+TimeUnit.DAYS.toMillis(5)); // and five days from now
		// use spring framework's default methods to find the desired timeslots
		List<TimeSlot> times = timeSlotRepo.findByStartDateAfterOrStartTimeAfter(windowStartDate, windowStartTime);
		List<Request> unpairedRequests = new ArrayList<Request>();
		for (TimeSlot t : times) {
			unpairedRequests.clear();
			for(Request request: t.getRequest()) {
				if(!request.isPaired()) {
					unpairedRequests.add(request);
				}
			}
			
			Collections.shuffle(unpairedRequests);
			while(unpairedRequests.size()>1) {
				Request one = unpairedRequests.remove(unpairedRequests.size()-1);
				Request two = unpairedRequests.remove(unpairedRequests.size()-1);
				Pairing pair = new Pairing();
				Set<TimeSlot> timeslots = new HashSet<TimeSlot>();
				for(TimeSlot requestTimeSlot : one.getTimeSlot()) {
					timeslots.add(requestTimeSlot);
				}
				pair.setTimeSlot(timeslots);
				Set<Request> requests = new HashSet<Request>();
				pair.setRequest(requests);
				String linkToChoose = new Random().nextBoolean()?one.getLink():two.getLink();
				pair.setLink(linkToChoose);
				t.setNumPairs(t.getNumPairs()+1);
				one.setPaired(true);
				two.setPaired(true);
				one.setPairing(pair);
				two.setPairing(pair);
				
				timeSlotRepo.save(t);
				requestRepo.save(one);
				requestRepo.save(two);
				pairingRepo.save(pair);
			}
		}
		
	}
	@Transactional
	public void cleanup() {
		Long current = System.currentTimeMillis();
		Date cleanupStartDate = new java.sql.Date(current - TimeUnit.HOURS.toMillis(1));
		Time cleanupStartTime = new java.sql.Time(current - TimeUnit.HOURS.toMillis(1));
		List<TimeSlot> outdated = timeSlotRepo.findByStartDateEqualsAndStartTimeBeforeOrStartDateBefore(cleanupStartDate, cleanupStartTime,cleanupStartDate); 
		for(TimeSlot t :outdated) {
			pairingRepo.deleteAll(t.getPairing());
			Set<Request> outdatedRequests = t.getRequest();
			for (Request request:outdatedRequests) {
				request.setTimeSlot(null);
			}
			t.setRequest(null);
			requestRepo.deleteAll(t.getRequest());
			timeSlotRepo.delete(t);
		}
				
		
		String startTimeString = cleanupStartTime.toString();
		startTimeString = startTimeString.substring(0, 3)+"00:00";
		String lastIntHourString = cleanupStartDate.toString()+" "+startTimeString;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date;
		try {
			date = sdf.parse(lastIntHourString);
			long lastIntHourMilis = date.getTime();
			if(!initialized) {
				// create all the timeslots for five days ahead
				for(int i = 0; i < 5*24*2; i++) {
					Date dateToCreate = new java.sql.Date(lastIntHourMilis + TimeUnit.MINUTES.toMillis(i*30));
					Time timeToCreate = new java.sql.Time(lastIntHourMilis + TimeUnit.MINUTES.toMillis(i*30));
					createTimeSlot(dateToCreate,timeToCreate );
				}
				initialized = true;
			}
			else {
				Date dateToCreate = new java.sql.Date(lastIntHourMilis + TimeUnit.MINUTES.toMillis(5*24*2*30));
				Time timeToCreate = new java.sql.Time(lastIntHourMilis + TimeUnit.MINUTES.toMillis(5*24*2*30));
			}

		} catch (ParseException e) {
			System.out.println("error in scheduled cleanup");
			e.printStackTrace();
		}
		
		date = new java.util.Date(current);
		System.out.println("Cleanup time: " + sdf.format(date));
		
			
		
		
		
	}
	private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
