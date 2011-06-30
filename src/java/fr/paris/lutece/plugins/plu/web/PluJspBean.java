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
import fr.paris.lutece.plugins.plu.business.atome.AtomeFilter;
import fr.paris.lutece.plugins.plu.business.atome.IAtomeServices;
import fr.paris.lutece.plugins.plu.business.file.File;
import fr.paris.lutece.plugins.plu.business.file.FileFilter;
import fr.paris.lutece.plugins.plu.business.file.IFileServices;
import fr.paris.lutece.plugins.plu.business.fileContent.FileContent;
import fr.paris.lutece.plugins.plu.business.fileContent.IFileContentServices;
import fr.paris.lutece.plugins.plu.business.folder.Folder;
import fr.paris.lutece.plugins.plu.business.folder.FolderFilter;
import fr.paris.lutece.plugins.plu.business.folder.IFolderServices;
import fr.paris.lutece.plugins.plu.business.folderVersion.FolderVersion;
import fr.paris.lutece.plugins.plu.business.folderVersion.IFolderVersionServices;
import fr.paris.lutece.plugins.plu.business.history.History;
import fr.paris.lutece.plugins.plu.business.history.IHistoryServices;
import fr.paris.lutece.plugins.plu.business.plu.IPluServices;
import fr.paris.lutece.plugins.plu.business.plu.Plu;
import fr.paris.lutece.plugins.plu.business.type.ITypeServices;
import fr.paris.lutece.plugins.plu.business.type.Type;
import fr.paris.lutece.plugins.plu.business.version.IVersionServices;
import fr.paris.lutece.plugins.plu.business.version.Version;
import fr.paris.lutece.plugins.plu.business.version.VersionFilter;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage form features ( manage,
 * create, modify, remove)
 */
