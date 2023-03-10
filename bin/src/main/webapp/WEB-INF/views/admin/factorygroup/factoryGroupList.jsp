<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>factoryGroupList</title>
</head>
<body>
공장그룹리스트1
<br>
<c:forEach var="item" items="${list.content}">
	<div>
			<h4>${item.factoryGroupId}, ${item.factoryGroupName}, ${item.groupDesc}, ${item.useFlag}</h4>
	</div>
</c:forEach>

<a href="?page=${list.number-1}">이전</a>
<a class="page-link" href="?page=${list.number+1}">이후</a></li>
</body>
</html>