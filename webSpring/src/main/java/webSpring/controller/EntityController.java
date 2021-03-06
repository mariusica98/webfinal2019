package webSpring.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import webSpring.entity.PDF;
import webSpring.model.PdfViewModel;
import webSpring.repository.PDFRepository;

@Controller
public class EntityController {

	@Autowired
	private PDFRepository pdfRepository;

	@GetMapping("/")
	public String greetingForm(Model model, HttpServletResponse response) {
		model.addAttribute("pdfViewModel", new PdfViewModel());

		return "entity";
	}

	@PostMapping("/")
	public String formProcess(Model model, PdfViewModel pdfViewModel, @RequestParam("submit") String reqParam,
			@RequestParam(name = "searchedWord", required = false) String searchedWord, HttpServletResponse response) {

		List<String> searchedText;

		switch (reqParam) {
		case "Cautare":

			if (!searchedWord.isEmpty()) {
				searchedWord = searchedWord.substring(0, 1).toUpperCase() + searchedWord.substring(1);
				searchedText = this.search(this.getTextFromDatabase(), searchedWord);
			} else {
				return "entity";
			}
				
			pdfViewModel.setResult(searchedText.toString());
			pdfViewModel.setResults(searchedText);
			model.addAttribute("pdfViewModel", pdfViewModel);
			model.addAttribute("SearchedWord", searchedWord);

			if (searchedText.isEmpty()) {
				model.addAttribute("mesaj1", "No entity found");
				exportToPDF2(searchedWord, "Entity-Output11.pdf");
			} else
				model.addAttribute("mesaj1", "entity found");

			exportToPDF(searchedText, "Entity-Output.pdf", searchedWord);
			return "entity";

		case "Resetati cautarea":
			return "entity";

		case "Export PDF":
			try {

				downloadFile(response, "Entity-Output.pdf");

			} catch (IOException e) {
				e.printStackTrace();
			}
			return "entity";
			
		case "Export PDF2":
			try {
				downloadFile(response, "Entity-Output11.pdf");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "person";

		case "Spre pagina Person":
			return "redirect:/person";

//		case "Download":
//			String text = this.getText();
//
//			Iterable<PDF> pdfs = pdfRepository.findAll();
//			List<PDF> pdfList = new ArrayList<>();
//
//			for (PDF pdf : pdfs) {
//				pdfList.add(pdf);
//			}
//
//			if (pdfList.size() == 0) {
//				PDF pdf = new PDF();
//				pdf.setText(text);
//				pdfRepository.save(pdf);
//			}
//
//			model.addAttribute("mesaj2", "S-a realizat descarcarea");
//			return "entity";

		default:
			return "entity";
		}
	}

	private List<String> search(List<String> list2, String cuvantcautat) {

		List<String> entries = new ArrayList<>();
		List<String> finalText = new ArrayList<>();

		if (!cuvantcautat.isEmpty()) {

			for (int i = 0; i < list2.size(); i++) {
				if (list2.get(i).contains(cuvantcautat)) {
					String entry = "";

					for (int j = 0; j < 36; j++) {
						finalText.add(list2.get(j + i));
						entry = entry + " " + list2.get(j + i);
					}
					entries.add(entry);
				}
			}
		} else {
			return null;
		}
		return entries;
	}

	private List<String> parseTextfromDatabase(String text) {
		List<String> words = new ArrayList<>(Arrays.asList(text.split(" ")));
		return words;
	}

	private List<String> getTextFromDatabase() {
		Iterable<PDF> allText = pdfRepository.findAll();

		StringBuilder finalText = new StringBuilder();
		for (PDF pdf : allText) {
			finalText.append(pdf);
		}
		String result = finalText.toString();
		List<String> list = parseTextfromDatabase(result);

		return list;
	}

//	private String getText() {
//
//		String text = "";
//		PDDocument document;
//
//		try {
//			document = PDDocument.load(new File("src/main/resources/static/pdf/entity.pdf"));
//
//			if (!document.isEncrypted()) {
//				PDFTextStripper stripper = new PDFTextStripper();
//				text = stripper.getText(document);
//				// System.out.println("Text:" + text);
//
//			}
//			document.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return text;
//
//	}
	
	//functie cand nu gaseste entitate
	private void exportToPDF2(String searchedWord, String fileName) {

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
					"Nicio informatie despre: " + searchedWord + "in entity.pdf ", textFont);
			title.setSpacingBefore(60f);
			title.setSpacingAfter(30f);
			pdfDoc1.add(title);

		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		} finally {
			pdfDoc1.close();
		}

	}
	

	private void exportToPDF(List<String> result, String fileName, String searchedName) {
		Document pdfDoc = new Document(PageSize.A4, 34, 34, 100, 90);

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

			if (!result.isEmpty()) {
				PdfPTable table = new PdfPTable(1);
				table.setWidths(new int[] { 5 });
				table.setWidthPercentage(75);
				table.addCell(setCell(cellFontBold, "ENTITY"));

				List<String> listToShow = new ArrayList<>();

				for (int i = 0; i < result.size(); i++) {
					String word = result.get(i).replace("\r\n", " ");
					listToShow.add(word);
					table.addCell(setCell(cellFont, word));
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

	private static MediaType getMediaTypeForFileName(String fileName) {

//		String mineType = servletContext.getMimeType(fileName);
		String mimeType = "application/pdf";

		try {
			MediaType mediaType = MediaType.parseMediaType(mimeType);
			return mediaType;
		} catch (Exception e) {
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

	public static void downloadFile(HttpServletResponse response, String fileName) throws IOException {

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