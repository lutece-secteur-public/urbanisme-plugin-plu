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

import fr.paris.lutece.plugins.plu.business.Atome;
import fr.paris.lutece.plugins.plu.business.Folder;
import fr.paris.lutece.plugins.plu.business.FolderFilter;
import fr.paris.lutece.plugins.plu.business.IAtomeServices;
import fr.paris.lutece.plugins.plu.business.IFolderServices;
import fr.paris.lutece.plugins.plu.business.IPluServices;
import fr.paris.lutece.plugins.plu.business.IVersionServices;
import fr.paris.lutece.plugins.plu.business.Plu;
import fr.paris.lutece.plugins.plu.business.Version;
import fr.paris.lutece.plugins.plu.business.VersionFilter;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Collection;

//import java.sql.Date;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class PluJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_MANAGE_PLU = "PLU_MANAGEMENT";

    // properties for page titles
    private static final String PROPERTY_PAGE_TITLE_PLU_LIST = "plu.manage_plu.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_TREE_PLU = "plu.tree_plu.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_APPLICABLE_PLU = "plu.applicable_plu.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_ATOME = "plu.create_atome.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_VIEW_ATOME = "plu.view_atome.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ATOME = "plu.modify_atome.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_FOLDER = "plu.create_folder.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_FOLER = "plu.modify_folder.pageTitle";

    // templates
    private static final String TEMPLATE_MANAGE_PLU = "/admin/plugins/plu/manage_plu.html";
    private static final String TEMPLATE_TREE_PLU = "/admin/plugins/plu/tree_plu.html";
    private static final String TEMPLATE_APPLICABLE_PLU = "/admin/plugins/plu/applicable_plu.html";
    private static final String TEMPLATE_CREATE_ATOME = "/admin/plugins/plu/create_atome.html";
    private static final String TEMPLATE_VIEW_ATOME = "/admin/plugins/plu/view_atome.html";
    private static final String TEMPLATE_MODIFY_VERSION = "/admin/plugins/plu/modify_version.html";
    private static final String TEMPLATE_CREATE_FOLDER = "/admin/plugins/plu/create_folder.html";
    private static final String TEMPLATE_MODIFY_FOLDER = "/admin/plugins/plu/modify_folder.html";

    //Markers
    private static final String MARK_LIST_PLU_LIST = "plu_list";
    private static final String MARK_LIST_FOLDER_LIST = "folder_list";
    private static final String MARK_LIST_ATOME_LIST = "atome_list";
    private static final String MARK_LIST_VERSION_LIST = "version_list";
    private static final String MARK_PLU = "plu";
    private static final String MARK_FOLDER = "folder";
    private static final String MARK_VERSION = "version";

    // Messages
    private static final String MESSAGE_CONFIRM_ISO_PLU = "plu.message.confirmIsoPlu";
    private static final String MESSAGE_CONFIRM_SITE_PLU = "plu.message.confirmSitePlu";
    private static final String MESSAGE_CONFIRM_UPLOAD_ATOME = "plu.message.confirmUploadAtome";
    private static final String MESSAGE_CONFIRM_REMOVE_FOLDER = "plu.message.confirmeRemoveFolder";

    // parameters
    private static final String PARAMETER_PLU_ID = "id_plu";
    private static final String PARAMETER_PLU_VERSION = "version";
    private static final String PARAMETER_ID_PLU_LIST = "plu_list_id";
    private static final String PARAMETER_FOLDER_ID = "id_folder";
    private static final String PARAMETER_ID_FOLDER_LIST = "folder_list_id";
    private static final String PARAMETER_FOLDER_TITLE = "folder_title";
    private static final String PARAMETER_FOLDER_DESCRIPTION = "folder_description";
    private static final String PARAMETER_ATOME_TITLE = "atome_title";
    private static final String PARAMETER_ATOME_DESCRIPTION = "atome_description";
    private static final String PARAMETER_VERSION_ID = "id_version";
    private static final String PARAMETER_VERSION_D1 = "version_d1";
    private static final String PARAMETER_VERSION_D3 = "version_d3";
    private static final String PARAMETER_DATE_APPLICATION = "date_application";

    // Jsp Definition
    private static final String JSP_REDIRECT_TO_MANAGE_PLU = "ManagePlu.jsp";
    private static final String JSP_REDIRECT_TO_TREE_PLU = "TreePlu.jsp";
    private static final String JSP_DO_ISO_PLU = "jsp/admin/plugins/plu/DoIsoPlu.jsp";
    private static final String JSP_DO_SITE_PLU = "jsp/admin/plugins/plu/DoSitePlu.jsp";
    private static final String JSP_DO_UPLOAD_ATOME = "jsp/admin/plugins/plu/DoUploadAtome.jsp";
    private static final String JSP_DO_REMOVE_FOLDER = "jsp/admin/plugins/plu/DoRemoveFolder.jsp";
    int nIdPlu = 1;
    int nIdFolder = 0;
    String sDateDefault = "31/12/9999";

    //Folder folderSearch = null;

    //int nIdFolderOld = 0;
    private IFolderServices _folderServices;
    private IAtomeServices _atomeServices;
    private IVersionServices _versionServices;
    private IPluServices _pluServices;

    public PluJspBean(  )
    {
        super(  );
        _pluServices = (IPluServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.pluServices" );
        _folderServices = (IFolderServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.folderServices" );
        _atomeServices = (IAtomeServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.atomeServices" );
        _versionServices = (IVersionServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.versionServices" );
    }

    public String getManagePlu( HttpServletRequest request )
    {
        nIdPlu = 1;
        nIdFolder = 0;

        setPageTitleProperty( PROPERTY_PAGE_TITLE_PLU_LIST );

        Collection<Plu> pluList = _pluServices.findAll( getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_LIST_PLU_LIST, pluList );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_PLU, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    public String getApplicablePlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_APPLICABLE_PLU );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_APPLICABLE_PLU, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String doApplicablePlu( HttpServletRequest request )
        throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );
        Date da = stringToDate( request.getParameter( PARAMETER_DATE_APPLICATION ), "dd/MM/yyyy" );
        plu.setDa( da );
        _pluServices.update( plu, getPlugin(  ) );

        Collection<Version> versionOldList = _versionServices.findByD3D4( da );

        for ( Version version : versionOldList )
        {
            GregorianCalendar calendar = new GregorianCalendar(  );
            calendar.setTime( da );
            calendar.add( Calendar.DATE, -1 );
            
            Date date = calendar.getTime(  );
            version.setD4( date );
            _versionServices.update( version, getPlugin(  ) );
        }

        Collection<Version> versionNewList = _versionServices.findByD2( da );

        for ( Version version : versionNewList )
        {
            version.setD2( da );
            _versionServices.update( version, getPlugin(  ) );
        }

        return JSP_REDIRECT_TO_MANAGE_PLU;
    }

    public String getConfirmIsoPlu( HttpServletRequest request )
    {
        int nIdContact = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        UrlItem url = new UrlItem( JSP_DO_ISO_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdContact );
        url.addParameter( PARAMETER_ID_PLU_LIST, request.getParameter( PARAMETER_ID_PLU_LIST ) );

        Object[] args = { request.getParameter( PARAMETER_PLU_ID ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_ISO_PLU, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doIsoPlu( HttpServletRequest request )
    {
        return JSP_REDIRECT_TO_MANAGE_PLU;
    }

    public String getConfirmSitePlu( HttpServletRequest request )
    {
        int nIdContact = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        UrlItem url = new UrlItem( JSP_DO_SITE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdContact );
        url.addParameter( PARAMETER_ID_PLU_LIST, request.getParameter( PARAMETER_ID_PLU_LIST ) );

        Object[] args = { request.getParameter( PARAMETER_PLU_ID ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_SITE_PLU, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doSitePlu( HttpServletRequest request )
    {
        return JSP_REDIRECT_TO_MANAGE_PLU;
    }

    public String getTreePlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_TREE_PLU );

        if ( request.getParameter( PARAMETER_PLU_ID ) != null )
        {
            nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        }

        if ( request.getParameter( PARAMETER_FOLDER_ID ) != null )
        {
            nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        }

        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        /*FolderFilter folderFilter = new FolderFilter(  );
        folderFilter.set_title( request.getParameter( PARAMETER_FOLDER_TITLE ) );
        folderFilter.set_description( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        Collection<Folder> folderList = _folderServices.findByFilter( folderFilter );*/
        Collection<Folder> folderList = _folderServices.findByDateAndParent( plu.getDa(  ), nIdFolder );

        /*VersionFilter versionFilter = new VersionFilter(  );
        versionFilter.set_d2( plu.getDa(  ) );
        Collection<Version> versionList = _versionServices.findByFilter( versionFilter );*/
        Collection<Version> versionList = _versionServices.findByDateAndParent( plu.getDa(  ), nIdFolder );

        //Collection<Atome> atomeList = _atomeServices.findByDateAndParent( plu.getDa(  ), nIdFolder );
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );

        if ( nIdFolder != 0 )
        {
            Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );
            model.put( MARK_FOLDER, folder );
        }

        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_LIST_VERSION_LIST, versionList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TREE_PLU, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getCreateFolder( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_FOLDER );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_FOLDER, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    public String doCreateFolder( HttpServletRequest request )
    {
        Folder parentFolder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        Folder folder = new Folder(  );

        folder.setTitle( request.getParameter( PARAMETER_FOLDER_TITLE ) );
        folder.setDescription( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        folder.setParentFolder( parentFolder );

        _folderServices.create( folder, getPlugin(  ) );

        return JSP_REDIRECT_TO_TREE_PLU;
    }

    public String getConfirmRemoveFolder( HttpServletRequest request ) //throws AccessDeniedException
    {
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

        UrlItem url = new UrlItem( JSP_DO_REMOVE_FOLDER );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        url.addParameter( PARAMETER_ID_FOLDER_LIST, request.getParameter( PARAMETER_ID_FOLDER_LIST ) );

        Object[] args = { request.getParameter( PARAMETER_FOLDER_TITLE ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_FOLDER, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doRemoveFolder( HttpServletRequest request )
    {
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );
        _folderServices.remove( folder, getPlugin(  ) );

        return JSP_REDIRECT_TO_TREE_PLU;
    }

    public String getModifyFolder( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_FOLER );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_FOLDER, folder );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_FOLDER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String doModifyFolder( HttpServletRequest request )
    {
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );
        folder.setTitle( request.getParameter( PARAMETER_FOLDER_TITLE ) );
        folder.setDescription( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        _folderServices.update( folder, getPlugin(  ) );

        return JSP_REDIRECT_TO_TREE_PLU;
    }

    public String getCreateAtome( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_ATOME );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_ATOME, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    public String doCreateAtome( HttpServletRequest request )
        throws ParseException
    {
        Date dateDefault = stringToDate( sDateDefault, "dd/MM/yyyy" );

        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        Atome atome = new Atome(  );
        atome.setTitle( request.getParameter( PARAMETER_ATOME_TITLE ) );
        atome.setDescription( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
        atome.setFolder( folder );

        String sDate = request.getParameter( PARAMETER_VERSION_D1 );
        Date date = stringToDate( sDate, "dd-mm-yyyy" );

        Version version = new Version(  );
        version.setVersion( 1 );
        version.setD1( date );
        version.setD2( dateDefault );
        version.setD3( dateDefault );
        version.setD4( dateDefault );
        version.setAtome( atome );

        _atomeServices.create( atome, getPlugin(  ) );
        _versionServices.create( version, getPlugin(  ) );

        return JSP_REDIRECT_TO_TREE_PLU;
    }

    private Date stringToDate( String sDate, String sFormat )
        throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat( sFormat );

        return (Date) sdf.parse( sDate );
    }

    public String getViewAtome( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_VIEW_ATOME );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VIEW_ATOME, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getModifyVersion( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATOME );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_VERSION, version );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_VERSION, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String doModifyVersion( HttpServletRequest request )
        throws ParseException
    {
        String sDate = request.getParameter( PARAMETER_VERSION_D3 );
        Date date = stringToDate( sDate, "dd/MM/yyyy" );
        Date dateDefault = stringToDate( sDateDefault, "dd/MM/yyyy" );

        Plu plu = _pluServices.findByDaNull( date );

        if ( plu.getId(  ) == 0 )
        {
            plu.setDj( date );
            plu.setDa( dateDefault );
            _pluServices.create( plu, getPlugin(  ) );
        }

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );
        version.setD3( date );

        Version version2 = new Version(  );
        version2.setVersion( version.getVersion(  ) + 1 );
        version2.setAtome( version.getAtome(  ) );
        version2.setD1( date );
        version2.setD2( dateDefault );
        version2.setD3( dateDefault );
        version2.setD4( dateDefault );

        _versionServices.update( version, getPlugin(  ) );
        _versionServices.create( version2, getPlugin(  ) );

        return JSP_REDIRECT_TO_TREE_PLU;
    }

    public String getConfirmUploadAtome( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_DO_UPLOAD_ATOME );

        Object[] args = { request.getParameter( PARAMETER_ATOME_TITLE ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_UPLOAD_ATOME, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doUploadAtome( HttpServletRequest request )
    {
        return JSP_REDIRECT_TO_TREE_PLU;
    }
}
