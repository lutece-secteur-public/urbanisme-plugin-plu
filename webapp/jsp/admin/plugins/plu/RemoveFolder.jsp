<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="plu" scope="session" class="fr.paris.lutece.plugins.plu.web.PluJspBean" />

<%
	plu.init( request, plu.RIGHT_MANAGE_PLU );
    response.sendRedirect(plu.getConfirmRemoveFolder(request));
%>