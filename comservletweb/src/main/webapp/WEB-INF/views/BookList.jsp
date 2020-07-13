<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Book List</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
</head>
<body>
    <div id="global"></div>
    <h1>BookList</h1>
    <a href="<c:url value="/book_input" />">Add Book</a>
    <table>
        <thead>
            <tr>
                <th>分类</th>
                <th>标题</th>
                <th>书号</th>
                <th>作者</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${books}" var="book">
                <tr>
                    <td>${book.category.name}</td>
                    <td>${book.title}</td>
                    <td>${book.isbn}</td>
                    <td>${book.author}</td>
                    <td><a href="book_edit/${book.id}">编辑</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>