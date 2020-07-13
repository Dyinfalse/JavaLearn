package com.servlet.web.service;

import com.servlet.web.dao.Book;
import com.servlet.web.dao.Category;

import java.util.List;

public interface BookService {
    List<Category> getAllCategories();

    Category getCategory(long id);

    List<Book> getAllBooks();

    Book save(Book book);

    Book update(Book book);

    Book get(long id);

    long getNextId();

}
