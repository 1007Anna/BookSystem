package com.example.java_demo_test.service.impl;

import static com.example.java_demo_test.constant.Message.ERROR_BOOKINFO;
import static com.example.java_demo_test.constant.Message.ERROR_ISBN;
import static com.example.java_demo_test.constant.Message.ERROR_VALUE;
import static com.example.java_demo_test.constant.Message.SUCCESS_ADD;
import static com.example.java_demo_test.constant.Message.EXIST_ISBN;
import static com.example.java_demo_test.constant.Message.SUCCESS_REPLACE;
import static com.example.java_demo_test.constant.Message.WRONG_FORMAT;

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
import com.example.java_demo_test.repository01.BookDao;
import com.example.java_demo_test.service.ifs.BookService;
import com.example.java_demo_test.vo.BookRequest;
import com.example.java_demo_test.vo.BookResponse;
import com.example.java_demo_test.vo.BookResponseA;

@Service
public class BookServiceImpl implements BookService {
	

//	----------------------新增書籍----------------------
	
	@Autowired
	private BookDao bookDao;
	BookResponse bookResponse = new BookResponse();
	BookResponseA bookResponseA = new BookResponseA();

	@Override
	public BookResponse addBook(BookRequest bookRequest) {
		
		// 書籍のタイトル、作者、ISBN、種類のバリューがヌルした場合、エラーメッセージを返す
		// 確認書名、作者、ISBN、分類是否為空，若其中有一項是空的，就回傳新增失敗， 用BookResponse格式回傳
		if (!StringUtils.hasText(bookRequest.getTitle()) ||
				!StringUtils.hasText(bookRequest.getAuthor()) ||
				!StringUtils.hasText(bookRequest.getIsbn()) || 
				!StringUtils.hasText(bookRequest.getCategory())) {
			return new BookResponse(ERROR_BOOKINFO);
		}

		// もし書籍の価格、在庫量、販売量の数値がゼロ又はマイナスなら、エラーメッセージを返す
		// 確認價格、銷售量、庫存是否為 0 或負數，若其中有一項是，就回傳新增失敗， 用BookResponse格式回傳
		if (bookRequest.getPrice() < 1 || 
				bookRequest.getInventory() < 1 || 
				bookRequest.getSales() < 1) {
			return new BookResponse(ERROR_VALUE);
		}
		
		// 檢查資料庫中是否已存在指定ISBN的書籍，如果書籍已存在，則回傳包含ISBN和錯誤訊息的BookResponse物件
		if (bookDao.existsById(bookRequest.getIsbn())) {
			return new BookResponse(bookRequest.getIsbn(), EXIST_ISBN);
		}

		// 新しいBookクラスを作り、BookRequestクラスから書籍の資料を取得し、bookに保存する
		// 建立一個新的Book物件，並從BookRequest裡取得書名、作者、ISBN、價錢、庫存、銷售量和分類，並存入book
		Book book = new Book(bookRequest.getTitle(), bookRequest.getAuthor(), bookRequest.getIsbn(),
				bookRequest.getPrice(), bookRequest.getInventory(), bookRequest.getSales(), bookRequest.getCategory());
		// bookのデータをbookDaoに保存する
		// 將book裡的資料傳入bookDao並存儲
		bookDao.save(book);

		// タイトル、ISBN、および追加に成功したメッセージをBookResponseオブジェクトに返す
		// 回傳一個BookResponse物件，內容是新增成功的訊息
		return new BookResponse(SUCCESS_ADD);

	}
	
//	----------------------修改書籍----------------------
	
