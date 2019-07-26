package webSpring.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import webSpring.entity.Malicious;
import webSpring.entity.Person;
import webSpring.repository.MaliciousRepository;
import webSpring.repository.PersonRepository;

@Configuration
@EnableScheduling
public class DownloadFormURL {
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private MaliciousRepository maliciousRepository;

//	@Scheduled(fixedRate = 1080000)
//	private String doSome() {
//
//		loadToDatabase("https://www.bis.doc.gov/dpl/dpl.txt");
//		System.out.println("doSome");
//		//throw new DBException();
//		return "/person";
//		}
//	

	void loadToDatabase(String linkForDenied) {
		
		System.out.println("ceva");
		URL deniedPersonListURL;
		BufferedReader in = null;
		URLConnection yc;
		personRepository.deleteAll();
		maliciousRepository.deleteAll();

		try {
			deniedPersonListURL = new URL(linkForDenied);
			yc = deniedPersonListURL.openConnection();
			in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

			String strLine;
			while ((strLine = in.readLine()) != null) {
				String s1 = strLine.replaceAll(" \"", "\"");
				String s2 = s1.replaceAll("\" ", "\"");
				String s3 = s2.replaceAll("\"\"", "\" \"");

				String[] splited = s3.split("\"*\"");

				if (splited.length == 24) {
					Person person = new Person();
					person.setName(splited[1]);
					person.setStreetAddress(splited[3]);
					person.setCity(splited[5]);
					person.setState(splited[7]);
					person.setCountry(splited[9]);
					person.setPostalCode(splited[11]);
					person.setEffectiveDate(splited[13]);
					person.setExpirationDate(splited[15]);
					person.setStandardOrder(splited[17]);
					person.setLastUpdate(splited[19]);
					person.setAction(splited[21]);
					person.setFrCitation(splited[23]);

					personRepository.save(person);
				} else {
					Malicious malicious = new Malicious();
					malicious.setLine(strLine);

					maliciousRepository.save(malicious);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
