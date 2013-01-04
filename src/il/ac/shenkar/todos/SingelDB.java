package il.ac.shenkar.todos;

import il.ac.shenkar.todos.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;

public class SingelDB {
	
	private static SingelDB singelDB ;
	private static List<ItemDetails> array ;
	private  Context _context;
	private static DatabaseHandler entry;
	
	private SingelDB(Context context){
		_context=context;
		array = new ArrayList<ItemDetails>();
		entry = new DatabaseHandler(context);
		array =entry.getAllItemDetails();		
	}
	
	
	static SingelDB getInstance(Context context){
		if(singelDB==null){
			singelDB=new SingelDB(context);
		}
		return singelDB;
	}
	
	public DatabaseHandler getEntry(){
		return entry;
	}
	public List<ItemDetails> getList() {
		
		return array;
	}
	public  boolean addList(String name,String description) {
		singelDB.getEntry().addItemDetails(new ItemDetails(name, description));
				
		ItemDetails item_details = new ItemDetails();
		item_details.setName(name);
		item_details.setItemDescription(description); 
		if(array.isEmpty())
			item_details.set_id(1);
		else
			item_details.set_id(singelDB.getEntry().getItemDetails(array.size()+1).get_id());
		
		return  SingelDB.array.add(item_details);				
	}
	public void printDB(){
		List<ItemDetails> l = new ArrayList<ItemDetails>();
		l = singelDB.getEntry().getAllItemDetails();
		for(int i=0;i<l.size();++i){
			System.out.println("printDB: "+l.get(i));
		}
	}
	public void printARRAY(){
		List<ItemDetails> l = new ArrayList<ItemDetails>();
		l = singelDB.getList();
		for(int i=0;i<l.size();++i){
			System.out.println("print arry: "+l.get(i));
		}
	}
	public  boolean delList(int position) {
		
//		ItemDetails item_details = SingelDB.entry.getItemDetails(position+1);
		ItemDetails item_details = array.get(position);
		
		singelDB.getEntry().deleteItemDetails(item_details);
		item_details = SingelDB.array.remove(position);
		 if(item_details==null){
			 return false;
		 }		 
		 return true;		 
	}
	public  ItemDetails getItemDetails(int index) {
		return SingelDB.array.get(index);
	}
	public int getPosition(ItemDetails temDetails){
		int i=0; 
		while((SingelDB.array.size()>=i)&&(temDetails.get_id() != SingelDB.array.get(i).get_id())){
			++i;
		}
		return i;
	}
	
}
