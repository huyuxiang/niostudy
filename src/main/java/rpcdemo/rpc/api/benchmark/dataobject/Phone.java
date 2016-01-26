package rpcdemo.rpc.api.benchmark.dataobject;

import java.io.Serializable;

public class Phone implements Serializable	 {
		
	private String country ;
	private String area;
	private String number;
	private String extensionNumber;
	
	public Phone() {
		
	}
	
	public Phone(String country,String area, 
			String number, String extensionNumber) {
		this.country = country ;
		this.area = area;
		this.number = number;
		this.extensionNumber = extensionNumber;
	}
	
	public void setCountry(String country)  {
		this.country = country ;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public String getCountry() {
		return country;
	}

	public String getArea() {
		return area;
	}

	public String getNumber() {
		return number;
	}
	
}
