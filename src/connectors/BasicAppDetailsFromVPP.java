package connectors;

import exceptions.FetchException;

public class BasicAppDetailsFromVPP extends AppYourselfAPIGetConnector{
	private static String baseUrl = "https://api.appyourself.net:443/api/vpp/wizard/apps/";

	public BasicAppDetailsFromVPP(String email, String passw, String randomid) {
		super(email, passw, randomid, baseUrl + randomid);
		
	}
	

	public String getAppId() {
		return getJson().get("id").toString();
	}
}
