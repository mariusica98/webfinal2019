package webSpring.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import webSpring.HeaderFooterPageEvent;
import webSpring.entity.Malicious;
import webSpring.entity.Person;
import webSpring.model.PersonViewModelPerson;
import webSpring.repository.MaliciousRepository;
import webSpring.repository.PersonRepository;

@Controller
@Configuration
public class PersonController {
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private MaliciousRepository maliciousRepository;

	@Autowired
	private ServletContext servletContext;

	@Scheduled(fixedRate = 86400000)
	private void doSome() {

		loadToDatabase("https://www.bis.doc.gov/dpl/dpl.txt");
		System.out.println("doSome");
		getMapping();
	}

	@GetMapping("/person")
	public ModelAndView getMapping() {
		return new ModelAndView("person", "personViewModelPerson", new PersonViewModelPerson());
	}

	@PostMapping("/person")
	public String postMapping(@ModelAttribute PersonViewModelPerson personViewModelPerson,
			@RequestParam("submit") String reqParam, HttpServletResponse response, Model model) {
		System.out.println("POST");
		List<Person> searchedPersons = new ArrayList<>();
		List<Malicious> maliciousPersons = new ArrayList<>();

		switch (reqParam) {
		case "Cautare":
			String searchedName = personViewModelPerson.getName().trim();
			// verificare1
			if (!searchedName.equals("")) {
				Iterable<Person> persons = personRepository.findAll();
				for (Person person : persons) {
					if (person.getName().toUpperCase().contains(searchedName.toUpperCase())) {
						searchedPersons.add(person);
					}
				}
				personViewModelPerson.setSearchedList(searchedPersons);

				if (searchedPersons.isEmpty()) {
					exportToPDF1(searchedName, "Person-Output1.pdf");

				}

				Iterable<Malicious> malicious = maliciousRepository.findAll();
				for (Malicious person : malicious) {
					if (person.getLine().toUpperCase().contains(searchedName.toUpperCase())) {
						maliciousPersons.add(person);
					}
				}
				personViewModelPerson.setMaliciousList(maliciousPersons);

				exportToPDF(searchedPersons, maliciousPersons, "Person-Output.pdf", searchedName);
			}

			return "person";

		/*
		 * case "Download": loadToDatabase("https://www.bis.doc.gov/dpl/dpl.txt");
		 * model.addAttribute("mesaj", "S-a realizat descarcarea"); return "person";
		 */

		case "Resetati cautarea":
			personViewModelPerson.setName("");
			return "person";

		case "Export PDF":
			try {
				downloadFile(response, "Person-Output.pdf");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "person";

		// case de export 1
		case "Export PDF2":
			try {
				downloadFile(response, "Person-Output1.pdf");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "person";

		case "Spre pagina Entity":
			return "redirect:/";

		default:
			return "person";
		}
	}

	// functie in cazul in care nu gaseste pe cineva
	private void exportToPDF1(String searchedName, String fileName) {

		Document pdfDoc1 = new Document(PageSize.A4, 35, 35, 100, 90);
		Font cellFontBold = FontFactory.getFont("Times Roman", 8, BaseColor.BLACK);
		cellFontBold.setStyle(Font.BOLD);

		Font textFont = FontFactory.getFont("Times Roman", 14, BaseColor.BLACK);
		textFont.setStyle(Font.BOLD);

		try {

			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
			PdfWriter.getInstance(pdfDoc1, new FileOutputStream(fileName)).setPageEvent(event);
			pdfDoc1.open();
			Paragraph title = new Paragraph(
					"Nicio informatie despre: " + searchedName + " in: https://www.bis.doc.gov/dpl/dpl.txt ", textFont);
			title.setSpacingBefore(60f);
			title.setSpacingAfter(30f);
			pdfDoc1.add(title);

		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		} finally {
			pdfDoc1.close();
		}

	}

	private void exportToPDF(List<Person> searchedPersons, List<Malicious> maliciousPersons, String fileName,
			String searchedName) {
		Document pdfDoc = new Document(PageSize.A4, 35, 35, 100, 90);

		Font cellFontBold = FontFactory.getFont("Times Roman", 8, BaseColor.BLACK);
		cellFontBold.setStyle(Font.BOLD);

		Font cellFont = FontFactory.getFont("Times Roman", 8, BaseColor.BLACK);
		Font textFont = FontFactory.getFont("Times Roman", 14, BaseColor.BLACK);
		textFont.setStyle(Font.BOLD);

		try {
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
			PdfWriter.getInstance(pdfDoc, new FileOutputStream(fileName)).setPageEvent(event);
			pdfDoc.open();

			Paragraph title = new Paragraph("Informatii despre: " + searchedName, textFont);
			title.setSpacingBefore(60f);
			title.setSpacingAfter(30f);
			pdfDoc.add(title);

			if (!searchedPersons.isEmpty()) {
				PdfPTable table = new PdfPTable(12);
				table.setWidths(new int[] { 10, 10, 5, 3, 3, 5, 5, 5, 3, 4, 8, 8 });
				table.setWidthPercentage(100);
				table.addCell(setCell(cellFontBold, "NAME"));
				table.addCell(setCell(cellFontBold, "STREET ADDRESS"));
				table.addCell(setCell(cellFontBold, "CITY"));
				table.addCell(setRotatedCell(cellFontBold, "STATE"));
				table.addCell(setRotatedCell(cellFontBold, "COUNTRY"));
				table.addCell(setRotatedCell(cellFontBold, "POSTAL CODE"));
				table.addCell(setRotatedCell(cellFontBold, "EFFECTIVE DATE"));
				table.addCell(setRotatedCell(cellFontBold, "EXPIRATION DATE"));
				table.addCell(setRotatedCell(cellFontBold, "STANDARD ORDER"));
				table.addCell(setRotatedCell(cellFontBold, "LAST UPDATE"));
				table.addCell(setCell(cellFontBold, "ACTION"));
				table.addCell(setRotatedCell(cellFontBold, "FR. CITATION"));

				for (Person person : searchedPersons) {
					for (String s : person.toString().split("###")) {
						table.addCell(setCell(cellFont, s));
					}
				}
				table.setHeaderRows(1);
				pdfDoc.add(table);
			}

			if (!maliciousPersons.isEmpty()) {
				PdfPTable table = new PdfPTable(1);
				table.setWidthPercentage(100);
				table.setSpacingBefore(30f);
				table.setSpacingAfter(30f);

				table.addCell(setCell(cellFontBold,
						"This search offer also some unformatted information for: " + searchedName));

				for (Malicious person : maliciousPersons) {
					Paragraph docTitle = new Paragraph(person.getLine(), cellFont);
					PdfPCell cell = new PdfPCell(docTitle);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
				}
				pdfDoc.add(table);
			}

		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		} finally {
			pdfDoc.close();
		}
	}

	private PdfPCell setCell(Font cellFontBold, String text) {
		Paragraph p = new Paragraph(text, cellFontBold);
		PdfPCell cell = new PdfPCell(p);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return cell;
	}

	private PdfPCell setRotatedCell(Font cellFontBold, String text) {
		PdfPCell cell = setCell(cellFontBold, text);
		cell.setRotation(90);
		return cell;
	}

	private void deleteDatabase() {

		personRepository.deleteAll();
		maliciousRepository.deleteAll();
	}

	void loadToDatabase(String linkForDenied) {
		URL deniedPersonListURL;
		BufferedReader in = null;
		URLConnection yc;

	if(	personRepository.count() != 0 )
		deleteDatabase();
		
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

	private MediaType getMediaTypeForFileName(String fileName) {
		String mineType = servletContext.getMimeType(fileName);

		try {
			MediaType mediaType = MediaType.parseMediaType(mineType);
			return mediaType;
		} catch (Exception e) {
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

	private void downloadFile(HttpServletResponse response, String fileName) throws IOException {
		MediaType mediaType = getMediaTypeForFileName(fileName);
		File file = new File(fileName);

		response.setContentType(mediaType.getType()); // Content-Type: application/pdf
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
		response.setContentLength((int) file.length());

		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());

		byte[] buffer = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		outStream.flush();
		outStream.close();
		inStream.close();
	}
}