<!DOCTYPE html>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Log In</title>
<!--<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/main_css.css"/>" />-->
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/dist/css/social-share-kit.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/font/web fonts/sansation_light_macroman/stylesheet.css"/>" />
<link rel="stylesheet" href="<c:url value="/resources/font/web fonts/sansation_bold_macroman/stylesheet.css"/>" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>

<style>

#header {
    text-align:center;
	position:relative;
}

#header .bg{
    background-color:black;
    opacity:1;
}

#logo{
    float:none;
}

table {
	width: 500px;
	text-align: left;
}

td {
	padding: 10px;
}

td.col1 {
	width: 200px;
}

td.col2 {
	width: 300px;
}

input.inTable {
	width: 100%
}

#or_div{
	position:relative;
	width:100%;
	text-align:center;
	border-bottom: 1px solid transparent;
	line-height:0.1em;
	margin:20px 0 20px;
}

#or_div span{
	background:aliceblue;
	padding:0 10px;
}

</style>

<script>

function submitForgotPassword()
    {
      email = $('#emailDefault').val();
      if (email=='')
          return false;
      if (!confirm('A reset password will be sent to your email account: ' + email))
        return false;
      window.location.replace("/jack/gz/logon/password?user&email="+email);
      return false;
    }

</script>

</head>
<body>
<form action="<c:url value="/gz/logon/signin" />" method="POST">
  			<input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
	<div id="header">
		<div class="bg"></div>

	</div>
		<h2 style="padding:40px">Log In</h2>
		<div style="display: inline-block; position:relative;">
				<table>
					<tr>
						<td class="col1">Email Address:</td>
          	<td class="col2"><input id="emailDefault" class="inTable form-control" type="text"
							name="email" value="${logon['email']}" /></td>
					</tr>
					<tr>
						<td class="col1">Password:</td>
						<td class="col2"><input class="inTable form-control" type="password"
							name="password" value='88888888'/> </td>
					</tr>
					<tr>
						<td colspan="2" align="center"><input type="submit"
							value="Log In" class="btn btn-primary"/></td>
					</tr>
				</table>
				<input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
				<span style="color:#00569E;">${logon['infoMsg']}</span>
			  <span style="color: #ed1c24;">${logon['errMsg']}
          <c:if test="${(logon['errMsg']=='Authentication Error' || logon['errMsg']=='') && logon['email']!=''}">
              <a href="" onclick="return submitForgotPassword();">
                <font color='#045023'>Forgot Password?</font></a>
          </c:if>
        </span>
		</div>
  </form>
</body>
</html>
