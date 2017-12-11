package connectors;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import exceptions.FetchException;

public class BasicAppStatistics extends AppYourselfAPIGetConnector{
	private static LocalDate localDate = new Date()
    		.toInstant()
    		.atZone(ZoneId.systemDefault())
    		.toLocalDate()
    		.minusMonths(2);
    private static String year = Integer.toString(localDate.getYear());
    private static String month = Integer.toString(localDate.getMonthValue()).length()==1 ? "0" + Integer.toString(localDate.getMonthValue()) : Integer.toString(localDate.getMonthValue());
    private static String day = Integer.toString(localDate.getDayOfMonth()).length()==1 ? "0" + Integer.toString(localDate.getDayOfMonth()) : Integer.toString(localDate.getDayOfMonth());
	private static String baseUrl = "https://reseller.appyourself.net:443/api/apps/%s/statistics/app?fromDate=%s-%s-%s";


    public BasicAppStatistics(String email, String passw, String randomid){
    	super(email, passw, randomid, String.format(baseUrl,randomid, year, month, day));
    }

    public Integer getiOSAudience() {
        return Integer.parseInt(getJson().get("iOSDevices").toString());
    }

    public Integer getAndroidAudience() {
        return Integer.parseInt(getJson().get("androidDevices").toString());
    }

    public Integer getOrdersCount() {
        return Integer.parseInt(getJson().get("ordersCount").toString());
    }

    public Integer getModuleEmails() {
        int moduleEmails = 0;
        if (getJson().getJSONArray("moduleEmails").length() != 0) {
            JSONArray mEmails = getJson().getJSONArray("moduleEmails");
            for (int c=0; c<mEmails.length(); c++){
            	JSONObject mE = new JSONObject(mEmails.getJSONObject(c).toString());
            	JSONArray e = mE.getJSONArray("emails");
            	int i = 0;
                while (i < e.length()) {
                    JSONObject r = new JSONObject(e.getJSONObject(i).toString());
                    moduleEmails += Integer.parseInt(r.get("emails").toString());
                    ++i;
                }
            }
        }
        return moduleEmails;
    }
}