package connectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class StampcardStats extends AppYourselfAPIGetConnector{
	public int moduleId;
	public int stampcardId;
	
	private static String baseUrl="https://api.appyourself.net:443/api/modules/%s/stampcard";
	public StampcardStats(String email, String passw, String randomId, int moduleId) {
		super(email,passw,randomId, String.format(baseUrl, moduleId));
		this.moduleId=moduleId;
		
	}
	
	public Integer getStampcardId() {
		JSONObject j = getJson();
		int stampcardId=j.getInt("id");
		this.stampcardId=stampcardId;
		return stampcardId;
	}
	
	public Integer getQuantity() {
		return getJson().getInt("quantity");
	}
	
	
	public class StampcardHelper extends AppYourselfAPIGetConnector{
		private static final String baseUrl="https://api.appyourself.net:443/api/modules/%s/stampcards/%s/stats";
		public StampcardHelper(String email,String passw, String randomId, int moduleId, int stampcardId) {
			super(email,passw,randomId, String.format(baseUrl, moduleId, stampcardId), true);
		}
		
		public Integer getTotalStamps() {
			JSONArray j = this.getJsonArray();
			int totalStamps=0;
			for(int i=0; i<j.length();i++) {
				JSONObject device = j.getJSONObject(i);
				int currentCount=device.getInt("currentCount");
				int collected = device.getInt("collected");
				totalStamps= totalStamps + currentCount + (collected*getQuantity());
			}
			return totalStamps;
		}
		
		public Integer getUsersWithStamps() {
			return this.getJsonArray().length();
		}
		
		public Integer getAmountOfCollectedPrizes() {
			JSONArray j = this.getJsonArray();
			int amountOfCollectedPrizes=0;
			for(int i =0; i<j.length();i++) {
				amountOfCollectedPrizes += j.getJSONObject(i).getInt("collected");
			}
			return amountOfCollectedPrizes;
		}
	}
	
}

