package com.nikola;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;




import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Controller
public class AppController {
    private final GenerateFiles fileInfo;

    @Autowired
    public AppController(GenerateFiles fileInfo) {
        this.fileInfo = fileInfo;
    }

    @GetMapping("/")
    public String viewHomePage(org.springframework.ui.Model model) {
        return "index.html";
    }

    @GetMapping("/sortingPage")
    public String sortingPage() {
        return "sorting.html";
    }

    @GetMapping("/generateFilePage")
    public ResponseEntity<Object> downloadGeneratedFile() {
        fileInfo.generateFile();
        InputStream stream = new ByteArrayInputStream(fileInfo.getFileInfo().getBytes());
        InputStreamResource resource = new InputStreamResource(stream);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", "generatedFile.txt"));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/txt")).body(resource);
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("document") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
        long startTime = System.currentTimeMillis();
      

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

        System.out.println("Unsorted lines" + lines);

        // Sort the lines in descending order
        Collections.sort(lines, Collections.reverseOrder());

        System.out.println("Sorted lines: " + lines);

       
        // Stop the stopwatch after sorting and saving
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        redirectAttributes.addFlashAttribute("message", "Calculation time from upload->sorting->done file is: " + elapsedTime + " milliseconds.");
        return "redirect:/sortingPage";
    }

    
}
