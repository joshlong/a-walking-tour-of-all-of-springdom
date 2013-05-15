<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page session="false" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<h1>
    Information on the customer # ${customer.id}
</h1>
<table>
    <tr>
        <td>
            First Name:
        </td>
        <td>
            ${customer.firstName}
        </td>
    </tr>
    <tr>
        <td>
            Last Name:
        </td>
        <td>
            ${customer.lastName}
        </td>
    </tr>
</table>


</body>
</html>