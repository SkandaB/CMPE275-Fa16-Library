<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>




<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Dhanya And Skanda - Phone Page</title>
</head>
<body>
	<script>
		function deleteFunction() {

			alert("in delete function of phone");

			var deleteurl = "phone/${phone.id}";

			var xmlhttp;

			alert(window.location.origin);

			alert(window.location.pathname);

			alert(deleteurl);

			xmlhttp = new XMLHttpRequest();

			xmlhttp.onreadystatechange = function() {

				if (this.readyState == 4) {

					var baseurl = window.location.origin
							+ window.location.pathname;

					window.location = window.location.origin + "/lab2-1.0/phone";
				}

			};

			xmlhttp.open("DELETE", deleteurl, true);

			xmlhttp.send();

		}
	</script>

	<!-- <script>
		function updateFunction() {

			var number = document.getElementById("number").value;
			var description = document.getElementById("description").value;

			var city = document.getElementById("city").value;
			var state = document.getElementById("state").value;
			var street = document.getElementById("street").value;
			var zip = document.getElementById("zip_code").value;
			var users = document.getElementById("users").value;
			alert(users);

			
			var updateurl = "/phone/${phone.id}?number=" + number
					+ "&description=" + description + "&city=" + city
					+ "&state=" + state + "&street=" + street + "&zip_code="
					+ zip + "&uid=" + users;
			var xmlhttp;
			xmlhttp = new XMLHttpRequest();
			xmlhttp.onreadystatechange = function() {

				if (this.readyState == 4) {

					var baseurl = window.location.origin
							+ window.location.pathname;

					window.location = window.location.origin + "/phone/${phone.id}";
				}

			};
			
			xmlhttp.open("POST", updateurl, true);

			xmlhttp.send();

		}
	</script>
	 -->
	<script>
	function updateFunction() {
		var number = document.getElementById("number").value;
		var description = document.getElementById("description").value;
		var userIds="";
		var city = document.getElementById("city").value;
		var state = document.getElementById("state").value;
		var street = document.getElementById("street").value;
		var zip = document.getElementById("zip_code").value;
		var chk_arr =  document.getElementsByName("selectedUser");
		var chklength = chk_arr.length;             

		for(k=0;k< chklength;k++) {
		    if(chk_arr[k].checked) {
		    	if(userIds == "") {
				    userIds=chk_arr[k].value;
		    	} else {
		   			userIds=userIds+","+chk_arr[k].value;
		    	}
		    }
		} 

		var xmlhttp;
		alert("userIds=="+userIds);
		var updateurl = "phone/${phone.id}?number=" + number
		+ "&description=" + description + "&city=" + city
		+ "&state=" + state + "&street=" + street + "&zip_code="
		+ zip + "&uid=" +userIds;

		xmlhttp = new XMLHttpRequest();
		xmlhttp.onreadystatechange = function() {

			if (this.readyState == 4) {

				var baseurl = window.location.origin
						+ window.location.pathname;

				window.location = window.location.origin + "/lab2-1.0/phone/${phone.id}";
			}

		};
		xmlhttp.open("POST", updateurl, true);
		xmlhttp.send();
	}
	</script>
	
	<form name="submitForm" method="DELETE"></form>
	<form action="phone" method="POST">
		ID : ${phone.id} </br> Number : <input id="number" name="number"
			type="text" value=${phone.number}> </br> Description : <input
			id="description" name="description" type="text"
			value=${phone.description} > </br> Address </br> City : <input
			id="city" name="city" type="text" value=${phone.address.city} >
		State : <input id="state" name="state" type="text"
			value=${phone.address.state}> Street : <input id="street"
			name="street" type="text" value=${phone.address.street}> Zip
		code : <input id="zip_code" name="zip_code" type="text"
			value=${phone.address.zip}> </br>

		<c:forEach items="${users}" var="users">
		
	  <input type="checkbox" name="selectedUser" value="${users.id}" />${users.lastname},  ${users.firstname}<br>
	  		
		</c:forEach>


		<button type="button" name="update" onclick="updateFunction();">Update</button>

		<button type="button" name="delete" onclick="deleteFunction();">Delete</button>
</body>
</html>