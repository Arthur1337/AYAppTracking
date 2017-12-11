package connectors;
import exceptions.FetchException;


public class PushNotificationDetails extends AppYourselfAPIGetConnector{
	private static String baseURL = "https://api.appyourself.net:443/api/apps/%s/getNotificationsList?status=SENT&page=0&pageSize=1";

    public PushNotificationDetails(String email, String passw, String randomid){
    	
    	super(email, passw, randomid, String.format(baseURL, randomid));
    }


    public String getTotalElements() {
        return getJson().get("totalElements").toString();
    }
}