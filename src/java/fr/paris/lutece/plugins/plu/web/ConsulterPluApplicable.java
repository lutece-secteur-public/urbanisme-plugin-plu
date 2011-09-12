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

import fr.paris.lutece.plugins.plu.business.plu.IPluServices;
import fr.paris.lutece.plugins.plu.business.plu.Plu;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet redirecting to the last plu website
 */
public class ConsulterPluApplicable extends HttpServlet
{
    /**
     * Property name for path where plu websites are generated
     */
    public static String PATH_PLU_WEBSITES = "url.static.websites";

    private IPluServices _pluServices;

    /**
     * Constructor
     */
    public ConsulterPluApplicable( )
    {
        super( );
        _pluServices = (IPluServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.pluServices" );
    }

    /**  
     *
     */
    private static final long serialVersionUID = 6696862304846317051L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException the servlet Exception
     * @throws IOException the io exception
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response ) throws ServletException,
            IOException
    {
        Plu pluApplied = _pluServices.findPluApplied( );

        String pathPluWebsites = AppPropertiesService.getProperty( PATH_PLU_WEBSITES );
        pathPluWebsites = pathPluWebsites.replace( "{0}", String.valueOf( pluApplied.getId( ) ) );

        response.sendRedirect( AppPathService.getBaseUrl( request ) + pathPluWebsites );

    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException the servlet Exception
     * @throws IOException the io exception
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException,
            IOException
    {
        processRequest( request, response );
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException the servlet Exception
     * @throws IOException the io exception
     */
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException,
            IOException
    {
        processRequest( request, response );
    }

    /**
     * Returns a short description of the servlet.
     * @return message
     */
    public String getServletInfo( )
    {
        return "Servlet redirecting to the static website of the Plu Applicable";
    }
}