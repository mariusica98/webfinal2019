package webSpring.model;

import java.util.List;

public class PdfViewModel {
	
	private String result;
	
	private List<String> results;

	
	
	public List<String> getResults() {
		return results;
	}

	public void setResults(List<String> results) {
		this.results = results;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
