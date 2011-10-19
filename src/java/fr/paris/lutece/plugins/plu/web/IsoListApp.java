/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.plu.business.iso.IIsoServices;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * PluApp
 */
public class IsoListApp implements XPageApplication
{
    /** Parameters */
    private static final String PROPERTY_PAGE_TITLE = "plu.page_iso.pageTitle";
    private static final String PROPERTY_PAGE_PATH = "plu.page_iso.pageTitle";

    /** Templates */
    private static final String TEMPLATE_XPAGE_ISO_LIST = "skin/plugins/plu/iso_list.html";

    private IIsoServices _isoServices;

    /**
     * Constructor
     */
    public IsoListApp( )
    {
        _isoServices = (IIsoServices) SpringContextService.getBean( "plu.isoServices" );
    }

    /**
     * renvoie la page.
     * @param request le requête http
     * @param nMode le mode
     * @param plugin le Plugin actif
     * @return la page
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage( );

        page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, I18nService.getDefaultLocale( ) ) );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PAGE_PATH, I18nService.getDefaultLocale( ) ) );

        page.setContent( getIsoLists( request ) );

        return page;
    }

    /**
     * Renvoie le code html de la page listant les iso à télécharger
     * @param request the request
     * @return the iso list
     */
    private String getIsoLists( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( "isoList", _isoServices.findList( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_ISO_LIST, request.getLocale( ), model );

        return template.getHtml( );
    }
}
