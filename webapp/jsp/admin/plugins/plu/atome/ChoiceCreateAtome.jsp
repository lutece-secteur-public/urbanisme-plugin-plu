<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />

<jsp:useBean id="plu" scope="session" class="fr.paris.lutece.plugins.plu.web.PluJspBean" />

<% plu.init( request, plu.RIGHT_MANAGE_PLU ); %>
<%= plu.getChoiceCreateAtome ( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>