package connectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class ResellerApps {
    private String accessToken, email, passw;

    public ResellerApps(String email, String passw) {
        this.email = email;
        this.passw = passw;
    }

	public ArrayList<String> getApps() {
    	ArrayList<String> randomids = new ArrayList<String>();
    	int cForTests=0;
    	try{
    		
    	
    		int page = 0;
    		String[] results = helper(page);
    		int totalPages = Integer.parseInt(results[2]);
    		int nextPage = Integer.parseInt(results[1]);
    		randomids.add(results[0]);
    		//while (cForTests < 20) {
    		while (nextPage < totalPages) {
    			results = helper(nextPage);
    			if (!(results[1] instanceof String)) break;
    			totalPages = Integer.parseInt(results[2]);
    			nextPage = Integer.parseInt(results[1]);
    			randomids.add(results[0]);
    			cForTests++;
    		}
    	} catch(Exception e){
    		System.out.println("Exception in gathering App RandomIds with " + e.getMessage());
    	} finally{
    		return randomids;
    	}
    		
    }

    private String[] helper(int page) throws Exception {
        String inputLine;
        String[] result = new String[3];
        AccessToken a = new AccessToken(this.email, this.passw);
        this.accessToken= a.getAccessToken();
        String url = "https://reseller.appyourself.net:443/api/apps?pageSize=1&page=" + page + "&property=o.webPublished&search=&sort=DESC&ignoreCase=false&ignoreDesigner=true";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection)obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer" + this.accessToken);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        try {
            JSONObject json = new JSONObject(response.toString());
            JSONArray j = json.getJSONArray("content");
            JSONObject app = new JSONObject(j.getJSONObject(0).toString());
            JSONObject orderPackage = new JSONObject(app.getJSONObject("orderPackage").toString());
            String app_s = app.get("randomId").toString();
            if (orderPackage.toString() != "null") {
                result[0] = app_s;
                result[1] = json.get("nextPage").toString();
                result[2] = json.get("totalPages").toString();
            }
        }
        catch (Exception e) {
            // empty catch block
        }
        return result;
    }
}
