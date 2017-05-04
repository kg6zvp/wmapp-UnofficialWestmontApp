package enterprises.mccollum.wmapp.shuttle.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Data type intended to represent that a route is a sequence of stops
 * @author smccollum
 *
 */
@DatabaseTable(tableName = "SequentialStop")
public class SequentialStop {
	@DatabaseField(generatedId = false, id = true, canBeNull = false)
	Long id; // ID of the stop. Should be easy to find.
	
	@DatabaseField(canBeNull = true)
	Double distanceWeight; // find method Stefan wrote // Done
	
	@DatabaseField(canBeNull = true)
	Long timeWeight; // from current stop to next stop
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
	PhysicalStop physicalStop; // Current stop physical location

	@DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
	SequentialStop previousStop;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
	SequentialStop nextStop; // next stop in route*/
	
	@ForeignCollectionField(eager = false, foreignFieldName = "sequentialStop")
	ForeignCollection<ScheduledStop> scheduledStops;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
	Route route;
	
	@DatabaseField(canBeNull = true)
	Long lastUpdateTime; //Timestamp that this information was last updated
	
	@DatabaseField(canBeNull = true)
	Long currentEstimatedTime;

	public SequentialStop(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long generateId(Long routeId, Long stopId){
		return (100*routeId)+stopId;
	}

	public Double getDistanceWeight() {
		return distanceWeight;
	}
	
	/**
	 * Sometimes the shuttle server won't return a value for "SecondsToStop", if that is the case, call this function
	 * This function will then just set the current estimate to the current time minus the lastUpdateTime
	 * 
	 * @param currentEstimate
	 */
	public void updateCurrentEstimatedTime(Long currentEstimate){
		if(currentEstimate == null || currentEstimate >= 0){ //if there is no estimate or it's valid
			this.currentEstimatedTime = currentEstimate;
		}else{
			this.currentEstimatedTime -= (System.currentTimeMillis() - this.lastUpdateTime);
		}
		this.lastUpdateTime = System.currentTimeMillis();
	}

	/**
	 * Return the epoch time currently estimated to be the shuttle's arrival time at this stop
	 * @return the ETA in epoch time in milliseconds if the shuttle is running, null if not
	 */
	public Long getCurrentEstimatedTime() {
		return currentEstimatedTime;
	}

	public void setCurrentEstimatedTime(Long currentEstimatedTime) {
		this.currentEstimatedTime = currentEstimatedTime;
	}

	public void setDistanceWeight(double distanceWeight) {
		this.distanceWeight = distanceWeight;
	}


	public Long getTimeWeight() {
		return timeWeight;
	}


	public void setTimeWeight(Long timeWeight) {
		this.timeWeight = timeWeight;
	}


	public PhysicalStop getPhysicalStop() {
		return physicalStop;
	}


	public void setPhysicalStop(PhysicalStop physicalStop) {
		this.physicalStop = physicalStop;
	}

	public ForeignCollection<ScheduledStop> getScheduledStops() {
		return scheduledStops;
	}

	public void setDistanceWeight(Double distanceWeight) {
		this.distanceWeight = distanceWeight;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public void setNextStop(SequentialStop nextStop) {
		SequentialStop orig = this.nextStop;
		this.nextStop = nextStop;
		if(nextStop == null){ //if we're severing the relationship, make sure to do it on the other side
			if(orig != null)
				orig.setPreviousStop(null);
			return;
		}
		if(nextStop.getPreviousStop() != this)
			nextStop.setPreviousStop(this);
	}
	public SequentialStop getNextStop() {
		return nextStop;
	}//*/

	public SequentialStop getPreviousStop() {
		return previousStop;
	}

	public void setPreviousStop(SequentialStop previousStop) {
		SequentialStop orig = this.previousStop;
		this.previousStop = previousStop;
		if(previousStop == null){ //Sever the relationship bilaterally
			if(orig != null)
				orig.setNextStop(null);
			return;
		}
		if(previousStop.getNextStop() != this)
			previousStop.setNextStop(this);
	}
	
	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}
	
	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
