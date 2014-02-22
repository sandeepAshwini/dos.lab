package base;

public enum NationCategories {
	ROME("Rome"),
	GAUL("Gaul"),
	PARTHIA("Parthia");
	
	private String value;
	NationCategories(String category){
		this.value = category;
	}
	
	public String getCategory(){
		return this.value;
	}
}
