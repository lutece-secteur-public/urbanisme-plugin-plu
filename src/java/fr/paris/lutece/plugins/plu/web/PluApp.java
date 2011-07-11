/*
 * Copyright (c) 2002-2008, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.plu.web;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import javax.servlet.http.HttpServletRequest;


/**
* PluApp
*/
public class PluApp implements XPageApplication
{
    /** Parameters */
    private static final String PARAMETER_PAGE = "page";
    private static final String PARAMETER_PLU_ID = "id";

    /** Properties */
    private static final String PROPERTY_PAGE_TITLE = "plu.pageTitle";
    private static final String PROPERTY_PAGE_PATH = "plu.pagePathLabel";

    /** Templates */
    private static final String TEMPLATE_XPAGE_PLU = "skin/plugins/plu/page_plu.html";
    private static final String TEMPLATE_XPAGE_PLU_LISTS = "skin/plugins/plu/plu_list.html";

    /** Public Fields */
    public Plugin _plugin;

    /**
     * renvoie la page.
     * @param request le requête http
     * @param nMode le mode
     * @param plugin le Plugin actif
     * @return la page
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );

        String strPluginName = request.getParameter( PARAMETER_PAGE );
        _plugin = PluginService.getPlugin( strPluginName );

        page.setTitle( AppPropertiesService.getProperty( PROPERTY_PAGE_TITLE ) );
        page.setPathLabel( AppPropertiesService.getProperty( PROPERTY_PAGE_PATH ) );

        String strPluId = request.getParameter( PARAMETER_PLU_ID );

        if ( strPluId == null )
        {
            page.setContent( getPluLists( request ) );
        }

        if ( ( strPluId != null ) )
        {
            page.setContent( getPlu( request, strPluId ) );
        }

        return page;
    }

    private String getPluLists( HttpServletRequest request )
    {
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_PLU_LISTS, request.getLocale(  ) );

        return template.getHtml(  );
    }

    private String getPlu( HttpServletRequest request, String strPluId )
    {
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_PLU, request.getLocale(  ) );

        return template.getHtml(  );
    }
}
