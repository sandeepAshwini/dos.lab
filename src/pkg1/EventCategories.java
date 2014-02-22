package pkg1;

public enum EventCategories {
	STONE_LUGING("Gold Leaf"),
	STONE_SLEDDING("Silver Leaf"),
	STONE_SKATING("Bronze Leaf");
	
	private String value;
	EventCategories(String category){
		this.value = category;
	}
	
	public String getCategory(){
		return this.value;
	}

}
