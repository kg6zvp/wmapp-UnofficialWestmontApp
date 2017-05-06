package enterprises.mccollum.wmapp.shuttle.horizon;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import enterprises.mccollum.wmapp.R;
import enterprises.mccollum.wmapp.shuttle.control.ShuttleAtomicDataListener;
import enterprises.mccollum.wmapp.shuttle.control.ShuttleDataListener;
import enterprises.mccollum.wmapp.shuttle.control.ShuttleJunkie;
import enterprises.mccollum.wmapp.shuttle.model.SequentialStop;
import enterprises.mccollum.wmapp.shuttle.model.ShuttlePersistenceManager;
import enterprises.mccollum.wmapp.shuttle.view.ShuttleActivity;
import enterprises.mccollum.wmapp.shuttle.view.StopViewHolder;

/**
 * Created by smccollum on 04.05.17.
 */
public class ShuttleViewAdapter extends RecyclerView.Adapter<StopViewHolder> implements SwipeRefreshLayout.OnRefreshListener, ShuttleAtomicDataListener {
	final ShuttleActivity context;
	//List<SequentialStop> sequentialStops = null;
	
	public ShuttleViewAdapter(ShuttleActivity context){
		this.context = context;
	}
	
	@Override
	public void onRefresh() {
		context.getRootView().setRefreshing(true);
		ShuttleJunkie.getInstance(context).refreshStopTimes(this);
		//refresh data
	}
	
	@Override
	public StopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_stop_origin_list_item , parent, false);
		return new StopViewHolder(v, context);
	}
	
	@Override
	public void onBindViewHolder(StopViewHolder holder, int position) {
		/*if(sequentialStops == null) {
			sequentialStops = ShuttlePersistenceManager.getInstance(context).getSequentialStops().getAll();
			Long lowestId = sequentialStops.get(0).getId();
			Long highestId = sequentialStops.get(sequentialStops.size()-1).getId();
			//sequentialStops.sort();
		}//*/
		holder.setFields(
				//sequentialStops.get(position)
				ShuttlePersistenceManager.getInstance(context).getSequentialStops().get((long)position+1)
		);
	}
	
	@Override
	public int getItemCount() {
		return ShuttlePersistenceManager.getInstance(context).getSequentialStops().size();
	}
	
	@Override
	public void setHasStableIds(boolean hasStableIds) {
		super.setHasStableIds(hasStableIds);
	}
	
	@Override
	public void doneLoading(int updates) {
		context.getRootView().setRefreshing(false); //stop it spinning
		if(updates < 1) { //if no data was modified
			return;
		}
		notifyDataSetChanged();
	}
}
