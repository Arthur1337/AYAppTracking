package connectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;

public class AccessToken {
    private String resellerEmail;
    private String resellerPassword;

    public AccessToken(String email, String passw) {
        //this.resellerEmail = "master:" +email;
        this.resellerEmail = "ay:" +email;
        this.resellerPassword = passw;
    }

    public String getAccessToken() throws Exception {

        int c;
        URL url = new URL("https://api.appyourself.net/oauth/token");
        LinkedHashMap<String, String> payload = new LinkedHashMap<String, String>();
        payload.put("grant_type", "password");
        payload.put("username", this.resellerEmail);
        payload.put("password", this.resellerPassword);
        payload.put("client_id", this.resellerEmail);
        payload.put("client_secret", this.resellerPassword);
        //payload.put("scope", "read");   //needed for master access
        LinkedHashMap<String, Object> options = new LinkedHashMap<String, Object>();
        options.put("Accept", "application/json");
        options.put("method", "post");
        options.put("payload", payload);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry param : payload.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode((String) param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        while ((c = in.read()) >= 0) {
            sb.append((char)c);
        }
        String response = sb.toString();
        JSONObject json = new JSONObject(response);
        return json.get("access_token").toString();
    }
    
    

}