public class PluJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_MANAGE_PLU = "PLU_MANAGEMENT";

    // properties for page titles
    private static final String PROPERTY_PAGE_TITLE_PLU_LIST = "plu.manage_plu.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_APPROVE_PLU = "plu.approve_plu.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_APPLICABLE_PLU = "plu.applicable_plu.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PLU = "plu.modify_plu.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CORRECT_PLU = "plu.correct_plu.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_TREE_FOLDER = "plu.tree_folder.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_FOLDER = "plu.create_folder.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_FOLDER = "plu.modify_folder.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CORRECT_FOLDER = "plu.correct_folder.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_TREE_ATOME = "plu.tree_atome.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_ATOME = "plu.create_atome.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_VIEW_ATOME = "plu.view_atome.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ATOME = "plu.modify_atome.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CORRECT_ATOME = "plu.correct_atome.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_EVOLVE_ATOME = "plu.evolve_atome.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_JOIN_FILE = "plu.join_file.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_HTML = "plu.create_html.pageTitle";
    private static final String PROPERTY_DEFAULT_RESULT_PER_PAGE = "plu.resultList.itemsPerPage";

    // templates
    private static final String TEMPLATE_MANAGE_PLU = "/admin/plugins/plu/manage_plu.html";
    private static final String TEMPLATE_TREE_PLU = "/admin/plugins/plu/tree_plu.html";
    private static final String TEMPLATE_APPROVE_PLU = "/admin/plugins/plu/approve_plu.html";
    private static final String TEMPLATE_APPLICABLE_PLU = "/admin/plugins/plu/applicable_plu.html";
    private static final String TEMPLATE_MODIFY_PLU = "/admin/plugins/plu/modify_plu.html";
    private static final String TEMPLATE_CORRECT_PLU = "/admin/plugins/plu/correct_plu.html";
    private static final String TEMPLATE_CREATE_FOLDER = "/admin/plugins/plu/create_folder.html";
    private static final String TEMPLATE_MODIFY_FOLDER = "/admin/plugins/plu/modify_folder.html";
    private static final String TEMPLATE_CORRECT_FOLDER = "/admin/plugins/plu/correct_folder.html";
    private static final String TEMPLATE_VIEW_FOLDER = "/admin/plugins/plu/view_folder.html";
    private static final String TEMPLATE_CHOICE_CREATE_ATOME = "/admin/plugins/plu/choice_create_atome.html";
    private static final String TEMPLATE_CREATE_ATOME = "/admin/plugins/plu/create_atome.html";
    private static final String TEMPLATE_CREATE_ATOME_WITH_OLD = "/admin/plugins/plu/create_atome_with_old.html";
    private static final String TEMPLATE_VIEW_ATOME = "/admin/plugins/plu/view_atome.html";
    private static final String TEMPLATE_MODIFY_ATOME = "/admin/plugins/plu/modify_atome.html";
    private static final String TEMPLATE_CORRECT_ATOME = "/admin/plugins/plu/correct_atome.html";
    private static final String TEMPLATE_EVOLVE_ATOME = "/admin/plugins/plu/evolve_atome.html";
    private static final String TEMPLATE_JOIN_FILE = "/admin/plugins/plu/join_file.html";
    private static final String TEMPLATE_CREATE_HTML = "/admin/plugins/plu/create_html.html";
    private static final String TEMPLATE_IMPORT_HTML = "/admin/plugins/plu/import_html.html";
    private static final String TEMPLATE_DUPLICATE_HTML = "/admin/plugins/plu/duplicate_html.html";

    //Markers
    private static final String MARK_LIST_PLU_LIST = "plu_list";
    private static final String MARK_LIST_TYPE_LIST = "type_list";
    private static final String MARK_LIST_FOLDER_LIST = "folder_list";
    private static final String MARK_LIST_FOLDER_CHILD_LIST = "folder_child_list";
    private static final String MARK_LIST_ATOME_LIST = "atome_list";
    private static final String MARK_LIST_VERSION_LIST = "version_list";
    private static final String MARK_LIST_FILE_LIST = "file_list";
    private static final String MARK_LIST_FILE_ALL = "file_all";
    private static final String MARK_LIST_FILE_ALL_FORMAT = "file_all_format";
    private static final String MARK_PLU = "one_plu";
    private static final String MARK_PLU_APPLIED = "one_plu_applied";
    private static final String MARK_TYPE = "one_type";
    private static final String MARK_FOLDER = "one_folder";
    private static final String MARK_FOLDER_PARENT = "one_folder_parent";
    private static final String MARK_HTML = "folder_html";
    private static final String MARK_ATOME = "one_atome";
    private static final String MARK_VERSION = "one_version";
    private static final String MARK_NEW_VERSION = "new_version";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_PAGINATOR = "paginator";

    // Messages
    private static final String MESSAGE_CONFIRM_APPROVE_PLU = "plu.message.confirmApprovePlu";
    private static final String MESSAGE_CONFIRM_CANCEL_APPROVE_PLU = "plu.message.confirmCancelApprovePlu";
    private static final String MESSAGE_CONFIRM_APPLICABLE_PLU = "plu.message.confirmApplicablePlu";
    private static final String MESSAGE_CONFIRM_CANCEL_APPLICABLE_PLU = "plu.message.confirmCancelApplicablePlu";
    private static final String MESSAGE_CONFIRM_MODIFY_PLU = "plu.message.confirmModifyPlu";
    private static final String MESSAGE_CONFIRM_CORRECT_PLU = "plu.message.confirmCorrectPlu";
    private static final String MESSAGE_CONFIRM_ISO_PLU = "plu.message.confirmIsoPlu";
    private static final String MESSAGE_CONFIRM_SITE_PLU = "plu.message.confirmSitePlu";
    private static final String MESSAGE_CONFIRM_CREATE_FOLDER = "plu.message.confirmCreateFolder";
    private static final String MESSAGE_CONFIRM_CANCEL_CREATE_FOLDER = "plu.message.confirmCancelCreateFolder";
    private static final String MESSAGE_CONFIRM_MODIFY_FOLDER = "plu.message.confirmModifyFolder";
    private static final String MESSAGE_CONFIRM_CANCEL_MODIFY_FOLDER = "plu.message.confirmCancelModifyFolder";
    private static final String MESSAGE_CONFIRM_CORRECT_FOLDER = "plu.message.confirmCorrectFolder";
    private static final String MESSAGE_CONFIRM_CANCEL_CORRECT_FOLDER = "plu.message.confirmCancelCorrectFolder";
    private static final String MESSAGE_CONFIRM_REMOVE_FOLDER = "plu.message.confirmRemoveFolder";
    private static final String MESSAGE_CONFIRM_CREATE_ATOME = "plu.message.confirmCreateAtome";
    private static final String MESSAGE_CONFIRM_CANCEL_CREATE_ATOME = "plu.message.confirmCancelCreateAtome";
    private static final String MESSAGE_CONFIRM_MODIFY_ATOME = "plu.message.confirmModifyAtome";
    private static final String MESSAGE_CONFIRM_CANCEL_MODIFY_ATOME = "plu.message.confirmCancelModifyAtome";
    private static final String MESSAGE_CONFIRM_CORRECT_ATOME = "plu.message.confirmCorrectAtome";
    private static final String MESSAGE_CONFIRM_CANCEL_CORRECT_ATOME = "plu.message.confirmCancelCorrectAtome";
    private static final String MESSAGE_CONFIRM_EVOLVE_ATOME = "plu.message.confirmEvolveAtome";
    private static final String MESSAGE_CONFIRM_CANCEL_EVOLVE_ATOME = "plu.message.confirmCancelEvolveAtome";
    private static final String MESSAGE_CONFIRM_ARCHIVE_ATOME = "plu.message.confirmArchiveAtome";
    private static final String MESSAGE_CONFIRM_UPLOAD_ATOME = "plu.message.confirmUploadAtome";
    private static final String MESSAGE_ERROR_DATE_APPLICATION = "plu.message.errorDateApplication";
    private static final String MESSAGE_ERROR_DATE_APPLICATION_LOWER = "plu.message.errorDateApplicationLower";
    private static final String MESSAGE_ERROR_PLU_WORK = "plu.message.errorPluWork";
    private static final String MESSAGE_ERROR_REQUIRED_FIELD = "plu.message.errorRequiredfield";
    private static final String MESSAGE_ERROR_FOLDER_CREATE = "plu.message.errorFolderCreate";
    private static final String MESSAGE_ERROR_FOLDER_DELETE = "plu.message.errorFolderDelete";
    private static final String MESSAGE_ERROR_FOLDER_IMAGE_TYPE = "plu.message.errorFolderImageType";
    private static final String MESSAGE_ERROR_FILE_CREATE_SIZE = "plu.message.errorFileCreateSize";
    private static final String MESSAGE_ERROR_FILE_CREATE_NAME = "plu.message.errorFileCreateName";
    private static final String MESSAGE_ERROR_FILE_CREATE_TITLE = "plu.message.errorFileCreateTitle";
    private static final String MESSAGE_ERROR_REQUIRED_FILE_EPS = "plu.message.errorFileEps";
    private static final String MESSAGE_ERROR_REQUIRED_FILE_NO_EPS = "plu.message.errorFileNoEps";
    private static final String MESSAGE_ERROR_ATOME_CREATE_ID = "plu.message.errorAtomeCreateId";
    private static final String MESSAGE_ERROR_ATOME_CREATE_NAME = "plu.message.errorAtomeCreateName";
    private static final String MESSAGE_ERROR_ATOME_CREATE_TITLE = "plu.message.errorAtomeCreateTitle";
    private static final String MESSAGE_ERROR_ATOME_CREATE_NUM_VERSION = "plu.message.errorAtomeCreateNumVersion";
    private static final String MESSAGE_ERROR_ATOME_CREATE_NUM_VERSION_SUP = "plu.message.errorAtomeCreateNumVersionSup";

    // parameters
    private static final String PARAMETER_PLU_ID = "id_plu";
    private static final String PARAMETER_PLU_TYPE = "id_type";
    private static final String PARAMETER_PLU_CAUSE = "cause";
    private static final String PARAMETER_PLU_REFERENCE = "reference";
    private static final String PARAMETER_PLU_LIST_ID = "plu_list_id";
    private static final String PARAMETER_FOLDER_ID = "id_folder";
    private static final String PARAMETER_FOLDER_TITLE = "folder_title";
    private static final String PARAMETER_FOLDER_TITLE_OLD = "folder_title_old";
    private static final String PARAMETER_FOLDER_DESCRIPTION = "folder_description";
    private static final String PARAMETER_FOLDER_PARENT_ID = "id_parent_folder";
    private static final String PARAMETER_FOLDER_IMAGE = "folder_image";
    private static final String PARAMETER_FOLDER_IMAGE_CHECK = "image_check";
    private static final String PARAMETER_FOLDER_HTML = "folder_html";
    private static final String PARAMETER_FOLDER_HTML_CHECK = "html_check";
    private static final String PARAMETER_ATOME_ID = "id_atome";
    private static final String PARAMETER_ATOME_OLD_ID = "id_atome_old";
    private static final String PARAMETER_ATOME_NAME = "atome_name";
    private static final String PARAMETER_ATOME_TITLE = "atome_title";
    private static final String PARAMETER_ATOME_DESCRIPTION = "atome_description";
    private static final String PARAMETER_VERSION_ID = "id_version";
    private static final String PARAMETER_VERSION_NUM = "num_version";
    private static final String PARAMETER_VERSION_NUM_OLD = "num_version_old";
    private static final String PARAMETER_VERSION_D1 = "version_d1";
    private static final String PARAMETER_VERSION_D2 = "version_d2";
    private static final String PARAMETER_VERSION_D3 = "version_d3";
    private static final String PARAMETER_VERSION_D4 = "version_d4";
    private static final String PARAMETER_DATE_JURIDIQUE = "date_juridique";
    private static final String PARAMETER_DATE_APPLICATION = "date_application";
    private static final String PARAMETER_FILE_NAME = "file_name";
    private static final String PARAMETER_FILE_TITLE = "file_title";
    private static final String PARAMETER_FILE_FORMAT = "file_format";
    private static final String PARAMETER_FILE_CHECK = "file_check";
    private static final String PARAMETER_FILE = "file";
    private static final String PARAMETER_HISTORY_DESCRIPTION = "description";
    private static final String PARAMETER_PAGE_INDEX = "page_index";

    // Jsp Definition
    private static final String JSP_REDIRECT_TO_MANAGE_PLU = "../plu/ManagePlu.jsp";
    private static final String JSP_REDIRECT_TO_TREE_PLU = "../plu/TreePlu.jsp";
    private static final String JSP_REDIRECT_TO_CHOICE_CREATE_ATOME = "../atome/ChoiceCreateAtome.jsp";
    private static final String JSP_MANAGE_PLU = "jsp/admin/plugins/plu/plu/ManagePlu.jsp";
    private static final String JSP_TREE_PLU = "jsp/admin/plugins/plu/plu/TreePlu.jsp";
    private static final String JSP_CHOICE_CREATE_ATOME = "jsp/admin/plugins/plu/atome/ChoiceCreateAtome.jsp";
    private static final String JSP_DO_APPROVE_PLU = "jsp/admin/plugins/plu/plu/DoApprovePlu.jsp";
    private static final String JSP_DO_APPLICABLE_PLU = "jsp/admin/plugins/plu/plu/DoApplicablePlu.jsp";
    private static final String JSP_DO_MODIFY_PLU = "jsp/admin/plugins/plu/plu/DoModifyPlu.jsp";
    private static final String JSP_DO_CORRECT_PLU = "jsp/admin/plugins/plu/plu/DoCorrectPlu.jsp";
    private static final String JSP_DO_ISO_PLU = "jsp/admin/plugins/plu/plu/DoIsoPlu.jsp";
    private static final String JSP_DO_SITE_PLU = "jsp/admin/plugins/plu/plu/DoSitePlu.jsp";
    private static final String JSP_DO_CREATE_FOLDER = "jsp/admin/plugins/plu/folder/DoCreateFolder.jsp";
    private static final String JSP_DO_MODIFY_FOLDER = "jsp/admin/plugins/plu/folder/DoModifyFolder.jsp";
    private static final String JSP_DO_CORRECT_FOLDER = "jsp/admin/plugins/plu/folder/DoCorrectFolder.jsp";
    private static final String JSP_DO_REMOVE_FOLDER = "jsp/admin/plugins/plu/folder/DoRemoveFolder.jsp";
    private static final String JSP_DO_CREATE_ATOME = "jsp/admin/plugins/plu/atome/DoCreateAtome.jsp";
    private static final String JSP_DO_MODIFY_ATOME = "jsp/admin/plugins/plu/atome/DoModifyAtome.jsp";
    private static final String JSP_DO_CORRECT_ATOME = "jsp/admin/plugins/plu/atome/DoCorrectAtome.jsp";
    private static final String JSP_DO_EVOLVE_ATOME = "jsp/admin/plugins/plu/atome/DoEvolveAtome.jsp";
    private static final String JSP_DO_ARCHIVE_ATOME = "jsp/admin/plugins/plu/atome/DoArchiveAtome.jsp";
    private static final String JSP_DO_UPLOAD_ATOME = "jsp/admin/plugins/plu/atome/DoUploadAtome.jsp";
    private static final String JSP_REDIRECT_TO_VIEW_ATOME = "jsp/admin/plugins/plu/atome/ViewAtome.jsp";

    //Variables
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;
    private IPluServices _pluServices;
    private ITypeServices _typeServices;
    private IHistoryServices _historyServices;
    private IFolderServices _folderServices;
    private IAtomeServices _atomeServices;
    private IVersionServices _versionServices;
    private IFolderVersionServices _folderVersionServices;
    private IFileServices _fileServices;
    private IFileContentServices _fileContentServices;
    
    private List<File> _fileList = new ArrayList<File>(  );
    private Folder _folderHtml = new Folder(  );
    private Folder _folderImage = new Folder(  );

    public PluJspBean(  )
    {
        super(  );
        _pluServices = (IPluServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.pluServices" );
        _typeServices = (ITypeServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.typeServices" );
        _historyServices = (IHistoryServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.historyServices" );
        _folderServices = (IFolderServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.folderServices" );
        _atomeServices = (IAtomeServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.atomeServices" );
        _versionServices = (IVersionServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.versionServices" );
        _folderVersionServices = (IFolderVersionServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.folderVersionServices" );
        _fileServices = (IFileServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.fileServices" );
        _fileContentServices = (IFileContentServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.fileContentServices" );
    }

    public String getManagePlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_PLU_LIST );

        Plu plu = _pluServices.findPluApplied(  );
        List<Plu> pluList = _pluServices.findAll(  );
        
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU_APPLIED, plu );
        model.put( MARK_LIST_PLU_LIST, pluList );
        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_PLU, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    public String getApprovePlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_APPROVE_PLU );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        List<Type> typeList = _typeServices.findAll(  );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_TYPE_LIST, typeList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_APPROVE_PLU, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getConfirmCancelApprovePlu( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        UrlItem url = new UrlItem( JSP_MANAGE_PLU );

        Object[] args = { nIdPlu };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_APPROVE_PLU, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String getConfirmApprovePlu( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_DATE_JURIDIQUE ).equals( "" ) ||
                request.getParameter( PARAMETER_PLU_TYPE ).equals( "" ) ||
                request.getParameter( PARAMETER_PLU_CAUSE ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdType = Integer.parseInt( request.getParameter( PARAMETER_PLU_TYPE ) );
        String strCause = request.getParameter( PARAMETER_PLU_CAUSE );
        String strReference = request.getParameter( PARAMETER_PLU_REFERENCE );
        String strDate = request.getParameter( PARAMETER_DATE_JURIDIQUE );

        UrlItem url = new UrlItem( JSP_DO_APPROVE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_PLU_TYPE, nIdType );
        url.addParameter( PARAMETER_PLU_CAUSE, strCause );
        url.addParameter( PARAMETER_PLU_REFERENCE, strReference );
        url.addParameter( PARAMETER_DATE_JURIDIQUE, strDate );

        Object[] args = { nIdPlu, strCause, strDate };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_APPROVE_PLU, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doApprovePlu( HttpServletRequest request )
        throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdType = Integer.parseInt( request.getParameter( PARAMETER_PLU_TYPE ) );
        Type type = _typeServices.findByPrimaryKey( nIdType );
        plu.setType( type );
        plu.setCause( request.getParameter( PARAMETER_PLU_CAUSE ) );
        plu.setReference( request.getParameter( PARAMETER_PLU_REFERENCE ) );

        Date dj = stringToDate( request.getParameter( PARAMETER_DATE_JURIDIQUE ), "dd/MM/yyyy" );
        plu.setDj( dj );
        _pluServices.update( plu );

        List<Version> versionList = _versionServices.selectApprove( nIdPlu );
        for( Version version : versionList )
        {
        	version.setD1( dj );
        	_versionServices.update( version );
        }

        return JSP_REDIRECT_TO_MANAGE_PLU;
    }

    public String getApplicablePlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_APPLICABLE_PLU );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        Type type = _typeServices.findByPrimaryKey( plu.getType(  ).getId(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_TYPE, type );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_APPLICABLE_PLU, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getConfirmCancelApplicablePlu( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String strCause = request.getParameter( PARAMETER_PLU_CAUSE );

        UrlItem url = new UrlItem( JSP_MANAGE_PLU );

        Object[] args = { nIdPlu, strCause };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_APPLICABLE_PLU, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String getConfirmApplicablePlu( HttpServletRequest request )
        throws ParseException
    {
        if ( request.getParameter( PARAMETER_DATE_APPLICATION ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_APPLICATION, AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String strCause = request.getParameter( PARAMETER_PLU_CAUSE );
        String strDj = request.getParameter( PARAMETER_DATE_JURIDIQUE );
        String strDa = request.getParameter( PARAMETER_DATE_APPLICATION );
        Date dj = stringToDate( request.getParameter( PARAMETER_DATE_JURIDIQUE ), "dd/MM/yyyy" );
        Date da = stringToDate( request.getParameter( PARAMETER_DATE_APPLICATION ), "dd/MM/yyyy" );

        if ( da.before( dj ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_APPLICATION_LOWER,
                AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_DO_APPLICABLE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_DATE_JURIDIQUE, strDj );
        url.addParameter( PARAMETER_DATE_APPLICATION, strDa );

        Object[] args = { nIdPlu, strCause, da };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_APPLICABLE_PLU, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doApplicablePlu( HttpServletRequest request )
        throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );
        Date dj = stringToDate( request.getParameter( PARAMETER_DATE_JURIDIQUE ), "dd/MM/yyyy" );
        Date da = stringToDate( request.getParameter( PARAMETER_DATE_APPLICATION ), "dd/MM/yyyy" );

        plu.setDa( da );
        _pluServices.update( plu );

        List<Version> versionList = _versionServices.selectApplication( nIdPlu, da );
        for( Version version : versionList )
        {
        	version.setD2( da );
        	_versionServices.update( version );
        }

        versionList.clear(  );
        versionList = _versionServices.selectEvolution( nIdPlu - 1, dj );
        for( Version version : versionList )
        {
        	version.setD3( dj );
        	_versionServices.update( version );
        }

        GregorianCalendar calendar = new GregorianCalendar(  );
        calendar.setTime( da );
        calendar.add( Calendar.DATE, -1 );

        Date date = calendar.getTime(  );
        
        versionList.clear(  );
        versionList = _versionServices.selectArchive( nIdPlu - 1, date );
        for( Version version : versionList )
        {
        	version.setD4( date );
        	_versionServices.update( version );
        }

        Plu plu2 = new Plu(  );
        _pluServices.create( plu2 );

        plu2 = _pluServices.findPluWork(  );

        List<Folder> folderList = _folderServices.findByPluId( nIdPlu );
        Map<Integer, Integer> mapIdOldIdNew = new Hashtable<Integer, Integer>(  );

        for ( Folder folder : folderList )
        {
            Folder folder2 = new Folder(  );
            folder2.setPlu( plu2.getId(  ) );

            if ( mapIdOldIdNew.containsKey( folder.getParentFolder(  ) ) )
            {
                folder2.setParentFolder( mapIdOldIdNew.get( folder.getParentFolder(  ) ) );
            }
            else
            {
                folder2.setParentFolder( folder.getParentFolder(  ) );
            }

            folder2.setTitle( folder.getTitle(  ) );
            folder2.setDescription( folder.getDescription(  ) );
            folder2.setImg( folder.getImg(  ) );
            folder2.setHtml( folder.getHtml(  ) );
            _folderServices.create( folder2 );

            folder2 = _folderServices.findLastFolder(  );
            mapIdOldIdNew.put( folder.getId(  ), folder2.getId(  ) );

            List<FolderVersion> folderVersionList = _folderVersionServices.findByFolder( folder );

            for ( FolderVersion folderVersion : folderVersionList )
            {
                FolderVersion folderVersion2 = new FolderVersion(  );
                folderVersion2.setVersion( folderVersion.getVersion(  ) );
                folderVersion2.setFolder( folder2 );
                _folderVersionServices.create( folderVersion2 );
            }
        }

        return JSP_REDIRECT_TO_MANAGE_PLU;
    }

    public String getModifyPlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_PLU );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        Type type = _typeServices.findByPrimaryKey( plu.getType(  ).getId(  ) );

        List<Type> typeList = _typeServices.findAll(  );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_TYPE, type );
        model.put( MARK_LIST_TYPE_LIST, typeList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PLU, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getConfirmModifyPlu( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdType = Integer.parseInt( request.getParameter( PARAMETER_PLU_TYPE ) );
        String strCause = request.getParameter( PARAMETER_PLU_CAUSE );
        String strReference = request.getParameter( PARAMETER_PLU_REFERENCE );
        String strDate = request.getParameter( PARAMETER_DATE_JURIDIQUE );

        UrlItem url = new UrlItem( JSP_DO_MODIFY_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_PLU_TYPE, nIdType );
        url.addParameter( PARAMETER_PLU_CAUSE, strCause );
        url.addParameter( PARAMETER_PLU_REFERENCE, strReference );
        url.addParameter( PARAMETER_DATE_JURIDIQUE, strDate );

        Object[] args = { nIdPlu, strCause };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_MODIFY_PLU, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doModifyPlu( HttpServletRequest request )
        throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdType = Integer.parseInt( request.getParameter( PARAMETER_PLU_TYPE ) );
        Type type = _typeServices.findByPrimaryKey( nIdType );
        plu.setType( type );
        plu.setCause( request.getParameter( PARAMETER_PLU_CAUSE ) );
        plu.setReference( request.getParameter( PARAMETER_PLU_REFERENCE ) );

        Date dj = stringToDate( request.getParameter( PARAMETER_DATE_JURIDIQUE ), "dd/MM/yyyy" );
        plu.setDj( dj );
        _pluServices.update( plu );

        return JSP_REDIRECT_TO_MANAGE_PLU;
    }

    public String getCorrectPlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CORRECT_PLU );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        Type type = _typeServices.findByPrimaryKey( plu.getType(  ).getId(  ) );

        List<Type> typeList = _typeServices.findAll(  );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_TYPE, type );
        model.put( MARK_LIST_TYPE_LIST, typeList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CORRECT_PLU, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getConfirmCorrectPlu( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdType = Integer.parseInt( request.getParameter( PARAMETER_PLU_TYPE ) );
        String strCause = request.getParameter( PARAMETER_PLU_CAUSE );
        String strReference = request.getParameter( PARAMETER_PLU_REFERENCE );
        String strDescription = request.getParameter( PARAMETER_HISTORY_DESCRIPTION );

        UrlItem url = new UrlItem( JSP_DO_CORRECT_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_PLU_TYPE, nIdType );
        url.addParameter( PARAMETER_PLU_CAUSE, strCause );
        url.addParameter( PARAMETER_PLU_REFERENCE, strReference );
        url.addParameter( PARAMETER_HISTORY_DESCRIPTION, strDescription );

        Object[] args = { nIdPlu, strCause };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CORRECT_PLU, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doCorrectPlu( HttpServletRequest request )
        throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdType = Integer.parseInt( request.getParameter( PARAMETER_PLU_TYPE ) );
        Type type = _typeServices.findByPrimaryKey( nIdType );
        plu.setType( type );
        plu.setCause( request.getParameter( PARAMETER_PLU_CAUSE ) );
        plu.setReference( request.getParameter( PARAMETER_PLU_REFERENCE ) );
        _pluServices.update( plu );

        History history = new History(  );
        history.setPlu( nIdPlu );

        Date date = new Date(  );
        history.setDc( date );
        history.setDescription( request.getParameter( PARAMETER_HISTORY_DESCRIPTION ) );
        _historyServices.create( history );

        return JSP_REDIRECT_TO_MANAGE_PLU;
    }

    public String getConfirmIsoPlu( HttpServletRequest request )
    {
        int nIdContact = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        UrlItem url = new UrlItem( JSP_DO_ISO_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdContact );
        url.addParameter( PARAMETER_PLU_LIST_ID, request.getParameter( PARAMETER_PLU_LIST_ID ) );

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
        url.addParameter( PARAMETER_PLU_LIST_ID, request.getParameter( PARAMETER_PLU_LIST_ID ) );

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
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );
        if( plu == null )
        {
        	plu = new Plu(  );
        	plu.setId( 0 );
        }
        List<Plu> pluList = _pluServices.findAll(  );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_RESULT_PER_PAGE, 10 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_PLU_LIST, pluList );

        if ( request.getParameter( PARAMETER_FOLDER_ID ) != null )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_TREE_ATOME );

            int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

            Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
            if( folder == null )
            {
            	folder = new Folder(  );
            	folder.setId( 0 );
            }
            model.put( MARK_FOLDER, folder );

            if ( request.getParameter( PARAMETER_FOLDER_TITLE ) != null )
            {
                model.put( PARAMETER_FOLDER_TITLE, request.getParameter( PARAMETER_FOLDER_TITLE ) );
            }

            if ( ( request.getParameter( PARAMETER_ATOME_NAME ) != null ) ||
                    ( request.getParameter( PARAMETER_ATOME_TITLE ) != null ) ||
                    ( request.getParameter( PARAMETER_VERSION_NUM ) != null ) ||
                    ( request.getParameter( PARAMETER_VERSION_D1 ) != null ) ||
                    ( request.getParameter( PARAMETER_VERSION_D2 ) != null ) ||
                    ( request.getParameter( PARAMETER_VERSION_D3 ) != null ) ||
                    ( request.getParameter( PARAMETER_VERSION_D4 ) != null ) )
            {
                String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
                String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
                String strNumVersion = request.getParameter( PARAMETER_VERSION_NUM );
                String strD1 = request.getParameter( PARAMETER_VERSION_D1 );
                String strD2 = request.getParameter( PARAMETER_VERSION_D2 );
                String strD3 = request.getParameter( PARAMETER_VERSION_D3 );
                String strD4 = request.getParameter( PARAMETER_VERSION_D4 );

                AtomeFilter atomeFilter = new AtomeFilter(  );
                VersionFilter versionFilter = new VersionFilter(  );

                if ( !atomeName.equals( "" ) )
                {
                    atomeFilter.set_name( atomeName );
                }

                if ( !atomeTitle.equals( "" ) )
                {
                    atomeFilter.set_title( atomeTitle );
                }

                if ( !strNumVersion.equals( "" ) )
                {
                    Boolean p = Pattern.matches( "[0-9]+?", strNumVersion );

                    if ( p )
                    {
                        int numVersion = Integer.parseInt( strNumVersion );
                        versionFilter.set_version( numVersion );
                    }
                }

                if ( !strD1.equals( "" ) )
                {
                    Date d1;

                    try
                    {
                        d1 = stringToDate( strD1, "dd/MM/yyyy" );
                        versionFilter.set_d1( d1 );
                    }
                    catch ( Exception e )
                    {
                        e.printStackTrace(  );
                    }
                }

                if ( !strD2.equals( "" ) )
                {
                    Date d2;

                    try
                    {
                        d2 = stringToDate( strD2, "dd/MM/yyyy" );
                        versionFilter.set_d2( d2 );
                    }
                    catch ( ParseException e )
                    {
                        e.printStackTrace(  );
                    }
                }

                if ( !strD3.equals( "" ) )
                {
                    Date d3;

                    try
                    {
                        d3 = stringToDate( strD3, "dd/MM/yyyy" );
                        versionFilter.set_d3( d3 );
                    }
                    catch ( ParseException e )
                    {
                        e.printStackTrace(  );
                    }
                }

                if ( !strD4.equals( "" ) )
                {
                    Date d4;

                    try
                    {
                        d4 = stringToDate( strD4, "dd/MM/yyyy" );
                        versionFilter.set_d4( d4 );
                    }
                    catch ( ParseException e )
                    {
                        e.printStackTrace(  );
                    }
                }

                List<Version> versionList = _versionServices.findByFilter( atomeFilter, versionFilter );

                Paginator<Version> paginator = new Paginator<Version>( (List<Version>) versionList, _nItemsPerPage,
                        JSP_TREE_PLU + "?id_plu=" + plu.getId(  ) + "&id_folder=" + folder.getId(  ) + "&atome_name=" +
                        atomeName + "&atome_title=" + atomeTitle + "&num_version=" + strNumVersion + "&version_d1=" +
                        strD1 + "&version_d2=" + strD2 + "&version_d3=" + strD3 + "&version_d4=" + strD4,
                        PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

                model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
                model.put( MARK_PAGINATOR, paginator );
                model.put( MARK_LIST_VERSION_LIST, paginator.getPageItems(  ) );
                model.put( PARAMETER_ATOME_NAME, atomeName );
                model.put( PARAMETER_ATOME_TITLE, atomeTitle );
                model.put( PARAMETER_VERSION_NUM, strNumVersion );
                model.put( PARAMETER_VERSION_D1, strD1 );
                model.put( PARAMETER_VERSION_D2, strD2 );
                model.put( PARAMETER_VERSION_D3, strD3 );
                model.put( PARAMETER_VERSION_D4, strD4 );
            }
            else
            {
                List<Version> versionList = _versionServices.findByPluAndFolder( folder.getPlu(  ), nIdFolder );

                Paginator<Version> paginator = new Paginator<Version>( (List<Version>) versionList, _nItemsPerPage,
                        JSP_TREE_PLU + "?id_plu=" + plu.getId(  ) + "&id_folder=" + folder.getId(  ),
                        PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

                model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
                model.put( MARK_PAGINATOR, paginator );
                model.put( MARK_LIST_VERSION_LIST, paginator.getPageItems(  ) );
            }
        }
        else
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_TREE_FOLDER );

            if ( request.getParameter( PARAMETER_FOLDER_TITLE ) != null )
            {
                String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );

                FolderFilter folderFilter = new FolderFilter(  );

                if ( nIdPlu != 0 )
                {
                    folderFilter.set_plu( nIdPlu );
                }

                if ( !folderTitle.equals( "" ) )
                {
                    folderFilter.set_title( folderTitle );
                }

                List<Folder> folderList = _folderServices.findByFilter( folderFilter );

                Paginator<Folder> paginator = new Paginator<Folder>( (List<Folder>) folderList, _nItemsPerPage,
                        JSP_TREE_PLU + "?id_plu=" + nIdPlu + "&folder_title=" + folderTitle, PARAMETER_PAGE_INDEX,
                        _strCurrentPageIndex );

                model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
                model.put( MARK_PAGINATOR, paginator );
                model.put( MARK_LIST_FOLDER_LIST, paginator.getPageItems(  ) );
                model.put( PARAMETER_FOLDER_TITLE, folderTitle );
            }
            else
            {
                List<Folder> folderList = _folderServices.findByPluId( nIdPlu );
                Paginator<Folder> paginator = new Paginator<Folder>( (List<Folder>) folderList, _nItemsPerPage,
                        JSP_TREE_PLU + "?id_plu=" + nIdPlu, PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
                model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
                model.put( MARK_PAGINATOR, paginator );
                model.put( MARK_LIST_FOLDER_LIST, paginator.getPageItems(  ) );
            }
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TREE_PLU, getLocale(  ), model );

        _fileList.clear(  );
        _folderHtml.setHtml( null );
        _folderImage.setImg( null );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getCreateFolder( HttpServletRequest request )
    {
    	 setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_FOLDER );
    	 
        Plu plu = _pluServices.findPluWork(  );

        if ( plu.getId(  ) == 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_PLU_WORK, AdminMessage.TYPE_STOP );
        }
        
        Collection<Folder> folderList = _folderServices.findByPluId( plu.getId(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );

        if ( request.getParameter( PARAMETER_FOLDER_ID ) != null )
        {
            int idFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
            Folder folder = _folderServices.findByPrimaryKey( idFolder );
            _folderHtml.setHtml( folder.getHtml(  ) );
            model.put( MARK_HTML, 1 );
        }
        else
        {
            if ( request instanceof MultipartHttpServletRequest )
            {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_HTML );

                PhysicalFile physicalFile = new PhysicalFile(  );
                physicalFile.setValue( fileItem.get(  ) );
                _folderHtml.setHtml( physicalFile.getValue(  ) );
                model.put( MARK_HTML, 1 );
            }
            else
            {
                if ( request.getParameter( PARAMETER_FOLDER_HTML ) != null )
                {
                    String strHtml = request.getParameter( PARAMETER_FOLDER_HTML );
                    _folderHtml.setHtml( strHtml.getBytes(  ) );
                    model.put( MARK_HTML, 1 );
                }
            }
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_FOLDER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getConfirmCancelCreateFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );

        UrlItem url = new UrlItem( JSP_TREE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );

        Object[] args = { folderTitle };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_CREATE_FOLDER, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String getConfirmCreateFolder( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_TITLE ).equals( "" ) ||
                request.getParameter( PARAMETER_FOLDER_DESCRIPTION ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );

        UrlItem url = new UrlItem( JSP_DO_CREATE_FOLDER );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_PARENT_ID, request.getParameter( PARAMETER_FOLDER_PARENT_ID ) );
        url.addParameter( PARAMETER_FOLDER_TITLE, folderTitle );
        url.addParameter( PARAMETER_FOLDER_DESCRIPTION, request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );

        if ( request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK ) != null )
        {
            String[] check = request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK );

            for ( int j = 0; j < check.length; ++j )
            {
                url.addParameter( PARAMETER_FOLDER_HTML_CHECK, check[j] );
            }
        }

        Object[] args = { folderTitle };

        Folder folder = _folderServices.findForTestTitle( folderTitle );

        if ( folder.getTitle(  ) != null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FOLDER_CREATE, args, AdminMessage.TYPE_STOP );
        }

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_IMAGE );

            if( fileItem.getSize(  ) >  0 )
            {
	            String name = fileItem.getName(  );
	            String type = name.substring( name.lastIndexOf( "." ) );
	            if( !type.equals( ".jpg" ) && !type.equals( ".pgn" ) && !type.equals( ".gif" ) )
	            {
	            	return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FOLDER_IMAGE_TYPE, args, AdminMessage.TYPE_STOP );
	            }
	            
	            PhysicalFile physicalFile = new PhysicalFile(  );
	            physicalFile.setValue( fileItem.get(  ) );
	            
	            _folderImage.setImg( physicalFile.getValue(  ) );
            }
        }

        return AdminMessageService.getMessageUrl( (MultipartHttpServletRequest) request, MESSAGE_CONFIRM_CREATE_FOLDER,
            args, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
    }

    public String doCreateFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        int idParentFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_PARENT_ID ) );

        Folder folder = new Folder(  );
        folder.setPlu( nIdPlu );
        folder.setParentFolder( idParentFolder );
        folder.setTitle( request.getParameter( PARAMETER_FOLDER_TITLE ) );
        folder.setDescription( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        if( _folderImage.getImg(  ) != null )
        {
        	folder.setImg( _folderImage.getImg(  ) );
        }
        if ( "true".equals( request.getParameter( PARAMETER_FOLDER_HTML_CHECK ) ) )
        {
            folder.setHtml( _folderHtml.getHtml(  ) );
        }

        _folderServices.create( folder );

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + nIdPlu;
    }

    public String getConfirmRemoveFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

        UrlItem url = new UrlItem( JSP_DO_REMOVE_FOLDER );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );

        Object[] args = { request.getParameter( PARAMETER_FOLDER_TITLE ) };

        Folder folder = _folderServices.findForDelete( nIdFolder );

        if ( folder.getId(  ) != 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FOLDER_DELETE, args, AdminMessage.TYPE_STOP );
        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_FOLDER, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doRemoveFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        _folderServices.remove( folder );

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  );
    }

    public String getModifyFolder( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_FOLDER );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        Folder folderParent = _folderServices.findByPrimaryKey( folder.getParentFolder(  ) );

        Collection<Folder> folderList = _folderServices.findByPluId( nIdPlu );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_FOLDER_PARENT, folderParent );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_HTML );

            PhysicalFile physicalFile = new PhysicalFile(  );
            physicalFile.setValue( fileItem.get(  ) );
            _folderHtml.setImg( physicalFile.getValue(  ) );
            model.put( MARK_HTML, 1 );
        }
        else
        {
            if ( request.getParameter( PARAMETER_FOLDER_HTML ) != null )
            {
                String strHtml = request.getParameter( PARAMETER_FOLDER_HTML );
                _folderHtml.setHtml( strHtml.getBytes(  ) );
                model.put( MARK_HTML, 1 );
            }
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_FOLDER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getConfirmCancelModifyFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );

        UrlItem url = new UrlItem( JSP_TREE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );

        Object[] args = { folderTitle };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_MODIFY_FOLDER, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String getConfirmModifyFolder( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_TITLE ).equals( "" ) ||
                request.getParameter( PARAMETER_FOLDER_DESCRIPTION ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );
        String description = request.getParameter( PARAMETER_FOLDER_DESCRIPTION ).replaceAll( "\"", "\'" );

        UrlItem url = new UrlItem( JSP_DO_MODIFY_FOLDER );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        url.addParameter( PARAMETER_FOLDER_PARENT_ID, request.getParameter( PARAMETER_FOLDER_PARENT_ID ) );
        url.addParameter( PARAMETER_FOLDER_TITLE, folderTitle );
        url.addParameter( PARAMETER_FOLDER_DESCRIPTION, description );

        if ( request.getParameterValues( PARAMETER_FOLDER_IMAGE_CHECK ) != null )
        {
            String[] check = request.getParameterValues( PARAMETER_FOLDER_IMAGE_CHECK );

            for ( int j = 0; j < check.length; ++j )
            {
                url.addParameter( PARAMETER_FOLDER_IMAGE_CHECK, check[j] );
            }
        }

        if ( request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK ) != null )
        {
            String[] check = request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK );

            for ( int j = 0; j < check.length; ++j )
            {
                url.addParameter( PARAMETER_FOLDER_HTML_CHECK, check[j] );
            }
        }

        Object[] args = { folderTitle };
        Folder folder = _folderServices.findForTestTitle( folderTitle );

        if ( ( folder.getTitle(  ) != null ) &&
                !folder.getTitle(  ).equals( request.getParameter( PARAMETER_FOLDER_TITLE_OLD ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FOLDER_CREATE, args, AdminMessage.TYPE_STOP );
        }

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_IMAGE );

            String name = fileItem.getName(  );
            String type = name.substring( name.lastIndexOf( "." ) );
            if( !type.equals( ".jpg" ) && !type.equals( ".pgn" ) && !type.equals( ".gif" ) )
            {
            	return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FOLDER_IMAGE_TYPE, args, AdminMessage.TYPE_STOP );
            }
            
            PhysicalFile physicalFile = new PhysicalFile(  );
            physicalFile.setValue( fileItem.get(  ) );
            _folderImage.setImg( physicalFile.getValue(  ) );
        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_MODIFY_FOLDER, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doModifyFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        int idParentFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_PARENT_ID ) );
        String description = request.getParameter( PARAMETER_FOLDER_DESCRIPTION );

        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        folder.setTitle( request.getParameter( PARAMETER_FOLDER_TITLE ) );
        folder.setDescription( description );
        folder.setParentFolder( idParentFolder );

        if ( !"true".equals( request.getParameter( PARAMETER_FOLDER_IMAGE_CHECK ) ) )
        {
            folder.setImg( _folderImage.getImg(  ) );
        }

        if ( !"true".equals( request.getParameter( PARAMETER_FOLDER_HTML_CHECK ) ) )
        {
            folder.setHtml( _folderHtml.getHtml(  ) );
        }

        _folderServices.update( folder );

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  );
    }

    public String getCorrectFolder( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CORRECT_FOLDER );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        Folder folderParent = _folderServices.findByPrimaryKey( folder.getParentFolder(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_FOLDER_PARENT, folderParent );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_HTML );

            PhysicalFile physicalFile = new PhysicalFile(  );
            physicalFile.setValue( fileItem.get(  ) );
            _folderHtml.setImg( physicalFile.getValue(  ) );
            model.put( MARK_HTML, 1 );
        }
        else
        {
            if ( request.getParameter( PARAMETER_FOLDER_HTML ) != null )
            {
                String strHtml = request.getParameter( PARAMETER_FOLDER_HTML );
                _folderHtml.setHtml( strHtml.getBytes(  ) );
                model.put( MARK_HTML, 1 );
            }
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CORRECT_FOLDER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getConfirmCancelCorrectFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );

        UrlItem url = new UrlItem( JSP_TREE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );

        Object[] args = { folderTitle };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_CORRECT_FOLDER, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String getConfirmCorrectFolder( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_TITLE ).equals( "" ) ||
                request.getParameter( PARAMETER_FOLDER_DESCRIPTION ).equals( "" ) ||
                request.getParameter( PARAMETER_HISTORY_DESCRIPTION ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );

        UrlItem url = new UrlItem( JSP_DO_CORRECT_FOLDER );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_PARENT_ID, request.getParameter( PARAMETER_FOLDER_PARENT_ID ) );
        url.addParameter( PARAMETER_FOLDER_TITLE, folderTitle );
        url.addParameter( PARAMETER_FOLDER_DESCRIPTION, request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        url.addParameter( PARAMETER_HISTORY_DESCRIPTION, request.getParameter( PARAMETER_HISTORY_DESCRIPTION ) );

        if ( request.getParameterValues( PARAMETER_FOLDER_IMAGE_CHECK ) != null )
        {
            String[] check = request.getParameterValues( PARAMETER_FOLDER_IMAGE_CHECK );

            for ( int j = 0; j < check.length; ++j )
            {
                url.addParameter( PARAMETER_FOLDER_IMAGE_CHECK, check[j] );
            }
        }

        if ( request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK ) != null )
        {
            String[] check = request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK );

            for ( int j = 0; j < check.length; ++j )
            {
                url.addParameter( PARAMETER_FOLDER_HTML_CHECK, check[j] );
            }
        }

        Object[] args = { folderTitle };
        Folder folder = _folderServices.findForTestTitle( folderTitle );

        if ( ( folder != null ) && !folder.getTitle(  ).equals( request.getParameter( PARAMETER_FOLDER_TITLE_OLD ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FOLDER_CREATE, args, AdminMessage.TYPE_STOP );
        }

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_IMAGE );

            String name = fileItem.getName(  );
            String type = name.substring( name.lastIndexOf( "." ) );
            if( !type.equals( ".jpg" ) && !type.equals( ".pgn" ) && !type.equals( ".gif" ) )
            {
            	return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FOLDER_IMAGE_TYPE, args, AdminMessage.TYPE_STOP );
            }
            
            PhysicalFile physicalFile = new PhysicalFile(  );
            physicalFile.setValue( fileItem.get(  ) );
            _folderImage.setImg( physicalFile.getValue(  ) );
        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CORRECT_FOLDER, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doCorrectFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        int idParentFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_PARENT_ID ) );

        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        folder.setTitle( request.getParameter( PARAMETER_FOLDER_TITLE ) );
        folder.setDescription( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        folder.setParentFolder( idParentFolder );

        if ( !"true".equals( request.getParameter( PARAMETER_FOLDER_IMAGE_CHECK ) ) )
        {
            folder.setImg( _folderImage.getImg(  ) );
        }

        if ( !"true".equals( request.getParameter( PARAMETER_FOLDER_HTML_CHECK ) ) )
        {
            folder.setHtml( _folderHtml.getHtml(  ) );
        }

        _folderServices.update( folder );

        History history = new History(  );
        history.setPlu( nIdPlu );
        history.setFolder( nIdFolder );

        Date date = new Date(  );
        history.setDc( date );
        history.setDescription( request.getParameter( PARAMETER_HISTORY_DESCRIPTION ) );
        _historyServices.create( history );

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  );
    }

    public String getViewFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        Folder folderParent = _folderServices.findByPrimaryKey( folder.getParentFolder(  ) );
        List<Folder> folderChildList = _folderServices.findByParent( folder.getParentFolder(  ) );

        List<Version> listVersion = _versionServices.findByPluAndFolder( nIdPlu, nIdFolder );

        List<File> fileList = new ArrayList<File>(  );

        for ( Version version : listVersion )
        {
            fileList.addAll( _fileServices.findByVersion( version.getId(  ) ) );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_FOLDER_PARENT, folderParent );
        model.put( MARK_LIST_FOLDER_CHILD_LIST, folderChildList );
        model.put( MARK_LIST_VERSION_LIST, listVersion );
        model.put( MARK_LIST_FILE_LIST, fileList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VIEW_FOLDER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getChoiceCreateAtome( HttpServletRequest request )
    {
        Plu plu = _pluServices.findPluWork(  );

        if ( plu.getId(  ) == 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_PLU_WORK, AdminMessage.TYPE_STOP );
        }

        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_ATOME );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        if ( folder == null )
        {
        	folder = new Folder(  );
            folder.setId( 0 );
        }

        List<Atome> atomeList = _atomeServices.findAll(  );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_ATOME_LIST, atomeList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CHOICE_CREATE_ATOME, getLocale(  ), model );

        _fileList.clear(  );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getCreateAtome( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_ATOME );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        if ( folder == null )
        {
        	folder = new Folder(  );
            folder.setId( 0 );
        }

        Collection<Folder> folderList = _folderServices.findByPluId( nIdPlu );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );

            if ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) &&
            		( request.getParameter( PARAMETER_FILE_NAME ) != null ) &&
                    ( multipartRequest.getFile( PARAMETER_FILE ) != null ) )
            {
                for( File fileTest : _fileList )
                {
                	if( fileTest.getTitle(  ).equals( request.getParameter( PARAMETER_FILE_TITLE ) ) )
                	{
                		return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FILE_CREATE_TITLE, AdminMessage.TYPE_STOP );
                	}
                }
                
                File file = new File(  );
