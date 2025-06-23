<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <h2>新增機台</h2>

    <c:if test="${not empty error}">
        <p style="color:red;">${error}</p>
    </c:if>

    <form>
        <label>機台名稱：</label>
        <input type="text" name="machineName" value="${param.machineName}" required><br>

        <label>出廠編號：</label>
        <input type="text" name="serialNumber" value="${param.serialNumber}" required><br>

        <label>狀態：</label>
        <input type="text" name="mstatus" value="${param.mstatus}"><br>

        <label>位置：</label>
        <input type="text" name="machineLocation" value="${param.machineLocation}"><br>

        <button type="submit">送出</button>
    </form>
</body>
</html>