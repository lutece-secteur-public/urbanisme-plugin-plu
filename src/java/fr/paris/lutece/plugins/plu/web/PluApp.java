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

import fr.paris.lutece.plugins.plu.business.plu.PluServices;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * PluApp
 */
public class PluApp implements XPageApplication
{
    /** Parameters */
    private static final String PARAMETER_DATE_APPLICATION_DEBUT = "dateApplicationDebut";
    private static final String PARAMETER_DATE_APPLICATION_FIN = "dateApplicationFin";
    private static final String PARAMETER_SEARCH = "search";
    private static final String PARAMETER_ERRORS = "errors";
    private static final String PARAMETER_LIST_PLU = "listPlu";

    /** Properties */
    private static final String PROPERTY_PAGE_TITLE = "plu.search.title";
    private static final String PROPERTY_PAGE_PATH = "plu.search.title";
    private static final String PROPERTY_ERROR_DATE_REQUIRED = "plu.error.date.required";
    private static final String PROPERTY_ERROR_DATE_FORMAT = "plu.error.date.format";

    /** Templates */
    private static final String TEMPLATE_XPAGE_PLU_SEARCH = "skin/plugins/plu/plu_search.html";

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

        if ( request.getParameter( PARAMETER_SEARCH ) != null )
        {
            page.setContent( doPluSearch( request ) );
        }
        else
        {
            page.setContent( getPluSearch( request ) );
        }

        return page;
    }

    /**
     * @param request the request
     * @return the search
     */
    private String getPluSearch( HttpServletRequest request )
    {
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_PLU_SEARCH );

        return template.getHtml( );
    }

    
    /**
     * @param request the request
     * @return the search
     */
    private String doPluSearch( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        String sDateDebut = request.getParameter( PARAMETER_DATE_APPLICATION_DEBUT );
        String sDateFin = request.getParameter( PARAMETER_DATE_APPLICATION_FIN );
        model.put( PARAMETER_DATE_APPLICATION_DEBUT, sDateDebut );
        model.put( PARAMETER_DATE_APPLICATION_FIN, sDateFin );

        List<String> errors = new ArrayList<String>( );

        if ( StringUtils.isEmpty( sDateDebut ) || StringUtils.isEmpty( sDateFin ) )
        {
            errors.add( I18nService.getLocalizedString( PROPERTY_ERROR_DATE_REQUIRED, I18nService.getDefaultLocale( ) ) );
        }
        else
        {
            try
            {
                Date dateDebut;
                SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
                dateDebut = formatter.parse( sDateDebut );

                Date dateFin;
                dateFin = formatter.parse( sDateFin );

                model.put( PARAMETER_LIST_PLU, PluServices.getInstance( ).findWithFilters( dateDebut, dateFin ) );
            }
            catch ( ParseException e )
            {
                errors.add( I18nService.getLocalizedString( PROPERTY_ERROR_DATE_FORMAT, I18nService.getDefaultLocale( ) ) );
            }
        }
        model.put( PARAMETER_ERRORS, errors );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_PLU_SEARCH, request.getLocale( ), model );

        return template.getHtml( );
    }
}