	@Override
	public BookResponse replaceBookInfo(BookRequest bookRequest) {
		
		if (!StringUtils.hasText(bookRequest.getTitle()) || 
				!StringUtils.hasText(bookRequest.getAuthor()) ||
				!StringUtils.hasText(bookRequest.getIsbn()) ||
				!StringUtils.hasText(bookRequest.getCategory()) ||
				bookRequest.getPrice() == null || 
				bookRequest.getInventory() == null ||
				bookRequest.getSales() == null) {
			return new BookResponse(ERROR_BOOKINFO);
		}
		
		Optional<Book> bookOpt = bookDao.findById(bookRequest.getIsbn());
		
		if(!bookOpt.isPresent()) {
			return new BookResponse(ERROR_ISBN);
		}
		
		Book bookInfo = bookOpt.get();
		
		bookInfo.setTitle(bookRequest.getTitle());
		bookInfo.setAuthor(bookRequest.getAuthor());
		bookInfo.setIsbn(bookRequest.getIsbn());
		bookInfo.setPrice(bookRequest.getPrice());
		bookInfo.setInventory(bookRequest.getInventory());
		bookInfo.setSales(bookRequest.getSales());
		bookInfo.setCategory(bookRequest.getCategory());
		
		bookDao.save(bookInfo);
		
		return new BookResponse(SUCCESS_REPLACE);
	}
	
//	----------------------分類查詢(回傳List<BookResponse>物件)----------------------

	@Override
	public List<BookResponse> searchBookByCategory(BookRequest bookRequest) {

		// 検索条件に一致する書籍データを作ったリストに保存する
		// 建立一個List，用來儲存符合搜尋條件的書籍清單
		List<BookResponse> bookInfoList = new ArrayList<>();

		// 検索条件に一致する書籍データを作ったリストに保存する
		List<BookResponse> ResponseBookList = new ArrayList<>();

		// Requestから種類のデータを取得し、categoryListに保存する
		// 從Request內取得分類清單，並存入"categoryList"
		List<String> categoryList = bookRequest.getCategoryList();

		// カテゴリーリストがヌルの場合は、エラーメッセージを返す
		// 如果分類清單為空，則回傳錯誤訊息
		if (CollectionUtils.isEmpty(categoryList)) {
			BookResponse bookResponse = new BookResponse();
			bookResponse.setMessage(ERROR_BOOKINFO);
			bookInfoList.add(bookResponse);
			return bookInfoList;
		}

		// 重複する書籍データを削除するため、HashSetを作って使用し、検索条件に一致する書籍データをHashSetに保存する
		// 建立一個Book類別的Set，用來儲存符合搜尋條件的書籍清單，使用HashSet可以自動去除重複的書籍
		Set<Book> bookSet = new HashSet<>();

		// categoryListから種類のデータを検索する
		// 從清單內搜尋分類(文學、奇幻....)
		for (String categoryItem : categoryList) {
			// データベースの資料とitem1の種類データに一致するとそのデータを取得する
			// 取得符合 item1 內種類的書籍清單(每次搜尋都會覆蓋前面儲存的資料)
			List<Book> bookList = bookDao.findByCategoryContaining(categoryItem);

			// 検索されたbookListのデータがresponseBookListに存在しない場合は、更新する
			// 判斷搜出來的bookList內的資料是否在responseBookList內，沒有就更新進去
			if (!CollectionUtils.isEmpty(bookList)) {
				bookSet.addAll(bookList);
			}
		}

		// 検索された書籍データをBookResponse形式に変換し、bookByCategoryContainingListに保存する
		// 將搜尋到的書籍轉換成BookResponse格式，並放進bookByCategoryContainingList
		for (Book bookItem : bookSet) {
			BookResponse bookResponse = new BookResponse(bookItem.getTitle(), bookItem.getAuthor(), 
					bookItem.getIsbn(), bookItem.getPrice(), bookItem.getInventory());
			ResponseBookList.add(bookResponse);
		}

		// 検索条件に一致する書籍リストを返する
		// 回傳符合搜尋條件的書籍清單
		return ResponseBookList;
	}

//	----------------------分類查詢(回傳BookResponse物件)----------------------
	
