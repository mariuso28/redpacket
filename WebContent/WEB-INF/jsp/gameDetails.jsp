<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
  <title>gameDetails</title>
  <link href="../../css/style.css" rel="stylesheet" type="text/css" />
  <style>
  </style>
</head>

<body>
  <div class="main">

    <form:form method="post" action="processAccount" modelAttribute="gameDetails">
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
  		<div class="headerWelcome">
  			<h2 style="font-weight:700;">
          Game Details for ${gameDetails.game.name} ${gameDetails.game.type}
        </h2>
        <h2>
          Started : <fmt:formatDate value="${gameDetails.gamePlay.started}" pattern="dd-MMM-yy" />
          Ended : <fmt:formatDate value="${gameDetails.gamePlay.ended}" pattern="dd-MMM-yy" />
          <c:if test="${gameDetails.game.type != 'FANTAN'}" >
          Outcome : <c:forEach items="${gameDetails.outcomeImage}" var="img">
                      <img width="40" height="40" src="data:image/png;base64,${img.image}" alt="No image" border='0'>
                      </c:forEach>
          </c:if>
          <c:if test="${gameDetails.game.type == 'FANTAN'}" >
          Outcome : <c:forEach items="${gameDetails.outcomeImage}" var="img">
                      <img width="180" height="40" src="data:image/png;base64,${img.image}" alt="No image" border='0'>
                      </c:forEach>
          </c:if>
  			</h2>
  		</div>
      <div class="navHeading">
        <a href="processAccount?cancelGameDetails"><img src="../../img/back2.png" width="30" height="30"></a>
      </div>

      <c:if test="${gameDetails.game.type == '3KINGS'  || gameDetails.game.type == 'INBETWEEN'}" >
      <div style="height:300px;">
        <table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
          <colgroup>
              <col span="1" style="width: 10%;">
              <col span="1" style="width: 20%;">
              <col span="1" style="width: 20%;">
              <col span="1" style="width: 10%;">
              <col span="1" style="width: 10%;">
              <col span="1" style="width: 10%;">
              <col span="1" style="width: 10%;">
              <col span="1" style="width: 10%;">
          </colgroup>
        	<tr style="color:purple; background-color:#555;">
        		<td align="center"><strong><font color="#FFF" style="bold" size="2">Made</font></strong></td>
        		<td align="center"><strong><font color="#FFF" style="bold" size="2">Player</font></strong></td>
        		<td align="center"><strong><font color="#FFF" style="bold" size="2">Banker</font></strong></td>
        		<td align="center"><strong><font color="#FFF" style="bold" size="2">Type</font></strong></td>
            <td align="right"><strong><font color="#FFF" style="bold" size="2">Stake</font></strong></td>
            <td align="right"><strong><font color="#FFF" style="bold" size="2">Win</font></strong></td>
            <td align="center"><strong><font color="#FFF" style="bold" size="2">cards</font></strong></td>
            <td align="center"><strong><font color="#FFF" style="bold" size="2">dealer</font></strong></td>
        	</tr>
        	<c:forEach items="${gameDetails.bets}" var="tx" varStatus="status">
          <c:set var="dc" value="${gameDetails.dealerImages[status.index]}" scope="request" />
          <c:set var="cc" value="${gameDetails.playerImages[status.index]}" scope="request" />
        	<tr>
        		<td align="center"><fmt:formatDate value="${tx.made}" pattern="dd-MMM-yy" /></td>
            <td align="center">${tx.playerEmail}</td>
            <td align="center">${tx.bankerEmail}</td>
              <c:if test="${tx.type=='W'}">
                    <td align="center">Wager</td>
                </c:if>
                <c:if test="${tx.type=='R'}">
                    <td align="center">Royal</td>
                </c:if>
                <c:if test="${tx.type=='T'}">
                    <td align="center">Tie</td>
                </c:if>
            <td align="right"><fmt:formatNumber value="${tx.stake}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
            <td align="right"><fmt:formatNumber value="${tx.win}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
<!--            <td align="center">${tx.cards}</td>
            <td align="center">${tx.dealerCards}</td> -->

            <td align="center">
            <c:forEach items="${cc}" var="card">
                    <img width="24" height="36" src="data:image/png;base64,${card.image}" alt="No image" border='0'>
            </c:forEach>
            </td>
            <td align="center">
            <c:forEach items="${dc}" var="card">
                    <img width="24" height="36" src="data:image/png;base64,${card.image}" alt="No image" border='0'>
            </c:forEach>
            </td>
        	</tr>
        	</c:forEach>
      </table>
    </div>
  </c:if>
  <c:if test="${gameDetails.game.type == 'FANTAN'}" >
  <div style="height:300px;">
    <table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
      <colgroup>
          <col span="1" style="width: 10%;">
          <col span="1" style="width: 20%;">
          <col span="1" style="width: 20%;">
          <col span="1" style="width: 10%;">
          <col span="1" style="width: 10%;">
          <col span="1" style="width: 10%;">
          <col span="1" style="width: 10%;">
          <col span="1" style="width: 10%;">
          <col span="1" style="width: 10%;">
      </colgroup>
      <tr style="color:purple; background-color:#555;">
        <td align="center"><strong><font color="#FFF" style="bold" size="2">Made</font></strong></td>
        <td align="center"><strong><font color="#FFF" style="bold" size="2">Player</font></strong></td>
        <td align="center"><strong><font color="#FFF" style="bold" size="2">Banker</font></strong></td>
        <td align="center"><strong><font color="#FFF" style="bold" size="2">Type</font></strong></td>
        <td align="right"><strong><font color="#FFF" style="bold" size="2">Stake</font></strong></td>
        <td align="right"><strong><font color="#FFF" style="bold" size="2">Win</font></strong></td>
        <td align="center"><strong><font color="#FFF" style="bold" size="2">face1 </font></strong></td>
        <td align="center"><strong><font color="#FFF" style="bold" size="2">face2 </font></strong></td>
        <td align="center"><strong><font color="#FFF" style="bold" size="2">face3 </font></strong></td>
      </tr>
      <c:forEach items="${gameDetails.bets}" var="tx" varStatus="status">
      <c:set var="cc" value="${gameDetails.playerImages[status.index]}" scope="request" />
      <tr>
        <td align="center"><fmt:formatDate value="${tx.made}" pattern="dd-MMM-yy" /></td>
        <td align="center">${tx.playerEmail}</td>
        <td align="center">${tx.bankerEmail}</td>
        <td align="center">${tx.type}</td>
        <td align="right"><fmt:formatNumber value="${tx.stake}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
        <td align="right"><fmt:formatNumber value="${tx.win}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
        <c:forEach items="${cc}" var="img">
        <td align="center">
              <img width="48 height="24" src="data:image/png;base64,${img.image}" alt="No image" border='0'>
        </td>
      </c:forEach>
      </tr>
      </c:forEach>
  </table>
