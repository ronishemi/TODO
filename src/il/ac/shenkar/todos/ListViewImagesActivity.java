package il.ac.shenkar.todos;

import il.ac.shenkar.todos.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ListViewImagesActivity extends Activity {
	
	public SingelDB dB1=SingelDB.getDB(this);
	private Button btnAdd;
	private ListView lv1;
	private ItemListBaseAdapter adapter; 
	static final int NEW_ACTIVITY = 100;
	
	/** Called when the activity is first created. */
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dB1.updateListFromDB();
        btnAdd = (Button) findViewById(R.id.btAdd);
        lv1 = (ListView) findViewById(R.id.listV_main);
        adapter= new ItemListBaseAdapter(ListViewImagesActivity.this, dB1.getList());
        lv1.setAdapter(adapter);
        
         btnAdd.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ListViewImagesActivity.this, TaskListActivity.class);
				startActivityForResult(intent, ListViewImagesActivity.NEW_ACTIVITY);
				
			}
		});
         
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == NEW_ACTIVITY && resultCode == RESULT_OK){
			String returnTask = data.getStringExtra("newMessage");
			dB1.getEntry().open();
			dB1.getEntry().createEntry(returnTask, returnTask);
			dB1.getEntry().close();
			dB1.addList(returnTask, returnTask);
			adapter.notifyDataSetChanged();
			
		}
	}
	
	
       
}