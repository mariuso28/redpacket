<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Category" content="text/html; charset=utf-8" />
	<title>adminScheduleDownTime</title>

	<script type="text/javascript" src="../../scripts/jquery-1.7.1.js"></script>
	<script type="text/javascript" src="../../scripts/jquery.ui.core.js"></script>
	<script type="text/javascript" src="../../scripts/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="../../scripts/jquery.ui.datepicker.js"></script>
	<script type="text/javascript" src="../../scripts/DateTimePicker.js"></script>

	<link type="text/css" href="../../css/jquery.ui.theme.css" rel="stylesheet" />
	<link type="text/css" href="../../css/jquery.ui.datepicker.css" rel="stylesheet" />
	<link type="text/css" href="../../css/demos.css" rel="stylesheet" />


		<script type="text/javascript">

		    	$(function () {
		    		$("#downtime").datetimepicker();
		    	});

		</script>


		<style category="text/css">
			@font-face {
				font-family: myFont;
				src:url(../../fonts/Exo2-Regular.otf);
				font-weight: normal;
				font-style: normal;
			}

			* {
				margin: 0;
			}

			html, body, input, select {
				height: 100%;
				font-family: myFont;
				font-size: large;
			}

			table {
				vertical-align: top;
			}

			.wrapper {
				min-height: 100%;
				height: auto !important;
				height: 100%;
				margin: 0 auto -2em;
			}

			.modelSelection {
				width: 100%;
				height: 40px;
			}

			.inputwrapper{
				width: 800px;
				height: 100%;
			}

			.inputcolumn{
				float: left;
				width: 800px;
			}

			.section {
				width: 100%;
				height: 100%;
				border: solid;
				border-top-width: 1px;
				border-right-width: 1px;
				border-bottom-width: 1px;
				border-left-width: 1px;
			}

			.spacer5px {
				width: 100%;
				height: 5px;

			}

			.vspacer5px {
				float: left;
				height: 100%;
				width: 5px;

			}

			.heading {
				font-family: myFont;
				font-size: large;
				font-weight: bold;
				text-align: left;
				color: #000;
				background-color: #f6941e;
				padding-left: 3px;
			}

			.outer {
				display: table;
				position: absolute;
				height: 100%;
				width: 100%;
			}

			.middle {
				display: table-cell;
				vertical-align: top;
			}

			.inner {
				margin-left: auto;
				margin-right: auto;
				width: 1100px;
				height: 130px;
				border: 2px solid #00aef0;
			}

			.publish {
			 	float:left;
			    width: 780px;
			    height: 700px;
				padding: 10px;
				vertical-align: top;
			}

			.lower {
			    float:left;
			    width: 780px;
			    height: 100px;
				padding: 10px;
			}
			.downTimeEntry {
			    padding-top: 5px;
			    padding-left: 5px;

			}


			.headerLogo {
				float: left;
				width: 170px;
				height: 130px;
				text-align: right;
				padding-right: 10px;
				background-color: #00aef0;
			}

			.topHeading {
				font-family: myFont;
				height: 90px;
				width: 895px;
				background-color: #00aef0;
				font-size: xx-large;
				font-style: normal;
				font-weight: bold;
				color: #fff;
				padding-left: 25px;
				padding-top: 40px;
				float: left;
				box-sizing: content-box;
			}

		</style>

        <link rel="stylesheet" href="layout.css" ... />
</head>
 <body>

	<form:form id="myForm" method="post" action="exec" modelAttribute="adminDownTimeForm">

		<input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
    
	<div class="modelSelection">

		<fmt:formatDate value="${adminDownTimeForm.scheduledDownTime}" pattern="MM/dd/yyyy HH:mm:ss" var="d1" />

		<div class="inner">
    <div class="topHeading">
          <a href="/jack/gz/admin/exec?returnAdmin"><img src="../../img/back2.png" width="30" height="30"></a>
			Schedule Down Time For All Gaming (Games Will Not be Restarted After)
			</div>   <!-- topHeading -->
			<div class="headerLogo">
				 <img src="../../img/Admin.jpg" width="120" height="120" />
			</div>  <!-- headerLogo -->

        <div class="publish">
						<div class="inputwrapper">
				<div class="inputcolumn">
					<td width="430px">
						<div class="spacer5px">
						</div>
						<div class="section">
							<table width="430px" height="300px">
								<tr>
									<div class="heading">
                      <c:choose>
                        <c:when test="${adminDownTimeForm.downTimeSet}">
                          Down Time Currently Scheduled for :
                            <fmt:formatDate value="${adminDownTimeForm.scheduledDownTime}" pattern="dd-MMM-yy HH:mm"/></td>
                        </c:when>
                        <c:otherwise>
                          Down Time Currently Not Scheduled
                        </c:otherwise>
                      </c:choose>
                  </div>
								</tr>
              <br/>
  								<tr>
  									<div class="downTimeEntry">Schedule
                      <input readonly	id="downtime" style="width:250px; height: 20px;" type="text"
                        name="command.scheduledDownTime" value="${d1}"/></div>
  								</tr>
                  <input type="hidden" name="command.scheduledDownTime" value="${d1}"/>
							</table>
						</div>
					</td>
					<table width="600px">
						<tr>
							<td>
							<input type="submit" name="submitDownTime"
									value="Set Down Time" class="button" style="height:30px;"/>
							</td>
							<td><input type="submit" name="cancelDownTime"
									value="Cancel Scheduled Down Time" class="button" style="height:30px;"/>
							</td>
						</tr>
            <br/>
            <tr>
              <td>
                ${adminDownTimeForm.versionCode}
              </td>
            </tr>
            <tr>
              <td>
                <input name="command.versionCode" type="text" size="68" maxlength="68"
                  style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
              </td>
              <td><input type="submit" name="updateVersionCode"
                  value="Update Version Code" class="button" style="height:30px;"/>
              </td>
              </br>
              </br>
            </tr>
            <tr>
              <td colspan="3"><font color="red">${adminDownTimeForm.errMsg}</font></td>
            </tr>
            <tr>
              <td colspan="3"><font color="blue">${adminDownTimeForm.infoMsg}</font></td>
            </tr>
					</table>
				</div>
			</div>
		</div>
		</div>
		</div>
	</form:form>
</body>
</html>
