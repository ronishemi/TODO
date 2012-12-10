package il.ac.shenkar.todos;

import java.util.ArrayList;

import android.content.Context;

public class SingelDB {
	
	private static SingelDB singelDB =null;
	private static ArrayList<ItemDetails> array = null;
	private static ArrayList<ItemDetails> array1 = null;
	private  Context _context;
	private static DatabaseHandler entry=null;
	
	private SingelDB(Context context){
		_context=context;
	}
	
	
	static SingelDB getDB(Context context){
		if(singelDB==null){
			array = new ArrayList<ItemDetails>();
			singelDB=new SingelDB(context);
			entry = new DatabaseHandler(context);
			
		}
		return singelDB;
	}
	public DatabaseHandler getEntry(){
		return entry;
	}
	public void updateListFromDB(){
		entry.open();
		array1 =(ArrayList<ItemDetails>)entry.getAllItemDetails();
		entry.close();
		array = array1;
	}
	public  ArrayList<ItemDetails> getList() {
		return array;
	}
	public  boolean addList(String name,String description) {
		ItemDetails item_details = new ItemDetails();
		item_details.setName(name);
		item_details.setItemDescription(description); 
	 return	SingelDB.array.add(item_details);
	
	}
	public  boolean delList(int position) {
		ItemDetails item_details = SingelDB.array.remove(position);
		 if(item_details==null)
			 return false;
		 return true;
		 
	}
	public  ItemDetails getItemDetails(int index) {
		return SingelDB.array.get(index);
	}
	
}
