package enterprises.mccollum.wmapp.shuttle.model;

import java.util.Calendar;
import java.util.TimeZone;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Route")
public class Route {
	@Expose
	@DatabaseField(generatedId = false, id = true, canBeNull = false)
	Long id;
	
	@Expose
	@DatabaseField
	Long lastUpdate;
	
	@Expose
	@DatabaseField
	String name;
	
	@Expose
	@DatabaseField
	Integer startHour;
	
	@Expose
	@DatabaseField
	Integer startMinute;
	
	@Expose
	@DatabaseField
	Integer endHour;
	
	@Expose
	@DatabaseField
	Integer endMinute;
	
	@ForeignCollectionField(eager = false, foreignFieldName = "route")
	ForeignCollection<SequentialStop> sequentialStops;
	
	public Route() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ForeignCollection<SequentialStop> getSequentialStops() {
		return sequentialStops;
	}

	public void setSequentialStops(ForeignCollection<SequentialStop> sequentialStops) {
		this.sequentialStops = sequentialStops;
	}

	/**
	 * Look through the information in this route and poulate the start and end time fields
	 */
	public void populateStartAndEndTimes(){
		Integer earliestTime = null; //represented in terms of minutes after 00:00
		Integer latestTime = null; //represented in terms of minutes after 00:00
		for(SequentialStop seqStop : getSequentialStops()){
			for(ScheduledStop schedStop : seqStop.getScheduledStops()){
				Integer currentStopTime = parseToMinutes(schedStop.getStopTime());
				if(earliestTime == null){
					earliestTime = currentStopTime;
				}else{
					if(currentStopTime < earliestTime)
						earliestTime = currentStopTime;
				}
				
				if(latestTime == null){
					latestTime = currentStopTime;
				}else{
					if(currentStopTime > latestTime)
						latestTime = currentStopTime;
				}
			}
		}
		if(earliestTime != null && latestTime != null){
			Integer earliestHour = earliestTime/60;
			Integer earliestMinute = earliestTime % 60;
			setStartHour(earliestHour);
			setStartMinute(earliestMinute);
			
			Integer latestHour = latestTime/60;
			Integer latestMinute = latestTime % 60;
			setEndHour(latestHour);
			setEndMinute(latestMinute);
		}
	}
	
	/**
	 * Parse an epoch time to hours and minutes, inferring the time zone and using daylightSavings
	 * @param epochTime
	 * @return
	 */
	private Integer parseToMinutes(Long epochTime){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(epochTime);
		c.setTimeZone(TimeZone.getDefault());
		return (60*c.get(Calendar.HOUR_OF_DAY)
							+
				c.get(Calendar.MINUTE));
	}
	
	/**
	 * Returns a quick estimate of whether or not the shuttles are running without thrashing the database
	 * @param time
	 * @return
	 */
	public Boolean isRunning(Long time) {
		if(getStartMinutes() == null || getEndMinutes() == null)
			return false;
		Integer currentHourMinutes = parseToMinutes(time);
		return (getStartMinutes() <= currentHourMinutes
				&& currentHourMinutes <= getEndMinutes());
	}

	public Integer getStartMinutes(){
		if(getStartHour() == null || getStartMinute() == null)
			return null;
		return (60*getStartHour()
					+
				getStartMinute());
	}
	
	public Integer getStartHour() {
		return startHour;
	}

	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}

	public Integer getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(Integer startMinute) {
		this.startMinute = startMinute;
	}

	public Integer getEndMinutes(){
		if(getEndHour() == null || getEndMinute() == null)
			return null;
		return (60*getEndHour()
					+
				getEndMinute());
	}
	
	public Integer getEndHour() {
		return endHour;
	}

	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}

	public Integer getEndMinute() {
		return endMinute;
	}
	
	public void setEndMinute(Integer endMinute) {
		this.endMinute = endMinute;
	}
	
	public Long getLastUpdate() {
		return lastUpdate;
	}
	
	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
