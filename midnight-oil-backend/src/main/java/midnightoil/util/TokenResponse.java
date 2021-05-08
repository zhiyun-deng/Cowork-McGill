package midnightoil.util;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {
	public String access_token;
	public String token_type;
	public String refresh_token;
	public Integer expires_in;
	public String scope;
}
