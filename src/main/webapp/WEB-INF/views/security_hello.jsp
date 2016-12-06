<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: SkandaBhargav
  Date: 12/5/16
  Time: 10:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Security</title>
</head>
<body>
<h4>Reports</h4></div>
<sec:authorize access="hasRole('ROLE_PATROM')">
    <h2>This text is only visible to a user</h2>
    <br/>
</sec:authorize>

<sec:authorize access="hasRole('ROLE_LIBRARIAN')">
    <h2>This text is only visible to an admin</h2>
    <br/>
</sec:authorize>

<sec:authorize access="hasAuthority('ROLE_PATROM')">
    <h2>This text is only visible to a use hasAuthority r</h2>
    <br/>
</sec:authorize>

<sec:authorize access="hasAuthority('ROLE_LIBRARIAN')">
    <h2>This text is only visible to an admin hasAuthority</h2>
    <br/>
</sec:authorize>
</body>
</html>
