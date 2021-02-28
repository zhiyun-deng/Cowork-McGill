package midnightoil.util;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingRequest {
	public String topic;
	public Integer type;
	public String start_time;
	public Integer duration;
	public String timezone;
	public class settings{
		public boolean join_before_host;
	}
}
