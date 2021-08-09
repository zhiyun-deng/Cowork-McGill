package midnightoil.dto;

public class TimeslotDto {
	private String startTime;
	private String startDate;
	private String endTime;
	private String endDate;
	private int numRequests;
	private int numPairs;
	public TimeslotDto(String startTime, String startDate, String endTime, String endDate) {
		super();
		this.startTime = startTime;
		this.startDate = startDate;
		this.endTime = endTime;
		this.endDate = endDate;
	}
	public TimeslotDto(String startTime, String startDate, String endTime, String endDate, int numRequests, int numPairs) {
		super();
		this.startTime = startTime;
		this.startDate = startDate;
		this.endTime = endTime;
		this.endDate = endDate;
		this.numRequests = numRequests;
		this.numPairs = numPairs;
	}
	public String getStartTime() {
		return startTime;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getEndTime() {
		return endTime;
	}
	public String getEndDate() {
		return endDate;
	}
	@Override
	public String toString() {
		return "[startTime=" + startTime + ", startDate=" + startDate + ", endTime=" + endTime
				+ ", endDate=" + endDate + "]";
	}
	public int getNumRequests() {
		return numRequests;
	}
	public int getNumPairs() {
		return numPairs;
	}
	
}
