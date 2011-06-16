<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />

<link rel="stylesheet" type="text/css" href="css/plugin-plu/plugin_plu.css" />

<jsp:useBean id="plu" scope="session" class="fr.paris.lutece.plugins.plu.web.PluJspBean" />

<% plu.init( request, plu.RIGHT_MANAGE_PLU ); %>
<%= plu.getCorrectAtome ( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>