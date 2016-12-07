<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="../../fragments/header.jsp" />

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

    <h1>All Users</h1>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>Number</th>
            <th>Email</th>
            <th>Role</th>
        </tr>
        </thead>

        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.sjsuid}</td>
                <td>${user.useremail}</td>
                <td>${user.role}</td>
            </tr>
        </c:forEach>
    </table>

</div>

<jsp:include page="../../fragments/footer.jsp" />

</body>
</html>