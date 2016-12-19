<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <title>Welcome To Library Management System</title>
    <link rel="stylesheet" type="text/css" href="resources/core/css/style.css">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.2.0/css/bootstrap.css">
    <link rel="stylesheet" href="resources/core/css/splash.css">
    <%--<script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>--%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Bitter' rel='stylesheet' type='text/css'>
    <%--<link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/css/bootstrap-combined.min.css" rel="stylesheet">--%>
    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.0.3/angular.min.js"></script>
    <link href="https://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet"/>
</head>

<style>

    .form-style-9 {
        max-width: 450px;
        background: #FAFAFA;
        padding: 30px;
        margin: 10px auto;
        box-shadow: 1px 1px 25px rgba(0, 0, 0, 0.35);
        border-radius: 10px;
        border: 6px solid #305A72;
    }

    .form-style-9 ul {
        padding: 0;
        margin: 0;
        list-style: none;
    }

    .form-style-9 ul li {
        display: block;
        margin-bottom: 10px;
        min-height: 35px;
    }

    .form-style-9 ul li .field-style {
        box-sizing: border-box;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        padding: 8px;
        outline: none;
        border: 1px solid #B0CFE0;
        -webkit-transition: all 0.30s ease-in-out;
        -moz-transition: all 0.30s ease-in-out;
        -ms-transition: all 0.30s ease-in-out;
        -o-transition: all 0.30s ease-in-out;
    }

    .form-style-9 ul li .field-style:focus {
        box-shadow: 0 0 5px #B0CFE0;
        border: 1px solid #B0CFE0;
    }

    .form-style-9 ul li .field-split {
        width: 49%;
    }

    .form-style-9 ul li .field-full {
        width: 100%;
    }

    .form-style-9 ul li input.align-left {
        float: left;
    }

    .form-style-9 ul li input.align-right {
        float: right;
    }

    .form-style-9 ul li textarea {
        width: 100%;
        height: 100px;
    }

    .form-style-9 ul li input[type="button"],
    .form-style-9 ul li input[type="submit"] {
        -moz-box-shadow: inset 0px 1px 0px 0px #3985B1;
        -webkit-box-shadow: inset 0px 1px 0px 0px #3985B1;
        box-shadow: inset 0px 1px 0px 0px #3985B1;
        background-color: #216288;
        border: 1px solid #17445E;
        display: inline-block;
        cursor: pointer;
        color: #FFFFFF;
        padding: 8px 18px;
        text-decoration: none;
        font: 12px Arial, Helvetica, sans-serif;
    }

    .form-style-9 ul li input[type="button"]:hover,
    .form-style-9 ul li input[type="submit"]:hover {
        background: linear-gradient(to bottom, #2D77A2 5%, #337DA8 100%);
        background-color: #28739E;
    }

    /*End form styling*/

    .modal {
        /*//height: 45%;*/
        text-align: center;
        padding: 0 !important;
    }

    /*.modal:before {*/
    /*content: '';*/
    /*display: inline-block;*/
    /*height: 100%;*/
    /*width: 100%;*/
    /*vertical-align: middle;*/
    /*margin-right: -4px; !* Adjusts for spacing *!*/
    /*}*/

    .modal-header {
        text-align: center;
    }

    .modal-body {
        height: 60%;
    }

    .modal-dialog {
        width: 40%;
        display: inline-block;
        text-align: left;
        vertical-align: middle;
    }

    /*End modal styling*/
    #block2, fgtpass {
        padding-bottom: 100px;
        position: fixed;
    }

    #fgtpass {
        position: relative;
    }

    .col-xs-6, #block1, #block2 {
        display: inline;
    }

    .remember-forgot {
        width: 900px
    }
</style>

<script type="text/javascript">
    $(document).ready(function () {
//            $('.forgot-pass').click(function(event) {
//                $(".pr-wrap").toggleClass("show-pass-reset");
//            });
//            $('.pass-reset-submit').click(function(event) {
//                $(".pr-wrap").removeClass("show-pass-reset");
//            });
        $("#signupbutton").click(function () {
            $('#registerUserModal').modal('show');
        });
    });
