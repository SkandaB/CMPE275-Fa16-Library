<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <title>LMS-Group2</title>

    <spring:url value="/resources/core/css/hello.css" var="coreCss"/>
<spring:url value="/resources/core/css/bootstrap.min.css"
	var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />
</head>

<%--<spring:url value="/lmsdashboard" var="urlHome"/>--%>
<spring:url value="/register" var="userlogout"/>

<nav class="navbar navbar-inverse ">
	<div class="container">
		<div class="navbar-header">
            <a class="navbar-brand">CMPE275-Group2-Library Management System</a>

            <a style="padding-left: 600px" class="navbar-brand" href="${userlogout}">Logout</a>
            <%--<a style="padding-left: 600px" class="navbar-brand" href="${userlogout}">LogOut</a>--%>

            <%--&lt;%&ndash;<ul class="nav navbar-nav navbar-right">&ndash;%&gt;--%>
            <%--<li style="padding-left: 1000px;"><a href="<c:url value="/logout" />"><i class="glyphicon glyphicon-lock"></i>Logout</a>--%>
            <%--</li>--%>
            <%--</ul>--%>
		</div>
	</div>

</nav>