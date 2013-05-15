<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title></title>
    <style>
        body {
            margin: 0px;
            overflow: hidden
        }
    </style>
    <script language="javascript" src="<%=request.getContextPath()%>/swfobject.js"></script>
    <script type="text/javascript" language="javascript">
        function myOnLoad() {
            var host = "<%=request.getServerName()%>";
            var contextPath = "<%=request.getContextPath()%>";
            var so = new SWFObject(contextPath + "/client.swf", "client", "100%", "100%", "8", "#000000");
            so.addParam("scale", "noscale");
            so.addParam('width', '100%');
            so.addParam('height', '100%')
            so.addParam('flashVars', 'contextPath=' + contextPath + '&host=' + host);

            so.write("client");

        }
    </script>

</head>
<body scroll="no" onload="myOnLoad()">
<div id="client"></div>


</body>
</html>


                