<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">


<jsp:include page="../../fragments/header.jsp"/>
<head>
    <title>Search for Books!!</title>
</head>

<body>
<div class="container">
    <form:form method="post" modelAttribute="book">
        <p><font color="red">${errorMessage}</font></p>
        <table>
            <tr>
                <td>ISBN</td>
                <td><form:input path="isbn"/></td>
            </tr>
            <tr>
                <td>Title</td>
                <td><form:input path="title"/></td>
            </tr>
            <tr>
                <td>Author</td>
                <td><form:input path="author"/></td>
            </tr>
            <tr>
                <td>Call Number</td>
                <td><form:input path="callnumber"/></td>
            </tr>
            <tr>
                <td>Publisher</td>
                <td><form:input path="publisher"/></td>
            </tr>
            <tr>
                <td>Keywords</td>
                <td><form:input path="keywords"/></td>
            </tr>

            <tr>
                <td colspan="3"><input type="submit" value="Create"/></td>
            </tr>
        </table>
    </form:form>
</div>


</body>
</html>