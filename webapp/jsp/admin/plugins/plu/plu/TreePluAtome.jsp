<%@ page errorPage="../../../ErrorPage.jsp" %>

<link rel="stylesheet" type="text/css" href="css/plugin-plu/plugin_plu.css" />

<jsp:useBean id="plu" scope="session" class="fr.paris.lutece.plugins.plu.web.PluJspBean" />

<% plu.init( request, plu.RIGHT_MANAGE_PLU ); %>
<% String ret = plu.getTreePluAtome ( request );
if ( ret.contains( "AdminMessage.jsp" ) )
{
	response.sendRedirect( ret );
}
else
{%>
<jsp:include page="../../../AdminHeader.jsp" />
<link rel="stylesheet" type="text/css" href="css/plugin-plu/plugin_plu.css" />
<%
	out.print( ret );
%>
<%@ include file="../../../AdminFooter.jsp" %>
<%
}%>