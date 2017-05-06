package enterprises.mccollum.wmapp.shuttle.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import enterprises.mccollum.wmapp.R;
import enterprises.mccollum.wmapp.shuttle.control.ShuttleJunkie;
import enterprises.mccollum.wmapp.shuttle.model.SequentialStop;

/**
 * Created by smccollum on 04.05.17.
 */

public class StopViewHolder extends RecyclerView.ViewHolder {
	TextView stopName;
	TextView stopEta;
	TextView routeName;
	
	SequentialStop sequentialStop;
	
	Context ctx;
	
	public StopViewHolder(View itemView, Context ctx) {
		super(itemView);
		this.ctx = ctx.getApplicationContext();
		stopName = (TextView) itemView.findViewById(R.id.tvStopName);
		stopEta = (TextView) itemView.findViewById(R.id.tvETA);
		routeName = (TextView) itemView.findViewById(R.id.tvShuttleRoute);
	}
	
	public void setFields(SequentialStop sequentialStop){
		if(sequentialStop == null){
			stopName.setText("Stop");
			stopEta.setText("ETA");
			routeName.setText("Route");
			return;
		}
		final long stopId = sequentialStop.getId();
		final Context fctx = ctx;
		
		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShuttleJunkie.getInstance(fctx).subscribeToNotification(stopId);
			}
		});
		
		stopName.setText(
				String.format("%d.) %s",
					sequentialStop.getId(),
					sequentialStop.getPhysicalStop().getName())
		);
		String ste = null;
		if(sequentialStop.getCurrentEstimatedTime() != null) {
			ste = new Date(sequentialStop.getCurrentEstimatedTime()).toString();
		}else{
			ste = "no ETA";
		}
		stopEta.setText(ste);
		routeName.setText(sequentialStop.getRoute().getName());
	}
}
