package com.example.java_demo_test.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.java_demo_test.entity.Book;
import com.example.java_demo_test.repository.BookDao;
import com.example.java_demo_test.service.ifs.BookService;
import com.example.java_demo_test.vo.BookRequest;
import com.example.java_demo_test.vo.BookResponse;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookDao bookDao;
	BookResponse bookResponse = new BookResponse();

	@Override
	public BookResponse addBook(BookRequest bookRequest) {
		if (!StringUtils.hasText(bookRequest.getTitle()) || !StringUtils.hasText(bookRequest.getAuthor())
				|| !StringUtils.hasText(bookRequest.getIsbn()) || !StringUtils.hasText(bookRequest.getCategory())) {
			return new BookResponse(bookRequest.getTitle(), "書名、作者、ISBN、類別不得為空");
		}
		if (bookRequest.getPrice() < 0 || bookRequest.getInventory() <= 0 || bookRequest.getSales() <= 0) {
			return new BookResponse(bookRequest.getTitle(), "價錢、銷售量、庫存不得為0");
		}
//		if (bookDao.existsById(bookRequest.getIsbn())) {
//			return new BookResponse(bookRequest.getIsbn(), "此書已存在");
//		}
		Book book = new Book(bookRequest.getTitle(), bookRequest.getAuthor(), bookRequest.getIsbn(),
				bookRequest.getPrice(), bookRequest.getInventory(), bookRequest.getSales(), bookRequest.getCategory());

		bookDao.save(book);
		return new BookResponse(book.getTitle(), book.getIsbn(), "新增書單成功");

	}

	@Override
	public List<BookResponse> getBookByCategoryContaining(BookRequest bookRequest) {

		List<BookResponse> bookByCategoryList = new ArrayList<>();
		List<BookResponse> bookByCategoryContainingList = new ArrayList<>();
		// 先從Request內取得分類清單(List)
		List<String> categoryList = bookRequest.getCategoryList();
		if (CollectionUtils.isEmpty(categoryList)) {
			BookResponse bookResponse = new BookResponse();
			bookResponse.setMessage("類別欄不得為空");
			bookByCategoryList.add(bookResponse);
			return bookByCategoryList;
		}
		Set<Book> responseBookList = new HashSet<>();
		// 從清單內搜尋分類(文學、奇幻....)
		for (String item1 : categoryList) {
			// 取得符合 item1 內種類的書籍清單(每次搜尋都會覆蓋前面儲存的資料)
			List<Book> bookList = bookDao.findByCategoryContaining(item1);
			// 判斷>搜出來的bookList內的資料是否在responseBookList內，沒有就放進去(用foreach做)
			if (!CollectionUtils.isEmpty(bookList)) {
				responseBookList.addAll(bookList);
			}
		}
		for (Book item2 : responseBookList) {
			BookResponse bookResponse = new BookResponse(item2.getTitle(), item2.getAuthor(), item2.getIsbn(),
					item2.getPrice(), item2.getInventory());
			bookByCategoryContainingList.add(bookResponse);
		}
		return bookByCategoryContainingList;
	}

	@Override
	public BookResponse getBookByCategory(BookRequest bookRequest) {

		// 建一個清單來接最後要回傳的書籍資料
		List<Book> responseBookList = new ArrayList<>();

		// 先從Request內取得分類清單(List)
		List<String> requestCategoryList = bookRequest.getCategoryList();
		
		if (CollectionUtils.isEmpty(requestCategoryList)) {
			bookResponse.setMessage("輸入分類不得為空");
			return bookResponse;
		}
		// 從清單內搜尋分類資料(文學、奇幻....)
		for (String item : requestCategoryList) {
			if (!StringUtils.hasText(item)) {
				bookResponse.setMessage("請輸入文字");
				return bookResponse;
			}
			// 建一個清單來接書籍資料(Book類別)
			List<Book> categoryList = bookDao.findByCategoryContaining(item); // 後蓋前，所以 後 會等於 前的型別
			// 若輸入的資訊為111(非分類名稱)，則跳過後進到下一圈搜尋
			if (CollectionUtils.isEmpty(categoryList)) {
				continue;
			}
			// 判斷搜出來的categoryList內的資料是否在responseCategoryList內，沒有就放進去
			for (Book bookItem : categoryList) {
				// 取categoryList裡的書名、作者、ISBN、價錢、銷售量的資料
				Book book = new Book(bookItem.getTitle(), bookItem.getAuthor(), bookItem.getIsbn(), bookItem.getPrice(),
						bookItem.getInventory());
				// 將需要的書籍資料丟進responseBookList(書籍清單)裡面
				responseBookList.add(book);
			}
		}

		// 防呆：若For迴圈跑完後，responseBookList內無資料，則回傳無此書籍
		if (CollectionUtils.isEmpty(responseBookList)) {
			bookResponse.setMessage("無此書籍");
			return bookResponse;
		}

		return new BookResponse(responseBookList);
	}

	@Override
	public List<BookResponse> getBookByTitleOrAuthorOrIsbnToConsumer(BookRequest bookRequest) {
		// 從Request內取得搜尋書籍用的值(參數)
		String searchText = bookRequest.getSearch();
		// 防呆：如果searchText的值為null或空字串，則回傳"輸入關鍵字不得為空"
		if (!StringUtils.hasText(searchText)) {
			bookResponse.setMessage("輸入關鍵字不得為空");
			List<BookResponse> searchTextList = new ArrayList<>();
			searchTextList.add(bookResponse);
			return searchTextList;
		}
		// 建一個List存放從資料庫搜尋比對後的資料
		List<Book> bookList = bookDao.findByTitleOrAuthorOrIsbn(searchText, searchText, searchText);
		// 將bookList符合條件的資料取出，並存放在responseBookList
		List<BookResponse> responseBookList = new ArrayList<>();
		for (Book item : bookList) {
			BookResponse bookResponse = new BookResponse(item.getTitle(), item.getAuthor(), item.getIsbn(),
					item.getPrice());
			responseBookList.add(bookResponse);
		}
		// 防呆：若輸入的關鍵字與資料庫資料不符，則回傳"無此書籍"
		if (CollectionUtils.isEmpty(responseBookList)) {
			bookResponse.setMessage("無此書籍");
			List<BookResponse> errorTextList = new ArrayList<>();
			errorTextList.add(bookResponse);
			return errorTextList;
		}
		return responseBookList;
	}

	@Override
	public List<BookResponse> getBookByTitleOrAuthorOrIsbnToBookseller(BookRequest bookRequest) {
		// 從Request內取得搜尋書籍用的值(參數)
		String searchText = bookRequest.getSearch();
		// 防呆：如果searchText的值為null或空字串，則回傳"輸入關鍵字不得為空"
		if (!StringUtils.hasText(searchText)) {
			bookResponse.setMessage("輸入關鍵字不得為空");
			List<BookResponse> searchTextList = new ArrayList<>();
			searchTextList.add(bookResponse);
			return searchTextList;
		}
		// (如果search不是空字串)建一個List存放從資料庫搜尋比對後的資料(搜尋符合搜尋條件的書籍列表)
		List<Book> bookList = bookDao.findByTitleOrAuthorOrIsbn(searchText, searchText, searchText);
		// 將bookList符合條件的資料取出，並存放在responseBookList
		List<BookResponse> responseBookList = new ArrayList<>();
		for (Book item : bookList) {
			BookResponse bookResponse = new BookResponse(item.getTitle(), item.getAuthor(), item.getIsbn(),
					item.getPrice(), item.getInventory(), item.getSales());
			responseBookList.add(bookResponse);
		}
		// 防呆：若輸入的關鍵字與資料庫資料不符，則回傳"無此書籍"
		if (CollectionUtils.isEmpty(responseBookList)) {
			bookResponse.setMessage("無此書籍");
			List<BookResponse> errorTextList = new ArrayList<>();
			errorTextList.add(bookResponse);
			return errorTextList;
		}
		return responseBookList;
	}

	@Override
	public BookResponse updateInventory(BookRequest bookRequest) {

		// 判斷傳入的BookRequest物件中的ISBN 或 庫存和價錢是否為空 ; 如果是，就設置一個錯誤訊息並返回。
		if (!StringUtils.hasText(bookRequest.getIsbn())
				|| (bookRequest.getUpdateInventory() == null && bookRequest.getUpdatePrice() == null)) {
			bookResponse.setMessage("ISBN不得為空");
			return bookResponse;
		}
		// 使用bookDao.findById方法查詢資料庫裡的ISBN的資料，並將資料存入Optional<Book>類別的optional物件中。
		Optional<Book> optional = bookDao.findById(bookRequest.getIsbn());
		// 判斷optional裡的ISBN是否存在 ; 如果不存在，就設置一個錯誤訊息並返回。
		if (!optional.isPresent()) {
			bookResponse.setMessage("無此書籍");
			return bookResponse;
		}
		// 如果optional裡的ISBN資料存在，從Optional取出資料存入book
		Book book = optional.get();
		// 更新Book物件的inventory和price，並將其儲存回資料庫
		book.setInventory(bookRequest.getUpdateInventory());
		book.setPrice(bookRequest.getUpdatePrice());
		Book newBook = bookDao.save(book);
		bookResponse = new BookResponse(newBook.getTitle(), newBook.getAuthor(), newBook.getIsbn(), newBook.getPrice(),
				newBook.getInventory(), "更新成功");
		return bookResponse;
	}

	@Override
	public List<Book> getBookTop5BySalesOrderBySalesDesc() {

		// 建立一個空的bookList，用來儲存搜尋到的書籍資料
		List<Book> bookList = new ArrayList<>();
		// 使用bookDao.findTop5ByOrderBySalesDesc()方法查詢資料庫，並將回傳的資料存入responseSalesList
		List<Book> responseSalesList = bookDao.findTop5ByOrderBySalesDesc();
		// 搜尋responseSalesList內的銷售資料，並將其加入到bookList裡
		for (Book item : responseSalesList) {
			Book newbook = new Book(item.getTitle(), item.getAuthor(), item.getIsbn(), item.getPrice());
			bookList.add(newbook);
		}
		return bookList;
	}

	@Override
	public List<BookResponse> buyBookToUpdateSales(BookRequest bookRequest) {

		int bookTotlePrice = 0;
		// 建立一個bookMap，來取bookRequest內的ISBN和ordered的資料
		Map<String, Integer> bookMap = bookRequest.getBookMap();
		for (Entry<String, Integer> item : bookMap.entrySet()) {
			// 取ISBN
			String itemKey = item.getKey();
			// 判斷從資料庫抓出的itemKey的資料是否存在，若無則回報錯誤
			if(!bookDao.existsById(itemKey)) {
				bookResponse.setMessage("書碼輸入錯誤");
				List<BookResponse> itemKeyList = new ArrayList<>();
				itemKeyList.add(bookResponse);
				continue;
			}
			
			// 設一個bookOpt來搜尋Book裡的ISBN
			Optional<Book> bookOpt = bookDao.findById(itemKey);
			Book newbook = bookOpt.get();
			
			// 設一個price取價格
			int price = newbook.getPrice();
			if(item.getValue() <= 0) {
				bookResponse.setMessage("數量輸入錯誤");
				List<BookResponse> itemValueList = new ArrayList<>();
				itemValueList.add(bookResponse);
				continue;
			}			
			int totleprice = price * item.getValue();
			bookTotlePrice += totleprice;
		}

		// 取ISBN並更改資料
		List<Book> bookList = bookDao.findAllById(bookMap.keySet());
		if (CollectionUtils.isEmpty(bookList)) {
			bookResponse.setMessage("無此書籍");
			List<BookResponse> newBookList = new ArrayList<>();
			newBookList.add(bookResponse);
			return newBookList;
		}
		List<Book> saveBookList = new ArrayList<>();
		List<BookResponse> responseBookList = new ArrayList<>();
		for (Book item : bookList) {
			item.setInventory(item.getInventory() - bookMap.get(item.getIsbn()));
			item.setSales(item.getSales() + bookMap.get(item.getIsbn()));
			saveBookList.add(item);
		}
		// 將所有需要保存到資料庫的書籍資料存入資料庫
		List<Book> saveBooks = bookDao.saveAll(saveBookList);
		for(Book bookItem : saveBooks) {
			BookResponse newBookResponse = new BookResponse(bookItem.getTitle(), bookItem.getAuthor(), 
					bookItem.getIsbn(),bookItem.getPrice(), bookRequest.getOrdered(), bookTotlePrice);
			responseBookList.add(newBookResponse);
		}		
		return responseBookList;
	}

	@Override
	public BookResponse buyBook(BookRequest bookRequest) {
		
		// 宣告一個整數變數(bookTotlePrice)，並初始化為0，這個變數用於存儲書籍的總價格
		int bookTotlePrice = 0;
		// 建立一個bookMap，用來儲存從BookRequest類別內獲取的ISBN和訂購數量的資料
		Map<String, Integer> bookMap = bookRequest.getBookMap();
		// 使用 foreach 迴圈遍歷 "bookMap" 中的每一個key跟value，並存入"item"
		for (Entry<String, Integer> item : bookMap.entrySet()) {
			// 從"item"中獲取書籍的ISBN，並存入"itemKey"
			String itemKey = item.getKey();
//			if(item.getValue() <= 0) {
//				bookResponse.setMessage("數量輸入錯誤");
//				continue;
//			}
			// 判斷從資料庫抓出的itemKey的資料是否存在，若無則回報錯誤
			if(!bookDao.existsById(itemKey)) {
				bookResponse.setMessage("書碼輸入錯誤");		
				// 比對多筆資料，若其中一筆為正確就繼續執行
				continue;
			}
			
			// 從資料庫中搜尋ISBN的書籍資料，並存入"bookOpt"
			Optional<Book> bookOpt = bookDao.findById(itemKey);
			// 將"bookOpt"裡的資料取出，並存入"newbook"
			Book newbook = bookOpt.get();
			// 取得該書籍的價格
			int price = newbook.getPrice();	
			// 計算該書籍的總金額 (價格 * 訂購數量)
			int totlePrice = price * item.getValue();
			// 累加書籍總金額
			bookTotlePrice += totlePrice;
		}
		// 從資料庫中查詢所有書籍的訂購資訊(取ISBN對應的值)
		List<Book> bookList = bookDao.findAllById(bookMap.keySet());
		for(Book bookListItem : bookList) {
			if(bookMap.get(bookListItem.getIsbn()) < 1) {
				bookResponse.setMessage("數量不得小於1");
				return bookResponse;
			}
		}
		// 判斷查詢結果是否為空，若為空，回傳錯誤訊息
		if (CollectionUtils.isEmpty(bookList)) {
			bookResponse.setMessage("請輸入正確書碼");
			return bookResponse;
		}
		// 建立一個"saveBookList"來儲存需要更新至資料庫的書籍訂購資訊
		List<Book> saveBookList = new ArrayList<>();
		// 使用foreach迴圈遍歷查詢的每一筆書籍
		for (Book item : bookList) {
			// 更新書籍庫存量
			item.setInventory(item.getInventory() - bookMap.get(item.getIsbn()));
			// 更新書籍銷售量
			item.setSales(item.getSales() + bookMap.get(item.getIsbn()));
			// 存入saveBookList
			saveBookList.add(item);
		}
		List<Book> responseBookList = new ArrayList<>();
		// 將所有需要保存到資料庫的書籍資料存入資料庫
		List<Book> saveBooks = bookDao.saveAll(saveBookList);
		for(Book bookItem : saveBooks) {
			Book book = new Book(bookItem.getTitle(), bookItem.getAuthor(), bookItem.getIsbn(),
					bookItem.getPrice(), bookMap.get(bookItem.getIsbn()));
			responseBookList.add(book);
		}		
		return new BookResponse(responseBookList,bookTotlePrice);
	}
	

}
