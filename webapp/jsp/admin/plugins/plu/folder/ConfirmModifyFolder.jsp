<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="plu" scope="session" class="fr.paris.lutece.plugins.plu.web.PluJspBean" />

<% plu.init( request, plu.RIGHT_MANAGE_PLU ); %>
<%
	if ( request.getParameter( "actionValidate" )!=null )
   		response.sendRedirect(plu.getConfirmModifyFolder(request,false));
	else{
%>
<jsp:include page="../../../AdminHeader.jsp" />
<%
	out.print( plu.getCreateHtml( request ) );
%>
<%@ include file="../../../AdminFooter.jsp" %>
<%	} %>