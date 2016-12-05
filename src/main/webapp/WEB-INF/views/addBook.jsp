<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Upload File Request Page</title>
</head>
<body>
<form method="POST" modelAttribute="book" enctype="multipart/form-data">
    <%--modelAttribute="book" --%>
    <%--action="uploadBookImage" --%>

    <table>
        <tr>
            <td>ISBN</td>
            <td><input type="text" placeholder="10 or 13 digit ISBN code" id="isbn" name="isbn"></td>
        </tr>
        <tr>
            <td>Author</td>
            <td><input type="text" placeholder="Author of the book" id="author" name="author"></td>
        </tr>
        <tr>
            <td>Title</td>
            <td><input type="text" placeholder="Title of the book" id="title" name="title"></td>
        </tr>
        <tr>
            <td>Call Number</td>
            <td><input type="text" placeholder="Call Number" id="callnumber" name="callnumber"></td>
        </tr>
        <tr>
            <td>Publisher</td>
            <td><input type="text" placeholder="Publisher of the book" id="publisher" name="publisher"></td>
        </tr>
        <tr>
            <td>Year of Publication</td>
            <td><input type="text" placeholder="Year of publication" id="year_of_publication"
                       name="year_of_publication"></td>
        </tr>
        <tr>
            <td>Location</td>
            <td><input type="text" placeholder="Location of the book in library" id="location" name="location"></td>
        </tr>
        <tr>
            <td>Number of copies</td>
            <td><input type="text" placeholder="Number of copies" id="num_of_copies" name="num_of_copies"></td>
        </tr>
        <tr>
            <td>Current Status</td>
            <td><input type="text" placeholder="Current Status" id="current_status" name="current_status"></td>
        </tr>
        <tr>
            <td>Keywords</td>
            <td><input type="text" placeholder="Keywords" id="keywords" name="keywords"></td>
        </tr>
        <tr>
            <td>Image</td>
            <td><input type="file" id="imagefile" name="imagefile"></td>
        </tr>
        <tr>
            <td colspan="3"><input type="submit" value="Create"/></td>
        </tr>
    </table>
    <br>
</form>

<%--<%@ page contentType="text/html;charset=UTF-8"%>--%>
<%--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>--%>
<%--<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>

<%--<html>--%>
<%--<head>--%>
    <%--<title>Add Book</title>--%>
<%--</head>--%>
<%--<body>--%>

<%--</body>--%>
</html>