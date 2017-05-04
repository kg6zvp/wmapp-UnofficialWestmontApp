package enterprises.mccollum.wmapp.shuttle.model;

import java.util.LinkedList;
import java.util.List;

import enterprises.mccollum.wmapp.model.GenericEntityManager;
import enterprises.mccollum.wmapp.model.ModulePersistenceResolver;
import enterprises.mccollum.wmapp.model.Reference;
import enterprises.mccollum.wmapp.model.Shadow;

/**
 * Created by smccollum on 03.05.17.
 */

public class RouteShadow implements Shadow<Route, Long> {
	Long id;
	
	Long lastUpdate;
	
	String name;
	
	Integer startHour;
	
	Integer startMinute;
	
	Integer endHour;
	
	Integer endMinute;
	
	List<Reference<Long>> sequentialStops;
	
	public RouteShadow() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public List<Reference<Long>> getSequentialStops() {
		if(sequentialStops == null)
			sequentialStops = new LinkedList<>();
		return sequentialStops;
	}

	@Override
	public Route populate(ModulePersistenceResolver pm) {
		GenericEntityManager<Route, Long> em = (GenericEntityManager<Route, Long>) pm.getEntityManager(Route.class);
		Route route = null;
		if(em.containsKey(getId())){
			route = em.get(id);
		}else{
			route = new Route();
			route.setId(getId());
			route = em.persist(route);
		}
		route.setLastUpdate(lastUpdate);
		route.setName(name);
	
		route.setStartHour(startHour);
		route.setStartMinute(startMinute);
		
		route.setEndHour(endHour);
		route.setEndMinute(endMinute);
		
		GenericEntityManager<SequentialStop, Long> seqEm = (GenericEntityManager<SequentialStop, Long>) pm.getEntityManager(SequentialStop.class);
		for(Reference<Long> seqStopRef : sequentialStops){
			if(seqEm.containsKey(seqStopRef.getId())){
				SequentialStop ss = seqEm.get(seqStopRef.getId());
				ss.setRoute(route);
				route = em.save(route);
				seqEm.save(ss);
			}
		}
		return em.save(route);
	}
}
