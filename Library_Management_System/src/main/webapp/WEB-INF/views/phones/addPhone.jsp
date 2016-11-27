<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<html>
<head>
<title>Add Phone</title>
</head>
<body>
    <p><font color="red">${errorMessage}</font></p>
    <form action="phone" method="POST">
        Number : <input name="number" type="text" />
        Description : <input name="description" type="text" />
        Address 
       	City : <input name="city" type="text" />
       	State : <input name="state" type="text" />
       	Street : <input name="street" type="text" />
       	Zip code : <input name="zip_code" type="text" /> 
        
         <input type="submit" />
    </form>
</body>
</html>