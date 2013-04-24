<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<h1>
    Welcome!
</h1>

<ol>
    <LI>
        <a href="javascript:window.location='http://127.0.0.1:8080<%=request.getContextPath()%>/display?id='+prompt('which customerId, please?')">Click
            on this link</a>
        to retrieve information on a customer
    </LI>
    <LI>
        To add a new customer to the system, visit <a href="httP://127.0.0.1:8080<%=request.getContextPath()%>/add">the
        'Add Customer' page</a>.
    </LI>

</ol>
</body>
</html>