//                FileContent fileContent = new FileContent(  );
                PhysicalFile physicalFile = new PhysicalFile(  );
                physicalFile.setValue( fileItem.get(  ) );
                
//                fileContent.setFile( physicalFile.getValue(  ) );

                String name = fileItem.getName(  );
                String type = name.substring( name.lastIndexOf( "." ) );
                file.setName( request.getParameter( PARAMETER_FILE_NAME ) );
                file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
                file.setFile( physicalFile.getValue(  ) );
                file.setMimeType( type );
                file.setSize( (int) fileItem.getSize(  ) );
                //file.setMimeType( FileSystemUtil.getMIMEType( FileUploadService.getFileNameOnly( fileItem ) ) );
                _fileList.add( file );
            }
        }

        if ( _fileList != null )
        {
            model.put( MARK_LIST_FILE_LIST, _fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_ATOME, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getCreateAtomeWithOld( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        Collection<Folder> folderList = _folderServices.findByPluId( nIdPlu );

        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        Atome atome = _atomeServices.findByPrimaryKey( nIdAtome );
        Folder folder = _folderServices.findByAtome( nIdAtome );

        int numVersion = _versionServices.findMaxVersion( nIdAtome );
        Version version = _versionServices.findByAtomeAndNumVersion( nIdAtome, numVersion );

        if ( _fileList.isEmpty(  ) )
        {
            _fileList.addAll( _fileServices.findByVersion( version.getId(  ) ) );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_ATOME, atome );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );

            if ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) &&
                    ( multipartRequest.getFile( PARAMETER_FILE ) != null ) )
            {
                File file = new File(  );
//                FileContent fileContent = new FileContent(  );
                PhysicalFile physicalFile = new PhysicalFile(  );
                physicalFile.setValue( fileItem.get(  ) );

                String name = fileItem.getName(  );
                for( File fileTest : _fileList )
                {
                	if( fileTest.getName(  ).equals( name ) )
                	{
                		return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FILE_CREATE_NAME, AdminMessage.TYPE_STOP );
                	}
                }
                
