<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Skanda, Dhanya Lab 2</title>
</head>
<body>
<h1>Oops.......!</h1>
<h2>Error Code: ${responseCode}</h2>
<h3>Reason: ${errorMessage}</h3>
<h3>If you are Logged in, Click on Back to Dashboard</h3>
<form style="color: #28739E; font-size: medium;" action="${pageContext.request.contextPath}/dashboard">
    <input type="submit" value="Back To Dashboard"/>
    <h3>If you have not Registered/Logged In then, Click Back to Register </h3>
    <button onclick="goBack()">Back To Register</button>

    <script>
        function goBack() {
            window.history.back();
        }
    </script>
</form>
</body>
</html>