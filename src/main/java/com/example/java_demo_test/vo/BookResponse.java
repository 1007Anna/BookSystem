package com.example.java_demo_test.vo;

import java.util.List;
import java.util.Map;

import com.example.java_demo_test.entity.Book;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookResponse {

	private String title;

	private String author;

	private String isbn;

	private Integer price;

	private Integer inventory;
	
	private Integer sales;

	private String category; // 類別

	private List<Book> responseBookList;

	private String message;
	
	private Integer bookTotlePrice;
	
	private List<String> errIsbnList;

	private Integer totlePrice;

	private Map<String, Integer> bookMap;

	public BookResponse() {

	}

	public BookResponse(List<Book> responseBookList) {
		super();
		this.responseBookList = responseBookList;
	}
	

	public BookResponse(Integer bookTotlePrice) {
		super();
		this.bookTotlePrice = bookTotlePrice;
	}

	public BookResponse(String message) {
		super();
		this.message = message;
	}

	

	public BookResponse(String title, String author, String isbn, Integer price, Integer inventory) {
		super();
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
		this.inventory = inventory;
	}

	public BookResponse(String isbn, Integer inventory, Integer sales, String message) {
		super();
		this.isbn = isbn;
		this.inventory = inventory;
		this.sales = sales;
		this.message = message;
	}

	public BookResponse(String title, String author, String isbn, Integer price) {
		super();
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
	}

	public BookResponse(String title, String isbn, String message) {
		super();
		this.title = title;
		this.isbn = isbn;
		this.message = message;
	}

	public BookResponse(String isbn, String message) {
		super();
		this.isbn = isbn;
		this.message = message;
	}

	public BookResponse(String title, String author, String isbn, Integer price, Integer inventory, String message) {
		super();
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
		this.inventory = inventory;
		this.message = message;
	}
	

	public BookResponse(List<Book> responseBookList, Integer bookTotlePrice, String message) {
		super();
		this.responseBookList = responseBookList;
		this.bookTotlePrice = bookTotlePrice;
		this.message = message;
	}

	public BookResponse(String title, String author, String isbn, Integer price, Integer sales,
			Integer bookTotlePrice) {
		super();
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
		this.sales = sales;
		this.bookTotlePrice = bookTotlePrice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public Integer getSales() {
		return sales;
	}

	public void setSales(Integer sales) {
		this.sales = sales;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Book> getResponseBookList() {
		return responseBookList;
	}

	public void setResponseBookList(List<Book> responseBookList) {
		this.responseBookList = responseBookList;
	}

	public Integer getBookTotlePrice() {
		return bookTotlePrice;
	}

	public void setBookTotlePrice(Integer bookTotlePrice) {
		this.bookTotlePrice = bookTotlePrice;
	}

	public Map<String, Integer> getBookMap() {
		return bookMap;
	}

	public void setBookMap(Map<String, Integer> bookMap) {
		this.bookMap = bookMap;
	}

	public Integer getTotlePrice() {
		return totlePrice;
	}

	public void setTotlePrice(Integer totlePrice) {
		this.totlePrice = totlePrice;
	}

	public List<String> getErrIsbnList() {
		return errIsbnList;
	}

	public void setErrIsbnList(List<String> errIsbnList) {
		this.errIsbnList = errIsbnList;
	}


	

}