//                fileContent.setFile( physicalFile.getValue(  ) );
                
                String type = name.substring( name.lastIndexOf( "." ) );
                file.setName( request.getParameter( PARAMETER_FILE_NAME ) );
                file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
                file.setFile( physicalFile.getValue(  ) );
                file.setMimeType( type );
                file.setSize( (int) fileItem.getSize(  ) );
                _fileList.add( file );
            }
        }

        if ( _fileList != null )
        {
            model.put( MARK_LIST_FILE_LIST, _fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_ATOME_WITH_OLD, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getConfirmCancelCreateAtome( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

        UrlItem url = new UrlItem( JSP_CHOICE_CREATE_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_CREATE_ATOME, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String getConfirmCreateAtome( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_ID ).equals( "" ) ||
                request.getParameter( PARAMETER_ATOME_ID ).equals( "" ) ||
                request.getParameter( PARAMETER_VERSION_NUM ).equals( "" ) ||
                request.getParameter( PARAMETER_ATOME_NAME ).equals( "" ) ||
                request.getParameter( PARAMETER_ATOME_TITLE ).equals( "" ) ||
                request.getParameter( PARAMETER_ATOME_DESCRIPTION ).equals( "" ) ||
                request.getParameter( PARAMETER_ATOME_NAME ).matches( "[ \']+?" ) ||
                request.getParameter( PARAMETER_ATOME_TITLE ).matches( "[ \']+?" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        int numVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
        String atomeDescription = request.getParameter( PARAMETER_ATOME_DESCRIPTION );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE );

        UrlItem url = new UrlItem( JSP_DO_CREATE_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        url.addParameter( PARAMETER_ATOME_ID, nIdAtome );
        url.addParameter( PARAMETER_VERSION_NUM, numVersion );
        url.addParameter( PARAMETER_ATOME_NAME, atomeName );
        url.addParameter( PARAMETER_ATOME_TITLE, atomeTitle );
        url.addParameter( PARAMETER_ATOME_DESCRIPTION, atomeDescription );

        Object[] argsEps = { atomeName, atomeTitle, numVersion };
        boolean noEps = false;
        boolean eps = false;

        for ( File file : _fileList )
        {
            if ( file.getMimeType(  ).equals( ".eps" ) )
            {
                eps = true;
            }
            else
            {
                noEps = true;
            }
        }

        if ( !eps )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FILE_EPS, argsEps,
                AdminMessage.TYPE_STOP );
        }

        if ( !noEps )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FILE_NO_EPS, argsEps,
                AdminMessage.TYPE_STOP );
        }

        for ( int j = 0; j < check.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_CHECK, check[j] );
        }

        for ( int j = 0; j < fileTitle.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_TITLE, fileTitle[j] );
        }

        int i = 0;

        for ( File file : _fileList )
        {
            for ( int j = 0; j < check.length; ++j )
            {
                int c = Integer.parseInt( check[j] );

                if ( c == i )
                {
                    Object[] argsFile = { file.getTitle(  ), file.getName(  ) };

                    if ( file.getSize(  ) <= 0 )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FILE_CREATE_SIZE, argsFile,
                            AdminMessage.TYPE_STOP );
                    }

                    for ( File file2 : _fileServices.findAll(  ) )
                    {
                        if ( file2.getName(  ).equals( file.getName(  ) ) && ( file2.getVersion(  ) == numVersion ) )
                        {
                            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FILE_CREATE_NAME,
                                argsFile, AdminMessage.TYPE_STOP );
                        }

                        if ( file2.getTitle(  ).equals( file.getTitle(  ) ) && ( file2.getVersion(  ) == numVersion ) )
                        {
                            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FILE_CREATE_TITLE,
                                argsFile, AdminMessage.TYPE_STOP );
                        }
                    }
                }
            }

            i++;
        }

        Object[] argsAtome = { atomeName, atomeTitle };

        for ( Atome atome : _atomeServices.findAll(  ) )
        {
            if ( atome.getId(  ) == nIdAtome )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ATOME_CREATE_ID, argsAtome,
                    AdminMessage.TYPE_STOP );
            }

            if ( atome.getName(  ).equals( atomeName ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ATOME_CREATE_NAME, argsAtome,
                    AdminMessage.TYPE_STOP );
            }

            if ( atome.getTitle(  ).equals( atomeTitle ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ATOME_CREATE_TITLE, argsAtome,
                    AdminMessage.TYPE_STOP );
            }
        }

        int maxVersion = _versionServices.findMaxVersion( nIdAtome );
        Object[] argsVersion = { atomeName, atomeTitle, numVersion, maxVersion };

        if ( maxVersion == numVersion )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ATOME_CREATE_NUM_VERSION, argsVersion,
                AdminMessage.TYPE_STOP );
        }

        if ( maxVersion > numVersion )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ATOME_CREATE_NUM_VERSION_SUP, argsVersion,
                AdminMessage.TYPE_STOP );
        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CREATE_ATOME, argsAtome, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doCreateAtome( HttpServletRequest request )
        throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        int numVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) );

        Atome atome = new Atome(  );
        atome.setId( nIdAtome );
        atome.setName( request.getParameter( PARAMETER_ATOME_NAME ) );
        atome.setTitle( request.getParameter( PARAMETER_ATOME_TITLE ) );
        atome.setDescription( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );

        _atomeServices.create( atome );

        Version version = new Version(  );
        version.setAtome( atome );
        version.setVersion( numVersion );

        _versionServices.create( version );

        Version version2 = _versionServices.findByAtomeAndNumVersion( nIdAtome, numVersion );

        FolderVersion folderVersion = new FolderVersion(  );
        folderVersion.setVersion( version2 );
        folderVersion.setFolder( folder );

        _folderVersionServices.create( folderVersion );

        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        int i = 0;
        int order = 1;

        for ( File file : _fileList )
        {
            for ( int j = 0; j < check.length; ++j )
            {
                int c = Integer.parseInt( check[j] );

                if ( c == i )
                {
                    if ( !file.getTitle(  ).equals( fileTitle[i] ) )
                    {
                        file.setTitle( fileTitle[i] );
                    }

                    file.setId( nIdAtome );
                    file.setOrder( order );
                    file.setVersion( version2.getId(  ) );

                    if ( file.getMimeType(  ).equals( ".eps" ) )
                    {
                        file.setEPS( 'O' );
                    }
                    else
                    {
                        file.setEPS( 'N' );
                    }

//                    _fileContentServices.create( file.getFile(  ) );
//                    FileContent fileContent = _fileContentServices.findLastFileContent(  );
//                    file.setFile( fileContent );
                    _fileServices.create( file );

                    order++;
                }
            }

            i++;
        }

        Date date = new Date(  );
        Plu newPlu = _pluServices.findPluWork(  );

        if ( newPlu.getDa(  ) != null )
        {
            newPlu.setDj( date );
            _pluServices.create( newPlu );
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
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );
        if( plu == null )
        {
        	plu = new Plu(  );
        	plu.setId( 0 );
        }

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        if( folder == null )
        {
        	folder = new Folder(  );
        	folder.setId( 0 );
        }

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion );

        List<File> fileAll = _fileServices.findAll(  );
        List<File> fileAllFormat = _fileServices.findAllMimeType(  );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_VERSION, version );
        model.put( MARK_LIST_FILE_ALL, fileAll );
        model.put( MARK_LIST_FILE_ALL_FORMAT, fileAllFormat );

        if ( request.getParameter( PARAMETER_FOLDER_TITLE ) != null )
        {
            model.put( PARAMETER_FOLDER_TITLE, request.getParameter( PARAMETER_FOLDER_TITLE ) );
        }

        if ( request.getParameter( PARAMETER_ATOME_NAME ) != null )
        {
            model.put( PARAMETER_ATOME_NAME, request.getParameter( PARAMETER_ATOME_NAME ) );
        }

        if ( request.getParameter( PARAMETER_ATOME_TITLE ) != null )
        {
            model.put( PARAMETER_ATOME_TITLE, request.getParameter( PARAMETER_ATOME_TITLE ) );
        }

        if ( request.getParameter( PARAMETER_VERSION_NUM ) != null )
        {
            model.put( PARAMETER_VERSION_NUM, request.getParameter( PARAMETER_VERSION_NUM ) );
        }

        if ( request.getParameter( PARAMETER_VERSION_D1 ) != null )
        {
            model.put( PARAMETER_VERSION_D1, request.getParameter( PARAMETER_VERSION_D1 ) );
        }

        if ( request.getParameter( PARAMETER_VERSION_D2 ) != null )
        {
            model.put( PARAMETER_VERSION_D2, request.getParameter( PARAMETER_VERSION_D2 ) );
        }

        if ( request.getParameter( PARAMETER_VERSION_D3 ) != null )
        {
            model.put( PARAMETER_VERSION_D3, request.getParameter( PARAMETER_VERSION_D3 ) );
        }

        if ( request.getParameter( PARAMETER_VERSION_D4 ) != null )
        {
            model.put( PARAMETER_VERSION_D4, request.getParameter( PARAMETER_VERSION_D4 ) );
        }

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_RESULT_PER_PAGE, 10 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        if ( request.getParameter( PARAMETER_FILE_TITLE ) != null )
        {
            String fileTitle = request.getParameter( PARAMETER_FILE_TITLE );
            String fileName = request.getParameter( PARAMETER_FILE_NAME );
            String fileFormat = request.getParameter( PARAMETER_FILE_FORMAT );
            String atomeName = request.getParameter( PARAMETER_ATOME_NAME );

            FileFilter fileFilter = new FileFilter(  );
            AtomeFilter atomeFilter = new AtomeFilter(  );

            if ( !fileTitle.equals( "" ) )
            {
                fileFilter.set_title( fileTitle );
            }

            if ( !fileName.equals( "" ) )
            {
                fileFilter.set_name( fileName );
            }

            if ( fileFormat != null )
            {
                fileFilter.set_mimeType( fileFormat );
            }

            if ( !atomeName.equals( "" ) )
            {
                atomeFilter.set_name( atomeName );
            }

            List<File> fileList = _fileServices.findByFilter( fileFilter, atomeFilter );

            Paginator<File> paginator = new Paginator<File>( (List<File>) fileList, _nItemsPerPage,
                    JSP_REDIRECT_TO_VIEW_ATOME + "?id_plu=" + nIdPlu + "&id_folder=" + nIdFolder + "&id_version=" +
                    nIdVersion + "&file_title=" + fileTitle + "&file_name=" + fileName + "&file_type=" + fileFormat +
                    "&atome_name=" + atomeName, PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

            model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
            model.put( MARK_PAGINATOR, paginator );
            model.put( MARK_LIST_FILE_LIST, paginator.getPageItems(  ) );
            model.put( MARK_LIST_FILE_LIST, fileList );
            model.put( PARAMETER_FILE_TITLE, fileTitle );
            model.put( PARAMETER_FILE_NAME, fileName );
            model.put( PARAMETER_FILE_FORMAT, fileFormat );
        }
        else
        {
            List<File> fileList = _fileServices.findByVersion( nIdVersion );

            Paginator<File> paginator = new Paginator<File>( (List<File>) fileList, _nItemsPerPage,
                    JSP_REDIRECT_TO_VIEW_ATOME + "?id_plu=" + plu.getId(  ) + "&id_folder=" + folder.getId(  ) +
                    "&id_version=" + version.getId(  ), PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

            model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
            model.put( MARK_PAGINATOR, paginator );
            model.put( MARK_LIST_FILE_LIST, paginator.getPageItems(  ) );
            model.put( MARK_LIST_FILE_LIST, fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VIEW_ATOME, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    //    public String getChoiceModifyAtome( HttpServletRequest request )
    //    {
    //        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATOME );
    //
    //        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
    //        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );
    //
    //        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
    //        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );
    //
    //        Map<String, Object> model = new HashMap<String, Object>(  );
    //        model.put( MARK_PLU, plu );
    //        model.put( MARK_VERSION, version );
    //
    //        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CHOICE_MODIFY_ATOME, getLocale(  ), model );
    //
    //        fileList.clear(  );
    //
    //        return getAdminPage( template.getHtml(  ) );
    //    }
    public String getModifyAtome( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATOME );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        Collection<Folder> folderList = _folderServices.findByPluId( nIdPlu );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion );

        if ( _fileList.isEmpty(  ) )
        {
            _fileList.addAll( _fileServices.findByVersion( nIdVersion ) );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_VERSION, version );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );

            if ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) &&
                    ( multipartRequest.getFile( PARAMETER_FILE ) != null ) )
            {
                File file = new File(  );
//                FileContent fileContent = new FileContent(  );
                PhysicalFile physicalFile = new PhysicalFile(  );
                physicalFile.setValue( fileItem.get(  ) );

                String name = fileItem.getName(  );
                for( File fileTest : _fileList )
                {
                	if( fileTest.getName(  ).equals( name ) )
                	{
                		return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FILE_CREATE_NAME, AdminMessage.TYPE_STOP );
                	}
                }
                
