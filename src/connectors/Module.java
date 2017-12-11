package connectors;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class Module extends AppYourselfAPIGetConnector {
	
	private static String baseUrl="https://api.appyourself.net:443/api/apps/%s/modules";

	private String[] modules = {"TEXT","MAP","COUPON","GALLERY","NEWSCENTER","EVENTS","CONTACT","SUBMENU","SUBMENU_SEPARATOR","RESERVATION","VIDEO","USER_MODULE","RESERVATION2","RESERVATION3","RESERVATION4","COLLECTION_PRODUCT","COLLECTION_MENU","COLLECTION_BRANCH","BUSINESS_HOURS","PDF",
					"TABLE_RESERVATION","FORMS","EXTERNAL_SERVICE_APPOINTMENT","WEBSITE","EXTERNAL_SERVICE_REVIEW", "STAMPCARD"};
		
	HashMap<String, Integer> hm = new HashMap<String, Integer>();
	public Module(String email, String passw, String randomId) {
		super(email,passw,randomId,String.format(baseUrl,randomId),true);
		
	}
	public void countModuleTypes() {
		JSONArray j = getJsonArray();
		String[] modulesFound = new String[j.length()];
		int i=0;
		while(i<j.length()) {
			JSONObject module = j.getJSONObject(i);
			String moduleType = module.getString("type");
			modulesFound[i] = moduleType;
			i++;
		}
		
		for (String module: modules) {
			int occurance=0;
			for(String moduleFound: modulesFound) {
				if (module.equals(moduleFound)) {
					occurance ++;
				}
			}
			hm.put(module, occurance);
			
		}
		
	}

	/**
	 * returns the number of interactive modules of this app
	 * TODO: define what interactive modules are
	 */

	public void countInteractiveModules(){

	}

	/**
	returns the amount of modules by moduleType
	 */
	public Integer getNumber(String moduleType) {
		return hm.get(moduleType);
		
	}

	/**
	 *
	 * @return moduleId of first stampcard module found
	 * TODO extend to all stampcard modules
	 */
	
	public Integer getStampcardModuleId() {
		int stampcardModuleId=-1;
		JSONArray j = getJsonArray();
		for(int i=0; i<j.length();i++) {
			JSONObject module = j.getJSONObject(i);
			if (module.getString("type").equals("STAMPCARD")) {
				stampcardModuleId=module.getInt("id");
			}
		}
		
		return stampcardModuleId;
		
	}
}
