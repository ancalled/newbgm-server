<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Upload audio file</title>
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
            <h4 class="text-center">Upload audio file</h4>
        </div>
        <div class="panel-body">
            <form class="form-signin" action="upload-audio" method="post" enctype="multipart/form-data">
                <input type="text" name='audioTitle' class="form-control" placeholder="Audio Title" required>
                <input type="text" name='artist' class="form-control" placeholder="artist" required>
                <input type="text" name='album' class="form-control" placeholder="album" required>
                <input type="text" name='releaseDate' class="form-control" placeholder="releaseDate like yyyy-MM-dd" required>
                <input type="file" name="file" class="form-control" placeholder="Choose file to upload" required>
                <button class="btn btn-lg btn-primary btn-block" type="submit">Upload</button>
            </form>
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
</div>
</body>
</html>