	@Override
	public BookResponse getBookByCategory(BookRequest bookRequest) {

		// 書籍データを保存するbookInfoListを作成する
		// 建一個清單來接最後要回傳的書籍資料
		List<Book> ResponseBookList = new ArrayList<>();

		// Requestから種類データを取得する
		// 先從Request內取得分類清單(List)
		List<String> categoryInfoList = bookRequest.getCategoryList();

		// categoryInfoListはヌルの場合、エラーメッセージを返す(分類為空)
		if (CollectionUtils.isEmpty(categoryInfoList)) {
			bookResponse.setMessage(ERROR_BOOKINFO);
			return bookResponse;
		}

		// 從清單內搜尋分類資料(文學、奇幻....)
		for (String item : categoryInfoList) {
			
			// categoryInfoListはヌルの場合、エラーメッセージを返す(分類欄""為空)
			if (!StringUtils.hasText(item)) {
				bookResponse.setMessage(ERROR_BOOKINFO);
				return bookResponse;
			}
			
			// 建一個清單來接書籍資料(Book類別)
			List<Book> categoryList = bookDao.findByCategoryContaining(item); // 後蓋前，所以 後 會等於 前的型別
			
			// 若輸入的格式錯誤(輸入的關鍵字為非分類名稱)，則跳過後進到下一圈搜尋
			if (CollectionUtils.isEmpty(categoryList)) {
				continue;
			} 
			
			// 判斷搜出來的categoryList內的資料是否在bookInfoList內，沒有就放進去
			for (Book bookItem : categoryList) {
				// 取categoryList裡的書名、作者、ISBN、價錢、銷售量的資料
				Book book = new Book(bookItem.getTitle(), bookItem.getAuthor(), bookItem.getIsbn(), 
						bookItem.getPrice(), bookItem.getInventory());
				// 將需要的書籍資料丟進responseBookList(書籍清單)裡面
				ResponseBookList.add(book);
			}
		}

		// 防呆：若For迴圈跑完後，bookInfoList內無資料，則回傳無此書籍
		if (CollectionUtils.isEmpty(ResponseBookList)) {
			bookResponse.setMessage(WRONG_FORMAT);
			return bookResponse;
		}

		return new BookResponse(ResponseBookList);
	}

//	----------------------書籍搜尋(消費者透過書名或ISBN或作者搜尋)----------------------
	
	@Override
	public List<BookResponse> searchBookByConsumer(BookRequest bookRequest) {

		// 從Request內取得搜尋書籍用的值(參數)
		String searchText = bookRequest.getSearch();
		
		// 防呆：如果searchText的值為null或空字串，則回傳"輸入關鍵字不得為空"
		if (!StringUtils.hasText(searchText)) {
			bookResponse.setMessage(ERROR_BOOKINFO);
			List<BookResponse> errorTextList = new ArrayList<>();
			errorTextList.add(bookResponse);
			return errorTextList;
		}
		
		// 建一個List存放從資料庫搜尋比對後的資料
		List<Book> bookList = bookDao.findByTitleOrAuthorOrIsbn(searchText, searchText, searchText);
		
		// 將bookList符合條件的資料取出，並存放在responseBookList
		List<BookResponse> responseBookList = new ArrayList<>();
		
		for (Book item : bookList) {
			BookResponse bookResponse = new BookResponse(item.getTitle(),
					item.getAuthor(), item.getIsbn(), item.getPrice());
			responseBookList.add(bookResponse);
		}
		
		// 防呆：若輸入的關鍵字與資料庫資料不符，則回傳"無此書籍"
		if (CollectionUtils.isEmpty(responseBookList)) {
			bookResponse.setMessage(ERROR_ISBN);
			
			List<BookResponse> errorTextList = new ArrayList<>();
			errorTextList.add(bookResponse);
			return errorTextList;
		}
		
		return responseBookList;
	}

//	----------------------書籍搜尋(書商透過書名或ISBN或作者搜尋)----------------------
	
