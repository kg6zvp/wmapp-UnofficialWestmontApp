package enterprises.mccollum.wmapp.auth;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by smccollum on 30.03.17.
 */

public class TokenSyncAdapter extends AbstractThreadedSyncAdapter {
	
	public TokenSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
	}
	
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
		//synchronize token
	}
}
