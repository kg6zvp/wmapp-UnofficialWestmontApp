package enterprises.mccollum.wmapp.shuttle.model;

import android.content.Context;

import enterprises.mccollum.wmapp.model.ModulePersistenceResolver;
import enterprises.mccollum.wmapp.model.GenericEntityManager;

/**
 * Designed to retrieve entity managers for each class
 * @author smccollum
 */
public class ShuttlePersistenceManager implements ModulePersistenceResolver {
	private static ShuttlePersistenceManager instance = null;
	
	public static ShuttlePersistenceManager getInstance(Context ctx){
		if(instance == null)
			instance = new ShuttlePersistenceManager(ctx.getApplicationContext());
		synchronized (instance) {
			return instance;
		}
	}
	
	ShuttleDBConnectionManager connectionManager;
	GenericEntityManager<PhysicalStop, Long> psem = null;
	GenericEntityManager<Route, Long> rem = null;
	GenericEntityManager<ScheduledStop, Long> schedsem = null;
	GenericEntityManager<SequentialStop, Long> seqsem = null;
	GenericEntityManager<ShuttleReminder, Long> remem = null;
	
	private ShuttlePersistenceManager(Context ctx){
		connectionManager = new ShuttleDBConnectionManager(ctx.getApplicationContext());
		getPhysicalStops();
		getRoutes();
		getSequentialStops();
		getScheduledStops();
		getReminders();
	}
	
	public GenericEntityManager<PhysicalStop, Long> getPhysicalStops(){
		if(psem == null)
			psem = new GenericEntityManager<>(connectionManager.getPhysicalStopBean());
		return psem;
	}
	public GenericEntityManager<Route, Long> getRoutes(){
		if(rem == null)
			rem = new GenericEntityManager<>(connectionManager.getRouteBean());
		return rem;
	}
	public GenericEntityManager<ScheduledStop, Long> getScheduledStops(){
		if(schedsem == null)
			schedsem = new GenericEntityManager<>(connectionManager.getScheduledStopBean());
		return schedsem;
	}
	public GenericEntityManager<SequentialStop, Long> getSequentialStops(){
		if(seqsem == null)
			seqsem = new GenericEntityManager<>(connectionManager.getSequentialStopBean());
		return seqsem;
	}
	public GenericEntityManager<ShuttleReminder, Long> getReminders(){
		if(remem == null)
			remem = new GenericEntityManager<>(connectionManager.getReminderBean());
		return remem;
	}
	
	@Override
	public GenericEntityManager<?, ?> getEntityManager(Class<?> typeOfEntity) {
		if(typeOfEntity.equals(PhysicalStop.class))
			return getPhysicalStops();
		if(typeOfEntity.equals(Route.class))
			return getRoutes();
		if(typeOfEntity.equals(ScheduledStop.class))
			return getScheduledStops();
		if(typeOfEntity.equals(SequentialStop.class))
			return getSequentialStops();
		if(typeOfEntity.equals(ShuttleReminder.class))
			return getReminders();
		return null;
	}
}
