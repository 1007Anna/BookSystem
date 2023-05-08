package com.example.java_demo_test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "book_system")
public class Book {
	
	@Column(name = "title")
	private String title; // 書名

	@Column(name = "author")
	private String author; // 作者

	@Id
	@Column(name = "ISBN")
	private String isbn; // 書號

	@Column(name = "price")
	private Integer price; // 價錢

	@Column(name = "inventory")
	@JsonProperty("ordered")
	private Integer inventory; // 庫存量

	@Column(name = "sales")
	private Integer sales; // 銷售量

	@Column(name = "category")
	private String category; // 類別

	public Book() {

	}

	public Book(String title, String author, String isbn, Integer price, Integer inventory, Integer sales,
			String category) {
		super();
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
		this.inventory = inventory;
		this.sales = sales;
		this.category = category;
	}

	public Book(String title, String author, String isbn, Integer price, Integer inventory) {
		super();
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
		this.inventory = inventory;
	}

	public Book(String title, String author, String isbn, Integer price) {
		super();
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
	}
	

	public Book(String title, String author, String isbn, Integer price, Integer inventory, Integer sales) {
		super();
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
		this.inventory = inventory;
		this.sales = sales;
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
	

}
