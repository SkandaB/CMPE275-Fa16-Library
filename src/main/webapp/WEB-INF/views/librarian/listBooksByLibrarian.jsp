<%--
  Created by IntelliJ IDEA.
  User: Sagar
  Date: 12/7/2016
  Time: 1:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="../../fragments/header.jsp"/>


<body>

<div class="container">

    <c:if test="${not empty msg}">
        <div class="alert alert-${css} alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <strong>${msg}</strong>
        </div>
    </c:if>
    <nav class="navbar navbar-inverse ">
        <div class="container">
            <div class="navbar-header">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/lmsdashboard">&nbsp; &nbsp;
                    &nbsp; My Dashboard</a>

            </div>

        </div>
    </nav>
    <h1>Librarian Book Search Page</h1>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>ISBN</th>
            <th>TITLE</th>
            <th>AUTHOR</th>
            <th>AVAILABILITY</th>

        </tr>
        </thead>
        <p><font color="red">${errorMessage}</font></p>
        <%--<form:form></form:form>--%>
        <c:forEach var="book" items="${books}">
        <tr>
            <td>${book.isbn}</td>
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td>${book.current_status}</td>
            <td>
                <spring:url value="/user/${userId}/books/${book.bookId}" var="userUrl"/>
                <spring:url value="${pageContext.request.contextPath}/book/deletebook/${book.bookId}" var="deleteUrl"/>

                <button class="btn btn-success"
                        onclick="location.href='${userUrl}'">Update Book
                </button>
                <button class="btn btn-danger"
                        onclick="location.href='${deleteUrl}'">Remove
                </button>
            </td>


        </tr>
        </c:forEach>
    </table>

</div>
<%--<div>--%>
<%--<a href="<c:url value="/logout" />">Logout</a>--%>
<%--</div>--%>
<jsp:include page="../../fragments/footer.jsp"/>

</body>
</html>
