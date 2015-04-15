package alertsManager;
public class City {

	private String city;
	private String id;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public City(String city, String id) {
		super();
		this.city = city;
		this.id = id;
	}

}
