<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<style>
table {
  border-collapse: collapse;
  width: 100%;
  height: 50%;
}

table, th, td {
  border: 1px solid black;
}
</style>
</head>
<body>
<form method="POST" action="bankDet" onSubmit="return validateDataEntry();">
		${cardMessage}<br />
		Bank Name : <input type="text" name="bankName" id="bankName" /><br /><br />

		Card No :   <input type="text" name="cardNo" id="cardNo" style="margin-left:2%"/><br /><br />

		Expire Date : <input type="text" name="expireDate" id="expireDate" /><br /><br />

		<input type="submit" value="Submit"  style="margin-left:10%"/><br />
		
</form>
<br/>
<label style="margin-left:10%;">OR</label>
<br/>
<form action="import" method="post" enctype="multipart/form-data" onSubmit="return validateFileUpload();">
    <input type="file" name="file" id="file"/>
    <input type="submit" value="Import" />
</form>
<br/>
<table>
		    <tr>
		        <th>Bank</th>
		        <th>Card Number</th>
		        <th>Expire Date</th>
		    </tr>
			<c:forEach items="${bnkDet}" var="bnkDet">
			   <tr>
	       		 <td> ${bnkDet.bankName} </td>
			   	 <td> ${bnkDet.cardNo}  </td>
			     <td> ${bnkDet.expireDate}</td>
			     </tr>
			</c:forEach>
</table>
		
<script>
function validateDataEntry() {
	var bankName = $("#bankName").val();
	var cardNo = $("#cardNo").val();
	var expireDate = $("#expireDate").val();
	var bankRegex =  /^[a-zA-Z ]+$/;
   	var cardRegex = /^[- 0-9]+$/;
     	var expireDateRegex = /^[-0-9a-zA-Z]+$/;
  
	if(bankName == ""){
		alert("Please enter bank name field");
        return false;
	}else if(cardNo== ""){ 
		alert("Please enter card no field");
        return false;
	}else if(expireDate== ""){
		alert("Please enter expire date field");
        return false;
	}else if (!bankRegex.test(bankName)){
    	alert("Please Enter valid Bank Name");
        return false;
    }else if (!cardRegex.test(cardNo)){
    	alert("Please Enter valid Card Number");
        return false;
    }else if (!expireDateRegex.test(expireDate)){
    	alert("Please Enter valid Expire Date");
        return false;
    }
   // return luhnCheck(cardNo);
}
function validateFileUpload(){
	var file = $("#file").val();
	 if (file.length == 0) { 
		 alert('Please select the file');
		 return false;
	 }
}
function luhnCheck(val) {
    var sum = 0;
    for (var i = 0; i < val.length; i++) {
        var intVal = parseInt(val.substr(i, 1));
        if (i % 2 == 0) {
            intVal *= 2;
            if (intVal > 9) {
                intVal = 1 + (intVal % 10);
            }
        }
        sum += intVal;
    }
    return (sum % 10) == 0;
} 
function onSubmitFun() {
			var bnkDet = {};
			bnkDet.bankName = $("#bankName").val();
			bnkDet.cardNo = $("#cardNo").val();
			bnkDet.expireDate = $("#expireDate").val();

			$.ajax({
				type : "POST",
				dataType : "json",
				url : "/bankDet",
				data : bnkDet,
				async : true,
				cache : false,
				success : function(msg) {
					console.log(msg);
				}
			});
}
</script>

</body>
</html>
