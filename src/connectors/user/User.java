package connectors.user;

import connectors.AppYourselfAPIGetConnector;
import org.json.JSONArray;

import java.util.ArrayList;

public class User extends AppYourselfAPIGetConnector {

    private static String baseUrl= "https://api.appyourself.net/api/apps/%s/users?size=%s";
    private static int baseSize=1000;
    public ArrayList<String> entities = new ArrayList<String>();
    private Integer totalUsers=-1;

    public User(String email, String passw, String randomId){
        super(email, passw, randomId, String.format(baseUrl, randomId, baseSize));

        if(!connectionFailed())
            parseEntities();
    }


    public ArrayList<String> getEntities() {
        return entities;
    }


    public Integer getTotalUsers(){
        return totalUsers;
    }



    private void parseEntities(){
        enrichJson();
        JSONArray jsonEntities = getJson().getJSONArray("entities");
        for (int i=0; i<jsonEntities.length(); i++){
            entities.add(jsonEntities.getJSONObject(i).getString("id"));
        }
        totalUsers=entities.size();
    }

    /**
     * Finds the json response with all users of this app
     */
    private void enrichJson(){
        int users = getJson().getJSONObject("metainfo").getInt("size");
        boolean maxSize=(users<baseSize);
        while(!maxSize){
            int size=baseSize*4;
            this.setURL(String.format(baseUrl,getRandomId(), size));
            refreshJson();
            users = getJson().getJSONObject("metainfo").getInt("size");
            if(users<size){
                maxSize=true;
            }
        }
    }


}
