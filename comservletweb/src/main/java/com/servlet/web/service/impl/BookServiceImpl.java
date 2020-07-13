package com.servlet.web.service.impl;

import com.servlet.web.dao.Book;
import com.servlet.web.dao.Category;
import com.servlet.web.service.BookService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private List<Category> categories;
    private List<Book> books;

    public BookServiceImpl (){
        categories = new ArrayList<Category>();
        Category category1 = new Category(1, "小说");
        Category category2 = new Category(2, "散文");
        Category category3 = new Category(3, "传记");
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);

        books = new ArrayList<Book>();
        books.add(new Book(1L, "97827198239", "小说标题", category1, "小说作者"));
        books.add(new Book(2L, "97827198230", "散文标题", category2, "散文作者"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public Category getCategory(long id) {
        for(Category category : categories){
            if(category.getId() == id){
                return category;
            }
        }
        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        return books;
    }

    @Override
    public Book save(Book book) {
        book.setId(getNextId());
        books.add(book);
        return null;
    }

    @Override
    public Book update(Book book) {
        int bookCount = books.size();
        for(int i = 0; i < bookCount; i++){
            Book saveBook = books.get(i);
            if(saveBook.getId() == book.getId()){
                books.set(i, book);
                return book;
            }
        }
        return null;
    }

    @Override
    public Book get(long id) {
        for(Book book : books){
            if(id == book.getId()){
                return book;
            }
        }
        return null;
    }

    @Override
    public long getNextId() {
        long id = 0L;
        for(Book book : books){
            long bookId = book.getId();
            if(bookId > id){
                id = bookId;
            }
        }
        return id + 1;
    }
}