	@Override
	public List<BookResponseA> searchBookByBookseller(BookRequest bookRequest) {
		
		// 從Request內取得搜尋書籍用的值(參數)
		String searchText = bookRequest.getSearch();
		BookResponseA bookResponseA = new BookResponseA();
		
		// 防呆：如果searchText的值為null或空字串，則回傳"欄位不得為空"
		if (!StringUtils.hasText(searchText)) {
			bookResponseA.setMessage(ERROR_BOOKINFO);
			List<BookResponseA> errorTextList = new ArrayList<>();
			errorTextList.add(bookResponseA);
			return errorTextList;
		}
		
		// (如果search不是空字串)建一個List存放從資料庫搜尋比對後的資料(搜尋符合搜尋條件的書籍列表)
		List<Book> bookList = bookDao.findByTitleOrAuthorOrIsbn(searchText, searchText, searchText);
		
		// 將bookList符合條件的資料取出，並存放在responseBookList
		List<BookResponseA> responseBookList = new ArrayList<>();
		
		for (Book item : bookList) {
			BookResponseA newbookResponseA = new BookResponseA(item.getTitle(), item.getAuthor(),
					item.getIsbn(), item.getPrice(), item.getInventory(), item.getSales());
			responseBookList.add(newbookResponseA);
		}
		
		// 防呆：若輸入的關鍵字與資料庫資料不符，則回傳"無此書籍"
		if (CollectionUtils.isEmpty(responseBookList)) {
			bookResponse.setMessage(ERROR_ISBN);
			List<BookResponseA> errorTextList = new ArrayList<>();
			errorTextList.add(bookResponseA);
			return errorTextList;
		}
		
		return responseBookList;
	}
	
//	----------------------書籍搜尋(判斷身分後透過書名或ISBN或作者搜尋)----------------------

	@Override
	public List<BookResponse> searchBook(BookRequest bookRequest) {

		// Requestから検索に使用するキーワードを取得する
		// 從Request內取得搜尋書籍用的值(參數)
		String searchText = bookRequest.getSearch();

		// searchTextがヌルまたは空の場合は、「キーワードを入力してください」と返す。
		// 防呆：如果searchText的值為null或空字串，則回傳"輸入關鍵字不得為空"
		if (!StringUtils.hasText(searchText)) {
			bookResponse.setMessage(ERROR_BOOKINFO);
			List<BookResponse> errorList = new ArrayList<>();
			errorList.add(bookResponse);
			return errorList;
		}

		// Requestから身分を判定するキーワードを取得する
		String status = bookRequest.getStatus();

		// statusがヌルまたは空の場合は、「キーワードを入力してください」と返す。
		// 防呆：如果status的值為null或空字串，則回傳"輸入關鍵字不得為空"
		if (!StringUtils.hasText(status)) {
			bookResponse.setMessage(ERROR_BOOKINFO);
			List<BookResponse> errorList = new ArrayList<>();
			errorList.add(bookResponse);
			return errorList;
		}

		// 書籍データを保存するresponseBookListを作成する
		List<BookResponse> responseBookList = new ArrayList<>();

		// データベースから検索条件に一致する書籍データを取得し、作ったListに保存する
		// (如果search不是空字串)建一個List存放從資料庫搜尋比對後的資料(搜尋符合搜尋條件的書籍列表)
		List<Book> bookList = bookDao.findByTitleOrAuthorOrIsbn(searchText, searchText, searchText);

		// 業者の場合
		if (status.equals("業者")) {
			// bookListからデータを検索する
			for (Book item : bookList) {
				// 書籍の資料をbookResponseに保存する
				// 建立一個BookResponse物件，包含書籍的標題、作者、ISBN、價格、庫存和銷售量等資訊
				BookResponse bookResponse = new BookResponse(item.getTitle(), item.getAuthor(), item.getIsbn(),
						item.getPrice(), item.getInventory(), item.getSales());
				// BookResponseオブジェクトをresponseBookListに追加し、最後にresponseBookListを返す
				// 將BookResponse物件加到responseBookList中，最後回傳responseBookList
				responseBookList.add(bookResponse);
			}
		// 消費者の場合
		} else if (status.equals("消費者")) {
			
			// bookListからデータを検索する
			for (Book item : bookList) {
				// 書籍の資料をbookResponseに保存する
				BookResponse bookResponse = new BookResponse(item.getTitle(),
						item.getAuthor(), item.getIsbn(), item.getPrice());
				// BookResponseオブジェクトをresponseBookListに追加し、最後にresponseBookListを返す
				responseBookList.add(bookResponse);
			}
		}
		
		// 入力されたキーワードがデータベースのデータと一致しない場合は、「この書籍はありません」と返してください
		// 防呆：若輸入的關鍵字與資料庫資料不符，則回傳"無此書籍"
		if (CollectionUtils.isEmpty(responseBookList)) {
			bookResponse.setMessage(ERROR_ISBN);
			List<BookResponse> errorTextList = new ArrayList<>();
			errorTextList.add(bookResponse);
			return errorTextList;
		}
		
		return responseBookList;
	}
	
//	----------------------更新庫存和價格----------------------

