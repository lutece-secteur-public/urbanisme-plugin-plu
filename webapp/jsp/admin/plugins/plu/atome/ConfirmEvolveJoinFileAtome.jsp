<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="plu" scope="session" class="fr.paris.lutece.plugins.plu.web.PluJspBean" />

<% plu.init( request, plu.RIGHT_MANAGE_PLU ); %>
<%
	if ( request.getParameter( "action" ).equals( "Valider" ) )
		response.sendRedirect(plu.getConfirmEvolveAtome(request));
	else {
%>
<jsp:include page="../../../AdminHeader.jsp" />
<%
		out.print(plu.getJoinFile(request));
%>
<%@ include file="../../../AdminFooter.jsp" %>
<%
	}
%>