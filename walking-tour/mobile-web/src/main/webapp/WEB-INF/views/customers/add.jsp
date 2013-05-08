<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>

<html>
<head>
    <title>Welcome</title>
</head>
<body>
<h1>
    Add Customer
</h1>

<form:form method="post" commandName="customer">
    <table>
        <tr>
            <td>
                First Name:
            </td>
            <td>

                <form:input path="firstName"/>
            </td>
        </tr>
        <tr>
            <td>
                Last Name:
            </td>
            <td>
                <form:input path="lastName"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Add Customer">

            </td>
        </tr>
    </table>

</form:form>

</body>
</html>