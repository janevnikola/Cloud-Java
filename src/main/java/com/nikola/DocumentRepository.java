package com.nikola;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//Repositoy to access the database and work with

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>{
	
	//we list the documents
	//we overwrite the findAll() method which is implemented default by Spring Data JPA
	@Query("SELECT new Document(document.id,document.name) FROM Document document ORDER BY document.uploadTime DESC")
	List<Document> findAll();
	
	

}
