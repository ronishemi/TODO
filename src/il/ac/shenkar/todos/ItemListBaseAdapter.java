package il.ac.shenkar.todos;


import il.ac.shenkar.todos.R;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ItemListBaseAdapter extends BaseAdapter {
	private static ArrayList<ItemDetails> itemDetailsrrayList;
	private LayoutInflater l_Inflater;
	private Context context_;
		
	public ItemListBaseAdapter(Context context, ArrayList<ItemDetails> results) {
		context_=context;
		itemDetailsrrayList = results;
		l_Inflater = LayoutInflater.from(context);
	}
	public SingelDB dB1=SingelDB.getInstance(context_);
	public int getCount() {
		return itemDetailsrrayList.size();
	}

	public Object getItem(int position) {
		return itemDetailsrrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
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
					itemDetails.setName(dB1.getItemDetails((Integer) v.getTag()).getName());
					dB1.getEntry().open();
					dB1.getEntry().deleteEntry(itemDetails);
					dB1.getEntry().close();
					dB1.delList((Integer) v.getTag());
					notifyDataSetChanged();
					
				  Toast.makeText(context_, "You deleted: "+v.getTag() , Toast.LENGTH_LONG).show();
					  
				}
			});
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txt_itemName.setText(itemDetailsrrayList.get(position).getName());
		holder.btn_item.setTag(position);
	
		return convertView;
	}
	

	static class ViewHolder {
		TextView txt_itemName;
		Button btn_item;
	}
}