	@Override
	public BookResponse updateBookInfo(BookRequest bookRequest) {

		// ISBN、在庫数、価格が空であるかどうかを判定し、もし空であれば、エラーメッセージを返す
		// 判斷傳入的BookRequest物件中的ISBN 或 庫存和價錢是否為空 ; 如果是，就設置一個錯誤訊息並返回。
		if (!StringUtils.hasText(bookRequest.getIsbn()) ||
				(bookRequest.getUpdateInventory() == null && 
				bookRequest.getUpdatePrice() == null)) {
			bookResponse.setMessage(ERROR_BOOKINFO);
			return bookResponse;
		}
		
		// bookDao.findByIdメソッドを使って、データベースのISBNデータを検索し、そのデータをoptionalに保存する
		// 使用bookDao.findById方法查詢資料庫裡的ISBN的資料，並將資料存入Optional<Book>類別的optional物件中。
		Optional<Book> optional = bookDao.findById(bookRequest.getIsbn());
		
		// OptionalのISBNが存在するかどうかを判断し、存在しない場合はエラーメッセージを返す
		// 判斷optional裡的ISBN是否存在 ; 如果不存在，就設置一個錯誤訊息並返回。
		if (!optional.isPresent()) {
			bookResponse.setMessage(ERROR_ISBN);
			return bookResponse;
		}
		
		// もしoptionalにISBNのデータが存在する場合、Optionalからデータを取り出してbookに保存する
		// 如果optional裡的ISBN資料存在，從Optional取出資料存入book
		Book book = optional.get();
		
		// Bookオブジェクトの在庫量と価格を更新し、データベースに保存する
		// 更新Book物件的inventory和price，並將其儲存回資料庫
		book.setInventory(bookRequest.getUpdateInventory());
		book.setPrice(bookRequest.getUpdatePrice());
		Book newBook = bookDao.save(book);
		
		// BookResponseオブジェクトを作って、更新された書籍資料をbookResponseに保存する
		bookResponse = new BookResponse(newBook.getTitle(), newBook.getAuthor(), newBook.getIsbn(), 
				newBook.getPrice(), newBook.getInventory(), SUCCESS_REPLACE);
		// 更新された書籍資料を返す
		return bookResponse;
	}
	
//	----------------------暢銷書排行榜----------------------

