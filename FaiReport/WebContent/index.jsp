<%@page import="com.fai.beans.Report"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>报表</title>
<style type="text/css">
table.hovertable {
	font-family: verdana, arial, sans-serif;
	font-size: 13px;
	color: #333333;
	border-width: 1px;
	border-color: #999999;
	border-collapse: collapse;
}

table.hovertable tr {
	background-color: #d4e3e5;
}

table.hovertable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}

table.hovertable th {
	background-color: #c3dde0;
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
</style>
</head>
<body>
	<form action="ShowReport" method="post">
		<input type="submit" value="生成报表">
	</form>
	<table class="hovertable">
		<tr>
			<th colspan="5">报表</th>
		</tr>

		<tr>
			<th>注册人数</th>
			<th>付费人数</th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
		<%
			List list = null;
			if (session.getAttribute("report") != null) {
				list = (List) session.getAttribute("report");
				Report rp;
				for (int i = 0; i < list.size(); i++) {
					rp = new Report();
					rp = (Report) list.get(i);
		%>
		<tr onmouseover="this.style.backgroundColor='#ffff66';"
			onmouseout="this.style.backgroundColor='#d4e3e5';">
			<td><%=rp.getRigNum()%></td>
			<td><%=rp.getPayNmu()%></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<%
			}

			}
		%>
	</table>












</body>
</html>