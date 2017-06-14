<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <title>accountEdit</title>
  <link href="../../css/style.css" rel="stylesheet" type="text/css" />
  <style>
    tr:nth-child(odd) {
      background-color: #ccc;
    }
    tr:nth-child(even) {
      background-color: #aaa;
    }
  </style>

<script type="text/javascript">

function isNumber(s , checkFloat, checkNegative) {
     var found = false;
     var i;
     var dCheck = false;
     for (i = 0; i < s.length; i++)
     {
         var c = s.charAt(i);
         if((c == "-") && (i == 0) && (s.length > 0)) {
           if(checkNegative == false) {
             found = true
           }
         }
         else {
           if( ((c == ".") && (checkFloat == true) && (dCheck == false)))
           {
     	     dCheck = true
           }
           else if (((c < "0") || (c > "9")))
           {
               found = true
           }
       }
     }
     if( s.length == 0)
     {
         found = true
     }

     return found==false;
     }

     function testNumber(testField,fieldName,doubleAllowed) {

		oFormObject = document.forms['myForm'];
   		var field = testField;
   		oformElement = oFormObject.elements[field];
		var value = oformElement.value;
		var isValid = isNumber(value,doubleAllowed,false);
                if (!isValid)
		{
                    alert(fieldName + ' has incorrect format');
		    if (doubleAllowed)
			oformElement.value = '0.00';
		    else
			oformElement.value = '0';
		    return false;
                }
		else
		   return true;
	 }


      function getKey(e)
      {
        if (window.event)
           return window.event.keyCode;
        else if (e)
           return e.which;
        else
           return null;
      }
      function restrictChars(e, obj)
      {
        var CHAR_AFTER_DP = 2;  // number of decimal places
        var validList = "0123456789.";  // allowed characters in field
        var key, keyChar;
        key = getKey(e);
        if (key == null) return true;
        // control keys
        // null, backspace, tab, carriage return, escape
        if ( key==0 || key==8 || key==9 || key==13 || key==27 )
           return true;
        // get character
        if (key==190)
          keyChar = ".";
        else
          keyChar = String.fromCharCode(key);
        // check valid characters
        if (validList.indexOf(keyChar) != -1)
        {
          // check for existing decimal point
          var dp = 0;
          if( (dp = obj.value.indexOf( ".")) > -1)
          {
            if( keyChar == ".")
              return false;  // only one allowed
            else
            {
              // room for more after decimal point?
              if( obj.value.length - dp <= CHAR_AFTER_DP)
                return true;
            }
          }
          else return true;
        }
        // not a valid character
        return false;
      }


</script>

</head>


