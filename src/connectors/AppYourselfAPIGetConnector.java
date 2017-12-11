package connectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import exceptions.FetchException;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class AppYourselfAPIGetConnector {
	private String at;
	private String randomid;
	private String url;
	private JSONObject json = new JSONObject();
	private JSONArray jsonArray = new JSONArray();
	private boolean connectionFailed= false;

	public AppYourselfAPIGetConnector(String email, String passw, String randomId, String url){

		try{
			AccessToken token = new AccessToken(email, passw);
			this.at = token.getAccessToken();
			this.randomid= randomId;
			this.url = url;
			this.json = helper();
		}
		catch(Exception fe){
			//this.json = new JSONObject();
			connectionFailed=true;
		}

	}
	public AppYourselfAPIGetConnector(String email, String passw, String randomId, String url, boolean requiresArrayAsjson){
		if (requiresArrayAsjson==true){
			try{
				AccessToken token = new AccessToken(email, passw);
				this.at = token.getAccessToken();
				this.randomid= randomId;
				this.url = url;
				this.jsonArray = arrayHelper();
			}
			catch(Exception fe){
				//this.jsonArray = new JSONArray();
				connectionFailed=true;
				fe.printStackTrace();
			}
		}
	}


	private JSONObject helper() throws FetchException{
		try{
			String inputLine;
			URL obj = new URL(this.url);
			HttpURLConnection con = (HttpURLConnection)obj.openConnection();
			con.setConnectTimeout(15000);
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", "Bearer" + this.at);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			JSONObject jsonResponse = new JSONObject(response.toString());
			return jsonResponse;
		} catch(IOException e){
			throw new FetchException("Failed to fetch json with IOException " + e.getMessage());
		}
	}

	public void refreshJson() throws FetchException{
		this.json=helper();
	}
	private JSONArray arrayHelper() throws FetchException {
		try{
			String inputLine;
			URL obj = new URL(getURL());
			HttpURLConnection con = (HttpURLConnection)obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", "Bearer" + getAccessToken());
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			JSONArray jsonResponse = new JSONArray(response.toString());
			return jsonResponse;
		}catch (IOException e){
			throw new FetchException("Failed to fetch json with IOException " + e.getMessage());
		}
	}

	public JSONObject getJson(){
		return this.json;
	}
	public JSONArray getJsonArray(){
		return this.jsonArray;
	}

	public String getURL(){
		return this.url;
	}
	public void setURL(String url){
		this.url = url;
	}

	public String getAccessToken(){
		return this.at;
	}

	public String getRandomId(){
		return this.randomid;
	}
	
	public boolean connectionFailed(){
		return connectionFailed;
	}

}
