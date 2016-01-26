package rpcdemo.rpc.api.benchmark.dataobject;

import java.io.Serializable;
import java.util.List;

public class PersonInfo implements Serializable {
	List<Phone> phones;
	
	Phone fax;
	
	FullAddress fullAddress;
	
	String mobileNo;
	
	String name;
	boolean male;
	boolean female;
	String department;
	String jobTitle;
	String homepageUrl;
	public List<Phone> getPhones() {
		return phones;
	}
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}
	public Phone getFax() {
		return fax;
	}
	public void setFax(Phone fax) {
		this.fax = fax;
	}
	public FullAddress getFullAddress() {
		return fullAddress;
	}
	public void setFullAddress(FullAddress fullAddress) {
		this.fullAddress = fullAddress;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMale() {
		return male;
	}
	public void setMale(boolean male) {
		this.male = male;
	}
	public boolean isFemale() {
		return female;
	}
	public void setFemale(boolean female) {
		this.female = female;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getHomepageUrl() {
		return homepageUrl;
	}
	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
	}
	
}
