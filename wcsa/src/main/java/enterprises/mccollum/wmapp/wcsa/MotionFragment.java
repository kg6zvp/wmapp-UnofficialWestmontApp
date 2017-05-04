package enterprises.mccollum.wmapp.wcsa;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MotionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MotionFragment extends Fragment {
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_DOING_MOTION = "motion";
	
	private Boolean doingMotion;
	
	@BindView(R.id.motion_sec_btn)
	Button motionSecond;
	@BindView(R.id.present_button)
	Button present;
	@BindView(R.id.object_button)
	Button object;
	
	Unbinder unBinder;
	
	public MotionFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param doingMotion Whether or not a motion is currently in progress
	 * @return A new instance of fragment MotionFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MotionFragment newInstance(Boolean doingMotion) {
		MotionFragment fragment = new MotionFragment();
		Bundle args = new Bundle();
		args.putBoolean(ARG_DOING_MOTION, doingMotion);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			doingMotion = getArguments().getBoolean(ARG_DOING_MOTION);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_motion, container, false);
		unBinder = ButterKnife.bind(this, v);
		return v;
	}
	
	@OnClick(R.id.motion_sec_btn)
	public void motionOrSecond(){
		//TODO: ReST API call
	}
	
	@OnClick(R.id.present_button)
	public void votePresent(){
		//TODO: ReST API call
	}
	
	@OnClick(R.id.object_button)
	public void voteObject(){
		//TODO: ReST API call
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.voting_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.editBill:
				//TODO: Edit bill
				break;
			default:
				//Do nothing
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unBinder.unbind();
	}
}