//                fileContent.setFile( physicalFile.getValue(  ) );
                
                String type = name.substring( name.lastIndexOf( "." ) );
                file.setName( request.getParameter( PARAMETER_FILE_NAME ) );
                file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
                file.setFile( physicalFile.getValue(  ) );
                file.setMimeType( type );
                file.setSize( (int) fileItem.getSize(  ) );
                _fileList.add( file );
            }
        }

        if ( _fileList != null )
        {
            model.put( MARK_LIST_FILE_LIST, _fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_ATOME, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getConfirmCancelModifyAtome( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );

        UrlItem url = new UrlItem( JSP_TREE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );

        Object[] args = { atomeName, atomeTitle };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_MODIFY_ATOME, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String getConfirmModifyAtome( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_ID ).equals( "" ) ||
                request.getParameter( PARAMETER_ATOME_ID ).equals( "" ) ||
                request.getParameter( PARAMETER_VERSION_NUM ).equals( "" ) ||
                request.getParameter( PARAMETER_ATOME_NAME ).equals( "" ) ||
                request.getParameter( PARAMETER_ATOME_TITLE ).equals( "" ) ||
                request.getParameter( PARAMETER_ATOME_DESCRIPTION ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        int nIdAtomeOld = Integer.parseInt( request.getParameter( PARAMETER_ATOME_OLD_ID ) );
        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        int numVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
        String atomeDescription = request.getParameter( PARAMETER_ATOME_DESCRIPTION );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE );

        UrlItem url = new UrlItem( JSP_DO_MODIFY_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        url.addParameter( PARAMETER_ATOME_ID, nIdAtome );
        url.addParameter( PARAMETER_ATOME_OLD_ID, nIdAtomeOld );
        url.addParameter( PARAMETER_VERSION_ID, nIdVersion );
        url.addParameter( PARAMETER_VERSION_NUM, numVersion );
        url.addParameter( PARAMETER_ATOME_NAME, atomeName );
        url.addParameter( PARAMETER_ATOME_TITLE, atomeTitle );
        url.addParameter( PARAMETER_ATOME_DESCRIPTION, atomeDescription );

        for ( int j = 0; j < check.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_CHECK, check[j] );
        }

        for ( int j = 0; j < fileTitle.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_TITLE, fileTitle[j] );
        }

        int i = 0;

        for ( File file : _fileList )
        {
            for ( int j = 0; j < check.length; ++j )
            {
                int c = Integer.parseInt( check[j] );

                if ( c == i )
                {
                    Object[] argsFile = { file.getTitle(  ), file.getName(  ) };

                    if ( file.getSize(  ) <= 0 )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FILE_CREATE_SIZE, argsFile,
                            AdminMessage.TYPE_STOP );
                    }
                }
            }

            i++;
        }

        Object[] args = { atomeName, atomeTitle };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_MODIFY_ATOME, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doModifyAtome( HttpServletRequest request )
        throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
        String atomeDescription = request.getParameter( PARAMETER_ATOME_DESCRIPTION );

        int nIdAtomeOld = Integer.parseInt( request.getParameter( PARAMETER_ATOME_OLD_ID ) );
        Atome atomeOld = _atomeServices.findByPrimaryKey( nIdAtomeOld );
        _atomeServices.remove( atomeOld );

        Atome atome = new Atome(  );
        atome.setId( nIdAtome );
        atome.setName( atomeName );
        atome.setTitle( atomeTitle );
        atome.setDescription( atomeDescription );
        _atomeServices.create( atome );

        int numVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) );
        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion );
        atome = _atomeServices.findByPrimaryKey( nIdAtome );

        version.setAtome( atome );
        version.setVersion( numVersion );
        _versionServices.update( version );

        FolderVersion folderVersion = _folderVersionServices.findByFolderAndVersion( folder, version );
        version = _versionServices.findByPrimaryKey( nIdVersion );

        folderVersion.setVersion( version );
        folderVersion.setFolder( folder );
        _folderVersionServices.update( folderVersion );

        List<File> fileExistList = _fileServices.findByVersion( nIdVersion );

        if ( !fileExistList.isEmpty(  ) )
        {
            for ( File file : fileExistList )
            {
//            	_fileContentServices.remove( file.getFile(  ) );
                _fileServices.remove( file );
            }
        }

        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        int i = 0;
        int order = 1;

        for ( File file : _fileList )
        {
            for ( int j = 0; j < check.length; ++j )
            {
                int c = Integer.parseInt( check[j] );

                if ( c == i )
                {
                    if ( !file.getTitle(  ).equals( fileTitle[i] ) )
                    {
                        file.setTitle( fileTitle[i] );
                        _fileServices.update( file );
                    }

                    file.setId( nIdAtome );
                    file.setOrder( order );
                    file.setVersion( version.getId(  ) );

                    if ( file.getMimeType(  ).equals( ".eps" ) )
                    {
                        file.setEPS( 'O' );
                    }
                    else
                    {
                        file.setEPS( 'N' );
                    }

//                    _fileContentServices.create( file.getFile(  ) );
//                    FileContent fileContent = _fileContentServices.findLastFileContent(  );
//                    file.setFile( fileContent );
                    _fileServices.create( file );

                    order++;
                }
            }

            i++;
        }

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  ) + "&id_folder=" + folder.getId(  );
    }

    public String getCorrectAtome( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CORRECT_ATOME );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );
        if( plu == null )
        {
        	plu = new Plu(  );
        	plu.setId( 0 );
        }

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion );

        Folder folder = _folderServices.findByVersion( nIdVersion );

        if ( _fileList.isEmpty(  ) )
        {
            _fileList.addAll( _fileServices.findByVersion( nIdVersion ) );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_VERSION, version );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );

            if ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) &&
                    ( multipartRequest.getFile( PARAMETER_FILE ) != null ) )
            {
                File file = new File(  );
//                FileContent fileContent = new FileContent(  );
                PhysicalFile physicalFile = new PhysicalFile(  );
                physicalFile.setValue( fileItem.get(  ) );

                String name = fileItem.getName(  );
                for( File fileTest : _fileList )
                {
                	if( fileTest.getName(  ).equals( name ) )
                	{
                		return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FILE_CREATE_NAME, AdminMessage.TYPE_STOP );
                	}
                }
                
