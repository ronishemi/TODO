package il.ac.shenkar.todos;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;


import il.ac.shenkar.todos.BroadCast.ReminderBroadCastReceiver;

import il.ac.shenkar.todos.service.UrlService;
import il.ac.shenkar.todos.service.MyResultReceiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Todo extends Activity implements
MyResultReceiver.Receiver{
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
		private MyResultReceiver receiver;
		private int REL_SWIPE_MIN_DISTANCE; 
	    private int REL_SWIPE_MAX_OFF_PATH;
	    private int REL_SWIPE_THRESHOLD_VELOCITY;
	
	private SingelDB dB1;
	private TextView time;
	private Button btnAdd;
	private Button btnUrl;
	//private Button btnService;
	private ListView lv1;
	private ItemListBaseAdapter adapter; 
	static final int NEW_ACTIVITY = 100;
	private int posiTion;
	private URL url;
	/** Called when the activity is first created. */
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        DisplayMetrics dm = getResources().getDisplayMetrics();
        REL_SWIPE_MIN_DISTANCE = (int)(120.0f * dm.densityDpi / 160.0f + 0.5); 
        REL_SWIPE_MAX_OFF_PATH = (int)(250.0f * dm.densityDpi / 160.0f + 0.5);
        REL_SWIPE_THRESHOLD_VELOCITY = (int)(200.0f * dm.densityDpi / 160.0f + 0.5);
                
     // Set our receiver
     		receiver = new MyResultReceiver(new Handler());
     		receiver.setReceiver(this);
     	   StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
     		try {
				url = new URL("http://mobile1-tasks-dispatcher.herokuapp.com/task/random");
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        setContentView(R.layout.main);
        dB1=SingelDB.getInstance(this);           
        Bundle extras = getIntent().getExtras();
        
        btnAdd = (Button) findViewById(R.id.btAdd);
        lv1 = (ListView) findViewById(R.id.listV_main);
        adapter= new ItemListBaseAdapter(Todo.this, dB1.getList());
        lv1.setAdapter(adapter);
        if(extras !=null) {
        	String id = extras.getString("uniqueId");
        	int uniqueId = 0;
        	
        	if(id != null){
        		uniqueId = Integer.parseInt(id);
             
            	lv1.setSelectionFromTop(dB1.getPosition(dB1.getEntry().getItemDetails(uniqueId)), 0);
        	}
        	//getIntent().removeExtra(ACTIVITY_SERVICE);
        }        
        final GestureDetector gestureDetector = new GestureDetector(new MyGestureDetector());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
            	         	
            	
                return gestureDetector.onTouchEvent(event); 
            }};
        lv1.setOnTouchListener(gestureListener);  
       
        
         btnAdd.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
			
				Intent intent = new Intent(Todo.this, TaskListActivity.class);
				startActivityForResult(intent, Todo.NEW_ACTIVITY);
				
			}
		});
         btnUrl = (Button) findViewById(R.id.btUrl);
         btnUrl.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
		      
		          GetTodoFromWeb task = new GetTodoFromWeb ();
		          task.execute(url);	
		        	       
				
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
       	if(id != null){
        	int uniqueId = Integer.parseInt(id);
        	
        	lv1.setSelectionFromTop(dB1.getPosition(dB1.getEntry().getItemDetails(uniqueId)), 0);
        }
       //getIntent().removeExtra(ACTIVITY_SERVICE);
       	}
    	
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == NEW_ACTIVITY && resultCode == RESULT_OK){
			 
			String editPosition = null; 

			ItemDetails itemDetails = new ItemDetails();
			editPosition = data.getStringExtra("posiTion");
			Intent intent = new Intent(Todo.this.getApplicationContext(),ReminderBroadCastReceiver.class);
			String returnTask = data.getStringExtra("newMessage");
			long TimeToBroadCastReceiver = Long.parseLong(data.getStringExtra("TimeToBroadCastReceiver"));
			int unqueId = 0;
			
			if(editPosition == null){

			unqueId = dB1.getList().size();
			dB1.addList(returnTask,returnTask);
			
			}else{
			unqueId = Integer.parseInt(editPosition);	
			dB1.getItemDetails(unqueId).setName(returnTask);	
			dB1.getEntry().updateItemDetails(dB1.getItemDetails(unqueId));			
			}
			adapter.notifyDataSetChanged();
			itemDetails = dB1.getItemDetails(unqueId);
			unqueId = itemDetails.get_id();
			
			if((TimeToBroadCastReceiver > 0)&&(returnTask.compareTo("SERVICE") != 0 )){
			intent.putExtra("newMessage", returnTask);
			intent.putExtra("uniqueId",String.valueOf(unqueId));
			intent.setAction(returnTask);
			PendingIntent pendingIntent	= PendingIntent.getBroadcast(Todo.this.getApplicationContext(),unqueId,intent,PendingIntent.FLAG_UPDATE_CURRENT);		
			AlarmManager alarmManager	=(AlarmManager)Todo.this.getApplicationContext().getSystemService(ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP,TimeToBroadCastReceiver,pendingIntent);
			}
			else if((returnTask.compareTo("SERVICE") == 0 )) {		
			    Calendar calendar = Calendar.getInstance();
		        calendar.setTimeInMillis(System.currentTimeMillis());
		        calendar.set(Calendar.SECOND, 0);
		        calendar.set(Calendar.MINUTE,0);
		        calendar.set(Calendar.HOUR, 0);
		        calendar.set(Calendar.AM_PM, Calendar.AM);
		        calendar.add(Calendar.DAY_OF_MONTH, 1); 
		        
		       if(calendar.getTimeInMillis() > System.currentTimeMillis()){ 
		    	
		       intent.putExtra("url", url.toString());
		       	intent.putExtra("uniqueId",String.valueOf(unqueId));
				intent.putExtra("SERVICE", "SERVICE");
				intent.putExtra(UrlService.RECEIVER_KEY, receiver);
				intent.putExtra(UrlService.COMMAND_KEY, UrlService.PERFORM_SERVICE_ACTIVITY);
				intent.setAction(returnTask);
				PendingIntent pendingIntent	= PendingIntent.getBroadcast(Todo.this.getApplicationContext(),unqueId,intent,PendingIntent.FLAG_UPDATE_CURRENT);		
				AlarmManager alarmManager	=(AlarmManager)Todo.this.getApplicationContext().getSystemService(ALARM_SERVICE);
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
		       }
			}
		}				
	}
	
	
    private void myOnItemClick(int position) {
      //  String str = MessageFormat.format("Item clicked = {0,number}", position);
      //  Toast.makeText(this, str+" pos: "+position, Toast.LENGTH_SHORT).show();
    }
    
    private void onLTRFling() {
     	 if(posiTion <= dB1.getList().size() && posiTion >= 0){
        	   ItemDetails itemDetails = dB1.getItemDetails(posiTion);
         	   itemDetails.setDeleted("deleted");	
         	  dB1.getEntry().updateItemDetails(itemDetails);
         	   adapter.notifyDataSetChanged();
            	 }
    
    }

    private void onRTLFling() {
    	 if(posiTion <= dB1.getList().size() && posiTion >= 0){
        	 ItemDetails itemDetails = dB1.getItemDetails(posiTion);
        	 itemDetails.setDeleted("ok");
        	 dB1.getEntry().updateItemDetails(itemDetails);
			adapter.notifyDataSetChanged();
			
		   }
    }

    class MyGestureDetector extends SimpleOnGestureListener{ 

        // Detect a single-click and call my own handler.
        @Override 
        public boolean onSingleTapUp(MotionEvent e) {

        	return false;
        }
        

        @Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			super.onLongPress(e);
			posiTion = lv1.pointToPosition((int)e.getX(), (int)e.getY());
            
        	Intent intent = new Intent(Todo.this, TaskListActivity.class);
        	intent.putExtra("posiTion", String.valueOf(posiTion));
        	intent.putExtra("massage",dB1.getItemDetails(posiTion).getName());
        	startActivityForResult(intent, Todo.NEW_ACTIVITY); 
        	
		}


		@Override 
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { 
        	posiTion = lv1.pointToPosition((int)e1.getX(), (int)e1.getY());
        	if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH) 
                return false; 
            if(e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE && Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) { 
                onRTLFling(); 
            }  else if (e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE && 
                Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) { 
                onLTRFling(); 
            } 
            return false; 
        } 
    } 
    private class GetTodoFromWeb extends AsyncTask<URL,Integer,String>{	
    	
    	protected	String	doInBackground(URL... args)	{
            publishProgress(0); // Invokes onProgressUpdate()
    	return readTodo2(args[0]);
    			
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO Auto-generated method stub
    		super.onProgressUpdate(values);
    		
    	 ImageView img=(ImageView)findViewById(R.id.time);
			img.setImageResource(R.drawable.sand_clock);
 	       img.setVisibility(View.VISIBLE);
  		
    	}
    	protected void onPostExecute(String result) {
    		// TODO Auto-generated method stub
    		super.onPostExecute(result);
    		 ImageView img=(ImageView)findViewById(R.id.time);
 			img.setImageResource(R.drawable.sand_clock);
 	       img.setVisibility(View.INVISIBLE);
    		try {
    	       	JSONObject jsonObject = new JSONObject(result);
    	       //	String id =(String) jsonObject.get("_id");
    	       	String topic = (String) jsonObject.get("topic");
    	       	String description = (String) jsonObject.get("description");
    	       	if(topic != null){
    	       	dB1.addList(topic,description);
    	       	adapter.notifyDataSetChanged();
    	       	}
    	       	
    		} catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	}	
    		
    	@SuppressWarnings("finally")
    	public String readTodo2(URL url) {
    		String response = null;
    		try{
    		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
    		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
    		
    		InputStreamReader inReader	=	new	InputStreamReader(in);
    		BufferedReader bufferedReader	=	new	BufferedReader(inReader);
    		StringBuilder responseBuilder	=	new	StringBuilder();
    		for	(String	line=bufferedReader.readLine();	line!=null;	
    			line=bufferedReader.readLine()){responseBuilder.append(line);	
    		}
    		response =	responseBuilder.toString();	
    				
    		}catch (Exception e) {
    	        e.printStackTrace();
    	    }
    		finally{
    		return response;
    		}
    	}	
    }
public void onReceiveResult(int resultCode, Bundle resultBundle) {
		
		switch (resultCode) {
		case UrlService.STATUS_RUNNING:
			// Don't do anything, the service is running
			break;
		case UrlService.STATUS_SUCCESS:
			boolean wasSuccess = resultBundle
					.getBoolean(UrlService.SERVICE_WAS_SUCCESS_KEY);
			if (wasSuccess) {
				Toast.makeText(getApplicationContext(),
						"The service was a success", Toast.LENGTH_LONG).show();
			} else {
				// Show not success message
				Toast.makeText(getApplicationContext(),
						"The service was a failure", Toast.LENGTH_LONG).show();
			}
			break;
		case UrlService.STATUS_FINISHED:
			
			try {
				JSONObject jsonObject = new JSONObject((String) resultBundle.get("ServiceResult"));
				String topic = (String) jsonObject.get("topic");
    	       	String description = (String) jsonObject.get("description");
    	       	if(topic != null){
    	       	dB1.addList(topic,description);
    	       	adapter.notifyDataSetChanged();
    	       	}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case UrlService.STATUS_ERROR:
			Toast.makeText(getApplicationContext(), "The service had an error",
					Toast.LENGTH_LONG).show();
			break;
		}
	}
   
    
}