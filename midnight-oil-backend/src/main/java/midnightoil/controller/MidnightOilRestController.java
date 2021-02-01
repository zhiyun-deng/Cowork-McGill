package midnightoil.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import midnightoil.dto.RequestDto;
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
	public RequestDto createRequest(@RequestParam String date, @RequestParam String startTime, @RequestParam String token) throws IllegalArgumentException {
		Date startDateObj = Date.valueOf(date);
		Time startTimeObj = Time.valueOf(startTime);
		TimeSlot t = service.getTimeSlot(startDateObj,startTimeObj);
		Request r = service.createRequest(t);
		return convertToDto(r);
	}
	@GetMapping(value = { "/token", "/token/" })
	public String getToken() {
		return service.getToken();
	}
	

}
