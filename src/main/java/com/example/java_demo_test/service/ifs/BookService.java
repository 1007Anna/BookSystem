package com.example.java_demo_test.service.ifs;

import java.util.List;

import com.example.java_demo_test.entity.Book;
import com.example.java_demo_test.vo.BookRequest;
import com.example.java_demo_test.vo.BookResponse;

public interface BookService {
	
	// 書名、ISBN、作者、價格、庫存數量、銷售量、分類(一種書籍可能會有多種分類)的新增、修改功能
	public BookResponse addBook(BookRequest bookRequest);
	
	// 書籍分類搜尋(一或多)，只顯示書名、ISBN、作者、價格、庫存量
	public List<BookResponse> getBookByCategoryContaining(BookRequest bookRequest);
	
	// 書籍分類搜尋(一或多)，只顯示書名、ISBN、作者、價格、庫存量
	public BookResponse getBookByCategory(BookRequest bookRequest);

	// 書籍搜尋(消費者透過書名或ISBN或作者搜尋)，只顯示書名、ISBN、作者、價格
	public List<BookResponse> getBookByTitleOrAuthorOrIsbnToConsumer(BookRequest bookRequest);
	
	// 書籍搜尋(書商透過書名或ISBN或作者搜尋)，只顯示書名、ISBN、作者、價格
	public List<BookResponse> getBookByTitleOrAuthorOrIsbnToBookseller(BookRequest bookRequest);
	
	// 更新書籍資料
	public BookResponse updateInventory(BookRequest bookRequest);
	
	//暢銷書排行榜(依照銷售量取前5，排序) ，只顯示書名、ISBN、作者、價格
	public List<Book> getBookTop5BySalesOrderBySalesDesc();
	
	//書籍銷售(消費者可買多本，但至多3本，取前3)，只顯示書名、ISBN、作者、價格、購買數量，購買總價格
	public List<BookResponse>  buyBookToUpdateSales(BookRequest bookRequest);

	//書籍銷售(消費者可買多本，但至多3本，取前3)，只顯示書名、ISBN、作者、價格、購買數量，購買總價格
	public BookResponse buyBook(BookRequest bookRequest);
	
	
}
