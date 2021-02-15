package midnightoil.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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

/*ALL TIMES ARE UTC TIME*/
@CrossOrigin(origins = "*")
@RestController
public class MidnightOilRestController {
	@Autowired
	private MidnightOilService service;
	@PostMapping(value = { "/request", "/request/" })
	public String createRequest(@RequestParam String date, @RequestParam String startTime, @RequestParam String token) throws IllegalArgumentException {
		Date startDateObj = Date.valueOf(date);
		Time startTimeObj = Time.valueOf(startTime);
		TimeSlot t = service.getTimeSlot(startDateObj,startTimeObj);
		if (t==null) {
			t = service.createTimeSlot(startDateObj, startTimeObj);			
		}
		Request r = service.createRequest(t,token);
		return "Success! Your request ID is " + r.getId().toString();
	}
	@GetMapping(value = { "/token", "/token/" })
	public String getToken(@RequestParam String code) {
		return service.getToken(code);
	}
	@GetMapping(value = { "/verify", "/verify/" })
	public boolean testVerify(@RequestParam String token) {
		return service.verifyToken(token);
	}
	@GetMapping(value = { "/getrequest", "/getrequest/" })
	public RequestDto getRequestInfo(@RequestParam Integer id) {
		return convertToDto(service.getRequest(id));
	}
	private RequestDto convertToDto(Request r) {
		if(r==null) {
			return null;
		}
		TimeSlot[] times = (TimeSlot[])r.getTimeSlot().toArray();
		TimeslotDto[] timesDto = new TimeslotDto[times.length];
		for (int i = 0; i < times.length; i++) {
			timesDto[i] = convertToDto(times[i]);
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
		if(t==null) return null;
		return new TimeslotDto(t.getStartTime().toString(),t.getStartDate().toString(),t.getEndTime().toString(),t.getEndDate().toString());
	}
	
	

}
