package enterprises.mccollum.wmapp.shuttle.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "PhysicalStop")
public class PhysicalStop {
	@DatabaseField(generatedId = false, id = true, canBeNull = false)
	Long id; // Id that should be easily found
	
	@DatabaseField(canBeNull = true)
	String name; // description name that needs to be found 
	
	@DatabaseField
	Double latitude; // Latitutde that should be easily found
	
	@DatabaseField
	Double longitude; // Longitude that should be easily found
	
	@ForeignCollectionField(eager = false, foreignFieldName = "physicalStop")
	ForeignCollection<SequentialStop> stops;

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

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public ForeignCollection<SequentialStop> getStops() {
		return stops;
	}
}