</div>
</c:if>
<c:if test="${gameDetails.game.type == 'BELANGKAI'}" >
<div style="height:300px;">
  <table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
    <colgroup>
        <col span="1" style="width: 10%;">
        <col span="1" style="width: 20%;">
        <col span="1" style="width: 20%;">
        <col span="1" style="width: 10%;">
        <col span="1" style="width: 10%;">
        <col span="1" style="width: 10%;">
        <col span="1" style="width: 15%;">
        <col span="1" style="width: 15%;">
    </colgroup>
    <tr style="color:purple; background-color:#555;">
      <td align="center"><strong><font color="#FFF" style="bold" size="2">Made</font></strong></td>
      <td align="center"><strong><font color="#FFF" style="bold" size="2">Player</font></strong></td>
      <td align="center"><strong><font color="#FFF" style="bold" size="2">Banker</font></strong></td>
      <td align="center"><strong><font color="#FFF" style="bold" size="2">Type</font></strong></td>
      <td align="right"><strong><font color="#FFF" style="bold" size="2">Stake</font></strong></td>
      <td align="right"><strong><font color="#FFF" style="bold" size="2">Win</font></strong></td>
      <td align="center"><strong><font color="#FFF" style="bold" size="2">face1 </font></strong></td>
      <td align="center"><strong><font color="#FFF" style="bold" size="2">face2 </font></strong></td>
    </tr>
    <c:forEach items="${gameDetails.bets}" var="tx" varStatus="status">
        <c:set var="cc" value="${gameDetails.playerImages[status.index]}" scope="request" />
    <tr>
      <td align="center"><fmt:formatDate value="${tx.made}" pattern="dd-MMM-yy" /></td>
      <td align="center">${tx.playerEmail}</td>
      <td align="center">${tx.bankerEmail}</td>
      <td align="center">${tx.type}</td>
      <td align="right"><fmt:formatNumber value="${tx.stake}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
      <td align="right"><fmt:formatNumber value="${tx.win}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
      <c:forEach items="${cc}" var="img">
          <td align="center">
              <img width="30" height="30" src="data:image/png;base64,${img.image}" alt="No image" border='0'>
          </td>
      </c:forEach>
    </tr>
    </c:forEach>
</table>
</div>
</c:if>
<c:if test="${gameDetails.game.type == 'SICBO'}" >
<div style="height:300px;">
  <table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
    <colgroup>
      <col span="1" style="width: 10%;">
      <col span="1" style="width: 20%;">
      <col span="1" style="width: 20%;">
      <col span="1" style="width: 10%;">
      <col span="1" style="width: 10%;">
      <col span="1" style="width: 10%;">
    </colgroup>
    <tr style="color:purple; background-color:#555;">
      <td align="center"><strong><font color="#FFF" style="bold" size="2">Made</font></strong></td>
      <td align="center"><strong><font color="#FFF" style="bold" size="2">Player</font></strong></td>
      <td align="center"><strong><font color="#FFF" style="bold" size="2">Banker</font></strong></td>
      <td align="center"><strong><font color="#FFF" style="bold" size="2">Type</font></strong></td>
      <td align="right"><strong><font color="#FFF" style="bold" size="2">Stake</font></strong></td>
      <td align="right"><strong><font color="#FFF" style="bold" size="2">Win</font></strong></td>
    </tr>
    <c:forEach items="${gameDetails.bets}" var="tx" varStatus="status">
    <c:set var="cc" value="${gameDetails.playerImages[status.index]}" scope="request" />
    <tr>
      <td align="center"><fmt:formatDate value="${tx.made}" pattern="dd-MMM-yy" /></td>
      <td align="center">${tx.playerEmail}</td>
      <td align="center">${tx.bankerEmail}</td>
      <td align="center">${tx.type}</td>
      <td align="right"><fmt:formatNumber value="${tx.stake}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
      <td align="right"><fmt:formatNumber value="${tx.win}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
    </tr>
    </c:forEach>
</table>
</div>
</c:if>
</form:form>
  </div>
</body>
</html>
