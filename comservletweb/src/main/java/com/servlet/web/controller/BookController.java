package com.servlet.web.controller;

import com.servlet.web.dao.Book;
import com.servlet.web.dao.Category;
import com.servlet.web.service.BookService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 这是一个表单提交数据绑定的例子
 */
@Controller
public class BookController {
    @Autowired
    BookService bookService;

    private static final Log logger = LogFactory.getLog(BookController.class);


    /**
     * 新建Book 表单
     * @param model
     * @return
     */
    @RequestMapping(value="/book_input")
    public String inputBook (Model model){

        List<Category> categories = bookService.getAllCategories();
        logger.info(categories);
        model.addAttribute("categories", categories);
        Book book = new Book();
        logger.info(book);
        model.addAttribute("book", book);

        return "BookAddForm";
    }


    /**
     * 编辑书本
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value="/book_edit/{id}")
    public String editBook (Model model, @PathVariable long id){

        List<Category> categories = bookService.getAllCategories();

        model.addAttribute("categories", categories);

        Book book = bookService.get(id);

        model.addAttribute("book", book);

        return "BookEditForm";
    }


    /**
     * 保存表单
     * @param book
     * @return
     */
    @RequestMapping(value="/book_save")
    public String saveBook (@ModelAttribute Book book) {
        Category category = bookService.getCategory(book.getCategory().getId());

        book.setCategory(category);

        bookService.save(book);
        return "redirect:/book_list";
    }

    /**
     * book更新
     * @param book
     * @return
     */
    @RequestMapping(value="/book_update")
    public String bookUpdate (@ModelAttribute Book book){
        Category category = bookService.getCategory(book.getCategory().getId());
        book.setCategory(category);
        bookService.update(book);
        return "redirect:/book_list";
    }

    /**
     * bookList
     * @param model
     * @return
     */
    @RequestMapping(value="/book_list")
    public String bookList (Model model){
        logger.info("book_list");

        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);

        return "BookList";
    }

}
