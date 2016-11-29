<%--
  Created by IntelliJ IDEA.
  User: svadhera
  Date: 11/28/16
  Time: 4:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Group 2: View Book</title>
</head>
<body>
Book Details:
<br>
<table>
    <tr>
        <td>ISBN</td>
        <td>${book.isbn}</td>
    </tr>
    <tr>
        <td>Author</td>
        <td>${book.author}</td>
    </tr>
    <tr>
        <td>Title</td>
        <td>${book.title}</td>
    </tr>
    <tr>
        <td>Call Number</td>
        <td>${book.callnumber}</td>
    </tr>
    <tr>
        <td>Publisher</td>
        <td>${book.publisher}</td>
    </tr>
    <tr>
        <td>Year of Publication</td>
        <td>${book.year_of_publication}</td>
    </tr>
    <tr>
        <td>Location</td>
        <td>${book.location}</td>
    </tr>
    <tr>
        <td>Number of copies</td>
        <td>${book.num_of_copies}</td>
    </tr>
    <tr>
        <td>Current Status</td>
        <td>${book.current_status}</td>
    </tr>
    <tr>
        <td>Keywords</td>
        <td>${book.keywords}</td>
    </tr>
    <tr>
        <td>Image</td>
        <%--<td>${book.image}</td>--%>
        <td><img alt="img" src="data:image/jpeg;base64,${imageString}"/></td>
    </tr>
</table>
<br>
</body>
</html>
