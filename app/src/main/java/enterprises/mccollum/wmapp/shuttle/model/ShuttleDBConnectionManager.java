package enterprises.mccollum.wmapp.shuttle.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import enterprises.mccollum.wmapp.shuttle.model.*;

/**
 * Created by smccollum on 07.04.17.
 */
public class ShuttleDBConnectionManager extends OrmLiteSqliteOpenHelper {
	public static final String DB_NAME = "shuttle.db";
	public static final int DB_VERSION = 1;
	
	Dao<PhysicalStop, Long> physicalStopBean = null;
	Dao<Route, Long> routeBean = null;
	Dao<SequentialStop, Long> sequentialStopBean = null;
	Dao<ScheduledStop, Long> scheduledStopBean = null;
	Dao<ShuttleReminder, Long> remindersBean = null;
	
	/*public ShuttleDBConnectionManager(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
	}//*/
	
	public ShuttleDBConnectionManager(Context ctx){
		super(ctx, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			createTables(connectionSource);
		}catch (Exception e){
			Log.e("ShuttleDBCMan", e.getMessage());
		}
	}
	
	private void createTables(ConnectionSource connectionSource) throws Exception{
		TableUtils.createTable(connectionSource, PhysicalStop.class);
		TableUtils.createTable(connectionSource, Route.class);
		TableUtils.createTable(connectionSource, SequentialStop.class);
		TableUtils.createTable(connectionSource, ScheduledStop.class);
		TableUtils.createTable(connectionSource, ShuttleReminder.class);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		if(oldVersion != newVersion){
			try {
				TableUtils.dropTable(connectionSource, PhysicalStop.class, false);
				TableUtils.dropTable(connectionSource, Route.class, false);
				TableUtils.dropTable(connectionSource, SequentialStop.class, false);
				TableUtils.dropTable(connectionSource, ScheduledStop.class, false);
				TableUtils.dropTable(connectionSource, ShuttleReminder.class, false);
				
				createTables(connectionSource);
			}catch (Exception e){
				Log.e("ShuttleDBCMan", e.getMessage());
			}
		}
	}
	
	public Dao<PhysicalStop, Long> getPhysicalStopBean(){
		if(physicalStopBean == null){
			try{
				physicalStopBean = getDao(PhysicalStop.class);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		return physicalStopBean;
	}
	public Dao<Route, Long> getRouteBean(){
		if(routeBean == null){
			try{
				routeBean = getDao(Route.class);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		return routeBean;
	}
	public Dao<SequentialStop, Long> getSequentialStopBean(){
		if(sequentialStopBean == null){
			try{
				sequentialStopBean = getDao(SequentialStop.class);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		return sequentialStopBean;
	}
	public Dao<ScheduledStop, Long> getScheduledStopBean(){
		if(scheduledStopBean == null){
			try{
				scheduledStopBean = getDao(ScheduledStop.class);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		return scheduledStopBean;
	}
	
	public Dao<ShuttleReminder, Long> getReminderBean(){
		if(remindersBean == null){
			try{
				remindersBean = getDao(ShuttleReminder.class);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		return remindersBean;
	}
}
