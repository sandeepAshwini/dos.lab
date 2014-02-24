package base;

public enum EventStatus {
	SCHEDULED("Scheduled"),
	IN_PROGRESS("In Progress"),
	COMPLETED("Completed"),
	INTERRUPTED("Interrupted");
	
	private String value;
	EventStatus(String category){
		this.value = category;
	}
	
	public String getCategory(){
		return this.value;
	}

}