</script>
<body>
<div class="row">
    <div class="col-md-12 text-center">
        <button class="btn btn-lg btn-success" id="signupbutton">Sign Up!</button>
    </div>
</div>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="pr-wrap">
                <div class="pass-reset">
                    <label>Enter the email you signed up with</label>
                    <input type="email" placeholder="Email"/>
                    <input type="submit" value="Submit" class="pass-reset-submit btn btn-success btn-sm"/>
                </div>
            </div>

            <div class="wrap">
                <p class="form-title">Sign In</p>
                <form:form class="login" action="${pageContext.request.contextPath}/dashboard" commandName="loginForm">
                    <input name="useremail" autofocus placeholder="Email" type="text" path="useremail" size="30"/>
                    <form:errors cssStyle="font-kerning: auto" path="useremail"/>
                    <input name="password" placeholder="Password" type="password" path="password"
                           placeholder="Password"/> <form:errors path="password"/>
                    <input type="submit" value="Sign In" class="btn btn-default btn-sm"/>
                    <%--<div class="remember-forgot">--%>

                    <%--<div class="col-xs-6" id="block1">--%>
                    <%--<div class="checkbox" >--%>
                    <%--<label>--%>
                    <%--<input type="checkbox" />--%>
                    <%--Remember Me--%>
                    <%--</label>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="col-xs-7 forgot-pass-content" id="block2">--%>
                    <%--<a href="javascription:void(0)" if="fgtpass" class="forgot-pass">Forgot Password</a>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                </form:form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade in" id="registerUserModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Register new user</h4>
            </div>
            <div class="modal-body">
                <%--<form:form  method="post" action="${pageContext.request.contextPath}/registration" modelAttribute="user" name="registrationform" id="registrationform">--%>
                <%--<input type="text" id="sjsuid" name="sjsuid"  /><br><br>--%>
                <%--<input type="email" name=useremail"  /><br><br>--%>
                <%--<input type="password" name ="password" id="password"  /><br><br>--%>
                <%--<a id="registerBtn">Register</a>--%>
                <%--</form:form>--%>
                <form:form class="form-style-9" method="post" action="${pageContext.request.contextPath}/register"
                           commandName="userForm" modelAttribute="user" name="registeruserform" id="registeruserform"
                           onsubmit="return validate()">
                    <ul>
                        <li>
                            <input type="text" autofocus id="sjsuid" name="sjsuid"
                                   class="field-style field-full align-none"
                                   placeholder="SJSU ID"> <form:errors path="sjsuid" ata-rule-required="true"/>
                        </li>
                        <li>
                            <input type="text" name="useremail" class="field-style field-full align-none"
                                   placeholder="Email"/> <form:errors path="useremail" ata-rule-required="true"/>

                        </li>
                        <li>
                            <input type="password" name="password" class="field-style field-full align-none"
                                   placeholder="Password"/> <form:errors path="password" ata-rule-required="true"/>

                        </li>
                        <li>
                            <input type="submit" value="Register"/>
                        </li>
                    </ul>
                </form:form>
            </div>
            <%--<div class="modal-footer">--%>
            <%--<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>--%>
            <%--</div>--%>
        </div>
    </div>
</div>

</body>
<script type="text/javascript" src="resources/core/js/splash.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.7/angular.min.js"></script>
<script type="text/javascript"
        src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.7/angular-animate.min.js"></script>
<script type="text/javascript"
        src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.14.3/ui-bootstrap.min.js"></script>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">
    function validate() {
        if (document.registeruserform.useremail.value == "" && document.registeruserform.password.value == "") {
            alert("Username and password are required");
            document.login.useremail.focus();
            return false;
        }
        if (document.registeruserform.useremail.value == "") {
            alert("Username is required");
            document.f.username.focus();
            return false;
        }
        if (document.registeruserform.password.value == "") {
            alert("Password is required");
            document.f.password.focus();
            return false;
        }
    }
</script>

</html>