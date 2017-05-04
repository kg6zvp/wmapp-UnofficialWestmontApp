package enterprises.mccollum.wmapp.model;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by smccollum on 03.05.17.
 */
public class ShuttleSyncAdapter extends AbstractThreadedSyncAdapter {
	AccountManager am;
	
	public ShuttleSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		AccountManager.get(context);
	}
	
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
		
	}
}