	@Override
	public List<Book> getBookTop5() {

		// 検索された書籍データを保存するために、bookListを作って使用する
		// 建立一個空的bookList，用來儲存搜尋到的書籍資料
		List<Book> bookList = new ArrayList<>();
		
		// データベースからデータを検索し、返されたデータをresponseSalesListに保存する
		// 使用bookDao.findTop5ByOrderBySalesDesc()方法查詢資料庫，並將回傳的資料存入responseSalesList
		List<Book> responseSalesList = bookDao.findTop5ByOrderBySalesDesc();
		
		// responseSalesListのデータを検索し、bookListに追加する
		// 搜尋responseSalesList內的銷售資料，並將其加入到bookList裡
		for (Book item : responseSalesList) {
			Book newbook = new Book(item.getTitle(), item.getAuthor(), 
					item.getIsbn(), item.getPrice());
			bookList.add(newbook);
		}
		
		return bookList;
	}

//	----------------------書籍銷售----------------------
	
	@Override
	public List<BookResponse> shopBook(BookRequest bookRequest) {

		// 儲存訂單總價
		int bookTotalPrice = 0;

		// 建立一個bookMap，來取bookRequest內的ISBN和ordered的資料
		Map<String, Integer> bookMap = bookRequest.getBookMap();

		// 使用 foreach迴圈遍歷，對bookMap內的每一筆資料進行處理
		for (Entry<String, Integer> item : bookMap.entrySet()) {
			
			// 取ISBN
			String itemKey = item.getKey();

			// 判斷從資料庫抓出的itemKey的資料是否存在，若無則回報錯誤
			if (!bookDao.existsById(itemKey)) {
				bookResponse.setMessage(WRONG_FORMAT);
				List<BookResponse> itemKeyList = new ArrayList<>();
				itemKeyList.add(bookResponse);
				continue;
			}

			// 設一個bookOpt來搜尋Book裡的ISBN
			Optional<Book> bookOpt = bookDao.findById(itemKey);

			// 取得搜尋到的資料(bookOpt)，並存入newBook
			Book newBook = bookOpt.get();

			// 設一個price取價格
			int price = newBook.getPrice();

			// 若訂購數量<=0，回報錯誤
			if (item.getValue() <= 0) {
				bookResponse.setMessage(WRONG_FORMAT);
				List<BookResponse> itemList = new ArrayList<>();
				itemList.add(bookResponse);
				// 跳過這一筆資料，繼續處理下一筆(若上一筆輸入錯誤，但下一筆為正確格式時)
				continue;
			}

			// 計算總價
			int totalPrice = price * item.getValue();

			// 把總價存回bookTotalPrice
			bookTotalPrice += totalPrice;
		}

		// 取得所有訂單中所需更新的書籍(取得ISBN)
		List<Book> bookList = bookDao.findAllById(bookMap.keySet());

		// 若bookList為空，回報錯誤訊息(從Book格式轉成BookResponse格式)
		if (CollectionUtils.isEmpty(bookList)) {
			bookResponse.setMessage(ERROR_ISBN);

			// 從Book格式轉成BookResponse格式，並存入newBookList後回傳
			List<BookResponse> newBookList = new ArrayList<>();
			newBookList.add(bookResponse);
			return newBookList;
		}
		
		// 創建一個saveBookList，用於儲存需要更新至資料庫的書籍資料
		List<Book> saveBookList = new ArrayList<>();
		// 創建一個responseBookList，用於儲存需要回傳的書籍資料
		List<BookResponse> responseBookList = new ArrayList<>();

		// 使用foreach迴圈遍歷書籍列表中的每一本書籍
		for (Book item : bookList) {
			// 從bookMap中取得每本書籍需要更新的庫存量，並更新書籍庫存量與銷售量
			item.setInventory(item.getInventory() - bookMap.get(item.getIsbn()));
			item.setSales(item.getSales() + bookMap.get(item.getIsbn()));
			// 將已更新的書籍加入saveBookList中
			saveBookList.add(item);
		}
		
		// 將所有需要保存到資料庫的書籍資料存入資料庫
		List<Book> saveBooks = bookDao.saveAll(saveBookList);

		// 用foreach迴圈遍歷saveBooks中的資料，並將這些資料傳入newBookResponse
		for (Book bookItem : saveBooks) {
			BookResponse newBookResponse = new BookResponse(bookItem.getTitle(), bookItem.getAuthor(),
					bookItem.getIsbn(), bookItem.getPrice(), bookRequest.getOrdered(), bookTotalPrice);
			// 將newBookResponse的資料存入responseBookList
			responseBookList.add(newBookResponse);
		}

		return responseBookList;
	}
	
//	----------------------書籍銷售----------------------

