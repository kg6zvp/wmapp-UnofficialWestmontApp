package enterprises.mccollum.wmapp.shuttle.model;

import enterprises.mccollum.wmapp.API_URLs;

public class JaxDirectory {
	//public static final String BASE_PATH = "https://wmapp.mccollum.enterprises/shuttle/api";
	public static final String BASE_PATH = API_URLs.SHUTTLE_BASE_URL+"/api";
	public static final String ROUTES_PATH = BASE_PATH+"/routes/";
	public static final String PHYSICAL_STOPS_PATH = BASE_PATH+"/physicalStops/";
	public static final String SEQUENTIAL_STOPS_PATH = BASE_PATH+"/sequentialStops/";
	public static final String SCHEDULED_STOPS_PATH = BASE_PATH+"/scheduledStops/";
	public static final String REMINDERS_PATH = BASE_PATH+"/reminders/";
}
