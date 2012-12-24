package il.ac.shenkar.todos.BroadCast;
import il.ac.shenkar.todos.Todos;
import il.ac.shenkar.todos.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ReminderBroadCastReceiver extends	BroadcastReceiver{	


@SuppressWarnings("deprecation")
@Override
public void onReceive(Context context, Intent data) {
	
	
	String message = data.getStringExtra("newMessage");
	String id = data.getStringExtra("uniqueId");
	int uniqueId =Integer.parseInt(id);
	
	Intent	myIntent	=	new	Intent(context,	Todos.class);
	
	Bundle mBundle = new Bundle();
	//mBundle.putString("newMessage", message);
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
	
}  
}	
