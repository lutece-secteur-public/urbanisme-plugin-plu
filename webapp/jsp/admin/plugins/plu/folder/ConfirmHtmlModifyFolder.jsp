<%@page import="fr.paris.lutece.portal.service.i18n.I18nService"%>
<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="plu" scope="session" class="fr.paris.lutece.plugins.plu.web.PluJspBean" />

<%  plu.init( request, plu.RIGHT_MANAGE_PLU ); %>
<%
	if ( request.getParameter( "actionValidate" )!=null )
		response.sendRedirect(plu.getConfirmModifyFolder(request,false));
	else if ( request.getParameter( "actionCancel")!=null )
	{
		response.sendRedirect(plu.getConfirmCancelModifyFolder(request));
	}
	else
	{
%>
<jsp:include page="../../../AdminHeader.jsp" />
<%
			out.print( plu.getCreateHtml( request ) );
%>
<%@ include file="../../../AdminFooter.jsp" %>
<%
	}
%>