package il.ac.shenkar.todos;


import java.util.ArrayList;

import android.content.Context;

public class SingelDB {
	
	private static SingelDB singelDB =null;
	private static ArrayList<ItemDetails> array = null;
	private  Context context;
	
	private SingelDB(Context context){
		this.context=context;
	}
	
	
	static SingelDB getDB(Context context){
		if(singelDB==null){
			array = new ArrayList<ItemDetails>();
			singelDB=new SingelDB(context);
			
		}
		return singelDB;
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
	
}
