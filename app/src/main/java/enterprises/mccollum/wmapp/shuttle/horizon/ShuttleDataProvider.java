package enterprises.mccollum.wmapp.shuttle.horizon;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import enterprises.mccollum.wmapp.shuttle.model.JaxDirectory;

/**
 * Created by smccollum on 02.05.17.
 * @author smccollum
 */
public class ShuttleDataProvider extends ContentProvider{
	@Override
	public boolean onCreate() {
		return false;
	}
	
	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		if(uri.toString().contains(JaxDirectory.PHYSICAL_STOPS_PATH)) { //if it's physical stops
		}else if(uri.toString().contains(JaxDirectory.ROUTES_PATH)) {
			
		}else if(uri.toString().contains(JaxDirectory.SEQUENTIAL_STOPS_PATH)){
			
		}else if(uri.toString().contains(JaxDirectory.SCHEDULED_STOPS_PATH)){
			
		}else if(uri.toString().contains(JaxDirectory.REMINDERS_PATH)){
			
		}
		return null;
	}
	
	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		return null;
	}
	
	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
		return null;
	}
	
	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
		return 0;
	}
	
	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
		return 0;
	}
}
