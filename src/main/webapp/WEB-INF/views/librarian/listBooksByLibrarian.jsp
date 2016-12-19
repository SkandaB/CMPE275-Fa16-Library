<%--
  Created by IntelliJ IDEA.
  User: Sagar
  Date: 12/7/2016
  Time: 1:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html lang="en">
<style>
    #label {
        padding-top: 220px;
        top: 15px;
        box-sizing: border-box;
        position: absolute;
        /*!*background: blue;*!  left: 5%;*/
        width: 1200px;
        /*border:5px solid #990000;*/
        overflow: hidden;
        border-radius: 10px;
        /*background: rgba(60, 85, 100, 0.7);*/
    }
    .mytext {
        width: 110px;
    }
</style>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href='http://fonts.googleapis.com/css?family=Bitter' rel='stylesheet' type='text/css'>
<link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/css/bootstrap-combined.min.css" rel="stylesheet">
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.0.3/angular.min.js"></script>
<script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>


<script type="text/javascript">
    var array = [];
    array.push("Available");
    array.push("Reserved");
    array.push("Wait-Listed");
    updateBook = function (bookId, isbn, title, author, publisher, num_of_copies, status) {
        var index = array.indexOf(status);
        array.splice(index, 1);
        console.log(array);
        console.log(bookId);
        console.log(isbn);
        console.log(title);
        console.log(author);
        console.log(status);
        document.getElementById("updateBookFromUI").disabled = false;
        //document.getElementById("replacedheaders").style.display = 'block';

        var html = '';
        html = html + '<br><div id="label">';
        html = html + '<form  id="updatebookform"  method="post"  modelAttribute="book" action="${pageContext.request.contextPath}/book/updatebook">';
        html = html + '<tr id = ' + bookId + '>';
        html = html + '<td id="td1"><input type="text" style="width: 35px"  name="bookId" value=' + bookId + '  readonly ></td>&nbsp;&nbsp;';
        html = html + '<td id="td2"><input type="txt" style="width: 120px" name="isbn" value=\"' + isbn + '\"' + ' readonly></td>&nbsp;&nbsp;';
        html = html + '<td id="td3"><input type="text" style="width: 160px" class="mytext" name="title" value=\"' + title + '\"' + '></td>&nbsp;&nbsp;';
        html = html + '<td id="td4"><input type="text" style="width: 160px" class="mytext" name="author" value=\"' + author + '\"' + '></td>&nbsp;&nbsp;';
        html = html + '<td id="td5"><input type="text" style="width: 160px" class="mytext" name="publisher" value=\"' + publisher + '\"' + '></td>&nbsp;&nbsp;';
        html = html + '<td id="td5"><input type="number" min="0"  style="width: 160px" class="mytext" name="num_of_copies" value=\"' + num_of_copies + '\"' + '></td>&nbsp;&nbsp;';
//        html = html + '<td id="td6"><select style="width:120px; color: black"  name="current_status"><option selected="selected" value=\"' + status+ '\"'+'> </option>  </select></td>';
        html = html + '<td id="td6"><input type="text" style="width: 100px" class="mytext" name="current_status" value=\"' + status + '\"' + '></td>&nbsp;&nbsp;';


        html = html + '<td>' + '  <button class="btn btn-info" id=' + bookId + ' onClick="updateBook(\'' + bookId + '\',\'' + bookId + '\',\'' + isbn + '\',\'' + title + '\',\'' + author + '\',\'' + status + '\')">Edit</button> <button type="submit" class="btn btn-success" id="updateBookFromUI" >Update</button>' + '</td>';
        html = html + '</tr>';

        html = html + '</form>';
        html = html + '</div>';
        row = $('#' + bookId);
        row.replaceWith(html);
        //$('#ogiheaders').replaceWith();


    }
</script>


<jsp:include page="../../fragments/header.jsp"/>


<body>


<div class="container">

    <c:if test="${not empty msg}">
        <div class="alert alert-${css} alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <strong>${msg}</strong>
        </div>
    </c:if>
    <nav class="navbar navbar-inverse ">
        <div class="container">
            <div class="navbar-header">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/lmsdashboard">&nbsp; &nbsp;
                    &nbsp; My Dashboard</a>

            </div>

        </div>
    </nav>
    <h1>Librarian Book Search Page</h1>
    <table id="replacedtable" class="table table-striped">
        <thead>
        <tr style="display:none" id="replacedheaders">
            <th>&nbsp;ID &nbsp;&nbsp;</th>
            <th>&nbsp;&nbsp;&nbsp;&nbsp;ISBN&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TITLE&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;AUTHOR&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PUBLISHER</th>
            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# of copies</th>
            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;STATUS</th>
        </tr>
        <tbody id="replacedtablebody">
        </tbody>
        </thead>
    </table>
    <table class="table table-striped">
        <thead>
        <tr id="ogiheaders">
            <th>ID</th>
            <th>ISBN</th>
            <th>TITLE</th>
            <th>AUTHOR</th>
            <th>PUBLISHER</th>
            <th># of copies</th>
            <th>STATUS</th>

        </tr>
        </thead>
        <p><font color="red">${errorMessage}</font></p>
        <c:forEach var="book" items="${books}">
            <tr id="${book.bookId}">
                <td>${book.bookId}</td>
            <td>${book.isbn}</td>
            <td>${book.title}</td>
            <td>${book.author}</td>
                <td>${book.publisher}</td>
                <td>${book.num_of_copies}</td>
            <td>${book.current_status}</td>
            <td>
                <spring:url value="/book/deletebook/${book.bookId}" var="deleteUrl"/>

                <button class="btn btn-info"
                        onClick="updateBook('${book.bookId}', '${book.isbn}', '${book.title}', '${book.author}', '${book.publisher}', '${book.num_of_copies}', '${book.current_status}')">
                    Edit
                </button>
                <button disabled onClick="updateBooks()" class="btn btn-success" id="updateBookFromUI">Update</button>
                <button class="btn btn-danger" onclick="location.href='${deleteUrl}'">Remove</button>
            </td>


        </tr>
        </c:forEach>
    </table>

</div>
<%--<div>--%>
<%--<a href="<c:url value="/logout" />">Logout</a>--%>
<%--</div>--%>
<jsp:include page="../../fragments/footer.jsp"/>

</body>
</html>