<body>
  <div class="main">
    <form:form method="post" id="myForm" action="../acc/processAccount.html" modelAttribute="accountDetailsForm" >
    <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />


    <div class="headerPic">
			<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.png' border='0'>
		</div>
		<div class="headerWelcome">
			<h2 style="color:${currAccountUser.role.color}; font-weight:700;">
        ${currUser.role.desc}&nbsp; ${currUser.contact} - Maintain Account for ${currAccountUser.role.desc} ${currAccountUser.contact}
			</h2>
		</div>
    <div class="navHeading">
      <a href="processAccount?method=update&code=${currAccountUser.code}"><img src="../../img/back2.png" width="30" height="30"></a>
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
    </div>

      <table border="0" cellpadding="3" cellspacing="0" width="400">
      <tbody align="left" style="color:purple; background-color:${currAccountUser.role.color}">

        <tr>
      		<c:set var="linkColor" value="green" scope="page" />
      		<c:choose>
      		<c:when test="${member.account.balance < '0'}">
      			<c:set var="linkColor" value="red" scope="page"/>
        </c:when>
      		</c:choose>
      		<td width="70%">Current Balance:</td>
      		<td width="30%">
      		<font color=${linkColor} size="3">
                     		<fmt:formatNumber value="${currAccountUser.account.balance}"
      							 type="number" maxFractionDigits="2" minFractionDigits="2" />
          </font>
      		</td>
    	</tr>

      <tr><!--   onkeydown="return restrictChars(event, this);" -->    	<td width="70%">Credit Limit For Player:</td>
    		<td width="30%"><input type="text" name="command.newAccount.creditAsPlayer" size="10px"
    		value="${accountDetailsForm.prevAccount.creditAsPlayer}"
               />
    		</td>    	</tr>      <c:if test="${currAccountUser.role != 'ROLE_COMP'}">      <tr>        <td width="70%">Max Available:</td>    		<td width="30%">    		<font color=${linkColor} size="3">                   		<fmt:formatNumber value="${accountDetailsForm.availableCreditPlayer}"    							 type="number" maxFractionDigits="2" minFractionDigits="2" />                  	</font>    		</td>      </tr>
      </c:if>

      <tr>
    	<td width="70%">Credit Limit For Banker:</td>
        <td width="30%"><input type="text" name="command.newAccount.creditAsBanker" size="10px"
        value="${accountDetailsForm.prevAccount.creditAsBanker}" />
        </td>
    	</tr>
      <c:if test="${currAccountUser.role != 'ROLE_COMP'}">
      <tr>
        <td width="70%">Max Available:</td>
    		<td width="30%">
    		<font color=${linkColor} size="3">
                   		<fmt:formatNumber value="${accountDetailsForm.availableCreditBanker}"
    							 type="number" maxFractionDigits="2" minFractionDigits="2" />
                  	</font>
    		</td>
      </tr>
      </c:if>

    	<tr>
    		<td>Payment Days:</td>
    		<td>
    		<c:set var="pDay" value="${accountDetailsForm.prevAccount.paymentDays}"/>
    		<form:select path="command.newAccount.paymentDays" value="${pDay}">
    		<c:forEach items="${accountDetailsForm.paymentDays}" var="day" varStatus="status1">
    			<c:set var="dDay" value="${status1.index}" />
    			<option value="${day}" ${dDay == pDay ? 'selected' : ''}>
    				<fmt:formatNumber value="${day}"  type="number" maxFractionDigits="0" minFractionDigits="0"/>
    			</option>
    		</c:forEach>
    		</form:select>
    		</td>
    	</tr>

      <table border="0" cellpadding="3" cellspacing="0" width="400">
      <tbody align="left" style="color:purple; background-color:${currAccountUser.role.color}">

      <tr>
    	<td width="70%">Bet Commission:</td>
    		<td width="30%"><input type="text" name="command.newAccount.betCommission" size="10px"
    		value="${accountDetailsForm.prevAccount.betCommission}" />
    		</td>
    	</tr>
      <c:if test="${currAccountUser.role != 'ROLE_COMP'}">
      <tr>
        <td width="70%">Max Available:</td>
    		<td width="30%">
    		<font color=${linkColor} size="3">
                   		<fmt:formatNumber value="${currUser.account.betCommission}"
    							 type="number" maxFractionDigits="2" minFractionDigits="2" />
        </font>
    		</td>
      </tr>
      </c:if>

      <tr>
    	<td width="70%">Win Commission:</td>
        <td width="30%">
          <input type="text" name="command.newAccount.winCommission" size="10px"
          value="${accountDetailsForm.prevAccount.winCommission}" />
        </td>
    	</tr>
      <c:if test="${currAccountUser.role != 'ROLE_COMP'}">
      <tr>
        <td width="70%">Max Available:</td>
    		<td width="30%">
    		<font color=${linkColor} size="3">
                   		<fmt:formatNumber value="${currUser.account.winCommission}"
    							 type="number" maxFractionDigits="2" minFractionDigits="2" />
        </font>
    		</td>
      </tr>
      </c:if>
    </table>

    <tr><td><font color="red" size="3">${accountDetailsForm.message}</font></td></tr>
    <br/>
    <table border="0" cellpadding="3" cellspacing="0" width="300">
    <tbody align="left" >
    <tr style="background-color:#333;">
    	<td><input type="submit" name="saveAccount" value="Save" style="height:23px;"/></td>
    </tr>
    </tbody>
    </table>
    </form:form>
</div>
</body>
</html>
