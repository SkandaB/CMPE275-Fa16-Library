<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html>
<html lang="en">
<style>
    .error {
        color: red;
        font-weight: bold;
    }
</style>
<jsp:include page="../../fragments/header.jsp"/>
<body>
<p><font color="red">${errorMessage}</font></p>
<div>
    <table border="0" width="90%">
        <form:form action="${pageContext.request.contextPath}/register" commandName="userForm">
            <tr>
                <td align="left" width="20%">SJSU ID:</td>
                <td align="left" width="40%"><form:input path="sjsuid" size="30"/></td>
                <td align="left"><form:errors path="sjsuid" cssClass="error"/></td>
            </tr>
            <tr>
                <td align="left" width="20%">Email:</td>
                <td align="left" width="40%"><form:input path="useremail" size="30"/></td>
                <td align="left"><form:errors path="useremail" cssClass="error"/></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><form:password path="password" size="30"/></td>
                <td><form:errors path="password" cssClass="error"/></td>
            </tr>
            <tr>
                <td></td>
                <td align="center"><input type="submit" value="Submit"/></td>
                <td></td>
            </tr>
        </form:form>
    </table>


    <%-- <form action="${pageContext.request.contextPath}/register" method="POST">
         SJSU ID : <input name="sjsuid" type="text"/>
         <form:errors path="sjsuid"></form:errors>
         </br>
         User Email : <input name="useremail" type="text"/>
         <form:errors path="useremail"></form:errors>
         </br>
         Password : <input name="password" type="password"/>
         <form:errors path="password"></form:errors>
         </br>


         <input type="submit"/>
     </form>--%>
</div>
<div>
    <a href="<c:url value="/logout" />">Logout</a>
</div>
<jsp:include page="../../fragments/footer.jsp"/>

</body>
</html>