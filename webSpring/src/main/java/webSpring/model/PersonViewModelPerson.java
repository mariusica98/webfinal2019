package webSpring.model;

import java.util.ArrayList;
import java.util.List;

import webSpring.entity.Malicious;
import webSpring.entity.Person;

public class PersonViewModelPerson {

	private String name;
	private List<Person> searchedList;
	private List<Malicious> maliciousList;

	public PersonViewModelPerson() {
		name = "";
		searchedList = new ArrayList<>();
		maliciousList = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Person> getSearchedList() {
		return searchedList;
	}

	public void setSearchedList(List<Person> searchedList) {
		this.searchedList = searchedList;
	}

	public boolean isListEmpty() {
		return searchedList.isEmpty();
	}

	public List<Malicious> getMaliciousList() {
		return maliciousList;
	}

	public void setMaliciousList(List<Malicious> maliciousList) {
		this.maliciousList = maliciousList;
	}

	public boolean isMaliciousEmpty() {
		return maliciousList.isEmpty();
	}

	public boolean isNameEmpty() {
		return name.trim().equals("");
	}

}
