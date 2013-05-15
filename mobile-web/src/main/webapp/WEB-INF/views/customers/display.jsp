<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<h1>
 Look up customer information
</h1>
<form name="customerLookupForm">
    <input type="text" name="customerId"/> <a
    href="javascript: lookupCustomer( document.forms['customerLookupForm'].elements['customerId'].value ) ">Look up
    Customer</a>
</form>

<c:if test="${ not empty  param.id }">
    <h2> Information on the customer # ${customer.id} </h2>
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
</c:if>

<script>

    var baseUrl = (function () {
        var defaultPorts = {"http:": 80, "https:": 443};
        var loc = window.location, protocol = loc.protocol, port = loc.port;
        return protocol + "//" + window.location.hostname + (((port) && (port != defaultPorts[protocol])) ? (":" + port) : "");
    })();

    function lookupCustomer(customerId) {
        var number = parseInt('' + customerId);
        window.location = baseUrl + '/?id=' + number;
    }
</script>
