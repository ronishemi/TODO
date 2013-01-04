package il.ac.shenkar.todos.BroadCast;
import il.ac.shenkar.todos.Todo;
import il.ac.shenkar.todos.R;
import il.ac.shenkar.todos.service.UrlService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class ReminderBroadCastReceiver extends	BroadcastReceiver{	


@SuppressWarnings("deprecation")
@Override
public void onReceive(Context context, Intent data) {
	Intent	myIntent;
	
	String service = data.getStringExtra("SERVICE");
	ResultReceiver receiver;
	if(service == null){
	String message = data.getStringExtra("newMessage");
	String id = data.getStringExtra("uniqueId");
	int uniqueId =Integer.parseInt(id);
	myIntent =	new	Intent(context,	Todo.class);
	
	Bundle mBundle = new Bundle();
	mBundle.putString("uniqueId", id);
	myIntent.putExtras(mBundle);
	myIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	PendingIntent pendingIntent	=	PendingIntent.getActivity(context,uniqueId,myIntent,PendingIntent.FLAG_ONE_SHOT);	
	NotificationManager notificationManager	=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);	
	Notification notification = new Notification(R.drawable.lightning,message,System.currentTimeMillis());	
	notification.setLatestEventInfo(context,"MyReminder",message, pendingIntent);
	notification.defaults = Notification.DEFAULT_ALL;
	notification.flags |= Notification.FLAG_AUTO_CANCEL;
	notificationManager.notify(uniqueId,notification);
	}else{
	
		myIntent =	new	Intent(context,UrlService.class);
		receiver = data.getParcelableExtra(UrlService.RECEIVER_KEY);
		myIntent.putExtra("url", data.getStringExtra("url"));
		myIntent.putExtra("receiverTag", receiver);
		myIntent.putExtra(UrlService.RECEIVER_KEY, receiver);
		myIntent.putExtra(UrlService.COMMAND_KEY, UrlService.PERFORM_SERVICE_ACTIVITY);
		context.startService(myIntent);
		
		 
	}
}  
}	
