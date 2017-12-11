package connectors;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;

import org.json.JSONObject;

import exceptions.FetchException;

public class BasicAppDetails extends AppYourselfAPIGetConnector {
	private static String baseUrl = "https://api.appyourself.net:443/api/apps/";
    
   

    public BasicAppDetails(String email, String passw, String randomid){
    	super(email,passw, randomid, baseUrl + randomid);
    }
    
//TODO ESCAPE COMMA IN APP NAME
    public String getAppName() {
    	return getJson().get("name").toString();
    }

    public String getExternalId() {
        JSONObject orderPackage = new JSONObject(getJson().getJSONObject("orderPackage").toString());
        return orderPackage.get("externalId").toString();
    }

    public String getCustomer() {
        return getJson().get("customer").toString();
    }

    public String getSubmissionServiceAppStore() {
        return getJson().get("submissionServiceAppStore").toString();
    }

    public String getSubmissionServiceGooglePlay() {
        return getJson().get("submissionServiceGooglePlay").toString();
    }

    public String getSubmissionServiceAmazon() {
        return getJson().get("submissionServiceAmazon").toString();
    }

    public String getAppStoreURL() {
        return getJson().get("appStoreUrl").toString();
    }

    public String getGooglePlayURL() {
        return getJson().get("googlePlayId").toString();
    }
    
    public boolean isContractStartDateInRange(){
        if (getJson().get("contractStartDate").toString() != "null") {
            long timestamp = getJson().getLong("contractStartDate");
            Timestamp t = new Timestamp(timestamp);
            LocalDate l = t.toLocalDateTime().toLocalDate();
            LocalDate min = LocalDate.now().minusMonths(2);
            if(min.isBefore(l)){
            	return true;
            }else return false;
        } else return false;
    }

    public String getContractStartDate() {
        if (getJson().get("contractStartDate").toString() != "null") {
            long timestamp = getJson().getLong("contractStartDate");
            Timestamp t = new Timestamp(timestamp);
            LocalDate l = t.toLocalDateTime().toLocalDate();
            return l.toString();
        }
        return "null";
    }

    public String getHasNative() {
        return getJson().get("hasNative").toString();
    }

    public Integer getModuleOccurance(String Module){
        int occurance=0;
                                    // TODO WHAT HAPPENED HERE????
        return occurance;
    }

    private HashMap<String, Integer> getAppModules(){
        HashMap<String, Integer> moduleOccurances = new HashMap<>();
        String[] modules = {"TEXT","MAP","COUPON","GALLERY","NEWSCENTER","EVENTS","CONTACT","SUBMENU","SUBMENU_SEPARATOR","RESERVATION","VIDEO","USER_MODULE","RESERVATION2","RESERVATION3","RESERVATION4","COLLECTION_PRODUCT","COLLECTION_MENU","COLLECTION_BRANCH","BUSINESS_HOURS","PDF",
                "TABLE_RESERVATION","FORMS","EXTERNAL_SERVICE_APPOINTMENT","WEBSITE","EXTERNAL_SERVICE_REVIEW", "STAMPCARD"};

        return moduleOccurances;
    }


}