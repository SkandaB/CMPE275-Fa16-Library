<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="../../fragments/header.jsp" />

<body>

<nav class="navbar navbar-inverse ">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/user/${userId}/books">My Books</a>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/user/${userId}/dashboard">&nbsp; &nbsp;
                &nbsp; My Dashboard</a>

        </div>

    </div>
</nav>

<div class="container">
    <pre>${status}</pre>
</div>
<jsp:include page="../../fragments/footer.jsp" />

</body>
</html>