package il.ac.shenkar.todos;

import il.ac.shenkar.todos.R;
import il.ac.shenkar.todos.BroadCast.ReminderBroadCastReceiver;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Todos extends Activity {
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	public SingelDB dB1;
	private Button btnAdd;
	private ListView lv1;
	private ItemListBaseAdapter adapter; 
	static final int NEW_ACTIVITY = 100;
	
	/** Called when the activity is first created. */
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dB1=SingelDB.getInstance(this);      
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	
        	//String val = extras.getString("newMessage");
        	String id = extras.getString("uniqueId");
       	int uniqueId = Integer.parseInt(id);
       	Toast.makeText(this,dB1.getItemDetails(uniqueId).getName(), Toast.LENGTH_LONG).show();
        }
        
        btnAdd = (Button) findViewById(R.id.btAdd);
        lv1 = (ListView) findViewById(R.id.listV_main);
        adapter= new ItemListBaseAdapter(Todos.this, dB1.getList());
        lv1.setAdapter(adapter);
        
         btnAdd.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
			
				Intent intent = new Intent(Todos.this, TaskListActivity.class);
				startActivityForResult(intent, Todos.NEW_ACTIVITY);
				
			}
		});
         
	}	
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		dB1=SingelDB.getInstance(this); 
		Bundle extras = intent.getExtras();
        if(extras !=null) {
        	//String val = extras.getString("newMessage");
        	String id = extras.getString("uniqueId");
        	int uniqueId = Integer.parseInt(id);
          Toast.makeText(this,dB1.getItemDetails(uniqueId).getName(), Toast.LENGTH_LONG).show();
        }
    	
		

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == NEW_ACTIVITY && resultCode == RESULT_OK){
		
			Intent intent = new Intent("il.ac.shenkar.todos.BroadCast");
			String returnTask = data.getStringExtra("newMessage");
			long TimeToBroadCastReceiver = Long.parseLong(data.getStringExtra("TimeToBroadCastReceiver"));
			int unqueId;
			dB1.getEntry().open();
			dB1.getEntry().createEntry(returnTask, returnTask);
			dB1.getEntry().close();
			unqueId = dB1.getList().size();
			dB1.addList(returnTask, returnTask);
			adapter.notifyDataSetChanged();
						
			if(TimeToBroadCastReceiver > 0){
            
			intent.putExtra("newMessage", returnTask);
			intent.putExtra("uniqueId",String.valueOf(unqueId));
			PendingIntent pendingIntent	= PendingIntent.getBroadcast(Todos.this,unqueId,intent,PendingIntent.FLAG_ONE_SHOT);		
			AlarmManager alarmManager	=(AlarmManager)getSystemService(ALARM_SERVICE);									
			alarmManager.set(AlarmManager.RTC_WAKEUP,TimeToBroadCastReceiver,pendingIntent);
			
			}
		}
	}
	
	
       
}