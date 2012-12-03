package il.ac.shenkar.todos;

import il.ac.shenkar.todos.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TaskListActivity extends Activity {

	private Button btnEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		btnEdit = (Button) findViewById(R.id.btnMessage);
		btnEdit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(TaskListActivity.this,
						ListViewImagesActivity.class);
				EditText editText = (EditText) findViewById(R.id.edit_message);
				String message = editText.getText().toString();
				intent.putExtra("newMessage", message);
				if((message == null) || (message.isEmpty()))
					setResult(RESULT_CANCELED, intent);
				else
					setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

}
