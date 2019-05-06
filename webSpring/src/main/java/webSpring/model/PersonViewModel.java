package webSpring.model;

import java.util.ArrayList;
import java.util.List;

public class PersonViewModel {
	private String name;
	private List<String> searchedList;
	private String cautare;

	

	public PersonViewModel() {

		name = "";
		searchedList = new ArrayList<>();

	}

	public String getName() {

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getSearchedList() {
		return searchedList;
	}

	public void setSearchedList(List<String> searchedList) {
		this.searchedList = searchedList;
	}

	public boolean isNameEmpty() {
		return name.trim().equals("");
	}
}
