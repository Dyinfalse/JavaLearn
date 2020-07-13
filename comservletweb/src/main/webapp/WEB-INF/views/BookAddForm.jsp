<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Add Book</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
</head>
<body>
<div id="global"></div>
<h1>BookList</h1>
<form:form modelAttribute="book" action="/com_servlet_web_war/book_save" method="post">
    <fieldset>
        <legend>Add Book</legend>
        <p>
            <label for="category">Category: </label>
            <form:select
                    id="category"
                    path="category.id"
                    items="${categories}"
                    itemLabel="name"
                    itemValue="id"
            />
        </p>
        <p>
            <label for="title">Title:</label>
            <form:input id="title" path="title" />
        </p>
        <p>
            <label for="author">Author:</label>
            <form:input id="author" path="author" />
        </p>
        <p>
            <label for="isbn">ISBN:</label>
            <form:input id="isbn" path="isbn" />
        </p>
        <p id="buttons">
            <input id="reset" type="reset" tabindex="4">
            <input id="submit" type="submit" tabindex="5" value="Add Book">
        </p>
    </fieldset>
</form:form>
</body>
</html>