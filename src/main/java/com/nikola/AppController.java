package com.nikola;

import com.nikola.GenerateFiles;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.io.BufferedReader;
import java.io.InputStreamReader;


@Controller
public class AppController {
	 private final GenerateFiles fileInfo;
	@Autowired
	private DocumentRepository documentRepository;
	
	//allow users to view the home page
	public AppController(GenerateFiles fileInfo) {
        
        this.fileInfo = fileInfo;
    }
	
	@GetMapping("/")
	public String viewHomePage(org.springframework.ui.Model model) {//we set this list collection to the model so we can access it in the view using thymeleaf
		//we list the documents from the database with the overwritten function findAll()
		List<Document> listDocuments=documentRepository.findAll();
		model.addAttribute("listDocuments", listDocuments);
		
		
		return "index.html";
		
	}
	
	@GetMapping("/sortingPage")
	public String sortingPage() {

		return "sorting.html";
	}
	
	
	
	@GetMapping("/generateFilePage")
	   public ResponseEntity<Object> downloadGeneratedFile()  {
        fileInfo.generateFile();
        InputStream stream = new ByteArrayInputStream(fileInfo.getFileInfo().getBytes());
        InputStreamResource resource = new InputStreamResource(stream);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", "generatedFile.txt"));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok().headers(headers).contentType(
        MediaType.parseMediaType("application/txt")).body(resource);
    }
	
	
	
	
	//koga ke se upload povikaj go ovoj endpoint /upload i posle redirektiraj do sortingPage
	//za da go capture file-ot sto ke go uploadneme koristime @RequestParam
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("document") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException{
		String fileName=  org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename()); //we get the file name
	
		//we create a new document
		Document document=new Document();
		document.setName(fileName);
		//document.setContent(multipartFile.getBytes());
		document.setSize(multipartFile.getSize());
		
		
		//------------------!!!---------------------
		 
		
		  // Read the lines from the uploaded file
	    List<String> lines = new ArrayList<>();
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	          lines.add(line);
	        }
	      } catch (IOException e) {
	        return "redirect:/error"; // Handle file reading error
	      }
	    
	 // Sort the lines in descending order
	    Collections.sort(lines, Collections.reverseOrder());
	    
		Date uploadTime=new Date();
		
		document.setUploadTime(uploadTime);
		
	    // Save the sorted lines to the database
	    for (String line : lines) {
	   // 	int i=1;
	   // System.out.println("i= "+i);
	   // i++;
	      document.setContent(line.getBytes());
	   
	    }
	    documentRepository.save(document);

		
		//saving the file
	//	documentRepository.save(document);
		//display successfull message
		
		//1. implement SOrting:
		//2. implement calculation from strating to upload to finish (processing time for sorting) and display the result on screen
		//like System.out.println("Calculation time from upload->sorting->Done is: "+calculation_time);
		
		redirectAttributes.addFlashAttribute("message", "Calculation time from upload->sorting->done file is: "+uploadTime);
		
		//TODO:Implement sorting while uploading and save the file
		return "redirect:/sortingPage";
	}
	
	
	
	
	
	
	
	@GetMapping("/download") //download the file passed by id
	public void downloadSortedFile(@Param("id")Long id, HttpServletResponse httpServletResponse) throws Exception {//we need httpServletResponse so we can 
		//send the binary data of the document i.e. to the browser for the browser client to download the file
	Optional<Document> result=	documentRepository.findById(id);//we find the id of the document
	
	if(!result.isPresent()) {//document doesnt exist
		throw new Exception("The document with that id: "+result+"is not found");
	}
	else {
		//we continue to process the content of the file and send the binary data to the browser
		
		
		Document document=result.get();
		httpServletResponse.setContentType("application/octet-stream");
		String headerKey ="Content-Disposition";//we set the header key
		String headerValue="attachment; filename="+document.getName();//we set the header value
		
		httpServletResponse.setHeader(headerKey, headerValue);//we set the header key and header value to the httpServletResponse 
		//so the browser can understand the file
		
ServletOutputStream servletOutputStream=httpServletResponse.getOutputStream();//to send the data we need outputStream
		
		
	servletOutputStream.write(document.getContent());//we write the document bytes to the output stream
	servletOutputStream.close();//we close the output stream
	}
	
	
	
	
	
	
	}

}
