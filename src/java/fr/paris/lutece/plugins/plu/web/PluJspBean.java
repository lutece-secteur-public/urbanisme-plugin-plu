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

import fr.paris.lutece.plugins.plu.business.atome.Atome;
import fr.paris.lutece.plugins.plu.business.atome.IAtomeServices;
import fr.paris.lutece.plugins.plu.business.file.File;
import fr.paris.lutece.plugins.plu.business.file.IFileServices;
import fr.paris.lutece.plugins.plu.business.folder.Folder;
import fr.paris.lutece.plugins.plu.business.folder.FolderFilter;
import fr.paris.lutece.plugins.plu.business.folder.IFolderServices;
import fr.paris.lutece.plugins.plu.business.plu.IPluServices;
import fr.paris.lutece.plugins.plu.business.plu.Plu;
import fr.paris.lutece.plugins.plu.business.version.IVersionServices;
import fr.paris.lutece.plugins.plu.business.version.Version;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.filesystem.FileSystemUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;

//import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
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
    private static final String PROPERTY_PAGE_TITLE_JOIN_FILE = "plu.join_file.pageTtitle";

    // templates
    private static final String TEMPLATE_MANAGE_PLU = "/admin/plugins/plu/manage_plu.html";
    private static final String TEMPLATE_TREE_PLU = "/admin/plugins/plu/tree_plu.html";
    private static final String TEMPLATE_APPLICABLE_PLU = "/admin/plugins/plu/applicable_plu.html";
    private static final String TEMPLATE_CHOICE_CREATE_ATOME = "/admin/plugins/plu/choice_create_atome.html";
    private static final String TEMPLATE_CREATE_ATOME = "/admin/plugins/plu/create_atome.html";
    private static final String TEMPLATE_CREATE_ATOME_WITH_OLD = "/admin/plugins/plu/create_atome_with_old.html";
    private static final String TEMPLATE_VIEW_ATOME = "/admin/plugins/plu/view_atome.html";
    private static final String TEMPLATE_CHOICE_MODIFY_ATOME = "/admin/plugins/plu/choice_modify_atome.html";
    private static final String TEMPLATE_MODIFY_VERSION = "/admin/plugins/plu/modify_version.html";
    private static final String TEMPLATE_ASSOCIATE_VERSION = "/admin/plugins/plu/associate_version.html";
    private static final String TEMPLATE_BURST_VERSION = "/admin/plugins/plu/burst_version.html";
    private static final String TEMPLATE_CREATE_FOLDER = "/admin/plugins/plu/create_folder.html";
    private static final String TEMPLATE_MODIFY_FOLDER = "/admin/plugins/plu/modify_folder.html";
    private static final String TEMPLATE_JOIN_FILE = "/admin/plugins/plu/join_file.html";

    //Markers
    private static final String MARK_LIST_PLU_LIST = "plu_list";
    private static final String MARK_LIST_FOLDER_LIST = "folder_list";
    private static final String MARK_LIST_ATOME_LIST = "atome_list";
    private static final String MARK_LIST_VERSION_LIST = "version_list";
    private static final String MARK_LIST_FILE_LIST = "file_list";
    private static final String MARK_PLU = "plu";
    private static final String MARK_FOLDER = "one_folder";
    private static final String MARK_ATOME = "one_atome";
    private static final String MARK_VERSION = "one_version";

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
    private static final String PARAMETER_ID_PARENT_FOLDER = "id_parent_folder";
    private static final String PARAMETER_CHOICE_ATOME = "choice";
    private static final String PARAMETER_ATOME_ID = "id_atome";
    private static final String PARAMETER_ATOME_TITLE = "atome_title";
    private static final String PARAMETER_ATOME_DESCRIPTION = "atome_description";
    private static final String PARAMETER_VERSION_ID = "id_version";
    private static final String PARAMETER_VERSION_D1 = "version_d1";
    private static final String PARAMETER_VERSION_D3 = "version_d3";
    private static final String PARAMETER_DATE_APPLICATION = "date_application";
    private static final String PARAMETER_FILE_TITLE = "file_title";
    private static final String PARAMETER_FILE = "file";

    // Jsp Definition
    private static final String JSP_REDIRECT_TO_MANAGE_PLU = "../plu/ManagePlu.jsp";
    private static final String JSP_REDIRECT_TO_TREE_PLU = "../plu/TreePlu.jsp";
    private static final String JSP_DO_ISO_PLU = "jsp/admin/plugins/plu/plu/DoIsoPlu.jsp";
    private static final String JSP_DO_SITE_PLU = "jsp/admin/plugins/plu/plu/DoSitePlu.jsp";
    private static final String JSP_DO_UPLOAD_ATOME = "jsp/admin/plugins/plu/atome/DoUploadAtome.jsp";
    private static final String JSP_DO_REMOVE_FOLDER = "jsp/admin/plugins/plu/folder/DoRemoveFolder.jsp";
    int nIdFolder = 0;
    String sDateDefault = "31/12/9999";
    List<File> fileList = new ArrayList<File>(  );

    //Folder folderSearch = null;

    //int nIdFolderOld = 0;
    private IFolderServices _folderServices;
    private IAtomeServices _atomeServices;
    private IVersionServices _versionServices;
    private IPluServices _pluServices;
    private IFileServices _fileServices;

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
        //nIdPlu = 1;
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

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );

        if ( request.getParameter( PARAMETER_FOLDER_ID ) != null )
        {
            nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

            Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );
            model.put( MARK_FOLDER, folder );

            /*VersionFilter versionFilter = new VersionFilter(  );
            versionFilter.set_d2( plu.getDa(  ) );
            Collection<Version> versionList = _versionServices.findByFilter( versionFilter );*/
            Collection<Version> versionList = _versionServices.findByDateAndParent( plu.getDa(  ), nIdFolder );
            model.put( MARK_LIST_VERSION_LIST, versionList );
        }
        else
        {
            if ( request.getParameter( PARAMETER_FOLDER_TITLE ) != null )
            {
                FolderFilter folderFilter = new FolderFilter(  );
                folderFilter.set_title( request.getParameter( PARAMETER_FOLDER_TITLE ) );

                Collection<Folder> folderList = _folderServices.findByFilter( folderFilter );
                model.put( MARK_LIST_FOLDER_LIST, folderList );
            }
            else
            {
                Collection<Folder> folderList = _folderServices.findByDate( plu.getDa(  ) );
                model.put( MARK_LIST_FOLDER_LIST, folderList );
            }
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TREE_PLU, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getCreateFolder( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_FOLDER );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        Collection<Folder> folderList = _folderServices.findAll( getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_FOLDER_LIST, folderList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_FOLDER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String doCreateFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        int idParentFolder = Integer.parseInt( request.getParameter( PARAMETER_ID_PARENT_FOLDER ) );
        Folder parentFolder = new Folder(  );

        if ( idParentFolder == 0 )
        {
            parentFolder.setId( 0 );
        }
        else
        {
            parentFolder = _folderServices.findByPrimaryKey( idParentFolder, getPlugin(  ) );
        }

        Folder folder = new Folder(  );

        folder.setTitle( request.getParameter( PARAMETER_FOLDER_TITLE ) );
        folder.setDescription( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        folder.setParentFolder( parentFolder );

        _folderServices.create( folder, getPlugin(  ) );

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  );
    }

    public String getConfirmRemoveFolder( HttpServletRequest request ) //throws AccessDeniedException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

        UrlItem url = new UrlItem( JSP_DO_REMOVE_FOLDER );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        url.addParameter( PARAMETER_ID_FOLDER_LIST, request.getParameter( PARAMETER_ID_FOLDER_LIST ) );

        Object[] args = { request.getParameter( PARAMETER_FOLDER_TITLE ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_FOLDER, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doRemoveFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );
        _folderServices.remove( folder, getPlugin(  ) );

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  );
    }

    public String getModifyFolder( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_FOLER );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        Collection<Folder> folderList = _folderServices.findAll( getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_FOLDER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String doModifyFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        int idParentFolder = Integer.parseInt( request.getParameter( PARAMETER_ID_PARENT_FOLDER ) );
        Folder parentFolder = _folderServices.findByPrimaryKey( idParentFolder, getPlugin(  ) );

        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );
        folder.setTitle( request.getParameter( PARAMETER_FOLDER_TITLE ) );
        folder.setDescription( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        folder.setParentFolder( parentFolder );

        _folderServices.update( folder, getPlugin(  ) );

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  );
    }

    public String getChoiceCreateAtome( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_ATOME );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        Collection<Atome> atomeList = _atomeServices.findAll( getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_ATOME_LIST, atomeList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CHOICE_CREATE_ATOME, getLocale(  ), model );

        fileList.clear(  );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getCreateAtome( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_ATOME );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        Collection<Folder> folderList = _folderServices.findAll( getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );

        /*MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        
        FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );
        
        if ( ( fileItem != null ) && ( fileItem.getName(  ) != null ) )
        {
                File file = new File(  );
            PhysicalFile physicalFile = new PhysicalFile(  );
            physicalFile.setValue( fileItem.get(  ) );
            file.setTitle( FileUploadService.getFileNameOnly( fileItem ) );
            //file.setFile( physicalFile );
            file.setMimeType( FileSystemUtil.getMIMEType( FileUploadService.getFileNameOnly( fileItem ) ) );
        
            fileList.add(file);
            model.put( MARK_LIST_FILE_LIST, fileList );
        }*/
        String fileTitle = request.getParameter( PARAMETER_FILE_TITLE );

        //String file = request.getParameter( PARAMETER_FILE );
        boolean test = fileList.isEmpty(  );

        if ( !test || ( fileTitle != null ) || ( fileTitle == "" ) )
        {
            File file = new File(  );
            file.setTitle( fileTitle );
            fileList.add( file );
            model.put( MARK_LIST_FILE_LIST, fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_ATOME, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getCreateAtomeWithOld( HttpServletRequest request )
    {
        Collection<Folder> folderList = _folderServices.findAll( getPlugin(  ) );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        Atome atome = _atomeServices.findByPrimaryKey( nIdAtome, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_ATOME, atome );

        if (  /*fileList != null ||*/
            ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) &&
                ( request.getParameter( PARAMETER_FILE ) != null ) ) )
        {
            File file = new File(  );
            file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
            fileList.add( file );
            model.put( MARK_LIST_FILE_LIST, fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_ATOME_WITH_OLD, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String doCreateAtome( HttpServletRequest request )
        throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        Date dateDefault = stringToDate( sDateDefault, "dd/MM/yyyy" );

        nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        Atome atome = new Atome(  );
        //atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
        atome.setTitle( request.getParameter( PARAMETER_ATOME_TITLE ) );
        atome.setDescription( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
        atome.setFolder( folder );

        String sDate = request.getParameter( PARAMETER_VERSION_D1 );
        Date date = stringToDate( sDate, "dd/MM/yyyy" );

        Plu newPlu = _pluServices.findByDaNull( date );

        if ( newPlu.getId(  ) == 0 )
        {
            newPlu.setDj( date );
            newPlu.setDa( dateDefault );
            _pluServices.create( newPlu, getPlugin(  ) );
        }

        Version version = new Version(  );
        version.setVersion( 1 );
        version.setD1( date );
        version.setD2( dateDefault );
        version.setD3( dateDefault );
        version.setD4( dateDefault );
        version.setAtome( atome );

        _atomeServices.create( atome, getPlugin(  ) );
        _versionServices.create( version, getPlugin(  ) );

        for ( File file : fileList )
        {
            //si checkbox de name="file.title" à la value!=1
            //	créer le fichier
            File file2 = new File(  );
            file2.setTitle( file.getTitle(  ) );
            file2.setVersion( version );
            _fileServices.create( file2, getPlugin(  ) );
        }

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  );
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

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_VERSION, version );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VIEW_ATOME, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getChoiceModifyAtome( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATOME );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_VERSION, version );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CHOICE_MODIFY_ATOME, getLocale(  ), model );

        fileList.clear(  );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getModifyVersion( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATOME );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        Collection<Folder> folderList = _folderServices.findAll( getPlugin(  ) );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_VERSION, version );
        model.put( MARK_LIST_FOLDER_LIST, folderList );

        if (  /*fileList != null ||*/
            ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) &&
                ( request.getParameter( PARAMETER_FILE ) != null ) ) )
        {
            File file = new File(  );
            file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
            fileList.add( file );
            model.put( MARK_LIST_FILE_LIST, fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_VERSION, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String doModifyVersion( HttpServletRequest request )
        throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        Atome atome = new Atome(  );
        atome.setTitle( request.getParameter( PARAMETER_ATOME_TITLE ) );
        atome.setDescription( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
        atome.setFolder( folder );

        _atomeServices.update( atome, getPlugin(  ) );

        String sDate = request.getParameter( PARAMETER_VERSION_D3 );
        Date date = stringToDate( sDate, "dd/MM/yyyy" );
        Date dateDefault = stringToDate( sDateDefault, "dd/MM/yyyy" );

        Plu NewPlu = _pluServices.findByDaNull( date );

        if ( NewPlu.getId(  ) == 0 )
        {
            NewPlu.setDj( date );
            NewPlu.setDa( dateDefault );
            _pluServices.create( NewPlu, getPlugin(  ) );
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

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  ) + "&id_folder=" + folder.getId(  );
    }

    public String getAssociateVersion( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATOME );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        Collection<Folder> folderList = _folderServices.findByDate( plu.getDa(  ) );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_VERSION, version );

        if ( request.getParameter( PARAMETER_FOLDER_ID ) != null )
        {
            int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
            Collection<Version> versionList = _versionServices.findByDateAndParent( plu.getDa(  ), nIdFolder );
            model.put( MARK_LIST_VERSION_LIST, versionList );
        }
        else
        {
            Collection<Version> versionList = _versionServices.findByDateAndParent( plu.getDa(  ),
                    version.getAtome(  ).getFolder(  ).getId(  ) );
            model.put( MARK_LIST_VERSION_LIST, versionList );
        }

        if (  /*fileList != null ||*/
            ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) &&
                ( request.getParameter( PARAMETER_FILE ) != null ) ) )
        {
            File file = new File(  );
            file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
            fileList.add( file );
            model.put( MARK_LIST_FILE_LIST, fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSOCIATE_VERSION, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getBurstVersion( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATOME );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        Collection<Folder> folderList = _folderServices.findAll( getPlugin(  ) );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_VERSION, version );

        if (  /*fileList != null ||*/
            ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) &&
                ( request.getParameter( PARAMETER_FILE ) != null ) ) )
        {
            File file = new File(  );
            file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
            fileList.add( file );
            model.put( MARK_LIST_FILE_LIST, fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_BURST_VERSION, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
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

    public String getJoinFile( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_JOIN_FILE );

        String page = request.getParameter( "page" );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( "page", page );

        if ( request.getParameter( PARAMETER_ATOME_ID ) != null )
        {
            int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
            Atome atome = _atomeServices.findByPrimaryKey( nIdAtome, getPlugin(  ) );
            model.put( MARK_ATOME, atome );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_JOIN_FILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }
}
