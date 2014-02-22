package base;

public enum EventCategories {
	STONE_LUGING("Stone Luging"),
	STONE_SLEDDING("Stone Sledding"),
	STONE_SKATING("Stone Skating");
	
	private String value;
	EventCategories(String category){
		this.value = category;
	}
	
	public String getCategory(){
		return this.value;
	}

}
