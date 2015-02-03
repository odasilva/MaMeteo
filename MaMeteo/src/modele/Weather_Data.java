package modele;

public class Weather_Data {
	private int id;
	private String countrie;
	private int favorite;//0 false 1 true
	
	public Weather_Data(){
		
	}
	
	public Weather_Data(String countrie){
		this.countrie = countrie;
		this.favorite = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountrie() {
		return countrie;
	}

	public void setCountrie(String countrie) {
		this.countrie = countrie;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
	
	@Override
	public String toString() {
		return "Weather_Data [id=" + id + ", countrie=" + countrie
				+ ", favorite=" + favorite + "]";
	}
}
