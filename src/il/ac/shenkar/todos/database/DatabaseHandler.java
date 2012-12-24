package il.ac.shenkar.todos.database;


import il.ac.shenkar.todos.ItemDetails;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler {
	 // All Static variables
 
	// Database Version
    private static final int DATABASE_VERSION = 2;
     // Database Name
    
    private static final String DATABASE_NAME = "todoManager";
 // Table Name
    private static final String DATABASE_TABLE = "todos";
    
    // Contacts Table Columns names
    //private static final String KEY_ROWID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ITEMDESCRIPTION = "itemDescription";
    
    private DbHelper ourHelper;
    private static Context ourContext;
    private SQLiteDatabase ourDatabase;
    
    private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
					+ KEY_NAME + " TEXT,"
					+ KEY_ITEMDESCRIPTION + " TEXT" + ")";
			db.execSQL(CREATE_CONTACTS_TABLE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			// Drop older table if existed
	        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
	 
	        // Create tables again
	        onCreate(db);
		}
    	
    }
    public DatabaseHandler(Context c){
    	ourContext = c;
    }
    public DatabaseHandler open()throws SQLException{
    	ourHelper  = new DbHelper(ourContext);
    	ourDatabase = ourHelper.getWritableDatabase();
    	return this;
    }
    public void close(){
    	ourHelper.close();
    }
	public long createEntry(String name, String itemDescription) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_ITEMDESCRIPTION, itemDescription);
	return ourDatabase.insert(DATABASE_TABLE, null, cv);
		
	}
	public String getData() {
	String[] columns = new String[]{KEY_NAME ,KEY_ITEMDESCRIPTION};	
	Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);	
	String result="";
		
	int iName = c.getColumnIndex(KEY_NAME);
	int iItem = c.getColumnIndex(KEY_ITEMDESCRIPTION); 
	for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
	result = result + c.getString(iName) + " " + c.getString(iItem) + "\n";
	}
	return result;
	}
	// Getting All Contacts
    public List< ItemDetails> getAllItemDetails() {
        List< ItemDetails>  itemDetailsList = new ArrayList< ItemDetails>();
        // Select All Query
       
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE;
        Cursor c = ourDatabase.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            	 ItemDetails  itemDetails = new  ItemDetails();
            	 itemDetails.setName(c.getString(0));
            	 itemDetails.setItemDescription(c.getString(1));
                // Adding contact to list
            	 itemDetailsList.add(itemDetails);
         }
       
        // return contact list
        return itemDetailsList;
    }
	  // Getting single contact
	  public  ItemDetails  getItemDetails(String name) {
		  
		  Cursor cursor = ourDatabase.query(DATABASE_TABLE, new String[] {KEY_NAME,
				  KEY_ITEMDESCRIPTION }, KEY_NAME + "=?",
	                new String[] { name }, null, null, null, null);
	        
		  if (cursor.getCount() != 0){
	            cursor.moveToFirst();
	 	        ItemDetails itemDetails = new ItemDetails(cursor.getString(0), cursor.getString(1));
	             return itemDetails;
	        }
	       
	        return null;
	    }

	
	public void deleteEntry(ItemDetails itemDetails) {
		// TODO Auto-generated method stub
		ourDatabase.delete(DATABASE_TABLE, KEY_NAME + " = ?",
                new String[] { itemDetails.getName() });
	}
	
	
}
   