package enterprises.mccollum.wmapp.shuttle.model;

import enterprises.mccollum.wmapp.model.GenericEntityManager;
import enterprises.mccollum.wmapp.model.ModulePersistenceResolver;
import enterprises.mccollum.wmapp.model.Reference;
import enterprises.mccollum.wmapp.model.Shadow;

/**
 * Created by smccollum on 03.05.17.
 */

public class ScheduledStopShadow implements Shadow<ScheduledStop, Long> {
	Long id;
	
	Long stopTime;
	
	Reference<Long> sequentialStop;
	
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public ScheduledStop populate(ModulePersistenceResolver pm) {
		GenericEntityManager<ScheduledStop, Long> em = (GenericEntityManager<ScheduledStop, Long>) pm.getEntityManager(ScheduledStop.class);
		ScheduledStop scheduledStop = null;
		if(em.containsKey(getId())){
			scheduledStop = em.get(id);
		}else{
			scheduledStop = new ScheduledStop();
			scheduledStop.setId(id);
			scheduledStop = em.persist(scheduledStop);
		}
		scheduledStop.setStopTime(stopTime);
		GenericEntityManager<SequentialStop, Long> seqEm = (GenericEntityManager<SequentialStop, Long>) pm.getEntityManager(SequentialStop.class);
		if(seqEm.containsKey(sequentialStop.getId())) {
			SequentialStop ss = seqEm.get(sequentialStop.getId());
			scheduledStop.setSequentialStop(ss);
			scheduledStop = em.save(scheduledStop);
			seqEm.save(ss);
		}
		return em.save(scheduledStop);
	}
}
