package com.example.java_demo_test.vo;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookRequest {
	
	// 引用Book內的參數
//	private Book book;	

	private String title;

	private String author;

	private String isbn;

	private Integer price;

	private Integer inventory;

	private Integer sales;

	private String category;
	
	private List<Integer> salesList;

	private String search; // 搜尋關鍵字

	private Integer updateInventory; // 更新庫存

	private Integer updatePrice; // 更新價錢
	
	private Integer updateSales;	//更新銷售
	
	private Integer ordered;	// 訂購數量
	
	@JsonProperty("book_Map")
	private Map<String,Integer> bookMap;

	private List<String> categoryList;

	public BookRequest() {

	}

	public BookRequest(String title, String author, String isbn, Integer price, Integer inventory, Integer sales,
			String category) {
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
		this.inventory = inventory;
		this.sales = sales;
		this.category = category;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
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

	public List<String> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<String> categoryList) {
		this.categoryList = categoryList;
	}

	public Integer getUpdateInventory() {
		return updateInventory;
	}

	public void setUpdateInventory(Integer updateInventory) {
		this.updateInventory = updateInventory;
	}

	public Integer getUpdatePrice() {
		return updatePrice;
	}

	public void setUpdatePrice(Integer updatePrice) {
		this.updatePrice = updatePrice;
	}

	public List<Integer> getSalesList() {
		return salesList;
	}

	public void setSalesList(List<Integer> salesList) {
		this.salesList = salesList;
	}

	public Integer getOrdered() {
		return ordered;
	}

	public void setOrdered(Integer ordered) {
		this.ordered = ordered;
	}

	public Map<String, Integer> getBookMap() {
		return bookMap;
	}

	public void setBookMap(Map<String, Integer> bookMap) {
		this.bookMap = bookMap;
	}

	public Integer getUpdateSales() {
		return updateSales;
	}

	public void setUpdateSales(Integer updateSales) {
		this.updateSales = updateSales;
	}
	
	
	
	

}
