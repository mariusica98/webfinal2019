package webSpring.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Type;

@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String name;
	private String streetAddress;
	private String city;
	private String state;
	private String country;
	private String postalCode;
	private String effectiveDate;
	private String expirationDate;
	private String standardOrder;
	private String lastUpdate;
	private String action;

	@Type(type = "text")
	private String frCitation;

	public Person(Integer id, String name, String streetAddress, String city, String state, String country,
			String postalCode, String effectiveDate, String expirationDate, String standardOrder, String lastUpdate,
			String action, String frCitation) {
		super();
		this.id = id;
		this.name = name;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.country = country;
		this.postalCode = postalCode;
		this.effectiveDate = effectiveDate;
		this.expirationDate = expirationDate;
		this.standardOrder = standardOrder;
		this.lastUpdate = lastUpdate;
		this.action = action;
		this.frCitation = frCitation;
	}

	public Person() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getStandardOrder() {
		return standardOrder;
	}

	public void setStandardOrder(String standardOrder) {
		this.standardOrder = standardOrder;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFrCitation() {
		return frCitation;
	}

	public void setFrCitation(String frCitation) {
		this.frCitation = frCitation;
	}

	@Override
	public String toString() {
		return name + "###" + streetAddress + "###" + city + "###" + state + "###" + country + "###" + postalCode
				+ "###" + effectiveDate + "###" + expirationDate + "###" + standardOrder + "###" + lastUpdate + "###"
				+ action + "###" + frCitation;
	}

}
