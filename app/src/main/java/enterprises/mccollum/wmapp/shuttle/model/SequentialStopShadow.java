package enterprises.mccollum.wmapp.shuttle.model;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.List;

import enterprises.mccollum.wmapp.model.GenericEntityManager;
import enterprises.mccollum.wmapp.model.ModulePersistenceResolver;
import enterprises.mccollum.wmapp.model.Reference;
import enterprises.mccollum.wmapp.model.Shadow;

/**
 * Created by smccollum on 03.05.17.
 */

public class SequentialStopShadow implements Shadow<SequentialStop, Long> {
	Long id; // ID of the stop. Should be easy to find.
	
	Double distanceWeight; // find method Stefan wrote // Done

	Long timeWeight; // from current stop to next stop
	
	Reference<Long> physicalStop; // Current stop physical location
	
	Reference<Long> previousStop;
	
	Reference<Long> nextStop; // next stop in route*/
	
	List<Reference<Long>> scheduledStops;
	
	Reference<Long> route;
	
	Long lastUpdateTime; //Timestamp that this information was last updated
	
	Long currentEstimatedTime;
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public SequentialStop populate(ModulePersistenceResolver pm) {
		GenericEntityManager<SequentialStop, Long> em = (GenericEntityManager<SequentialStop, Long>) pm.getEntityManager(SequentialStop.class);
		SequentialStop sequentialStop = null;
		if(em.containsKey(getId())){ //if it exists, update it
			sequentialStop = em.get(id);
			if(sequentialStop.getLastUpdateTime() == lastUpdateTime)
				return sequentialStop;
		}else{
			sequentialStop = new SequentialStop();
			sequentialStop.setId(id);
			sequentialStop = em.persist(sequentialStop);
			
			sequentialStop.setDistanceWeight(distanceWeight);
			sequentialStop.setTimeWeight(timeWeight);
		
			GenericEntityManager<PhysicalStop, Long> psEm = (GenericEntityManager<PhysicalStop, Long>) pm.getEntityManager(PhysicalStop.class);
			GenericEntityManager<ScheduledStop, Long> schedEm = (GenericEntityManager<ScheduledStop, Long>) pm.getEntityManager(ScheduledStop.class);
			GenericEntityManager<Route, Long> rEm = (GenericEntityManager<Route, Long>) pm.getEntityManager(Route.class);
			if(psEm.containsKey(physicalStop.getId())){
				PhysicalStop ps = psEm.get(physicalStop.getId());
				sequentialStop.setPhysicalStop(ps);
				sequentialStop = em.save(sequentialStop);
				psEm.save(ps);
			}
			for(Reference<Long> ssRef : scheduledStops) {
				if (schedEm.containsKey(ssRef.getId())){
					ScheduledStop ss = schedEm.get(ssRef.getId());
					ss.setSequentialStop(sequentialStop);
					sequentialStop = em.save(sequentialStop);
					schedEm.save(ss);
				}
			}
			if(rEm.containsKey(route.getId())){
				Route r = rEm.get(route.getId());
				sequentialStop.setRoute(r);
				sequentialStop = em.save(sequentialStop);
				rEm.save(r);
			}
		}
		
		sequentialStop.setLastUpdateTime(lastUpdateTime);
		sequentialStop.setCurrentEstimatedTime(currentEstimatedTime);
		return em.save(sequentialStop);
	}
}
