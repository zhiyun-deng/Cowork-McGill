package midnightoil.dto;

public class RequestDto {
	private Integer id;
	private boolean paired;
	private String zoomLink;
	private TimeslotDto[] times;
	public RequestDto(Integer id, boolean paired, TimeslotDto[] times) {
		this.id = id;
		this.paired = paired;
		this.times = times;
	}
	public RequestDto(Integer id, boolean paired, String zoomLink, TimeslotDto[] times) {
		this.id = id;
		this.paired = paired;
		this.zoomLink = zoomLink;
		this.times = times;
	}
	public Integer getId() {
		return id;
	}
	public boolean isPaired() {
		return paired;
	}
	public String getZoomLink() {
		return zoomLink;
	}
	public String getTimeslotString() {
		String result = "";
		for(TimeslotDto time:times) {
			result  = result + time.toString()+ "\n";
		}
		return result;
	}
	
	

}
