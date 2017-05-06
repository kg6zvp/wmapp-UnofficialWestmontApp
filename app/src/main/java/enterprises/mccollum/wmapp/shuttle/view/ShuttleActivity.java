package enterprises.mccollum.wmapp.shuttle.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import enterprises.mccollum.wmapp.R;
import enterprises.mccollum.wmapp.model.GenericEntityManager;
import enterprises.mccollum.wmapp.shuttle.horizon.ShuttleViewAdapter;
import enterprises.mccollum.wmapp.shuttle.model.SequentialStop;
import enterprises.mccollum.wmapp.shuttle.model.ShuttlePersistenceManager;

public class ShuttleActivity extends AppCompatActivity {
	@BindView(R.id.root_view)
	SwipeRefreshLayout rootView;
	
	@BindView(R.id.stopsView)
	RecyclerView shuttlesList;
	
	RecyclerView.LayoutManager mLayoutManager;
	
	ShuttleViewAdapter shuttleViewAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shuttle);
		ButterKnife.bind(this);
		shuttleViewAdapter = new ShuttleViewAdapter(this);
		mLayoutManager = new LinearLayoutManager(this);
		
		shuttlesList.setLayoutManager(mLayoutManager);
		rootView.setOnRefreshListener(shuttleViewAdapter);
		
		shuttlesList.setAdapter(shuttleViewAdapter);
		//shuttlesList.scrollToPosition(0);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		shuttleViewAdapter.onRefresh();
	}
	
	public SwipeRefreshLayout getRootView() {
		return rootView;
	}
}
