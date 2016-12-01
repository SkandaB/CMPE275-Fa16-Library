<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="../../fragments/header.jsp"/>
<body>
<p><font color="red">${errorMessage}</font></p>
<div>
    <form action="/register" method="POST">
        SJSU ID : <input name="sjsuid" type="text"/>
        </br>
        User Email : <input name="useremail" type="text"/>
        </br>
        Password : <input name="password" type="password"/>
        </br>


        <input type="submit"/>
    </form>
</div>
<div>
    <a href="<c:url value="/logout" />">Logout</a>
</div>
<jsp:include page="../../fragments/footer.jsp"/>

</body>
</html>