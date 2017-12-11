//TODO FIXME when accesstoken expires while a randomid is processed, it returns 401 for all requests. better to request new AT in 401 exception
//TODO FIXME JSONObject["lastBuildEndDate"] is not a long.  Its null and should be cought
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import connectors.*;
import connectors.StampcardStats.StampcardHelper;
import connectors.user.User;
import connectors.user.UserDetailsHelper;


public class Handler {


	public static void main(String[] args){
		Date date = new Date();
		// String email = "at@appyourself.net"; //heise
		// String passw = "1e5a92b";  // heise pw
		//String email = "ay.rockz666@gmail.com"; //AY
		//String passw = "AppYourself123%";   //AY
		// String email = "arthur.teichmann@appyourself.net"; //master
		// String passw = "df5b3fd";
		// String passw ="27994a0"; //master
		String email = "at@appyourself.net"; //shore
		String passw ="1b56541"; // shore
		String at = null;
		int stampCardModuleId=-1;
		try{
			System.out.println("validating details ..");
			AccessToken token = new AccessToken(email, passw);
			at = token.getAccessToken();
			System.out.println("Login successful with access token: " + at);
		}
		catch (Exception e){
			date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("login failed due " + e.getMessage() + " " + dateFormat.format(date));
			System.out.println("Shutting down abortively");
			System.exit(0);
		}


//		ResellerApps resellerApps = new ResellerApps(email, passw);
//		System.out.println("Fetching apps ... this may take a while");
//		ArrayList<String> randomids = resellerApps.getApps();



		//Little workaround for faster app generation
		ArrayList<String> randomids = new ArrayList<String>();
		try{
			FileInputStream fstream = new FileInputStream("randomids.csv");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				randomids.add(strLine);
				//System.out.println (strLine);
			}
			br.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		// end of workaround


		System.out.println("Done. Found " + randomids.size() + " paid apps");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String csv = "Customer_Success_" + sdf.format(date) + ".csv";
		FileWriter writer;

		try {
			writer = new FileWriter(csv);
			CSVUtils.writeLine((Writer)writer, Arrays.asList("App ID", "App Name", "RandomID", "Email", "Paket", "Buchungsdatum", "native", "iOS Audience", "Android Audience", "Gesamt Audience", "Transaktionen", "ModuleEmails", "Neue Besucher", "Wiederkehrende Besucher", "Besucher Gesamt", "Gesendete PN", "Submission Service Apple", "Submission Service Google",  "AppStore URL", "Google Play URL", "Last Publish Date",
					"TEXT","MAP","COUPON","GALLERY","NEWSCENTER","EVENTS","CONTACT","SUBMENU","SUBMENU_SEPARATOR","RESERVATION","VIDEO","USER_MODULE","RESERVATION2","RESERVATION3","RESERVATION4","COLLECTION_PRODUCT","COLLECTION_MENU","COLLECTION_BRANCH","BUSINESS_HOURS","PDF",
					"TABLE_RESERVATION","FORMS","EXTERNAL_SERVICE_APPOINTMENT","WEBSITE","EXTERNAL_SERVICE_REVIEW", "STAMPCARD","Eingelöste Stempelkarten","Stempelkartennutzer","Insgesamt verteilte Stempel","Gesamt Nutzer","iOS User", "Android User", "Web App User", "widgetUser", "Nutzer mit Chat", "Nachrichten vom Owner", "Nachrichten vom User", "Userlist Push Audience"));
			for (String randomid : randomids) {
				ArrayList<String> list = new ArrayList<String>();
				String appName = "";
				String hasNative= "";
				String externalId= "";
				String moduleEmails= "";
				String newVisits= "";
				String submissionServiceAppStore= "";
				String totalVisits= "";
				String contractStartDate= "";
				String appStoreURL= "";
				String returningVisits= "";
				String androidAudience= "";
				String pushNotifications= "";
				String submissionServiceGooglePlay= "";
				String transactions= "";
				String customer_email= "";
				String submissionServiceAmazon= "";
				String iOSAudience= "";
				String appId = "";
				String totalAudience= "";
				String googlePlayURL= "";
				String lastPublishDate= "";
				String TEXT= "";
				String MAP= "";
				String COUPON= "";
				String GALLERY="";
				String NEWSCENTER="";
				String EVENTS="";
				String CONTACT="";
				String SUBMENU="";
				String SUBMENU_SEPARATOR="";
				String RESERVATION="";
				String VIDEO="";
				String USER_MODULE="";
				String RESERVATION2="";
				String RESERVATION3="";
				String RESERVATION4="";
				String COLLECTION_PRODUCT="";
				String COLLECTION_MENU="";
				String COLLECTION_BRANCH="";
				String BUSINESS_HOURS="";
				String PDF="";
				String TABLE_RESERVATION="";
				String FORMS="";
				String EXTERNAL_SERVICE_APPOINTMENT="";
				String WEBSITE="";
				String EXTERNAL_SERVICE_REVIEW="";
				String STAMPCARD="";
				String numberOfCollectedPrizes="";
				String numberOfStampcardUsers="";
				String numberofTotalStamps="";
				String totalUsers="";
				String iOSUser="";
				String androidUser="";
				String webAppUser="";
				String widgetUser="";
				String totalUsersWithChat="";
				String messagesFromUser="";
				String messagesFromOwner="";
				String userlistPushAudience="";


				try {



					BasicAppStatistics bas = new BasicAppStatistics(email, passw, randomid);
//					if((bas.getiOSAudience() + bas.getAndroidAudience()) <5){
//						continue;
//					}


					if (!bas.connectionFailed()) {
						System.out.println("connected to BAS");
						iOSAudience = bas.getiOSAudience().toString();
						androidAudience = bas.getAndroidAudience().toString();
						totalAudience = Integer.toString(bas.getiOSAudience() + bas.getAndroidAudience());
						transactions = bas.getOrdersCount().toString();
						moduleEmails = bas.getModuleEmails().toString();
					}


					BasicAppDetails bad = new BasicAppDetails(email, passw, randomid);


//					if (!bad.isContractStartDateInRange()){
//						continue;}

					if (!bad.connectionFailed()) {
						System.out.println("connected to BAD");

						appName = bad.getAppName();
						externalId = bad.getExternalId();
						customer_email = bad.getCustomer();
						submissionServiceAppStore = bad.getSubmissionServiceAppStore();
						submissionServiceGooglePlay = bad.getSubmissionServiceGooglePlay();
						//submissionServiceAmazon = bad.getSubmissionServiceAmazon();
						appStoreURL = bad.getAppStoreURL();
						googlePlayURL = bad.getGooglePlayURL();
						contractStartDate = bad.getContractStartDate();
						hasNative = bad.getHasNative();
					}

					BasicAppDetailsFromVPP vpp = new BasicAppDetailsFromVPP(email, passw, randomid);
					if (!vpp.connectionFailed()) {
						System.out.println("connected to VPP");

						appId = vpp.getAppId();
					}


					WebAppDetails wad = new WebAppDetails(email, passw, randomid);
					if (!wad.connectionFailed()) {
						System.out.println("connected to WAD");

						newVisits = Integer.toString(wad.getNewVisits());
						returningVisits = Integer.toString(wad.getReturningVisits());
						totalVisits = Integer.toString(wad.getNewVisits() + wad.getReturningVisits());
					}


					PushNotificationDetails pnd = new PushNotificationDetails(email, passw, randomid);
					System.out.println("connected to PND");

					if (!pnd.connectionFailed()) {
						pushNotifications = pnd.getTotalElements();

					}

					PublishAppInformation pai = new PublishAppInformation(email, passw, randomid);
					if (!pai.connectionFailed()) {
						System.out.println("connected to PAI");

						lastPublishDate = pai.getBuildEndDate();
					}



					Module mod = new Module(email,passw,randomid);
					if (!mod.connectionFailed()) {
						mod.countModuleTypes();
						TEXT=mod.getNumber("TEXT").toString();
						MAP=mod.getNumber("MAP").toString();
						COUPON=mod.getNumber("COUPON").toString();
						GALLERY=mod.getNumber("GALLERY").toString();
						NEWSCENTER=mod.getNumber("NEWSCENTER").toString();
						EVENTS=mod.getNumber("EVENTS").toString();
						CONTACT=mod.getNumber("CONTACT").toString();
						SUBMENU=mod.getNumber("SUBMENU").toString();
						SUBMENU_SEPARATOR=mod.getNumber("SUBMENU_SEPARATOR").toString();
						RESERVATION=mod.getNumber("RESERVATION").toString();
						VIDEO=mod.getNumber("VIDEO").toString();
						USER_MODULE=mod.getNumber("USER_MODULE").toString();
						RESERVATION2=mod.getNumber("RESERVATION2").toString();
						RESERVATION3=mod.getNumber("RESERVATION3").toString();
						RESERVATION4=mod.getNumber("RESERVATION4").toString();
						COLLECTION_PRODUCT=mod.getNumber("COLLECTION_PRODUCT").toString();
						COLLECTION_MENU=mod.getNumber("COLLECTION_MENU").toString();
						COLLECTION_BRANCH=mod.getNumber("COLLECTION_BRANCH").toString();
						BUSINESS_HOURS=mod.getNumber("BUSINESS_HOURS").toString();
						PDF=mod.getNumber("PDF").toString();
						TABLE_RESERVATION=mod.getNumber("TABLE_RESERVATION").toString();
						FORMS=mod.getNumber("FORMS").toString();
						EXTERNAL_SERVICE_APPOINTMENT=mod.getNumber("EXTERNAL_SERVICE_APPOINTMENT").toString();
						WEBSITE=mod.getNumber("WEBSITE").toString();
						EXTERNAL_SERVICE_REVIEW=mod.getNumber("EXTERNAL_SERVICE_REVIEW").toString();
						STAMPCARD=mod.getNumber("STAMPCARD").toString();
						stampCardModuleId=mod.getStampcardModuleId();
					}



					if (stampCardModuleId>0) {
						System.out.println("found stampcardModule with ID: " + stampCardModuleId);
						StampcardStats sstats = new StampcardStats(email,passw,randomid,stampCardModuleId);
						StampcardHelper shelp = sstats.new StampcardHelper(email,passw,randomid, sstats.moduleId, sstats.getStampcardId());
						numberOfCollectedPrizes=shelp.getAmountOfCollectedPrizes().toString();
						numberOfStampcardUsers=shelp.getUsersWithStamps().toString();
						numberofTotalStamps=shelp.getTotalStamps().toString();
					}
					User userEntity = new User(email,passw, randomid);
					if(!userEntity.connectionFailed()){
						System.out.println("connected to USR");
						totalUsers = userEntity.getTotalUsers().toString();
						UserDetailsHelper udh = new UserDetailsHelper(email,passw,randomid,userEntity.getEntities());
						iOSUser= String.valueOf(udh.iOSUser);
						androidUser= String.valueOf(udh.androidUser);
						webAppUser = String.valueOf(udh.browserUser);
						widgetUser = String.valueOf(udh.widgetUser);
						totalUsersWithChat = String.valueOf(udh.totalUsersWithChat);
						messagesFromOwner = String.valueOf(udh.messagesFromOwner);
						messagesFromUser = String.valueOf(udh.messagesFromUser);
						userlistPushAudience = String.valueOf(udh.pushAudience);

					}



				}
				catch (Exception fe){

					System.out.println("FetchException found in " + fe.getMessage() + " randomid: " + randomid);
					System.out.println("in ") ;
					fe.printStackTrace();

					continue;

				}




				list.add(appId);
				list.add(appName);
				list.add(randomid);
				list.add(customer_email);
				list.add(externalId);
				list.add(contractStartDate);
				list.add(hasNative);
				list.add(iOSAudience);
				list.add(androidAudience);
				list.add(totalAudience);
				list.add(transactions);
				list.add(moduleEmails);
				list.add(newVisits);
				list.add(returningVisits);
				list.add(totalVisits);
				list.add(pushNotifications);
				list.add(submissionServiceAppStore);
				list.add(submissionServiceGooglePlay);
				list.add(appStoreURL);
				list.add(googlePlayURL);
				list.add(lastPublishDate);
				list.add(TEXT);
				list.add(MAP);
				list.add(COUPON);
				list.add(GALLERY);
				list.add(NEWSCENTER);
				list.add(EVENTS);
				list.add(CONTACT);
				list.add(SUBMENU);
				list.add(SUBMENU_SEPARATOR);
				list.add(RESERVATION);
				list.add(VIDEO);
				list.add(USER_MODULE);
				list.add(RESERVATION2);
				list.add(RESERVATION3);
				list.add(RESERVATION4);
				list.add(COLLECTION_PRODUCT);
				list.add(COLLECTION_MENU);
				list.add(COLLECTION_BRANCH);
				list.add(BUSINESS_HOURS);
				list.add(PDF);
				list.add(TABLE_RESERVATION);
				list.add(FORMS);
				list.add(EXTERNAL_SERVICE_APPOINTMENT);
				list.add(WEBSITE);
				list.add(EXTERNAL_SERVICE_REVIEW);
				list.add(STAMPCARD);
				list.add(numberOfCollectedPrizes);
				list.add(numberOfStampcardUsers);
				list.add(numberofTotalStamps);
				list.add(totalUsers);
				list.add(iOSUser);
				list.add(androidUser);
				list.add(webAppUser);
				list.add(widgetUser);
				list.add(totalUsersWithChat);
				list.add(messagesFromOwner);
				list.add(messagesFromUser);
				list.add(userlistPushAudience);

				CSVUtils.writeLine((Writer)writer, list);
				System.out.println("done for " + randomid);


			}
			writer.flush();
			writer.close();
			System.out.println("Done");
		}
		catch (IOException e) {
			System.out.println("IOException found in " + e.getMessage());
		}


	}

}