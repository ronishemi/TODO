package il.ac.shenkar.todos.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.StrictMode;


public class UrlService extends IntentService {

	// Status Constants
	public static final int STATUS_RUNNING = 0x1;
	public static final int STATUS_FINISHED = 0x2;
	public static final int STATUS_SUCCESS = 0x3;
	public static final int STATUS_ERROR = 0x4;
	// Command Constants
	public static final int PERFORM_SERVICE_ACTIVITY = 0x5;

	public static final String COMMAND_KEY = "service_command";
	public static final String RECEIVER_KEY = "serivce_receiver";
	public static final String SERVICE_WAS_SUCCESS_KEY = "service_was_success";
	private URL url;
	private Bundle bundle;
	private ResultReceiver receiver;

	public UrlService() {
		super("UrlService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		this.receiver = intent.getParcelableExtra(RECEIVER_KEY);
		int command = intent.getIntExtra(COMMAND_KEY, PERFORM_SERVICE_ACTIVITY);
		//this.receiver.send(STATUS_RUNNING, Bundle.EMPTY);
		switch (command) {
		case PERFORM_SERVICE_ACTIVITY:
			
		  StrictMode.setThreadPolicy(
		    	        new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build()); 
			try {
				url = new URL(intent.getStringExtra("url"));
				 
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				  
			  String  result = readTodo2(url);
	  		  bundle= new Bundle();
	  	      bundle.putString("ServiceResult",result);
	  	     this.receiver.send(STATUS_FINISHED,bundle);          
	         this.stopSelf();
			  				
			break;
		default:
			receiver.send(STATUS_FINISHED, Bundle.EMPTY);
		}
		this.stopSelf();
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
  	  	receiver.send(STATUS_ERROR, Bundle.EMPTY);
		this.stopSelf();
  	    }
  		finally{
  		return response;
  		}
  	}	
}

