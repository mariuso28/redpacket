<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page trimDirectiveWhitespaces="true" %>

<html>

<head>

	<link href="../../css/style.css" rel="stylesheet" type="text/css" />

	<title>memberHome</title>

	<style>

	</style>

</head>

<body>
	<div class="main">

	<form:form method="post" id="myForm" action="processAgent" modelAttribute="memberForm">
	      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

	<c:choose>
	<c:when test="${currUser.role.rank != 6}">				<!-- home page -->
		<div class="headerPic">
			<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.png' border='0'>
		</div>
		<div class="headerWelcome">
			<h2 style="color:#aaa; font-weight:700;">
				Hi, ${currUser.role.desc}&nbsp; <font color="${currUser.role.color}">${currUser.contact}</font>!
			</h2>
		</div>
	</c:when>
	<c:otherwise>  <!-- maintain company -->
		<div class="headerWelcome">
			<h2>
		    	${currUser.role.desc}&nbsp; ${currUser.contact} - Maintain Companies
		 	</h2>
		</div>
  	<table>
  		<tr height="20">
  		<td>
  			<a href="../admin/exec?returnAdmin"><img src="../../img/back.jpg" width="50" height="30"></a>
  		</td>
  		</tr>
  		<tr height="20">
  			<td>&nbsp;</td>
  		</tr>
  	</table>
	</c:otherwise>
	</c:choose>
	<table cellspacing="1" cellpadding="5" border="0" bgcolor="#eee" id="shell" height="100" width="100%">
	   <tr height="20px" valign="top" align="left">
		   <td width="100%">
				<table background-color="${currUser.role.color}" width="100%" border="0" cellpadding="3" cellspacing="0" align="left" >
					<tbody style="color:F6DEFF;">
				   	<tr>
							<td style="width:34%; color:#333;">Username (email): <strong>${currUser.email}</strong></td>
							<td style="width:33%; color:#333;">Contact: <strong>${currUser.contact}</strong></td>
							<td style="width:33%; color:#333;">Phone: <strong>${currUser.phone}</strong></td>
				   	</tr>
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#ccc" width="800px">
				<table border="0" cellpadding="3" cellspacing="0">
			      <tr>
			      	<c:choose>
					     <c:when test="${currUser.role.rank <= 5}">
						       <c:set var="linkColor" value="green" scope="page" />
						       <c:choose>
							       <c:when test="${currUser.account.balance < '0'}">
								          <c:set var="linkColor" value="red" scope="page"/>
					           </c:when>
						       </c:choose>
						       <td bgcolor="#d4c66a"><strong>Current Balance:</strong></td>
						       <td bgcolor="#d4c66a" align="right">
											<font color=${linkColor} size="3">
				              		<fmt:formatNumber value="${currUser.account.balance}"  type="number" maxFractionDigits="2" minFractionDigits="2"/>
				           		</font>
						       </td>
				       	  	<td bgcolor="#ffe6aa"><strong>Payment Days:</strong></td>
				       		<td bgcolor="#ffe6aa"><fmt:formatNumber value="${currUser.account.paymentDays}"  type="number" maxFractionDigits="0" /></td>
				           <td bgcolor="#d4b56a"><strong>Credits</strong></td>
				       	  <td bgcolor="#d4b56a">Player:</td>
				       	  <td bgcolor="#d4b56a"><fmt:formatNumber value="${currUser.account.creditAsPlayer}"  type="number" maxFractionDigits="2" minFractionDigits="2"/></td>
				           <td bgcolor="#d4b56a">Banker:</td>
				           <td bgcolor="#d4b56a"><fmt:formatNumber value="${currUser.account.creditAsBanker}"  type="number" maxFractionDigits="2" minFractionDigits="2"/></td>
				           <td bgcolor="#b6a9ce"><strong>Commissions</strong></td>
				           <td bgcolor="#b6a9ce">Bet:</td>
				       	  <td bgcolor="#b6a9ce"><fmt:formatNumber value="${currUser.account.betCommission}"  type="number" maxFractionDigits="2" minFractionDigits="2"/></td>
				           <td bgcolor="#b6a9ce">Win:</td>
				           <td bgcolor="#b6a9ce"><fmt:formatNumber value="${currUser.account.winCommission}"  type="number" maxFractionDigits="2" minFractionDigits="2"/></td>
				       	  	<td>
											<a href="../acc/processAccount?method=rollup">
				       	  			<div class="lozengeButton">
				       		   			Account Summary
				       		   		</div>
											</a>
				       	  </td>
				       	  <c:choose>
				       	  <c:when test="${currUser.role.rank == 5}">
				       	      <td>
												<a href="../acc/processAccount?method=updateComp" >
						       	  		<div class="lozengeButton" style="background-color: #7f4b8e;">
						       	          Update Account
						       		   	</div>
											 </a>
				       	      </td>
				       	  </c:when>
				       	  </c:choose>
							</c:when>
						</c:choose>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#ccc" width="850px">
				<table border="0" cellpadding="3" cellspacing="0" >
					<tr>
						<td>Create Member: </td>
						<td>
						   <form:select path="createRole">
					    		<c:choose>
					    		<c:when test="${currUser.role.rank > 5}">
					    			<option value="ROLE_COMP">Company</option>
					    		</c:when>
					    		<c:otherwise>
					    	        <c:choose>
					    		<c:when test="${currUser.role.rank > 4}">
					    			<option value="ROLE_ZMA">ZMA</option>
					    		</c:when>
					    		</c:choose>
					    		<c:choose>
					    		<c:when test="${currUser.role.rank > 3}">
					    			<option value="ROLE_SMA">SMA</option>
					    		</c:when>
					    		</c:choose>
					    		<c:choose>
					    		<c:when test="${currUser.role.rank > 2}">
					    			<option value="ROLE_MA">MA</option>
					    		</c:when>
					    		</c:choose>
					    		<c:choose>
					    		<c:when test="${currUser.role.rank > 1}">
					    			<option value="ROLE_AGENT">AGENT</option>
					    		</c:when>
					    		</c:choose>
					    		<option value="ROLE_PLAY">PLAYER</option>
					    		</c:otherwise>
					    		</c:choose>
						   </form:select>
					   </td>
			    		<td><input type="submit" name="memberCreate" value="Create" style="font-family: 'Roboto Condensed'; font-size: 14px; font-weight: 700;"/></td>
					</tr>
				</table>
			</td>
		</tr>
	   <tr height="200px" valign="top" align="left">
			<td>
			<div style="height:300px;overflow:auto;overflow-x:hidden;">
				<table border="1" style="width:100%;" align="left">
					<tr bgcolor="#555" >
		      		<td><strong><font color="#FFF" style="bold" size="2">Member Code</font></strong></td>
		      		<td><strong><font color="#FFF" size="2">Type</font></strong></td>
		      		<td><strong><font color="#FFF" size="2">Contact</font></strong></td>
		      		<td><strong><font color="#FFF" size="2">Email(Logon Id)</font></strong></td>
		      		<td align="right"><strong><font color="#FFF" size="2">Balance</font></strong></td>
		      		<td align="right"><strong><font color="#FFF" size="2">Avaliable For Player</font></strong></td>
						<td align="right"><strong><font color="#FFF" size="2">Avaliable For Banker</font></strong></td>
		      		<td align="right"><strong><font color="#FFF" size="2">Outstanding</font></strong></td>
					</tr>
				    <c:choose>
				    <c:when test="${currUser.role.rank > 5}">
			      		<c:set var="editable" value="true" scope="request"/>
			      		<c:set var="tab" value="" scope="request"/>
			      		<c:set var="comps" value="${currUser.comps}" scope="request"/>
			      		<jsp:include page="_includeAll/compsDisplay.jsp"/>
				    </c:when>
				    <c:otherwise>
				    <c:choose>
				    <c:when test="${currUser.role.rank > 4}">
			      		<c:set var="editable" value="true" scope="request"/>
			      		<c:set var="tab" value="" scope="request"/>
			      		<c:set var="zmas" value="${currUser.zmas}" scope="request"/>
			      		<jsp:include page="_includeAll/zmasDisplay.jsp"/>
				    </c:when>
				    </c:choose>
				    <c:choose>
				    <c:when test="${currUser.role.rank > 3}">
			      		<c:set var="editable" value="true" scope="request"/>
			      		<c:set var="tab" value="" scope="request"/>
			      		<c:set var="smas" value="${currUser.smas}" scope="request"/>
			      		<jsp:include page="_includeAll/smasDisplay.jsp"/>
				    </c:when>
				    </c:choose>
				    <c:choose>
				    <c:when test="${currUser.role.rank > 2}">
			      		<c:set var="editable" value="true" scope="request"/>
			      		<c:set var="tab" value="" scope="request"/>
			      		<c:set var="mas" value="${currUser.mas}" scope="request"/>
			      		<jsp:include page="_includeAll/masDisplay.jsp"/>
				    </c:when>
				    </c:choose>
				    <c:choose>
				    <c:when test="${currUser.role.rank > 1}">
			      		<c:set var="editable" value="true" scope="request"/>
			      		<c:set var="tab" value="" scope="request"/>
			      		<c:set var="agents" value="${currUser.agents}" scope="request"/>
			      		<jsp:include page="_includeAll/agentsDisplay.jsp"/>
				    </c:when>
				    </c:choose>
			    	    <c:set var="tab" value="" scope="request"/>
			    	    <c:set var="editable" value="true" scope="request"/>
			    	    <c:set var="players" value="${currUser.players}" scope="request"/>
			    		    <jsp:include page="_includeAll/playersDisplay.jsp"/>
				    </c:otherwise>
			    </c:choose>
				</table>
		</td>
		</tr>
		<tr>
			<c:choose>
				<c:when test="${currUser.role.rank != 6}">
					<td>
						<a href="processAgent?editAgent">
							<div class="lozengeButton" style="background-color:navy;">
								Edit Profile
							</div>
						</a>
						<c:if test="${currUser.role.rank == 5}">
							<a href="processAgent?importCsvs">
								<div class="lozengeButton" style="background-color:orange;">
								 Import Cvs
								</div>
							</a>
							<a href="processAgent?closeOpenInvoices">
								<div class="lozengeButton" style="background-color:orange;">
									Close Open Invoices
								</div>
							</a>
					  </c:if>
						<a href="../logon/signin">
							<div class="lozengeButton" style="background-color:red;">
								Logoff
							</div>
						</a>
					</td>
				</c:when>
			</c:choose>
		</tr>
	</table>
		<br/>
		<font color="red">${memberForm.errMsg}</font>
		<br/>

	</form:form>
	</br>

	</div>  <!--  main -->
</body>
</html>
