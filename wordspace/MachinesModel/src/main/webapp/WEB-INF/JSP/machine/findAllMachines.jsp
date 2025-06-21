<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>機台列表</title>
</head>
<body>
	<h2>機台列表</h2>
	<table border="1">
		<tr>
			<th>機台ID</th>
			<th>名稱</th>
			<th>出廠編號</th>
			<th>狀態</th>
			<th>位置</th>
		</tr>
		<c:forEach var="machine" items="${machines}">
			<tr>
				<td>${machine.machineId}</td>
				<td>${machine.machineName}</td>
				<td>${machine.serialNumber}</td>
				<td>${machine.mstatus}</td>
				<td>${machine.machineLocation}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
