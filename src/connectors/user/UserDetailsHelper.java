package connectors.user;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDetailsHelper {

    private String email, passw, randomId;
    private ArrayList<String> entities = new ArrayList<>();
    public int iOSUser, androidUser, browserUser,widgetUser, unidentifiedUser=0;
    public int totalUsersWithChat=0;
    public int totalUsersFromHomescreen=0;
    public int messagesFromUser, messagesFromOwner=0;
    private HashMap<String, Integer> mostValuableModules = new HashMap<>(); //TODO doesnt make sense, change to TreeSet
    public int averageSessions=0;
    public int pushAudience=0;


    public UserDetailsHelper(String email, String passw, String randomId, ArrayList<String> entities){
        this.email=email;
        this.passw=passw;
        this.randomId=randomId;
        this.entities=entities;
        helper();
    }

    private void helper(){
        for(String entity:entities){
            UserDetails userDetails = new UserDetails(this.email,this.passw,this.randomId,entity);
            if(!userDetails.connectionFailed()){
                String platform = userDetails.getPlatform();
                switch (platform){
                    case "iOS": iOSUser++;
                        break;
                    case "Android": androidUser++;
                        break;
                    case "browser": browserUser++;
                        break;
                    case "widget": widgetUser++;
                        break;
                    default: unidentifiedUser++;
                        break;
                }
                if(userDetails.isChatUser)
                    totalUsersWithChat++;

                messagesFromOwner+=userDetails.getMessagesToUser();
                messagesFromUser+=userDetails.getMessagesToOwner();

                String favModule = userDetails.getFavoriteModule();
                if(mostValuableModules.containsKey(favModule))
                    mostValuableModules.put(favModule,mostValuableModules.get(favModule)+1);
                else
                    mostValuableModules.put(favModule,1);

                if(userDetails.isPushAudient)
                    pushAudience++;
            }
        }
    }

}

