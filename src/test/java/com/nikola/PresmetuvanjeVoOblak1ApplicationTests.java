package com.nikola;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;



@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PresmetuvanjeVoOblak1ApplicationTests {
	
	@Autowired
	private DocumentRepository documentRepository;

	//za testing purposes koristime testEntity Manager koja doagja od Spring DataJpa test
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	@Rollback(false)
	void testInsertDocument() throws IOException{
	
		//morame da procitame od file koj sto e na nasiot kompjuter
		File file=new File("C:\\Users\\user\\Desktop\\kucinja_familyTree.pdf");
		
		//kreirame document object
		Document document=new Document();

	//citame content od nasiot file vo bytes i da gi setirame bytes vo document objectot
		document.setName(file.getName());
		
byte [] bytesFile= Files.readAllBytes(file.toPath());
document.setContent(bytesFile);
long fileSize=bytesFile.length;
document.setSize(fileSize);
document.setUploadTime(new java.sql.Date(fileSize));


   Document savedDocument = documentRepository.save(document);//go zacuvuvame dokumentot so documentRepository.save i go dodavame vo savedDocument 
   //varijabla
   
   //koristime testEntityManager da go retreive document object bazirano na primary key
Document existDocument=testEntityManager.find(Document.class, savedDocument.getId());
	
	
	
	
	assertThat(existDocument.getSize()).isEqualTo(fileSize);
	
	}
}
