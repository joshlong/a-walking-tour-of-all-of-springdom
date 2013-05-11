<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html>
<html>
<head>
 

    <style type="text/css">
        body {
            font-size: larger;
        }
    </style>

</head>
<body>
 
<tiles:insertAttribute name="content"/>

<br/>

<tiles:insertAttribute name="footer"/>
</body>
</html>