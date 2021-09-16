package midnightoil.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import midnightoil.dto.RequestDto;
import midnightoil.dto.TimeslotDto;
import midnightoil.model.Pairing;
import midnightoil.model.Request;
import midnightoil.model.TimeSlot;
import midnightoil.service.MidnightOilService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*ALL TIMES ARE UTC TIME*/
@CrossOrigin
@RestController
public class MidnightOilRestController {
	@Autowired
	private MidnightOilService service;
	@DeleteMapping(value = { "/delete", "/delete/" })
	public String deleteRequest(@RequestParam String id, @RequestParam String token) {
		boolean success = service.deleteRequest(id, token);
		if(success) {
			return "Successfully deleted request.";
		}
		else{
			return "Unable to delete request.";
		}
	}
	@PostMapping(value = { "/request", "/request/" })
	public String createRequest(@RequestParam String date, @RequestParam String startTime, @RequestParam String token) throws IllegalArgumentException {
		Date startDateObj = Date.valueOf(date);
		System.out.println(1);
		Time startTimeObj = Time.valueOf(startTime);
		System.out.println(2);
		TimeSlot t = service.getTimeSlot(startDateObj,startTimeObj);
		System.out.println(3);
		if (t==null) {
			t = service.createTimeSlot(startDateObj, startTimeObj);	
			System.out.println(4);
		}
		System.out.println(5);
		Request r = service.createRequest(t,token);
		System.out.println(6);
		if(r==null) {
			return "token invalid";
		}
		return "Success! Your request ID is " + r.getId().toString();
	}
	@GetMapping(value = { "/token", "/token/" })
	public String getToken(@RequestParam String code) {
		return service.getToken(code);
	}
	@GetMapping(value = { "/times", "/times/" })
	public List<TimeslotDto> getAllTimeSlots() {
		List<TimeslotDto> res = new ArrayList<TimeslotDto>();
		for(TimeSlot t : service.getAllTimeSlot()) {
			res.add(convertToDto(t));
		}
		return res;
	}
	@GetMapping(value = { "/active", "/active/" })
	public List<TimeslotDto> getAllActiveTimeslot() {
		List<TimeslotDto> res = new ArrayList<TimeslotDto>();
		for(TimeSlot t : service.getAllActiveTimeSlot()) {
			res.add(convertToDto(t));
		}
		return res;
	}
	/*
	 * This endpoint would require user read permissions, which might be too much. 
	@GetMapping(value = { "/verify", "/verify/" })
	public boolean testVerify(@RequestParam String token) {
		return service.verifyToken(token);
	}*/
	@PostMapping(value = { "/schedule", "/schedule/" })
	public String testVerify(@RequestParam String token, @RequestParam String start_time, @RequestParam Integer duration) {
		return service.scheduleMeeting(token, start_time, duration);
	}
	@GetMapping(value = { "/getrequest", "/getrequest/" })
	public RequestDto getRequestInfo(@RequestParam String id) {
		return convertToDto(service.getRequest(id));
	}
	private RequestDto convertToDto(Request r) {
		if(r==null) {
			return null;
		}
		Set<TimeSlot> times = r.getTimeSlot();
		TimeslotDto[] timesDto = new TimeslotDto[times.size()];
		int i = 0;
		for(TimeSlot t : times) {
			timesDto[i] = convertToDto(t);
		}
		if(r.getPairing()==null) {
			
			return new RequestDto(r.getId(),r.isPaired(),timesDto);
		}
		else {
			Pairing pair = r.getPairing();
			
			return new RequestDto(r.getId(),r.isPaired(),pair.getLink(), timesDto);
		}
	}
	private TimeslotDto convertToDto(TimeSlot t) {
		if(t==null) throw new IllegalArgumentException("TimeSlot is null!");
		return new TimeslotDto(t.getStartTime().toString(),t.getStartDate().toString(),t.getEndTime().toString(),t.getEndDate().toString(), t.getNumRequests(), t.getNumPairs());
	}
	
	

}
