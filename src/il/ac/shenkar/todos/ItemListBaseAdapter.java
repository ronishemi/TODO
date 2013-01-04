package il.ac.shenkar.todos;

import il.ac.shenkar.todos.BroadCast.ReminderBroadCastReceiver;

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ItemListBaseAdapter extends BaseAdapter {
	private static List<ItemDetails> itemDetailsrrayList;
	private LayoutInflater l_Inflater;
	private Context context_;
	public SingelDB dB1=SingelDB.getInstance(context_);
	public ItemListBaseAdapter(Context context, List<ItemDetails> results) {
		context_=context;
		itemDetailsrrayList = results;
		l_Inflater = LayoutInflater.from(context);
	}
	
	public int getCount() {
		return itemDetailsrrayList.size();
	}

	public Object getItem(int position) {
		return itemDetailsrrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	public void cancelAlarm(Context context,String massage,int id){

		Intent intent = new Intent(context.getApplicationContext(),ReminderBroadCastReceiver.class);
		intent.putExtra("newMessage", massage);
		intent.putExtra("uniqueId",String.valueOf(id));
		intent.setAction(massage);
		PendingIntent pendingIntent	= PendingIntent.getBroadcast(context.getApplicationContext(),id,intent,PendingIntent.FLAG_CANCEL_CURRENT);		
		AlarmManager alarmManager	=(AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);									
		alarmManager.cancel(pendingIntent);
				
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
			
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.item_details_view, null);
			holder = new ViewHolder();
						
			holder.txt_itemName = (TextView) convertView.findViewById(R.id.name);
						
			holder.btn_item = (Button) convertView.findViewById(R.id.btDone);
			
			holder.btn_item.setOnClickListener(new OnClickListener() {
		
				public void onClick(View v) {
										
					ItemDetails itemDetails = new ItemDetails();
					int unqueId = (Integer) v.getTag();
					itemDetails = dB1.getItemDetails(unqueId);
													
					dB1.delList(unqueId);
					notifyDataSetChanged();
					
				  cancelAlarm(context_,itemDetails.getName(),(itemDetails.get_id()));  
				}
			});
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			
		}
		
		holder.txt_itemName.setText(itemDetailsrrayList.get(position).getName());
		
		holder.btn_item.setTag(position);
		if((itemDetailsrrayList.get(position).getDeleted() != null)&&itemDetailsrrayList.get(position).getDeleted().compareTo("deleted")==0)
		{
			holder.txt_itemName.setTextColor(Color.RED);
		holder.txt_itemName.setPaintFlags(holder.txt_itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}else{
			holder.txt_itemName.setTextColor(Color.YELLOW);
			holder.txt_itemName.setPaintFlags(holder.txt_itemName.getPaintFlags()& ~Paint.STRIKE_THRU_TEXT_FLAG);	
		}
		return convertView;
	}
	

	static class ViewHolder {
		TextView txt_itemName;
		Button btn_item;
	}
	
	
}
