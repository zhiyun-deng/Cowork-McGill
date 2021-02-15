package midnightoil.dto;

public class TimeslotDto {
	private String startTime;
	private String startDate;
	private String endTime;
	private String endDate;
	public TimeslotDto(String startTime, String startDate, String endTime, String endDate) {
		super();
		this.startTime = startTime;
		this.startDate = startDate;
		this.endTime = endTime;
		this.endDate = endDate;
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
}