	@Override
	public BookResponse orderBook(BookRequest bookRequest) {
		
		// 書籍の価格を保存するために使用されます
		// 宣告一個整數變數(bookTotlePrice)，並初始化為0，這個變數用於存儲書籍的總價格
	    int bookTotlePrice = 0;
	    
	    // BookRequestから取得したISBNと注文数を保存するために、bookMapを作る
		// 建立一個bookMap，用來儲存從BookRequest類別內獲取的ISBN和訂購數量的資料
	    Map<String, Integer> bookMap = bookRequest.getBookMap();
	    
	    // 儲存錯誤的Key
	    List<String> errIsbnList = new ArrayList<>();
	    
	    // 「bookMap」の各要素をループ処理で取得し、「key」と「value」を「item」に保存する
		// 使用 foreach 迴圈遍歷 "bookMap" 中的每一個key跟value，並存入"item"
	    for (Entry<String, Integer> item : bookMap.entrySet()) {
	    	
	    	// itemから書籍のISBNを取得し、「itemKey」に保存する
			// 從"item"中獲取書籍的ISBN，並存入"itemKey"
	        String itemKey = item.getKey();
	        
	        // itemから書籍のorderedを取得し、「itemValue」に保存する
	     	// 從"item"中獲取書籍的ordered，並存入"itemValue"
	        Integer itemValue = item.getValue();
	        
	        // データベースから取得されたitemKeyが存在するかどうかを確認し、或いはitemValue<1の場合，
	        // エラーデータをerrIsbnListに保存する
			// 判斷從資料庫抓出的itemKey的資料是否存在 或itemValue是否<1，若無則回報錯誤
	        if (!bookDao.existsById(itemKey) || itemValue < 1) {
	            errIsbnList.add(itemKey);
//	            errIsbnList.add(itemValue);
	            
	            // 複数のデータを比較し、1件でも正しい場合は続行する
				// 比對多筆資料，若其中一筆為正確就繼續執行
	            continue;
	        }
	        
			// データベースからISBNの書籍データを検索し、bookOptに保存する
			// 從資料庫中搜尋ISBN的書籍資料，並存入"bookOpt"
	        Optional<Book> bookOpt = bookDao.findById(itemKey);
			// 「bookOpt」からデータを取り出し、「newbook」に保存する
			// 將"bookOpt"裡的資料取出，並存入"newbook"
	        Book newbook = bookOpt.get();
			// 書籍の価格を取得する
			// 取得該書籍的價格
	        int price = newbook.getPrice();
			// 書籍の総額を計算する
			// 計算該書籍的總金額 (價格 * 訂購數量)
	        int totlePrice = price * item.getValue();
			// 書籍の総額を加算する
			// 累加書籍總金額
	        bookTotlePrice += totlePrice;
	        
	    }
	    
		// データベースに、全ての書籍の注文情報を検索する
		// 從資料庫中查詢所有書籍的訂購資訊(取ISBN對應的值)
	    List<Book> bookList = bookDao.findAllById(bookMap.keySet());
		// saveBookListを作成し、データベースに更新する書籍の注文情報を保存する
		// 建立一個"saveBookList"來儲存需要更新至資料庫的書籍訂購資訊
	    List<Book> saveBookList = new ArrayList<>();
		// foreachを使用し、検索された書籍の各レコードを繰り返し処理する
		// 使用foreach迴圈遍歷查詢的每一筆書籍
	    for (Book item : bookList) {
	    	
			// 書籍の在庫数を更新する
			// 更新書籍庫存量
	        item.setInventory(item.getInventory() - bookMap.get(item.getIsbn()));
			// 書籍の販売量を更新
			// 更新書籍銷售量
	        item.setSales(item.getSales() + bookMap.get(item.getIsbn()));
	        // 存入saveBookList
	        saveBookList.add(item);
	    }
	    // 空の場合、エラーメッセージを返す
	    // 判斷查詢結果是否為空，若為空，回傳錯誤訊息
	 	if (CollectionUtils.isEmpty(bookList)) {
	 		bookResponse.setMessage(ERROR_ISBN);
	 		return bookResponse;
	 	}
	 	
//	 	for(Book item : bookList) {
//			if(bookMap.get(item.getIsbn()) < 1) {
//				bookResponse.setMessage(ERROR_VALUE);
//				return bookResponse;
//			}
//		}
	    
	    List<Book> responseBookList = new ArrayList<>();
	    
		// 書籍データをデータベースに保存する
		// 將所有需要保存到資料庫的書籍資料存入資料庫
	    List<Book> saveBooks = bookDao.saveAll(saveBookList);
	    // 從saveBooks中逐一取出元素
	    for (Book bookItem : saveBooks) {
	    	// 新しいBookオブジェクトを作成し、bookItemからデータを取得してbookに保存する
			// 建立一個新的Book物件，每次迴圈中從bookItem中獲取資料，並存入book裡
	        Book book = new Book(bookItem.getTitle(), bookItem.getAuthor(), bookItem.getIsbn(),
	                             bookItem.getPrice(), bookMap.get(bookItem.getIsbn()));
	    
	        responseBookList.add(book);
	    }
	    
	    // ISBNや注文数に誤りがある場合、errIsbnListに保存し、最後にエラーメッセージを表示します
	    // 當有錯誤的書籍 ISBN 或訂購量時，將這些錯誤的 ISBN 存入 errIsbnList 中，並在最後顯示錯誤訊息。
	    if (!errIsbnList.isEmpty()) {
	        bookResponse.setMessage("以下書碼或訂購量輸入錯誤：" + errIsbnList);	     
	        
	    }
	    
	    // 新しく作成されたBookオブジェクトをresponseBookList、bookTotlePrice、messageに追加する
	    return new BookResponse(responseBookList, bookTotlePrice, bookResponse.getMessage());														
	}

//	----------------------搜尋書籍(透過ISBN)----------------------
	