//                fileContent.setFile( physicalFile.getValue(  ) );
                
                String type = name.substring( name.lastIndexOf( "." ) );
                file.setName( request.getParameter( PARAMETER_FILE_NAME ) );
                file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
                file.setFile( physicalFile.getValue(  ) );
                file.setMimeType( type );
                file.setSize( (int) fileItem.getSize(  ) );
                _fileList.add( file );
            }
        }

        if ( _fileList != null )
        {
            model.put( MARK_LIST_FILE_LIST, _fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CORRECT_ATOME, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getConfirmCancelCorrectAtome( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );

        UrlItem url = new UrlItem( JSP_TREE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );

        Object[] args = { atomeName, atomeTitle };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_CORRECT_ATOME, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String getConfirmCorrectAtome( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_ATOME_TITLE ).equals( "" ) ||
                request.getParameter( PARAMETER_ATOME_DESCRIPTION ).equals( "" ) ||
                request.getParameter( PARAMETER_HISTORY_DESCRIPTION ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        boolean noEps = false;
        boolean eps = false;

        for ( File file : _fileList )
        {
            if ( file.getMimeType(  ).equals( ".eps" ) )
            {
                eps = true;
            }
            else
            {
                noEps = true;
            }
        }

        if ( !eps )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FILE_EPS, AdminMessage.TYPE_STOP );
        }

        if ( !noEps )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FILE_NO_EPS,
                AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
        String atomeDescription = request.getParameter( PARAMETER_ATOME_DESCRIPTION );
        String strDescription = request.getParameter( PARAMETER_HISTORY_DESCRIPTION );
        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE );

        UrlItem url = new UrlItem( JSP_DO_CORRECT_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        url.addParameter( PARAMETER_ATOME_ID, nIdAtome );
        url.addParameter( PARAMETER_ATOME_TITLE, atomeTitle );
        url.addParameter( PARAMETER_ATOME_DESCRIPTION, atomeDescription );
        url.addParameter( PARAMETER_HISTORY_DESCRIPTION, strDescription );

        for ( int j = 0; j < fileTitle.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_TITLE, fileTitle[j] );
        }

        Object[] args = { atomeName, atomeTitle };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CORRECT_ATOME, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doCorrectAtome( HttpServletRequest request )
        throws ParseException
    {
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
        String atomeDescription = request.getParameter( PARAMETER_ATOME_DESCRIPTION );
        Atome atome = _atomeServices.findByPrimaryKey( nIdAtome );

        atome.setTitle( atomeTitle );
        atome.setDescription( atomeDescription );
        _atomeServices.update( atome );

        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE );
        int j = 0;

        for ( File file : _fileList )
        {
            if ( !file.getTitle(  ).equals( fileTitle[j] ) )
            {
                file.setTitle( fileTitle[j] );
                _fileServices.update( file );
            }

            j++;
        }

        History history = new History(  );
        history.setPlu( folder.getPlu( ) );
        history.setFolder( nIdFolder );
        history.setAtome( nIdAtome );

        Date date = new Date(  );
        history.setDc( date );
        history.setDescription( request.getParameter( PARAMETER_HISTORY_DESCRIPTION ) );
        _historyServices.create( history );

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + folder.getPlu( ) + "&id_folder=" + folder.getId(  );
    }

    public String getEvolveAtome( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_EVOLVE_ATOME );

        Plu plu = _pluServices.findPluWork(  );
        
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        if( folder == null )
        {
        	folder = new Folder(  );
        	folder.setId( 0 );
        }

        Collection<Folder> folderList = _folderServices.findByPluId( plu.getId(  ) );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion );

        if ( _fileList.isEmpty(  ) )
        {
            _fileList.addAll( _fileServices.findByVersion( nIdVersion ) );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLU, plu );
        model.put( MARK_VERSION, version );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );
        model.put( MARK_NEW_VERSION, version.getVersion(  ) + 1 );

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );

            if ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) &&
                    ( multipartRequest.getFile( PARAMETER_FILE ) != null ) )
            {
                File file = new File(  );
//                FileContent fileContent = new FileContent(  );
                PhysicalFile physicalFile = new PhysicalFile(  );
                physicalFile.setValue( fileItem.get(  ) );

                String name = fileItem.getName(  );
                for( File fileTest : _fileList )
                {
                	if( fileTest.getName(  ).equals( name ) )
                	{
                		return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FILE_CREATE_NAME, AdminMessage.TYPE_STOP );
                	}
                }
                
