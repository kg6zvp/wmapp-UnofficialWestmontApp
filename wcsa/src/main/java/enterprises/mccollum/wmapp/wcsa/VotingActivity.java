package enterprises.mccollum.wmapp.wcsa;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class VotingActivity extends Activity {
	
	public Fragment buttonFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voting);
		
		buttonFragment = getFragmentManager().findFragmentById(R.id.fragment); //shouldn't ever fail
	}
}
