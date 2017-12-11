package connectors;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import exceptions.FetchException;

public class WebAppDetails extends AppYourselfAPIGetConnector{
	private static LocalDate startDate = new Date()
			.toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDate()
			.minusDays(60);
	private static String baseURL = "https://api.appyourself.net:443/api/apps/%s/statistics/analytics?fromDate=%s";
    private int newUsers;
    private int total = 0;

    public WebAppDetails(String email, String passw, String randomid){
        super(email, passw, randomid, String.format(baseURL, randomid, startDate), true);
        this.getTotalEntrances();
    }
    
//    private JSONArray helper() throws Exception {
//        String inputLine;
//        Date t = new Date();
//        LocalDate today = t.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate startDate = today.minusDays(60);
//        String url = "https://api.appyourself.net:443/api/apps/" + this.randomid + "/statistics/analytics?fromDate=" + startDate.toString();
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection)obj.openConnection();
//        con.setRequestMethod("GET");
//        con.setRequestProperty("Authorization", "Bearer" + this.at);
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        StringBuffer response = new StringBuffer();
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//        JSONArray jsonResponse = new JSONArray(response.toString());
//        return jsonResponse;
//    }

    public void getTotalEntrances() {
        JSONArray j = getJsonArray();//this.json;
        int arraylength = j.length();
        int i = 0;
        while (i < arraylength) {
            JSONObject part = j.getJSONObject(i);
            JSONArray baseStatistics = part.getJSONArray("baseStatistics");
            if (baseStatistics.length() > 0) {
                JSONObject overall = baseStatistics.getJSONObject(baseStatistics.length() - 1);
                String statisticsType = overall.get("statisticType").toString();
                if (statisticsType.equals("OVERALL")) {
                    int newUser = overall.getInt("newUsers");
                    this.newUsers += newUser;
                    int totalUser = overall.getInt("entrances");
                    this.total += totalUser;
                } else {
                    System.out.println("statisticType does not match OVERALL, instead found: " + statisticsType);
                }
            }
            ++i;
        }
    }

    public int getNewVisits() {
        return this.newUsers;
    }

    public int getReturningVisits() {
        return this.total - this.newUsers;
    }
}