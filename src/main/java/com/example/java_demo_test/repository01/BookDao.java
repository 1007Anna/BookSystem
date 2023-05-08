package com.example.java_demo_test.repository01;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.java_demo_test.entity.Book;


@Repository	
public interface BookDao extends JpaRepository<Book, String>{
	
	List<Book> findByCategoryContaining(String category);
	
	List<Book> findByTitleOrAuthorOrIsbn(String title, String author, String isbn);
	
	List<Book> findByIsbn(String isbn);

	List<Book> findTop5ByOrderBySalesDesc();
	

}
