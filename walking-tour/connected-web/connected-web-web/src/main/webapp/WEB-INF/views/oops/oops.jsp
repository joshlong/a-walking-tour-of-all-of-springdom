<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page session="false" %>

<div class="normalBody">
    <h1> <spring:message code="oops.oops"/></h1>

    <P>
     <spring:message code="oops.something-went-wrong"/>
    </P>

    <P>
        <spring:message code="oops.full-exception-is"/>

    </P>
    <BLOCKQUOTE><CODE style="font-size: smaller; ">
        ${exception}
    </CODE></BLOCKQUOTE>
</div>




