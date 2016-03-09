<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title></title>
    <link href="<c:url value="/resources/stylesheets/bootstrap.css"/>" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/resources/javascript/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/javascript/bootstrap.js"/>"></script>
</head>
<body>
<div class="container">
    <div id="content" class="panel panel-default">
        <a href="<c:url value="/upload"/>">Upload audio</a>
        <a href="<c:url value="/logout"/>">Logout</a>
        <div class="panel-body">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <td>Customer</td>
                    <td>Request time</td>
                    <%--<td>Acr id</td>--%>
                    <td>Title</td>
                    <td>Label</td>
                    <td>Duration</td>
                    <td>Release date</td>
                    <td>Album</td>
                    <td>Genres</td>
                    <td>Artists</td>
                    <td>Play offset ms</td>
                    <td>ISRC code</td>
                    <td>UPC code</td>
                </tr>
                </thead>
                <tbody>
                <tbody>
                <c:forEach items="${musicRecs}" var="m" varStatus="loop">
                    <tr>
                        <td>${m.customer}</td>
                        <td>${m.recDate}</td>
                        <%--<td>${m.music.acrid}</td>--%>
                        <td>${m.music.title}</td>
                        <td>${m.music.label}</td>
                        <td>${m.music.duration}</td>
                        <td>${m.music.releaseDate}</td>
                        <td>${m.music.album}</td>
                        <td>${m.music.genres}</td>
                        <td>${m.music.artists}</td>
                        <td>${m.music.playOffset}</td>
                        <td>${m.music.isrcCode}</td>
                        <td>${m.music.upcCode}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>