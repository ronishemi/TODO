package il.ac.shenkar.todos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;

public class TaskListActivity extends Activity {
	
	
	Button ShowDatePicker;
	Button ShowTimePicker;
	Button Set;
	Button ReSet;
	Button Cancel;
	DatePicker DPic;
	TimePicker TPic;
	TextView Date;
	Dialog dialog;
	final Calendar c = Calendar.getInstance();
	SimpleDateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
	private ViewSwitcher switcher;
	private Button btnEdit;
	private Button btnReminder;
	private String posiTion = null;
	private EditText editText;
	static final int DATE_TIME_DIALOG_ID=0;
	long TimeToBroadCastReceiver;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		btnEdit = (Button) findViewById(R.id.btnMessage);
		btnReminder = (Button) findViewById(R.id.btnReminder);
		editText = (EditText) findViewById(R.id.edit_message);
		Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	
        	
        	posiTion = extras.getString("posiTion");
        	editText.setText(extras.getString("massage"));
        	//Toast.makeText(this," posiTion Task: "+posiTion, Toast.LENGTH_SHORT).show();
        }
/*-----------------Button to Add a new item to list------------------------*/
		btnEdit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(TaskListActivity.this,
						Todo.class);
				//editText = (EditText) findViewById(R.id.edit_message);
				String message = editText.getText().toString();
				intent.putExtra("posiTion", posiTion);
				intent.putExtra("newMessage", message);
				intent.putExtra("TimeToBroadCastReceiver",String.valueOf(TimeToBroadCastReceiver));
				if((message == null) || (message.isEmpty()))
					setResult(RESULT_CANCELED, intent);
				else
					setResult(RESULT_OK, intent);
				finish();
			}
		});
/*-----------------Button to Set a time and date reminder ---------------------------*/	
		Date = ((TextView) findViewById(R.id.Date));
		Date.setText(dfDate.format(c.getTime()));
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.datetimepicker);
		switcher = (ViewSwitcher) dialog.findViewById(R.id.DateTimePickerVS);
		DPic = (DatePicker) dialog.findViewById(R.id.DatePicker);
		TPic = (TimePicker) dialog.findViewById(R.id.TimePicker);
		TPic.setIs24HourView(true);
		ShowDatePicker = ((Button) dialog.findViewById(R.id.SwitchToDate));
		ShowDatePicker.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switcher.showPrevious();
				ShowDatePicker.setEnabled(false);
				ShowTimePicker.setEnabled(true);
			}
		});
		ShowTimePicker = ((Button) dialog.findViewById(R.id.SwitchToTime));
		ShowTimePicker.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switcher.showNext();
				ShowDatePicker.setEnabled(true);
				ShowTimePicker.setEnabled(false);
			}
		});
        
		Set = ((Button) dialog.findViewById(R.id.SetDateTime));
		Set.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				c.set(DPic.getYear(),  DPic.getMonth(), DPic.getDayOfMonth(), TPic.getCurrentHour(), TPic.getCurrentMinute());
				Date.setText(dfDate.format(c.getTime()));
				TimeToBroadCastReceiver = c.getTimeInMillis();
				dialog.cancel();
			}
		});
		ReSet = ((Button) dialog.findViewById(R.id.ResetDateTime));
		ReSet.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DPic.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				TPic.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
				TPic.setCurrentMinute(c.get(Calendar.MINUTE));
			}
		});
		Cancel = ((Button) dialog.findViewById(R.id.CancelDialog));
		Cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		btnReminder.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DPic.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				TPic.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
				TPic.setCurrentMinute(c.get(Calendar.MINUTE));
				showDialog(DATE_TIME_DIALOG_ID);
				
			}
		});
		dialog.setTitle("Select Date Time");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_TIME_DIALOG_ID:
			
			return dialog;
		}
		return null;
	}
}

