package com.example.java_demo_test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.java_demo_test.entity.Book;
import com.example.java_demo_test.service.ifs.BookService;
import com.example.java_demo_test.vo.BookRequest;
import com.example.java_demo_test.vo.BookResponse;
import com.example.java_demo_test.vo.BookResponseA;

@CrossOrigin
@RestController
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@PostMapping("add_book")	// 書籍的新增功能
	public BookResponse addBook(@RequestBody BookRequest bookRequest) {					
		return bookService.addBook(bookRequest);
	}
	
	@PostMapping("replace_book")	// 書籍的修改功能
	public BookResponse replaceBookInfo(@RequestBody BookRequest bookRequest) {					
		return bookService.replaceBookInfo(bookRequest);
	}
	
	@PostMapping("search_book_category")		// 書籍分類搜尋(一或多)
	public List<BookResponse> searchBookByCategory(@RequestBody BookRequest bookRequest) {
		return bookService.searchBookByCategory(bookRequest);	
	}
	
	@PostMapping("get_book_category")	// 書籍分類搜尋(一或多)
	public BookResponse getBookByCategor(@RequestBody BookRequest bookRequest) {	
		return bookService.getBookByCategory(bookRequest);
	}
	
	@PostMapping("search_book_by_consumer")	// 書籍搜尋(消費者)
	public List<BookResponse> searchBookByConsumer(@RequestBody BookRequest bookRequest) {	
		return bookService.searchBookByConsumer(bookRequest);	
	}
	
	@PostMapping("search_book_by_bookseller")	// 書籍搜尋(書商)
	public List<BookResponseA> searchBookByBookseller(@RequestBody BookRequest bookRequest) {	
		return bookService.searchBookByBookseller(bookRequest);	
	}
	
	@PostMapping("search_book")	// 書籍搜尋(書商&消費者)
	public List<BookResponse> searchBook(@RequestBody BookRequest bookRequest) {	
		return bookService.searchBook(bookRequest);	
	}
	
	@PostMapping("update_book_info")	// 更新庫存資料
	public BookResponse updateBookInfo(@RequestBody BookRequest bookRequest) {	
		return bookService.updateBookInfo(bookRequest);	
	}
	
	
	@GetMapping("get_book_top5")	//暢銷書排行榜(依照銷售量前5，排序)
	public List<Book> getBookTop5() {	
		return bookService.getBookTop5();	 
	}
	
	@PostMapping("shop_book")	//購買書籍
	public List<BookResponse> orderBook(@RequestBody BookRequest bookRequest) {	
		return bookService.shopBook(bookRequest);	 
	}
	
	@PostMapping("order_book")	//購買書籍
	public BookResponse buyBook(@RequestBody BookRequest bookRequest) {	
		return bookService.orderBook(bookRequest);	 
	}
	
	@PostMapping("search_isbn")	// 找書
	public List<BookResponse> searchIsbn(@RequestBody BookRequest bookRequest) {	
		return bookService.searchIsbn(bookRequest);	 
	}
	

}
