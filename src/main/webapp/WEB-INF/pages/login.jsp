<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Metashake</title>
    <link href="<c:url value="/resources/stylesheets/bootstrap.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/stylesheets/bootstrap-formhelpers.min.css"/>" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/resources/javascript/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/javascript/bootstrap.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/javascript/bootstrap-formhelpers.min.js"/>"></script>
</head>
<body>
<div class="container">
    <div class="panel panel-default panel-signin">
        <div class="panel-heading">
            <h4 class="text-center">Войдите, чтобы перейти к Metashake</h4>
        </div>
        <div class="panel-body">
            <form name='loginForm' class="form-signin" action="<c:url value='/login'/>" method='POST'>
                <input type="text" name='username' class="form-control" placeholder="Логин" required autofocus>
                <input type="password" name='password' class="form-control" placeholder="Пароль" required>
                <input type="hidden" name="authType" value="merchant">
                <button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>
            </form>
        </div>
    </div>
    <div class="panel-footer">
        <c:if test="${not empty error}">
            <div class="alert alert-danger text-center">
                <strong>${error}</strong>
            </div>
        </c:if>
        <c:if test="${not empty msg}">
            <div class="alert alert-warning text-center">
                <strong>${msg}</strong>
            </div>
        </c:if>
    </div>
</div>
</body>
</html>
