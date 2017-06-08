<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page trimDirectiveWhitespaces="true" %>

<html>
<head>
    <title>adminHome</title>
<style>
th{
text-align:center;

}

body{
font: 20px Arial, sans-serif;
}
</style>
</head>


<body>

  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
  <script type="text/javascript">


  function isNumber(s , checkFloat, checkNegative)
  {
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

  function modifyGameDuration(gameId,index)
  {

    oFormObject = document.forms['myForm'];
    var field = 'command.durations[' + index + ']' ;
    oformElement = oFormObject.elements[field];
    isValid = isNumber(oformElement.value,false,false);
    if (!isValid)
    {
      alert("Invalid Duration");
      return false;
    }

    alert('Duration will be modified to : ' + oformElement.value + ' from next game played.');

    var field = 'command.gameId' ;
    oformElement = oFormObject.elements[field];
    oformElement.value = gameId;


    var field = 'command.durationIndex' ;
    oformElement = oFormObject.elements[field];
    oformElement.value = index;

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='modifyDuration';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();

    return false;
  }

  function showGameStatus(status)
  {
      alert("Game State : " + status);
      return false;
  }

  </script>


<form:form method="post" id="myForm" action="exec" modelAttribute="adminForm">

  <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

<h2 style="background-color:${currUser.role.color}">Hi ${currUser.role.desc}&nbsp; ${currUser.contact}

&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.jpg' border='0'>
</h2>

  <input type="hidden" name="command.gameId"  value="" />
  <input type="hidden" name="command.durationIndex"  value="-1" />


<table border="0" cellpadding="3" cellspacing="0" width="600">
<tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
<tr><td width="30%">Logon Id (email):</td><td width="70%">${currUser.email}</td></tr>
<tr><td width="30%">Contact:</td>
  <td width="30%"><input name="command.profile.contact" type="text" value="${currUser.contact}" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
<tr><td width="30%">Password:</td>
  <td width="30%"><input name="command.profile.password" type="password" value="" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
<tr><td width="30%">Verify Password:</td>
  <td width="30%"><input name="command.verifyPassword" type="password" value="" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
<tr>
  <td width="30%">Phone:</td><td width="30%">
  <input name="command.profile.phone" type="phone" value="${currUser.phone}" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
<tr>
  <td width="30%">
    <input type="submit" name="modifyAdmin" value="Modify Profile" class="button" style="height:23px;"/>
  </td>
</tr>
</tbody>
</table>
<br/>
<tr><td><font color="red">${adminForm.errMsg}</font></td></tr>
<br/>
<table border="0" cellpadding="3" cellspacing="0" width="1100">
	<tbody align="center" style="font-family:verdana; color:purple; background-color:LightCyan">
	<tr>
    <td width="10">+/-</td>
		<td width="200">Room</td>
		<td width="100">Type</td>
		<td width="30">No.</td>
    <td width="150">Min Stake</td>
    <td width="150">Max Stake</td>
    <td width="300">Owner</td>
    <td width="60">Games</td>
    <td width="100">Status</td>
	</tr>
	</tbody>
	<tbody align="left" style="font-family:verdana; color:purple">
	<c:forEach items="${currRoomProfiles}" var="room" varStatus="status">
    <c:set var="cnt" value="${status.index}"/>
	  <tr>
      <c:choose>
        <c:when test="${currRoomExpandedGames[cnt]==true}">
          <td width="10"><a href="../admin/shrinkRoom?id=${room.id}"
            style="text-decoration:none;color:rgb(255,0,0)">-</a></td>
        </c:when>
        <c:otherwise>
          <td width="10"><a href="../admin/expandRoom?id=${room.id}"
            style="text-decoration:none;color:rgb(255,0,0)">+</a></td>
        </c:otherwise>
      </c:choose>
      <td width="200" align="left">
        ${room.name}</td>
  		<td width="100" align="left">${room.type}</td>
  		<td width="30">${room.roomNum}</td>
  		<td width="150"><fmt:formatNumber value="${room.minStake}" pattern="#0.00" /></td>
      <td width="150"><fmt:formatNumber value="${room.maxStake}" pattern="#0.00" /></td>
      <td width="300">${room.owner}</td>
      <td width="60">${fn:length(room.activeGames)}</td>
      <c:choose>
        <c:when test="${room.activated==true}">
          <td width="100"><a href="../admin/deactivateRoom?id=${room.id}" onclick="return confirm('All Games for this Room will also be Deactivated!!\n Are you sure?')">Deactivate</a></td>
        </c:when>
        <c:otherwise>
          <td width="100"><a href="../admin/activateRoom?id=${room.id}" onclick="return confirm('Are you sure? \n(Games for this Room will also NEED to be Manually Activated)')">Activate</a></td>
        </c:otherwise>
      </c:choose>
    </tr>
      <c:if test="${currRoomExpandedGames[cnt]==true}">
      <tr>
      <td colspan="9">
      <table border="0" cellpadding="3" cellspacing="0" width="1100">
      	<tbody align="center" style="font-family:verdana; color:blue; background-color:LightGreen">
      	<tr>
          <td width="300">Game</td>
          <td width="50">Duration</td>
          <td width="50"></td>
          <td width="300">Banker</td>
          <td width="300">Players</td>
          <td width="100">State</td>
          <td width="50">Status</td>
      	</tr>
      	</tbody>
      	<tbody align="right" style="font-family:verdana; color:black">
      	<c:forEach items="${room.activeGames}" var="game" varStatus="status1">
          <tr>
            <td width="300" align="left">${game.value.name}</td>
            <td width="50">
            <input name="command.durations[${status1.index}]" type="text" value="${game.value.duration}" size="5" maxlength="4"
              style="width:50; height:24px; font-family:inherit; font-size: 14px;"
             />
            </td>
        	  <td width="50"><input type="submit"
                  value="Change" class="button" style="height:23px;"
                    onClick="return modifyGameDuration('${game.value.id}',${status1.index})" /></td>
            <td width="300" align="left">${game.value.banker.email}</td>
            <td width="300" align="left">
              <select path="">
					      <c:forEach items="${game.value.players}" var="player" >
				  		 		<option value="">
					      		${player.value.email}
				  		 		</option>
				  		 	</c:forEach>
		  		 		</select>
            </td>
            <td>
              ${game.value.gameStatus}
            </td>
            <c:choose>
              <c:when test="${game.value.activated==true}">
                <td width="50" align="left"><a href="../admin/deactivateGame?id=${game.value.id}&amp;roomid=${room.id}" onclick="return confirm('Are you sure?')">Deactivate</a></td>
              </c:when>
              <c:otherwise>
                <td width="50" align="left"><a href="../admin/activateGame?id=${game.value.id}&amp;roomid=${room.id}" onclick="return confirm('Are you sure?')">Activate</a></td>
              </c:otherwise>
            </c:choose>
            </tr>
      	</c:forEach>
      	</tbody>
      	</table>
      </td>
      </tr>
      </c:if>

	</c:forEach>
	</tbody>
	</table>
</br>
</br>
<tr>
<c:choose>
  <c:when test="${adminForm.downTimeSet}">
    Down Time Next For Gaming Scheduled for :
      <fmt:formatDate value="${adminForm.scheduledDownTime}" pattern="dd-MMM-yy HH:mm"/></td>
  </c:when>
  <c:otherwise>
    Down Time For Gaming Currently Not Scheduled
  </c:otherwise>
</c:choose>
</tr>
</br>
</br>
<tr>
  <td><a href="../agnt/viewCompanies">Maintain Companies</a></td>
</tr>
<tr>
  <td><a href="../admin/scheduleDownTime">System Maintenance</a></td>
</tr>
</br>
<!--
<tr>
<td><a href="../admin/startGames">Start Games</a></td>
</tr>
-->
</br>
<tr>
<td><a href="../logon/signin">Logoff</a></td>
</tr>


</form:form>
</body>
</html>
