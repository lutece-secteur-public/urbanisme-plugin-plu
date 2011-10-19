<%@page import="fr.paris.lutece.portal.service.i18n.I18nService"%>
<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="plu" scope="session" class="fr.paris.lutece.plugins.plu.web.PluJspBean" />

<% plu.init( request, plu.RIGHT_MANAGE_PLU ); %>
<%
	if ( request.getParameter( "action" ).equals( "Valider" ) )
		response.sendRedirect(plu.getConfirmEvolveAtome(request));
	else if ( request.getParameter( "action" ).equals( I18nService.getLocalizedString( "plu.evolve_atome.buttonReturn", request.getLocale( ) ) ) )
	{
		response.sendRedirect(plu.getConfirmCancelEvolveAtome(request));
	}
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