package com.example.java_demo_test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.java_demo_test.entity.Book;
import com.example.java_demo_test.service.ifs.BookService;
import com.example.java_demo_test.vo.BookRequest;
import com.example.java_demo_test.vo.BookResponse;

@RestController
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@PostMapping("add_book")	// 書籍的新增、修改功能
	public BookResponse addBook(@RequestBody BookRequest bookRequest) {					
		return bookService.addBook(bookRequest);
	}
	
	@PostMapping("search_book_category_Containing")		// 書籍分類搜尋(一或多)
	public List<BookResponse> getBookByCategoryContaining(@RequestBody BookRequest bookRequest) {
		return bookService.getBookByCategoryContaining(bookRequest);	
	}
	
	@PostMapping("search_book_category")	// 書籍分類搜尋(一或多)
	public BookResponse getBookByCategor(@RequestBody BookRequest bookRequest) {	
		return bookService.getBookByCategory(bookRequest);
	}
	
	@PostMapping("search_book_by_title_or_author_or_isbn_to_consumer")	// 書籍搜尋(消費者)
	public List<BookResponse> getBookByTitleOrAuthorOrIsbnToConsumer(@RequestBody BookRequest bookRequest) {	
		return bookService.getBookByTitleOrAuthorOrIsbnToConsumer(bookRequest);	
	}
	
	@PostMapping("search_book_by_title_or_author_or_isbn_to_bookseller")	// 書籍搜尋(書商)
	public List<BookResponse> getBookByTitleOrAuthorOrIsbnToBookseller(@RequestBody BookRequest bookRequest) {	
		return bookService.getBookByTitleOrAuthorOrIsbnToBookseller(bookRequest);	
	}
	
	@PostMapping("update_book_inventory")	// 更新庫存資料
	public BookResponse getBookByUpdateInventory(@RequestBody BookRequest bookRequest) {	
		return bookService.updateInventory(bookRequest);	
	}
	
	@PostMapping("get_book_top5_by_sales")	//暢銷書排行榜(依照銷售量前5，排序)
	public List<Book> getBookTop5BySalesOrderBySalesDesc(@RequestBody BookRequest bookRequest) {	
		return bookService.getBookTop5BySalesOrderBySalesDesc();	 
	}
	
	@PostMapping("buy_book_to_update_sales")	//暢銷書排行榜(依照銷售量前5，排序)
	public List<BookResponse> buyBookToUpdateSales(@RequestBody BookRequest bookRequest) {	
		return bookService.buyBookToUpdateSales(bookRequest);	 
	}
	
	@PostMapping("buy_book")	//暢銷書排行榜(依照銷售量前5，排序)
	public BookResponse buyBook(@RequestBody BookRequest bookRequest) {	
		return bookService.buyBook(bookRequest);	 
	}

}
