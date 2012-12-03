package il.ac.shenkar.todos.data;

import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler/* extends SQLiteOpenHelper*/ {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "TodoManager";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "tasks";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
 
//    public DatabaseHandler(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
// 
//    // Creating Tables
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
//                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
//                + KEY_PH_NO + " TEXT" + ")";
//        db.execSQL(CREATE_CONTACTS_TABLE);
//    }
// 
//    // Upgrading database
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
// 
//        // Create tables again
//        onCreate(db);
//    }
//
//	@Override
//	public void onCreate(SQLiteDatabase arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
//		// TODO Auto-generated method stub
		
	}