//                fileContent.setFile( physicalFile.getValue(  ) );
                
                String type = name.substring( name.lastIndexOf( "." ) );
                file.setName( request.getParameter( PARAMETER_FILE_NAME ) );
                file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
                file.setFile( physicalFile.getValue(  ) );
                file.setMimeType( type );
                file.setSize( (int) fileItem.getSize(  ) );
                _fileList.add( file );
            }
        }

        if ( _fileList != null )
        {
            model.put( MARK_LIST_FILE_LIST, _fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EVOLVE_ATOME, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getConfirmCancelEvolveAtome( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );

        UrlItem url = new UrlItem( JSP_TREE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );

        Object[] args = { atomeName, atomeTitle };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_EVOLVE_ATOME, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String getConfirmEvolveAtome( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_ID ).equals( "" ) ||
                request.getParameter( PARAMETER_VERSION_NUM ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        int numVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) );
        int numVersionOld = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM_OLD ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE );

        UrlItem url = new UrlItem( JSP_DO_EVOLVE_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        url.addParameter( PARAMETER_ATOME_ID, nIdAtome );
        url.addParameter( PARAMETER_VERSION_ID, nIdVersion );
        url.addParameter( PARAMETER_VERSION_NUM, numVersion );

        Object[] argsVersion = { atomeName, atomeTitle, numVersion, numVersionOld };

        if ( numVersion < numVersionOld )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ATOME_CREATE_NUM_VERSION_SUP, argsVersion,
                AdminMessage.TYPE_STOP );
        }

        for ( int j = 0; j < check.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_CHECK, check[j] );
        }

        for ( int j = 0; j < fileTitle.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_TITLE, fileTitle[j] );
        }

        int i = 0;

        for ( File file : _fileList )
        {
            for ( int j = 0; j < check.length; ++j )
            {
                int c = Integer.parseInt( check[j] );

                if ( c == i )
                {
                    Object[] argsFile = { file.getTitle(  ), file.getName(  ) };

                    if ( file.getSize(  ) <= 0 )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FILE_CREATE_SIZE, argsFile,
                            AdminMessage.TYPE_STOP );
                    }
                }
            }

            i++;
        }

        Object[] args = { atomeName, atomeTitle, numVersionOld, numVersion };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_EVOLVE_ATOME, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doEvolveAtome( HttpServletRequest request )
        throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        Atome atome = _atomeServices.findByPrimaryKey( nIdAtome );

        int numVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) );
        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version versionOld = _versionServices.findByPrimaryKey( nIdVersion );

        Date d3 = stringToDate( "00/00/0000", "dd/MM/yyyy" );
        versionOld.setD3( d3 );
        _versionServices.update( versionOld );

        FolderVersion folderVersion = _folderVersionServices.findByFolderAndVersion( folder, versionOld );
        _folderVersionServices.remove( folderVersion );

        Version version = new Version(  );
        version.setAtome( atome );
        version.setVersion( numVersion );
        _versionServices.create( version );

        version = _versionServices.findByAtomeAndNumVersion( nIdAtome, numVersion );

        folderVersion = new FolderVersion(  );
        folderVersion.setVersion( version );
        folderVersion.setFolder( folder );
        _folderVersionServices.create( folderVersion );

        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        int i = 0;
        int order = 1;

        for ( File file : _fileList )
        {
            for ( int j = 0; j < check.length; ++j )
            {
                int c = Integer.parseInt( check[j] );

                if ( c == i )
                {
                    if ( !file.getTitle(  ).equals( fileTitle[i] ) )
                    {
                        file.setTitle( fileTitle[i] );
                    }

                    file.setId( nIdAtome );
                    file.setOrder( order );
                    file.setVersion( version.getId(  ) );

                    if ( file.getMimeType(  ).equals( ".eps" ) )
                    {
                        file.setEPS( 'O' );
                    }
                    else
                    {
                        file.setEPS( 'N' );
                    }

//                    _fileContentServices.create( file.getFile(  ) );
//                    FileContent fileContent = _fileContentServices.findLastFileContent(  );
//                    file.setFile( fileContent );
                    _fileServices.create( file );

                    order++;
                }
            }

            i++;
        }

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  ) + "&id_folder=" + folder.getId(  );
    }

    public String getConfirmArchiveAtome( HttpServletRequest request ) //throws AccessDeniedException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );

        UrlItem url = new UrlItem( JSP_DO_ARCHIVE_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        url.addParameter( PARAMETER_VERSION_ID, nIdVersion );

        Version version = _versionServices.findByPrimaryKey( nIdVersion );

        Object[] args = { version.getAtome(  ).getName(  ), version.getAtome(  ).getTitle(  ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_ARCHIVE_ATOME, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    public String doArchiveAtome( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        _folderServices.remove( folder );

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  ) + "&id_folder=" + folder.getId(  );
    }

    //    public String getAssociateVersion( HttpServletRequest request )
    //    {
    //        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATOME );
    //
    //        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
    //        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );
    //
    //        Collection<Folder> folderList = _folderServices.findByDate( plu.getDa(  ) );
    //
    //        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
    //        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );
    //
    //        Map<String, Object> model = new HashMap<String, Object>(  );
    //        model.put( MARK_PLU, plu );
    //        model.put( MARK_LIST_FOLDER_LIST, folderList );
    //        model.put( MARK_VERSION, version );
    //
    //        if ( request.getParameter( PARAMETER_FOLDER_ID ) != null )
    //        {
    //            int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
    //            Collection<Version> versionList = _versionServices.findByDateAndParent( plu.getDa(  ), nIdFolder );
    //            model.put( MARK_LIST_VERSION_LIST, versionList );
    //        }
    //        else
    //        {
    ////            Collection<Version> versionList = _versionServices.findByDateAndParent( plu.getDa(  ),
    ////                    version.getAtome(  ).getFolder(  ).getId(  ) );
    //        	Collection<Version> versionList = null;
    //            model.put( MARK_LIST_VERSION_LIST, versionList );
    //        }
    //
    //        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSOCIATE_VERSION, getLocale(  ), model );
    //
    //        return getAdminPage( template.getHtml(  ) );
    //    }
    //
    //    public String getBurstVersion( HttpServletRequest request )
    //    {
    //        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATOME );
    //
    //        Date date = new Date(  );
    //
    //        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
    //        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );
    //
    //        Collection<Folder> folderList = _folderServices.findAll(  );
    //
    //        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
    //        Version version = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );
    //
    //        if ( fileList.isEmpty(  ) )
    //        {
    //            fileList.addAll( _fileServices.findByVersion( nIdVersion ) );
    //
    //            int k = fileList.size(  );
    //
    //            if ( k < 1 )
    //            {
    //                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_BURST, AdminMessage.TYPE_STOP );
    //            }
    //        }
    //
    //        Map<String, Object> model = new HashMap<String, Object>(  );
    //        model.put( MARK_PLU, plu );
    //        model.put( MARK_LIST_FOLDER_LIST, folderList );
    //        model.put( MARK_VERSION, version );
    //        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
    //        model.put( MARK_LOCALE, getLocale(  ) );
    //        model.put( MARK_DATE, date );
    //
    //        if ( request instanceof MultipartHttpServletRequest )
    //        {
    //            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    //
    //            FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );
    //
    //            if ( ( request.getParameter( PARAMETER_FILE_TITLE ) != null ) &&
    //                    ( multipartRequest.getFile( PARAMETER_FILE ) != null ) )
    //            {
    //                File file = new File(  );
    //                PhysicalFile physicalFile = new PhysicalFile(  );
    //                physicalFile.setValue( fileItem.get(  ) );
    //
    //                String name = fileItem.getName(  );
    //                String type = name.substring( name.lastIndexOf( "." ) );
    //                file.setName( name );
    //                file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
    //                file.setFile( physicalFile.getValue(  ) );
    //                file.setMimeType( type );
    //                file.setSize( fileItem.getSize(  ) );
    //                fileList.add( file );
    //            }
    //        }
    //
    //        if ( !fileList.isEmpty(  ) )
    //        {
    //            model.put( MARK_LIST_FILE_LIST, fileList );
    //        }
    //
    //        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_BURST_VERSION, getLocale(  ), model );
    //
    //        return getAdminPage( template.getHtml(  ) );
    //    }
    //
    //    public String doBurstVersion( HttpServletRequest request )
    //        throws ParseException
    //    {
    //        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
    //        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );
    //
    //        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
    //        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
    //
    //        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
    //        Version versionOld = _versionServices.findByPrimaryKey( nIdVersion, getPlugin(  ) );
    //
    //        Atome atome = new Atome(  );
    //        atome.setName( request.getParameter( PARAMETER_ATOME_NAME ) );
    //        atome.setTitle( request.getParameter( PARAMETER_ATOME_TITLE ) );
    //        atome.setDescription( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
    //
    //        if ( request instanceof MultipartHttpServletRequest )
    //        {
    //            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    //            FileItem fileItem = multipartRequest.getFile( PARAMETER_ATOME_IMAGE );
    //
    //            PhysicalFile physicalFile = new PhysicalFile(  );
    //            physicalFile.setValue( fileItem.get(  ) );
    //        }
    //
    //        Date date = new Date(  );
    //        Date dateDefault = stringToDate( sDateDefault, "dd/MM/yyyy" );
    //
    //        Plu newPlu = _pluServices.findPluApplied(  );
    //
    //        if ( newPlu.getId(  ) == 0 )
    //        {
    //            newPlu.setDj( date );
    //            newPlu.setDa( dateDefault );
    //            _pluServices.create( newPlu );
    //        }
    //
    //        Version version = new Version(  );
    //        version.setVersion( 1 );
    //        version.setD1( date );
    //        version.setD2( dateDefault );
    //        version.setD3( dateDefault );
    //        version.setD4( dateDefault );
    ////        version.setAtome( atome );
    //
    //        _atomeServices.create( atome );
    //        _versionServices.create( version );
    //
    //        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
    //        int i = 0;
    //
    //        for ( File file : fileList )
    //        {
    //            for ( int j = 0; j < check.length; ++j )
    //            {
    //                int c = Integer.parseInt( check[j] );
    //
    //                if ( c == i )
    //                {
    //                    file.setVersion( version.getId(  ) );
    //                    _fileServices.create( file, getPlugin(  ) );
    //                    fileList.remove( c );
    //                }
    //
    //                if ( fileList.isEmpty(  ) )
    //                {
    //                    versionOld.setD3( date );
    //                    _versionServices.update( versionOld, getPlugin(  ) );
    //
    //                    return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId(  ) + "&id_folder=" + folder.getId(  );
    //                }
    //            }
    //
    //            i++;
    //        }
    //
    //        return JSP_REDIRECT_TO_BURST_VERSION + "?id_plu=" + plu.getId(  ) + "&id_version=" + versionOld.getId(  );
    //    }
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
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        String page = request.getParameter( "page" );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( "page", page );
        model.put( MARK_PLU, plu );

        if ( request.getParameter( PARAMETER_FOLDER_ID ) != null )
        {
            int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
            Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
            model.put( MARK_FOLDER, folder );
        }

        if ( request.getParameter( PARAMETER_VERSION_ID ) != null )
        {
            int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
            Version version = _versionServices.findByPrimaryKey( nIdVersion );
            model.put( MARK_VERSION, version );
        }

        if ( request.getParameter( PARAMETER_ATOME_ID ) != null )
        {
            int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
            Atome atome = _atomeServices.findByPrimaryKey( nIdAtome );
            int maxVersion = _versionServices.findMaxVersion( nIdAtome );
            Version version = _versionServices.findByAtomeAndNumVersion( nIdAtome, maxVersion );
            model.put( MARK_ATOME, atome );
            model.put( MARK_VERSION, version );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_JOIN_FILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getCreateHtml( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_HTML );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        String page = request.getParameter( "page" );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( "page", page );
        model.put( MARK_PLU, plu );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ) );

        if ( request.getParameter( PARAMETER_FOLDER_HTML ) != null )
        {
            model.put( MARK_HTML, _folderHtml );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_HTML, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getImportHtml( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_HTML );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        String page = request.getParameter( "page" );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( "page", page );
        model.put( MARK_PLU, plu );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_IMPORT_HTML, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getDuplicateHtml( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_HTML );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        List<Folder> folderList = _folderServices.findByPluId( nIdPlu );
        String page = request.getParameter( "page" );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( "page", page );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_FOLDER_LIST, folderList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DUPLICATE_HTML, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }
}
