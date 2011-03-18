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
import fr.paris.lutece.plugins.plu.business.type.ITypeServices;
import fr.paris.lutece.plugins.plu.business.type.Type;
import fr.paris.lutece.plugins.plu.business.version.IVersionServices;
import fr.paris.lutece.plugins.plu.business.version.Version;
import fr.paris.lutece.plugins.plu.business.version.VersionFilter;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.filesystem.FileSystemUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;

import java.sql.Blob;
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
    private static final String PROPERTY_DEFAULT_RESULT_PER_PAGE = "plu.resultList.itemsPerPage";

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
    private static final String MARK_LIST_TYPE_LIST = "type_list";
    private static final String MARK_LIST_FOLDER_LIST = "folder_list";
    private static final String MARK_LIST_ATOME_LIST = "atome_list";
    private static final String MARK_LIST_VERSION_LIST = "version_list";
    private static final String MARK_LIST_FILE_LIST = "file_list";
    private static final String MARK_PLU = "plu";
    private static final String MARK_FOLDER = "one_folder";
    private static final String MARK_ATOME = "one_atome";
    private static final String MARK_VERSION = "one_version";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_DATE = "date";

    // Messages
    private static final String MESSAGE_CONFIRM_ISO_PLU = "plu.message.confirmIsoPlu";
    private static final String MESSAGE_CONFIRM_SITE_PLU = "plu.message.confirmSitePlu";
    private static final String MESSAGE_CONFIRM_UPLOAD_ATOME = "plu.message.confirmUploadAtome";
    private static final String MESSAGE_CONFIRM_REMOVE_FOLDER = "plu.message.confirmeRemoveFolder";

    // parameters
    private static final String PARAMETER_PLU_ID = "id_plu";
    private static final String PARAMETER_PLU_VERSION = "version";
    private static final String PARAMETER_PLU_TYPE = "id_type";
    private static final String PARAMETER_PLU_CAUSE = "cause";
    private static final String PARAMETER_PLU_REFERENCE = "reference";
    private static final String PARAMETER_ID_PLU_LIST = "plu_list_id";
    private static final String PARAMETER_FOLDER_ID = "id_folder";
    private static final String PARAMETER_ID_FOLDER_LIST = "folder_list_id";
    private static final String PARAMETER_FOLDER_TITLE = "folder_title";
    private static final String PARAMETER_FOLDER_DESCRIPTION = "folder_description";
    private static final String PARAMETER_ID_PARENT_FOLDER = "id_parent_folder";
    private static final String PARAMETER_FOLDER_IMAGE = "folder_image";
    private static final String PARAMETER_CHOICE_ATOME = "choice";
    private static final String PARAMETER_ATOME_ID = "id_atome";
    private static final String PARAMETER_ATOME_NAME = "atome_name";
    private static final String PARAMETER_ATOME_TITLE = "atome_title";
    private static final String PARAMETER_ATOME_DESCRIPTION = "atome_description";
    private static final String PARAMETER_ATOME_IMAGE = "atome_image";
    private static final String PARAMETER_VERSION_ID = "id_version";
    private static final String PARAMETER_VERSION_D1 = "version_d1";
    private static final String PARAMETER_VERSION_D3 = "version_d3";
    private static final String PARAMETER_DATE_APPLICATION = "date_application";
    private static final String PARAMETER_FILE_TITLE = "file_title";
    private static final String PARAMETER_FILE_CHECK = "file_check";
    private static final String PARAMETER_FILE = "file";
    private static final String PARAMETER_PAGE_INDEX = "page_index";

    // Jsp Definition
    private static final String JSP_REDIRECT_TO_MANAGE_PLU = "../plu/ManagePlu.jsp";
    private static final String JSP_REDIRECT_TO_TREE_PLU = "../plu/TreePlu.jsp";
    private static final String JSP_REDIRECT_TO_CHOICE_CREATE_ATOME = "../atome/ChoiceCreateAtome.jsp";
    private static final String JSP_DO_ISO_PLU = "jsp/admin/plugins/plu/plu/DoIsoPlu.jsp";
    private static final String JSP_DO_SITE_PLU = "jsp/admin/plugins/plu/plu/DoSitePlu.jsp";
    private static final String JSP_DO_UPLOAD_ATOME = "jsp/admin/plugins/plu/atome/DoUploadAtome.jsp";
    private static final String JSP_REDIRECT_TO_VIEW_ATOME = "jsp/admin/plugins/plu/atome/ViewAtome.jsp";
    private static final String JSP_DO_REMOVE_FOLDER = "jsp/admin/plugins/plu/folder/DoRemoveFolder.jsp";

    //Variables
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;
    private IFolderServices _folderServices;
    private IAtomeServices _atomeServices;
    private IVersionServices _versionServices;
    private IPluServices _pluServices;
    private IFileServices _fileServices;
    private ITypeServices _typeServices;

    //int nIdFolder = 0;
    String sDateDefault = "31/12/9999";
    List<File> fileList = new ArrayList<File>(  );

    //Folder folderSearch = null;

    //int nIdFolderOld = 0;
    public PluJspBean(  )
    {
        super(  );
        _pluServices = (IPluServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.pluServices" );
        _folderServices = (IFolderServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.folderServices" );
        _atomeServices = (IAtomeServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.atomeServices" );
        _versionServices = (IVersionServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.versionServices" );
        _fileServices = (IFileServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.fileServices" );
        _typeServices = (ITypeServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.typeServices");
    }

    public String getManagePlu( HttpServletRequest request )
    {
        //nIdPlu = 1;
        //nIdFolder = 0;
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
        
        Collection<Type> typeList = _typeServices.findAll( getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_TYPE_LIST, typeList );

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
        
        int nIdType = Integer.parseInt( request.getParameter( PARAMETER_PLU_TYPE ) );
        Type type = _typeServices.findByPrimaryKey( nIdType, getPlugin(  ) );
        int nIdPlu2 = nIdPlu - 1;
        Plu plu2 = _pluServices.findByPrimaryKey( nIdPlu2 , getPlugin(  ) );
        plu2.setType( type );
        plu2.setCause( request.getParameter( PARAMETER_PLU_CAUSE ) );
        plu2.setReference( request.getParameter( PARAMETER_PLU_REFERENCE ) );
        _pluServices.update( plu2, getPlugin(  ) );

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

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_RESULT_PER_PAGE, 10 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );

        if ( request.getParameter( PARAMETER_FOLDER_ID ) != null )
        {
            int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

            Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );
            model.put( MARK_FOLDER, folder );

            if ( request.getParameter( PARAMETER_ATOME_TITLE ) != null )
            {
                /*VersionFilter versionFilter = new VersionFilter(  );
                versionFilter.set_d2( plu.getDa(  ) );
                Collection<Version> versionList = _versionServices.findByFilter( versionFilter );
                */
                Collection<Version> versionList = _versionServices.findByDateAndParent( plu.getDa(  ), nIdFolder );

                Paginator<Version> paginator = new Paginator<Version>( (List<Version>) versionList, _nItemsPerPage,
                        JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  ) + "&id_folder=" + folder.getId(  ),
                        PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

                model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
                model.put( MARK_PAGINATOR, paginator );
                model.put( MARK_LIST_VERSION_LIST, paginator.getPageItems(  ) );
            }
            else
            {
                Collection<Version> versionList = _versionServices.findByDateAndParent( plu.getDa(  ), nIdFolder );
                Paginator<Version> paginator = new Paginator<Version>( (List<Version>) versionList, _nItemsPerPage,
                        JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  ) + "&id_folder=" + folder.getId(  ),
                        PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

                model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
                model.put( MARK_PAGINATOR, paginator );
                model.put( MARK_LIST_VERSION_LIST, paginator.getPageItems(  ) );
            }
        }
        else
        {
            if ( request.getParameter( PARAMETER_FOLDER_TITLE ) != null )
            {
                FolderFilter folderFilter = new FolderFilter(  );
                folderFilter.set_title( request.getParameter( PARAMETER_FOLDER_TITLE ) );

                Collection<Folder> folderList = _folderServices.findByFilter( folderFilter );
                Paginator<Folder> paginator = new Paginator<Folder>( (List<Folder>) folderList, _nItemsPerPage,
                        JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  ) + "&folder_title=" +
                        folderFilter.get_title(  ), PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

                model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
                model.put( MARK_PAGINATOR, paginator );
                model.put( MARK_LIST_FOLDER_LIST, paginator.getPageItems(  ) );
            }
            else
            {
                Collection<Folder> folderList = _folderServices.findByDate( plu.getDa(  ) );
                Paginator<Folder> paginator = new Paginator<Folder>( (List<Folder>) folderList, _nItemsPerPage,
                        JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  ), PARAMETER_PAGE_INDEX,
                        _strCurrentPageIndex );

                model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
                model.put( MARK_PAGINATOR, paginator );
                model.put( MARK_LIST_FOLDER_LIST, paginator.getPageItems(  ) );
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
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );

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
        
        if( request instanceof MultipartHttpServletRequest )
        {
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_IMAGE  );
	        /*String name = fileItem.getName();
	        String contentType = fileItem.getContentType();
	        String fieldName = fileItem.getFieldName();
	        Long size = fileItem.getSize();*/
	        
	        PhysicalFile physicalFile = new PhysicalFile(  );
	        physicalFile.setValue( fileItem.get(  ) );
	        folder.setImg(physicalFile.getValue());
        }


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
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );

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
        
        if( request instanceof MultipartHttpServletRequest )
        {
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_IMAGE  );
	        PhysicalFile physicalFile = new PhysicalFile(  );
	        physicalFile.setValue( fileItem.get(  ) );
	        folder.setImg(physicalFile.getValue());
        }

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

        Date date = new Date(  );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        Collection<Folder> folderList = _folderServices.findAll( getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );
        model.put( MARK_DATE, date );

        if ( request instanceof MultipartHttpServletRequest )
        {
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        
	        FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );
	        
	        if ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) && ( multipartRequest.getFile( PARAMETER_FILE ) != null ) )
	        {
	            File file = new File(  );
	            PhysicalFile physicalFile = new PhysicalFile(  );
	            physicalFile.setValue( fileItem.get(  ) );
	            String name = fileItem.getName(  );
	            String type = name.substring( name.lastIndexOf( "." ) );
	            file.setName( name );
	            file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
	            file.setFile( physicalFile.getValue(  ) );
	            file.setMimeType( type );
	            file.setSize( fileItem.getSize(  ) );
	            //file.setMimeType( FileSystemUtil.getMIMEType( FileUploadService.getFileNameOnly( fileItem ) ) );
	            fileList.add(file);
	        }
        }

        if ( fileList != null )
        {
            model.put( MARK_LIST_FILE_LIST, fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_ATOME, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getCreateAtomeWithOld( HttpServletRequest request )
    {
        Collection<Folder> folderList = _folderServices.findAll( getPlugin(  ) );

        Date date = new Date(  );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );
        
        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        Atome atome = _atomeServices.findByPrimaryKey( nIdAtome, getPlugin(  ) );

        fileList.addAll( _fileServices.findByAtome( nIdAtome ) );
        
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_ATOME, atome );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );
        model.put( MARK_DATE, date );

        if ( request instanceof MultipartHttpServletRequest )
        {
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        
	        FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );
	        
	        if ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) && ( multipartRequest.getFile( PARAMETER_FILE ) != null ) )
	        {
	            File file = new File(  );
	            PhysicalFile physicalFile = new PhysicalFile(  );
	            physicalFile.setValue( fileItem.get(  ) );
	            String name = fileItem.getName(  );
	            String type = name.substring( name.lastIndexOf( "." ) );
	            file.setName( name );
	            file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
	            file.setFile( physicalFile.getValue(  ) );
	            file.setMimeType( type );
	            file.setSize( fileItem.getSize(  ) );
	            fileList.add(file);
	        }
        }

        if ( fileList != null )
        {
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
        
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        Atome atome = new Atome(  );
        atome.setName( request.getParameter( PARAMETER_ATOME_NAME ) );
        atome.setTitle( request.getParameter( PARAMETER_ATOME_TITLE ) );
        atome.setDescription( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
        atome.setFolder( folder );
        
        if ( request instanceof MultipartHttpServletRequest )
        {
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        FileItem fileItem = multipartRequest.getFile( PARAMETER_ATOME_IMAGE  );
	        
	        PhysicalFile physicalFile = new PhysicalFile(  );
	        physicalFile.setValue( fileItem.get(  ) );
	        atome.setImg(physicalFile.getValue());
        }

        Date date = new Date(  );
        Date dateDefault = stringToDate( sDateDefault, "dd/MM/yyyy" );

        Plu newPlu = _pluServices.findByDa( date );

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
        
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        int i = 0;
        
        for ( File file : fileList )
        {
        	for ( int j = 0; j < check.length; ++j )
        	{
        		int c = Integer.parseInt( check[j] ); 
        		if( c == i )
        		{
        			file.setVersion( version );
        			_fileServices.create( file, getPlugin(  ) );
        		}
        	}
        	
        	i++;
        }

        return JSP_REDIRECT_TO_CHOICE_CREATE_ATOME + "?id_plu=" + plu.getId(  ) + "&id_folder=" + folder.getId(  );
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
        
        Collection<File> fileList = _fileServices.findByVersion( nIdVersion );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_RESULT_PER_PAGE, 10 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Map<String, Object> model = new HashMap<String, Object>(  );

        Paginator<File> paginator = new Paginator<File>( (List<File>) fileList, _nItemsPerPage,
                JSP_REDIRECT_TO_VIEW_ATOME + "?id_plu=" + plu.getId(  ) + "&id_version=" + version.getId(  ),
                PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_LIST_FILE_LIST, paginator.getPageItems(  ) );
        model.put(MARK_LIST_FILE_LIST, fileList);
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

        Date date = new Date(  );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        Collection<Folder> folderList = _folderServices.findAll( getPlugin(  ) );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );
        
        fileList.addAll( _fileServices.findByVersion( nIdVersion ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_VERSION, version );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );
        model.put( MARK_DATE, date );

        if ( request instanceof MultipartHttpServletRequest )
        {
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        
	        FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );
	        
	        if ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) && ( multipartRequest.getFile( PARAMETER_FILE ) != null ) )
	        {
	            File file = new File(  );
	            PhysicalFile physicalFile = new PhysicalFile(  );
	            physicalFile.setValue( fileItem.get(  ) );
	            String name = fileItem.getName(  );
	            String type = name.substring( name.lastIndexOf( "." ) );
	            file.setName( name );
	            file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
	            file.setFile( physicalFile.getValue(  ) );
	            file.setMimeType( type );
	            file.setSize( fileItem.getSize(  ) );
	            fileList.add(file);
	        }
        }

        if ( fileList != null )
        {
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

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );

        Atome atome = new Atome(  );
        atome.setId( Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) ) );
        atome.setName( request.getParameter( PARAMETER_ATOME_NAME ) );
        atome.setTitle( request.getParameter( PARAMETER_ATOME_TITLE ) );
        atome.setDescription( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
        atome.setFolder( folder );
        
        if( request instanceof MultipartHttpServletRequest )
        {
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        FileItem fileItem = multipartRequest.getFile( PARAMETER_ATOME_IMAGE  );
	        
	        PhysicalFile physicalFile = new PhysicalFile(  );
	        physicalFile.setValue( fileItem.get(  ) );
	        atome.setImg(physicalFile.getValue());
        }

        _atomeServices.update( atome, getPlugin(  ) );

        Date date = new Date (  );
        Date dateDefault = stringToDate( sDateDefault, "dd/MM/yyyy" );
        
        Plu NewPlu = _pluServices.findByDa( date );

        if ( NewPlu.getId(  ) == 0 )
        {
            NewPlu.setDj( date );
            NewPlu.setDa( dateDefault );
            _pluServices.create( NewPlu, getPlugin(  ) );
        }

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );
        
        if( version.getD2() != dateDefault )
        {
	        version.setD3( date );
	        
	        Version version2 = new Version(  );
	        version2.setVersion( version.getVersion(  ) + 1 );
	        version2.setAtome( atome );
	        version2.setD1( date );
	        version2.setD2( dateDefault );
	        version2.setD3( dateDefault );
	        version2.setD4( dateDefault );
	        
	        _versionServices.update( version, getPlugin(  ) );
	        _versionServices.create( version2, getPlugin(  ) );
	        
	        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
	        int i = 0;
	        
	        for ( File file : fileList )
	        {
	        	for ( int j = 0; j < check.length; ++j )
	        	{
	        		int c = Integer.parseInt( check[j] ); 
	        		if( c == i )
	        		{
	        			file.setVersion( version2 );
	        			_fileServices.create( file, getPlugin(  ) );
	        		}
	        	}
	        	
	        	i++;
	        }
        }
        else
        {
        	String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
            int i = 0;
            
            for ( File file : fileList )
            {
            	for ( int j = 0; j < check.length; ++j )
            	{
            		int c = Integer.parseInt( check[j] ); 
            		if( c == i )
            		{
            			file.setVersion( version );
            			_fileServices.create( file, getPlugin(  ) );
            		}
            	}
            	
            	i++;
            }
        }
        
        
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

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSOCIATE_VERSION, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getBurstVersion( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATOME );

        Date date = new Date(  );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        Collection<Folder> folderList = _folderServices.findAll( getPlugin(  ) );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_VERSION, version );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );
        model.put( MARK_DATE, date );

        if ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) &&
                ( request.getParameter( PARAMETER_FILE ) != null ) )
        {
            File file = new File(  );
            file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
            fileList.add( file );
        }

        if ( fileList != null )
        {
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

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu, getPlugin(  ) );

        String page = request.getParameter( "page" );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( "page", page );
        model.put( MARK_PLU, plu );

        if ( request.getParameter( PARAMETER_FOLDER_ID ) != null )
        {
            int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
            Folder folder = _folderServices.findByPrimaryKey( nIdFolder, getPlugin(  ) );
            model.put( MARK_FOLDER, folder );
        }

        if ( request.getParameter( PARAMETER_VERSION_ID ) != null )
        {
            int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
            Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );
            model.put( MARK_VERSION, version );
        }

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
