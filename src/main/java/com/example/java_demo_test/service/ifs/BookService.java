package com.example.java_demo_test.service.ifs;

import java.util.List;

import com.example.java_demo_test.entity.Book;
import com.example.java_demo_test.vo.BookRequest;
import com.example.java_demo_test.vo.BookResponse;
import com.example.java_demo_test.vo.BookResponseA;

public interface BookService {
	
	// 書籍データの追加と改正する機能
	// 書名、ISBN、作者、價格、庫存數量、銷售量、分類(一種書籍可能會有多種分類)的新增、修改功能
	public BookResponse addBook(BookRequest bookRequest);
	
	public BookResponse replaceBookInfo(BookRequest bookRequest);
	
	// 種類で書籍を検索する機能(書籍タイトル、ISBN、作者、価格、在庫を示す)
	// 書籍分類搜尋(一或多)，只顯示書名、ISBN、作者、價格、庫存量
	public List<BookResponse> searchBookByCategory(BookRequest bookRequest);
	
	// 書籍分類搜尋(一或多)，只顯示書名、ISBN、作者、價格、庫存量
	public BookResponse getBookByCategory(BookRequest bookRequest);

	// 消費者に向けて書籍を検索する機能(書籍タイトル、ISBN、作者、価格を示す)
	// 書籍搜尋(消費者透過書名或ISBN或作者搜尋)，只顯示書名、ISBN、作者、價格
	public List<BookResponse> searchBookByConsumer(BookRequest bookRequest);
	
	// 業者に向けて書籍を検索する機能(書籍タイトル、ISBN、作者、価格、在庫量、販売量を示す)
	// 書籍搜尋(書商透過書名或ISBN或作者搜尋)，只顯示書名、ISBN、作者、價格、庫存、銷售
	public List<BookResponseA> searchBookByBookseller(BookRequest bookRequest);
	
	// 書籍を検索する機能(書籍タイトル、ISBN、作者、価格、在庫量、販売量を示す)
	public List<BookResponse> searchBook(BookRequest bookRequest);
	
	// 書籍の在庫数、価格の更新
	// 更新書籍資料(價錢、庫存)
	public BookResponse updateBookInfo(BookRequest bookRequest);
	
	// ランキングTOP5の書籍を検索する機能
	// 暢銷書排行榜(依照銷售量取前5，排序) ，只顯示書名、ISBN、作者、價格
	public List<Book> getBookTop5();
	
	// 書籍の購入する機能
	// 書籍銷售(消費者可買多本)，只顯示書名、ISBN、作者、價格、購買數量，購買總價格
	public List<BookResponse> shopBook(BookRequest bookRequest);

	// 書籍銷售(消費者可買多本)，只顯示書名、ISBN、作者、價格、購買數量，購買總價格
	public BookResponse orderBook(BookRequest bookRequest);
	
	// 書籍搜尋(利用ISBN搜尋)
	public List<BookResponse> searchIsbn(BookRequest bookRequest);
	
	
}
