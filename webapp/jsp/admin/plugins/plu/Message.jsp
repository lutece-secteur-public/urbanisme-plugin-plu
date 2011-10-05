<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="plu" scope="session" class="fr.paris.lutece.plugins.plu.web.PluJspBean" />

<jsp:include page="../../AdminHeader.jsp" />

<link rel="stylesheet" type="text/css" href="css/plugin-plu/plugin_plu.css" />


<% plu.init( request, plu.RIGHT_MANAGE_PLU ); %>
<%= plu.getMessage( request ) %>

<%@ include file="../../AdminFooter.jsp" %>