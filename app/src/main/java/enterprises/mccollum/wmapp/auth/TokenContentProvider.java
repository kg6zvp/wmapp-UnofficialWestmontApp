package enterprises.mccollum.wmapp.auth;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by smccollum on 05.04.17.
 *
 * This class is a stub because we really just want to make sure that the user gets a new authentication/authorization token from the server when they need it
 *
 */
public class TokenContentProvider extends ContentProvider {
	@Override
	public boolean onCreate() {
		int error = 0;
		if(error > 0)
			return false; //ContentProvider failed to initialize
		return true; //initialized successfully
	}
	
	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
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
