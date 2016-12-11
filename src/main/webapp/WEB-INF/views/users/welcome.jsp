<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="../../fragments/header.jsp" />
<style>
    a:link {
        text-decoration: none;
    }

    a:visited {
        text-decoration: none;
    }

    a:hover {
        text-decoration: underline;
    }

    a:active {
        text-decoration: underline;
    }
</style>
<body>

<div class="container">
    <h2>Welcome !! </h2>
    <h3> ${message} </h3>
    <br>
    <c:choose>
        <c:when test="${showsignin == 'true'}">
    <a href="<c:url value="/register" />">Sign In</a>
        </c:when>

    </c:choose>

</div>
<jsp:include page="../../fragments/footer.jsp" />

</body>
</html>