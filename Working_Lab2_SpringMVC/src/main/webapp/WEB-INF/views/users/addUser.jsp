<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<html>
<head>
<title>Add User</title>
</head>
<body>
    <p><font color="red">${errorMessage}</font></p>
    <form action="user" method="POST">
        First Name : <input name="firstname" type="text" /> 
        </br>
        Last Name : <input name="lastname" type="text" />
        </br>
        Title : <input name="title" type="text" />
        </br>
        Address 
        </br>
       	City : <input name="city" type="text" />
       	State : <input name="state" type="text" />
       	Street : <input name="street" type="text" />
       	Zip code : <input name="zip_code" type="text" /> 
        
         <input type="submit" />
    </form>
</body>
</html>