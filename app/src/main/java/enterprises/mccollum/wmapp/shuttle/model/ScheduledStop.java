package enterprises.mccollum.wmapp.shuttle.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Data type intended to represent a stop scheduled on a route and associated with a particular SequentialStop
 * @author smccollum
 *
 */
@DatabaseTable(tableName = "ScheduledStop")
public class ScheduledStop {
	@DatabaseField(generatedId = false, id = true, canBeNull = false)
	Long id;
	
	@DatabaseField(canBeNull = true)
	Long stopTime;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
	SequentialStop sequentialStop;
	
	public ScheduledStop(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getStopTime() {
		return stopTime;
	}

	public void setStopTime(Long stopTime) {
		this.stopTime = stopTime;
	}
	
	public SequentialStop getSequentialStop() {
		return sequentialStop;
	}
	
	public void setSequentialStop(SequentialStop sequentialStop) {
		this.sequentialStop = sequentialStop;
	}
}
