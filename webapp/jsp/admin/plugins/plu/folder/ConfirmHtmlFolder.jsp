<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="plu" scope="session" class="fr.paris.lutece.plugins.plu.web.PluJspBean" />

<% plu.init( request, plu.RIGHT_MANAGE_PLU ); %>
<%
	if ( request.getParameter( "action" ).equals( "Valider" ) )
		response.sendRedirect(plu.getConfirmCreateFolder(request));
	else
	{
%>
<jsp:include page="../../../AdminHeader.jsp" />
<%
		if ( request.getParameter( "action" ).equals( "Créer" ) || request.getParameter( "action" ).equals( "Modifier" ) )
		{
			out.print( plu.getCreateHtml( request ) );
		}
		else if (  request.getParameter( "action" ).equals( "Importer" ) )
		{
			out.print( plu.getImportHtml( request ) );
		}
		else if (  request.getParameter( "action" ).equals( "Dupliquer" ) )
		{
			out.print( plu.getDuplicateHtml( request ) );
		}
%>
<%@ include file="../../../AdminFooter.jsp" %>
<%
	}
%>