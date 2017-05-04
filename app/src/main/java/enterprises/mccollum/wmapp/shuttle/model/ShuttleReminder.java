package enterprises.mccollum.wmapp.shuttle.model;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;

public class ShuttleReminder {
	@Expose
	@DatabaseField(generatedId = false, id = true, canBeNull = false)
	Long id;
	
	@Expose
	@DatabaseField
	Long studentId;
	
	@Expose
	@DatabaseField
	Long sequentialStopId;
	
	@Expose
	@DatabaseField
	Long minimumDateTime;
	
	@Expose
	@DatabaseField
	Long timeAhead;
	
	public ShuttleReminder(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getSequentialStopId() {
		return sequentialStopId;
	}

	public void setSequentialStopId(Long sequentialStopId) {
		this.sequentialStopId = sequentialStopId;
	}
	public Long getMinimumDateTime() {
		return minimumDateTime;
	}

	public Long getTimeAhead() {
		return timeAhead;
	}

	public void setTimeAhead(Long timeAhead) {
		this.timeAhead = timeAhead;
	}

	public void setMinimumDateTime(Long minimumDateTime) {
		this.minimumDateTime = minimumDateTime;
	}
}
