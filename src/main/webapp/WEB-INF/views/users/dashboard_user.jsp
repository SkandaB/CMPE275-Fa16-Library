<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <title>Add User</title>
</head>
<body>
<p><font color="red">${errorMessage}</font></p>
<form action="user" method="POST">
    SJSU ID : <input name="sjsuid" type="text"/>
    </br>
    User Email : <input name="useremail" type="text"/>
    </br>
    Password : <input name="password" type="password"/>
    </br>
    Role : <input name="role" type="text"/>
    </br>


    <input type="submit"/>
</form>
</body>
</html>