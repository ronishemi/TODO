package il.ac.shenkar.todos;

public class ItemDetails {
	int _id;
	private String name ;
	private String itemDescription;
	private String deleted;
	public ItemDetails(){}
	public ItemDetails(int _id,String name,String itemDescription) {
		super();
		this._id = _id;
		this.name = name;
		this.itemDescription = itemDescription;
		this.deleted = "ok";
	}
	public ItemDetails(String name, String itemDescription) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.itemDescription = itemDescription;
		this.deleted = "ok";
	}
	public ItemDetails(int _id,String name,String itemDescription,String deleted) {
		super();
		this._id = _id;
		this.name = name;
		this.itemDescription = itemDescription;
		this.deleted = deleted;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	@Override
	public String toString() {
		return "ItemDetails [_id=" + _id + ", name=" + name
				+ ", itemDescription=" + itemDescription + ", deleted="
				+ deleted + "]";
	}
	

}