package connectors;
import java.sql.Timestamp;
import java.time.LocalDate;
import exceptions.FetchException;

public class PublishAppInformation extends AppYourselfAPIGetConnector{
	private static String baseURL = "https://api.appyourself.net:443/api/apps/%s/publish";

    public PublishAppInformation(String email, String passw, String randomid) throws FetchException{
        super(email, passw, randomid, String.format(baseURL, randomid));
    }


    public String getBuildEndDate() {
        long timestamp = getJson().getLong("lastBuildEndDate");
        Timestamp t = new Timestamp(timestamp);
        LocalDate l = t.toLocalDateTime().toLocalDate();
        return l.toString();
    }
}