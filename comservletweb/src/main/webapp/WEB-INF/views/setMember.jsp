<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Member</title>
</head>
<body>
<h2>Add Member</h2>
    <from:form action="/member/saveMember" method="post" commandName="member">
        <from:input path="name" />
        <from:checkbox path="sex" />
        <from:input path="description" />
        <from:input path="mobile" />
    </from:form>
</body>
</html>
