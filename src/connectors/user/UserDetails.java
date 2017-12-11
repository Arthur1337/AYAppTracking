package connectors.user;

import connectors.AppYourselfAPIGetConnector;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

public class UserDetails extends AppYourselfAPIGetConnector {

    private static String baseUrl= "https://api.appyourself.net/api/apps/%s/users/%s";
    public int totalMessages,totalSessions, messagesToUser, messagesToOwner=0;
    public double medianAppResponseTime, medianAppOwnerResponseTime=0.0;
    public boolean isChatUser = false;
    public String platform;
    public String favoriteModule;
    public boolean addedToHomescreen = false;
    public boolean hasSeenHSDialog = false;  //true if user has seen the add-to-homescreen dialog
    public boolean isPushAudient=false;


    public UserDetails(String email, String passw, String randomId, String userId){
        super(email, passw, randomId,String.format(baseUrl,randomId,userId));

        if (!connectionFailed()) {
            this.platform=getJson().get("platform").toString();
            this.favoriteModule=getJson().get("mostVisitedModule").toString();
            this.isPushAudient=isPushAudient();

            chatHelper();
            sessionHelper();


            if (isWebAppUser())
                webAppUserHelper();


        }

    }

    /**
     *
     */
    private void webAppUserHelper() {

        this.addedToHomescreen = getJson().getBoolean("homescreen");

        //condition that user has seen HS dialog:
        // 1. is Webapp User
        // 2. number of sessions > 1
        // 3. last visit after release date (24.10.2017)

        boolean enoughSessions=false;
        boolean validLastVisit=false;

        if (this.totalSessions > 1)
                enoughSessions=true;
        Timestamp hsRelease = new Timestamp(1508806800); //release date
        Timestamp lastVisit = new Timestamp(getJson().getLong("lastVisit"));
        if(lastVisit.after(hsRelease))
            validLastVisit = true;

        if (enoughSessions && validLastVisit)
            hasSeenHSDialog = true;



    }

    public String getFavoriteModule() {
        return this.favoriteModule;
    }

    public boolean isWebAppUser(){
        return (getPlatform().equals("browser"));

    }

    private boolean isPushAudient(){
        if (getJson().get("hwid").toString().equals("null") && getJson().get("pnsid").toString().equals("null"))
            return false;
        else
            return true;
    }

    private void chatHelper(){
        JSONArray chat = getJson().getJSONArray("chat");
        if (chat.length()>0){
            this.isChatUser=true;

            for(int i=0; i<chat.length();i++){
                Boolean appOwner = chat.getJSONObject(i).getBoolean("appOwner");

                if(appOwner)
                    this.messagesToUser++;
                else
                    this.messagesToOwner++;
            }
            this.medianAppResponseTime=medianAppResponseTime(chat);
            this.medianAppOwnerResponseTime=appOwnerResponseTime(chat);
        }
        this.totalMessages = this.messagesToOwner + this.messagesToUser;
    }


    private void sessionHelper(){
        // TODO compute number of sessions
        JSONArray sessions = getJson().getJSONArray("sessions");
        this.totalSessions = sessions.length();
        if(sessions.length()>0){



            // TODO compute average session length



        }



    }
    /**
     * @param chat
     * @return median bidirectional conversation response time in seconds
     */
    private double medianAppResponseTime(JSONArray chat){
        double numberOfConversations=0.0;
        long timeToResponse=0;
        long lower, upper=0;
        int index=0;
        boolean appOwner, opposite=true;
        while(index<chat.length()){
            appOwner = chat.getJSONObject(index).getBoolean("appOwner");
            lower = chat.getJSONObject(index).getLong("timestamp");
            index++;
            for (int i=index; i<chat.length();i++){
                opposite = chat.getJSONObject(i).getBoolean("appOwner");
                if(!(appOwner==opposite)){
                    upper= chat.getJSONObject(i).getLong("timestamp");
                    numberOfConversations++;
                    Date d1 = new Date(lower);
                    Date d2 = new Date(upper);
                    long diff = (d2.getTime() - d1.getTime()) / 1000;
                    timeToResponse+= diff;
                    lower=upper;
                    index=i;
                    break;
                }
            }
        }
        if (numberOfConversations > 0) {
            return (((double) timeToResponse)/numberOfConversations);
        } else return 0;
    }

    /**
     * @param chat
     * @return average response time of app owner to answer
     */
    private double appOwnerResponseTime(JSONArray chat){
        double numberOfConversations=0.0;
        long timeToResponse=0;
        long lower, upper=0;
        //int index=0;
        boolean appOwner, opposite=false;

        for (int index=0; index < chat.length();index++) {
            appOwner=chat.getJSONObject(index).getBoolean("appOwner");
            if(!appOwner){ //its customer
                lower=chat.getJSONObject(index).getLong("timestamp");
                for(int i=index;i<chat.length();i++){
                    opposite=chat.getJSONObject(i).getBoolean("appOwner");
                    if(opposite){ //its appOwner
                        upper=chat.getJSONObject(i).getLong("timestamp");
                        numberOfConversations++;
                        Date d1 = new Date(lower);
                        Date d2 = new Date(upper);
                        long diff = (d2.getTime() - d1.getTime()) / 1000;
                        timeToResponse+= diff;
                        index=i;
                        break;
                    }
                }
            }
        }
        if (numberOfConversations > 0) {
            return (((double) timeToResponse)/numberOfConversations);
        } else return 0;
    }

    public String getPlatform(){
        return this.platform;
    }

    public int getMessagesToUser(){
        return this.messagesToUser;
    }

    public int getMessagesToOwner(){
        return this.messagesToOwner;
    }


}
