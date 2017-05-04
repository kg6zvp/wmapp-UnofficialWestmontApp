package enterprises.mccollum.wmapp.shuttle.model;

import java.util.LinkedList;
import java.util.List;

import enterprises.mccollum.wmapp.model.GenericEntityManager;
import enterprises.mccollum.wmapp.model.ModulePersistenceResolver;
import enterprises.mccollum.wmapp.model.Reference;
import enterprises.mccollum.wmapp.model.Shadow;

public class PhysicalStopShadow implements Shadow<PhysicalStop, Long> {
	public static final String ID_COL = "ps_id";
	
	Long id; // Id that should be easily found
	
	String name; // description name that needs to be found
	
	Double latitude; // Latitutde that should be easily found
	
	Double longitude; // Longitude that should be easily found
	
	List<Reference<Long>> stops;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public PhysicalStop populate(ModulePersistenceResolver pm) {
		GenericEntityManager<PhysicalStop, Long> em = (GenericEntityManager<PhysicalStop, Long>) pm.getEntityManager(PhysicalStop.class);
		PhysicalStop stop = null;
		if(em.containsKey(getId())){
			stop = em.get(getId());
		}else{
			stop = new PhysicalStop();
			stop.setId(getId());
			stop = em.persist(stop);
		}
		stop.setName(name);
		stop.setLatitude(latitude);
		stop.setLongitude(longitude);
		GenericEntityManager<SequentialStop, Long> seqEm = (GenericEntityManager<SequentialStop, Long>) pm.getEntityManager(SequentialStop.class);
		for(Reference<Long> stopRef : stops) {
			if(seqEm.containsKey(stopRef.getId())) {
				SequentialStop ss = seqEm.get(stopRef.getId());
				//stop.addSequentialStop(ss);
				ss.setPhysicalStop(stop);
				stop = em.save(stop);
				seqEm.save(ss);
			}
		}
		return em.save(stop);
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

	public List<Reference<Long>> getStops() {
		if(stops == null)
			stops = new LinkedList<>(); //*/
		return stops;
	}

	public void setStops(List<Reference<Long>> stops) {
		this.stops = stops;
	}
	
	public void addSequentialStop(Reference<Long> stop){
		if(!getStops().contains(stop))
			stops.add(stop);
	}
}
