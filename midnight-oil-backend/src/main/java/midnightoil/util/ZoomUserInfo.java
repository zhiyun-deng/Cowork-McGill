package midnightoil.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoomUserInfo {
	public String id;
	public String email;
}