	@Override
	public List<BookResponse> searchIsbn(BookRequest bookRequest) {
		
		String isbn = bookRequest.getIsbn();
		
		// 防呆：如果isbn的值為null或空字串，則回傳"輸入關鍵字不得為空"
		if (!StringUtils.hasText(isbn)) {
			bookResponse.setMessage(ERROR_BOOKINFO);
			List<BookResponse> searchTextList = new ArrayList<>();
			searchTextList.add(bookResponse);
			return searchTextList;
		}
		
		// (如果isbn不是空字串)建一個List存放從資料庫搜尋比對後的資料(搜尋符合搜尋條件的書籍列表)
		List<Book> bookList = bookDao.findByIsbn(isbn);
		
		// 將bookList符合條件的資料取出，並存放在responseBookList
		List<BookResponse> responseBookList = new ArrayList<>();
		
		for (Book item : bookList) {
			BookResponse bookResponse = new BookResponse(item.getTitle(), item.getAuthor(),
					item.getIsbn(), item.getPrice(), item.getInventory());
			responseBookList.add(bookResponse);
		}
		
		// 防呆：若輸入的關鍵字與資料庫資料不符，則回傳"無此書籍"
		if (CollectionUtils.isEmpty(responseBookList)) {
			bookResponse.setMessage(ERROR_ISBN);
			List<BookResponse> errorTextList = new ArrayList<>();
			errorTextList.add(bookResponse);
			return errorTextList;
		}
		
		return responseBookList;
	}

}
