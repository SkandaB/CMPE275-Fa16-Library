<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="../../fragments/header.jsp"/>

<body>
<nav class="navbar navbar-inverse ">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/user/${userId}/dashboard">&nbsp; &nbsp;
                &nbsp; My Dashboard</a>

        </div>

    </div>
</nav>

<div class="container">

    <c:if test="${not empty msg}">
        <div class="alert alert-${css} alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <strong>${msg}</strong>
        </div>
    </c:if>
    <div class="container">
    <h1>All Books</h1>

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
        <c:forEach var="book" items="${books}">
            <tr>
                <td>${book.isbn}</td>
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td>${book.current_status}</td>
                <td>
                    <spring:url value="/user/${userId}/books/${book.bookId}"
                                var="userUrl"/>
                    <spring:url value="/user/${user.id}/books/wish/${book.bookId}"
                                var="updateUrl"/>
                    <!--Dhanya, your add to wish_list will come here  of wishlist-->

                    <button class="btn btn-info"
                            onclick="location.href='${userUrl}'">Add To Cart
                    </button>
                    <button class="btn btn-primary"
                            onclick="location.href='${updateUrl}'">Wish to read Later
                    </button>
                </td>

            </tr>
        </c:forEach>
    </table>
    </div>


</div>

<jsp:include page="../../fragments/footer.jsp"/>

</body>
</html>