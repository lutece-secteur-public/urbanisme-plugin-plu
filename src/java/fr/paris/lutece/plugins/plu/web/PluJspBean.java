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

import fr.paris.lutece.plugins.plu.business.atome.Atome;
import fr.paris.lutece.plugins.plu.business.atome.AtomeFilter;
import fr.paris.lutece.plugins.plu.business.atome.IAtomeServices;
import fr.paris.lutece.plugins.plu.business.etat.Etat;
import fr.paris.lutece.plugins.plu.business.etat.IEtatServices;
import fr.paris.lutece.plugins.plu.business.file.File;
import fr.paris.lutece.plugins.plu.business.file.FileFilter;
import fr.paris.lutece.plugins.plu.business.file.IFileServices;
import fr.paris.lutece.plugins.plu.business.folder.Folder;
import fr.paris.lutece.plugins.plu.business.folder.FolderFilter;
import fr.paris.lutece.plugins.plu.business.folder.IFolderServices;
import fr.paris.lutece.plugins.plu.business.folderversion.FolderVersion;
import fr.paris.lutece.plugins.plu.business.folderversion.IFolderVersionServices;
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
import fr.paris.lutece.plugins.plu.utils.PluUtils;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import freemarker.template.utility.StringUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;


/**
 * This class provides the user interface to manage form features ( manage,
 * create, modify, remove)
 */
public class PluJspBean extends PluginAdminPageJspBean
{
    /**
     * RIGHT_MANAGE_PLU
     */
    public static final String RIGHT_MANAGE_PLU = "PLU_MANAGEMENT";

    /** properties for page titles */
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

    /** Templates */
    private static final String TEMPLATE_MANAGE_PLU = "/admin/plugins/plu/manage_plu.html";
    private static final String TEMPLATE_TREE_PLU = "/admin/plugins/plu/tree_plu.html";
    private static final String TEMPLATE_TREE_PLU_ATOME = "/admin/plugins/plu/tree_plu_atome.html";
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
    private static final String TEMPLATE_MESSAGE = "/admin/plugins/plu/message.html";

    /** Markers */
    private static final String MARK_LIST_PLU_LIST = "plu_list";
    private static final String MARK_LIST_TYPE_LIST = "type_list";
    private static final String MARK_LIST_FOLDER_LIST = "folder_list";
    private static final String MARK_LIST_FOLDER_CHILD_LIST = "folder_child_list";
    private static final String MARK_LIST_FOLDER_PARENT_CHILD_LIST = "folder_parent_child_list";
    private static final String MARK_LIST_ATOME_LIST = "atome_list";
    private static final String MARK_LIST_VERSION_LIST = "version_list";
    private static final String MARK_LIST_FILE_LIST = "file_list";
    private static final String MARK_LIST_FILE_ALL = "file_all";
    private static final String MARK_LIST_FILE_ALL_FORMAT = "file_all_format";
    private static final String MARK_PLU = "one_plu";
    private static final String MARK_PLU_APPLIED = "one_plu_applied";
    private static final String MARK_PLU_WORK = "one_plu_work";
    private static final String MARK_TYPE = "one_type";
    private static final String MARK_FOLDER = "one_folder";
    private static final String MARK_FOLDER_PARENT = "one_folder_parent";
    private static final String MARK_HTML = "folder_html";
    private static final String MARK_HTML_IMPRESSION = "folder_html_impression";
    private static final String MARK_ATOME = "one_atome";
    private static final String MARK_VERSION = "one_version";
    private static final String MARK_NEW_VERSION = "new_version";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_PAGINATOR_DOSSER = "paginatorDossier";
    private static final String MARK_PAGINATOR_ATOME = "paginatorAtome";
    private static final String MARK_ERROR_MESSAGE = "errorMessage";
    private static final String MARK_ERROR_ARGS = "args";
    private static final String MARK_PAGE_RETURN = "pageReturn";
    private static final String MARK_PAGE_TARGET = "pageTarget";

    /** Messages */
    private static final String MESSAGE_CONFIRM_APPROVE_PLU = "plu.message.confirmApprovePlu";
    private static final String MESSAGE_CONFIRM_CANCEL_APPROVE_PLU = "plu.message.confirmCancelApprovePlu";
    private static final String MESSAGE_CONFIRM_APPLICABLE_PLU = "plu.message.confirmApplicablePlu";
    private static final String MESSAGE_CONFIRM_CANCEL_APPLICABLE_PLU = "plu.message.confirmCancelApplicablePlu";
    private static final String MESSAGE_CONFIRM_MODIFY_PLU = "plu.message.confirmModifyPlu";
    private static final String MESSAGE_CONFIRM_CORRECT_PLU = "plu.message.confirmCorrectPlu";
    private static final String MESSAGE_CONFIRM_ISO_PLU = "plu.message.confirmIsoPlu";
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
    private static final String MESSAGE_ERROR_DATE_APPROVE = "plu.message.errorDateApprove";
    private static final String MESSAGE_ERROR_DATE_APPLICATION = "plu.message.errorDateApplication";
    private static final String MESSAGE_ERROR_DATE_APPLICATION_LOWER = "plu.message.errorDateApplicationLower";
    private static final String MESSAGE_ERROR_PLU_WORK = "plu.message.errorPluWork";
    private static final String MESSAGE_ERROR_REQUIRED_FIELD = "plu.message.errorRequiredfield";
    private static final String MESSAGE_ERROR_DATE_FORMAT = "plu.message.errorDateFormat";
    private static final String MESSAGE_ERROR_FOLDER_CREATE = "plu.message.errorFolderCreate";
    private static final String MESSAGE_ERROR_FOLDER_DELETE = "plu.message.errorFolderDelete";
    private static final String MESSAGE_ERROR_FOLDER_IMAGE_TYPE = "plu.message.errorFolderImageType";
    private static final String MESSAGE_ERROR_FILE_CREATE_SIZE = "plu.message.errorFileCreateSize";
    private static final String MESSAGE_ERROR_FILE_CREATE_NAME = "plu.message.errorFileCreateName";
    private static final String MESSAGE_ERROR_FILE_CREATE_TITLE = "plu.message.errorFileCreateTitle";
    private static final String MESSAGE_ERROR_FILE_CREATE_TITLE_EMPTY = "plu.message.errorFileCreateTitleEmpty";
    private static final String MESSAGE_ERROR_REQUIRED_FILE_EPS = "plu.message.errorFileEps";
    private static final String MESSAGE_ERROR_REQUIRED_FILE_NO_EPS = "plu.message.errorFileNoEps";
    private static final String MESSAGE_ERROR_ATOME_CREATE_ID = "plu.message.errorAtomeCreateId";
    private static final String MESSAGE_ERROR_ATOME_CREATE_NAME = "plu.message.errorAtomeCreateName";
    private static final String MESSAGE_ERROR_ATOME_CREATE_TITLE = "plu.message.errorAtomeCreateTitle";
    private static final String MESSAGE_ERROR_ATOME_CREATE_FILE_CHECK = "plu.message.errorAtomeCreateFileCheck";
    private static final String MESSAGE_ERROR_ATOME_CREATE_NUM_VERSION_SUP = "plu.message.errorAtomeCreateNumVersionSup";
    private static final String MESSAGE_ERROR_ATOME_CREATE_NUM_VERSION_EXISTS = "plu.message.errorAtomeCreateNumVersionExists";
    private static final String MESSAGE_ERROR_ATOME_ID_NUMBER = "plu.message.errorAtomeIdNumber";
    private static final String MESSAGE_ERROR_VERSION_NUMBER = "plu.message.errorVersionNumer";

    /** Parameters */
    private static final String PARAMETER_PLU_ID = "id_plu";
    private static final String PARAMETER_PLU_TYPE = "id_type";
    private static final String PARAMETER_PLU_CAUSE = "cause";
    private static final String PARAMETER_PLU_REFERENCE = "reference";
    private static final String PARAMETER_FOLDER_ID = "id_folder";
    private static final String PARAMETER_FOLDER_ID_DUPLICATE = "id_folder_duplicate";
    private static final String PARAMETER_FOLDER_ID_ATOME = "id_folder_atome";
    private static final String PARAMETER_FOLDER_ID_RETURN = "id_folder_return";
    private static final String PARAMETER_FOLDER_TITLE = "folder_title";
    private static final String PARAMETER_FOLDER_TITLE_OLD = "folder_title_old";
    private static final String PARAMETER_FOLDER_DESCRIPTION = "folder_description";
    private static final String PARAMETER_FOLDER_PARENT_ID = "id_parent_folder";
    private static final String PARAMETER_FOLDER_IMAGE = "folder_image";
    private static final String PARAMETER_FOLDER_HTML_NOT_EMPTY = "joinHtml";
    private static final String PARAMETER_FOLDER_IMAGE_CHECK = "image_check";
    private static final String PARAMETER_FOLDER_HTML = "folder_html";
    private static final String PARAMETER_FOLDER_HTML_UTILISATION = "html_utilisation";
    private static final String PARAMETER_FOLDER_HTML_CHECK = "html_check";
    private static final String PARAMETER_FOLDER_HTML_CHECK_IMPRESSION = "html_check_impression";
    private static final String PARAMETER_ATOME_ID = "id_atome";
    private static final String PARAMETER_ATOME_NUM = "num_atome";
    private static final String PARAMETER_ATOME_OLD_ID = "id_atome_old";
    private static final String PARAMETER_ATOME_NAME = "atome_name";
    private static final String PARAMETER_ATOME_TITLE = "atome_title";
    private static final String PARAMETER_ATOME_DESCRIPTION = "atome_description";
    private static final String PARAMETER_ATOME_ALL = "atome_all";
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
    private static final String PARAMETER_FILE_TITLE_ATOME = "file_title_atome";
    private static final String PARAMETER_FILE_UTILISATION = "file_utilisation";
    private static final String PARAMETER_FILE_FORMAT = "file_format";
    private static final String PARAMETER_FILE_CHECK = "file_check";
    private static final String PARAMETER_FILE = "file";
    private static final String PARAMETER_HISTORY_DESCRIPTION = "description";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_PAGE = "page";
    private static final String PARAMETER_REINIT = "reinit";
    private static final String PARAMETER_CLEAR = "clear";
    private static final String PARAMETER_CANCEL_CREATE_FOLDER = "cancel_create_folder";

    /** Jsp Definition */
    private static final String JSP_REDIRECT_TO_MANAGE_PLU = "../plu/ManagePlu.jsp";
    private static final String JSP_REDIRECT_TO_TREE_PLU = "../plu/TreePlu.jsp";
    private static final String JSP_REDIRECT_TO_TREE_PLU_ATOME = "../plu/TreePluAtome.jsp";
    private static final String JSP_REDIRECT_TO_CHOICE_CREATE_ATOME = "../atome/ChoiceCreateAtome.jsp";
    private static final String JSP_MANAGE_PLU = "jsp/admin/plugins/plu/plu/ManagePlu.jsp";
    private static final String JSP_TREE_PLU = "jsp/admin/plugins/plu/plu/TreePlu.jsp";
    private static final String JSP_TREE_PLU_ATOME = "jsp/admin/plugins/plu/plu/TreePluAtome.jsp";
    private static final String JSP_CHOICE_CREATE_ATOME = "jsp/admin/plugins/plu/atome/ChoiceCreateAtome.jsp";
    private static final String JSP_DO_APPROVE_PLU = "jsp/admin/plugins/plu/plu/DoApprovePlu.jsp";
    private static final String JSP_DO_APPLICABLE_PLU = "jsp/admin/plugins/plu/plu/DoApplicablePlu.jsp";
    private static final String JSP_DO_MODIFY_PLU = "jsp/admin/plugins/plu/plu/DoModifyPlu.jsp";
    private static final String JSP_DO_CORRECT_PLU = "jsp/admin/plugins/plu/plu/DoCorrectPlu.jsp";
    private static final String JSP_DO_ISO_PLU = "jsp/admin/plugins/plu/plu/DoIsoPlu.jsp";
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
    private static final String JSP_MESSAGE = "jsp/admin/plugins/plu/Message.jsp";
    private static final String JSP_EVOLVE_ATOME = "jsp/admin/plugins/plu/atome/EvolveAtome.jsp";
    private static final String JSP_CORRECT_ATOME = "jsp/admin/plugins/plu/atome/CorrectAtome.jsp";
    private static final String JSP_CREATE_ATOME = "jsp/admin/plugins/plu/atome/CreateAtome.jsp";
    private static final String JSP_MODIFY_ATOME = "jsp/admin/plugins/plu/atome/ModifyAtome.jsp";

    private static final String[] LISTE_NOM_CHAMP = new String[] { PARAMETER_ATOME_DESCRIPTION, PARAMETER_ATOME_ID,
        PARAMETER_PLU_ID, PARAMETER_PLU_TYPE, PARAMETER_PLU_CAUSE, PARAMETER_PLU_REFERENCE, PARAMETER_FOLDER_ID,
        PARAMETER_FOLDER_ID_ATOME, PARAMETER_FOLDER_ID_RETURN, PARAMETER_FOLDER_TITLE, PARAMETER_FOLDER_TITLE_OLD,
        PARAMETER_FOLDER_DESCRIPTION, PARAMETER_FOLDER_PARENT_ID, PARAMETER_FOLDER_IMAGE,
        PARAMETER_FOLDER_IMAGE_CHECK, PARAMETER_FOLDER_HTML, PARAMETER_FOLDER_HTML_UTILISATION,
        PARAMETER_FOLDER_HTML_CHECK, PARAMETER_FOLDER_HTML_CHECK_IMPRESSION, PARAMETER_ATOME_ID,
        PARAMETER_ATOME_NUM, PARAMETER_ATOME_OLD_ID, PARAMETER_ATOME_NAME, PARAMETER_ATOME_TITLE,
        PARAMETER_ATOME_DESCRIPTION, PARAMETER_VERSION_ID, PARAMETER_VERSION_NUM, PARAMETER_VERSION_NUM_OLD,
        PARAMETER_VERSION_D1, PARAMETER_VERSION_D2, PARAMETER_VERSION_D3, PARAMETER_VERSION_D4,
        PARAMETER_DATE_JURIDIQUE, PARAMETER_DATE_APPLICATION, PARAMETER_FILE_NAME, PARAMETER_FILE_TITLE,
        PARAMETER_FILE_UTILISATION, PARAMETER_FILE_FORMAT, PARAMETER_FILE, PARAMETER_CLEAR,
        PARAMETER_HISTORY_DESCRIPTION, PARAMETER_PAGE_INDEX, PARAMETER_PAGE, };

    
    /** Variables */
    private int _nDefaultItemsPerPage;
    private int _nItemsPerPage;
    private String _strCurrentPageIndex;
    private String _strVide = "vide";
    private IPluServices _pluServices;
    private ITypeServices _typeServices;
    private IEtatServices _etatServices;
    private IHistoryServices _historyServices;
    private IFolderServices _folderServices;
    private IAtomeServices _atomeServices;
    private IVersionServices _versionServices;
    private IFolderVersionServices _folderVersionServices;
    private IFileServices _fileServices;
    private List<File> _fileList = new ArrayList<File>( );
    private Folder _folderHtml = new Folder( );
    private Folder _folderImage = new Folder( );
    
    /**
     * Constructor
     */
    public PluJspBean( )
    {
        super( );
        _pluServices = (IPluServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.pluServices" );
        _typeServices = (ITypeServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.typeServices" );
        _etatServices = (IEtatServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.etatServices" );
        _historyServices = (IHistoryServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.historyServices" );
        _folderServices = (IFolderServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.folderServices" );
        _atomeServices = (IAtomeServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.atomeServices" );
        _versionServices = (IVersionServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.versionServices" );
        _folderVersionServices = (IFolderVersionServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.folderVersionServices" );
        _fileServices = (IFileServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME, "plu.fileServices" );
    }

    /**
     * Generates a HTML form that displays the Plu manage
     * @param request the Http request
     * @return HTML
     */
    public String getManagePlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_PLU_LIST );

        Plu plu = _pluServices.findPluApplied( );
        List<Plu> pluList = _pluServices.findAll( );
        Plu pluWork = _pluServices.findPluWork( );
        Map<String, Object> model = new HashMap<String, Object>( );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_RESULT_PER_PAGE, 10 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Paginator<Plu> paginator = new Paginator<Plu>( (List<Plu>) pluList, _nItemsPerPage, JSP_MANAGE_PLU
                + "?plugin_name=plu", PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_LIST_PLU_LIST, paginator.getPageItems( ) );
        model.put( MARK_PLU_APPLIED, plu );
        model.put( MARK_PLU_WORK, pluWork );
        // model.put( MARK_LIST_PLU_LIST, pluList );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_PLU, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * Generates a HTML form for approve a Plu
     * @param request the Http request
     * @return HTML
     */
    public String getApprovePlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_APPROVE_PLU );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        List<Type> typeList = _typeServices.findAll( );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_TYPE_LIST, typeList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_APPROVE_PLU, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a message of confirmation of cancel for approve a PLU
     * @param request the Http request
     * @return message
     */
    public String getConfirmCancelApprovePlu( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        UrlItem url = new UrlItem( JSP_MANAGE_PLU );

        Object[] args = { nIdPlu };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_APPROVE_PLU, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Generates a message of confirmation for approve a PLU
     * @param request the Http request
     * @return message
     * @throws ParseException ParseException 
     */
    public String getConfirmApprovePlu( HttpServletRequest request ) throws ParseException
    {
        if ( request.getParameter( PARAMETER_DATE_JURIDIQUE ).equals( "" )
                || request.getParameter( PARAMETER_PLU_TYPE ).equals( "" )
                || request.getParameter( PARAMETER_PLU_CAUSE ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdType = Integer.parseInt( request.getParameter( PARAMETER_PLU_TYPE ) );
        String strCause = request.getParameter( PARAMETER_PLU_CAUSE );
        String strReference = request.getParameter( PARAMETER_PLU_REFERENCE );
        String strDate = request.getParameter( PARAMETER_DATE_JURIDIQUE );

        try
        {
            Date date = new Date( );
            if ( stringToDate( strDate, "dd/MM/yyyy" ).after( date ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_APPROVE, AdminMessage.TYPE_STOP );
            }
        }
        catch ( ParseException e )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_FORMAT, AdminMessage.TYPE_STOP );
        }



        UrlItem url = new UrlItem( JSP_DO_APPROVE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_PLU_TYPE, nIdType );
        
        try {
			url.addParameter( PARAMETER_PLU_CAUSE, URIUtil.encodeAll( strCause ) );
	        url.addParameter( PARAMETER_PLU_REFERENCE, URIUtil.encodeAll( strReference ) );
	        url.addParameter( PARAMETER_DATE_JURIDIQUE, URIUtil.encodeAll( strDate ) );
		}
        catch ( URIException e )
        {
			new AppException( "An error occured while encoding request parameters" );
		}


        Object[] args = { nIdPlu, strCause, strDate };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_APPROVE_PLU, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Approve a Plu and return to the Plu manage
     * @param request the Http request
     * @throws ParseException ParseException
     * @return HTML
     */
    public String doApprovePlu( HttpServletRequest request ) throws ParseException
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

        for ( Version version : versionList )
        {
            version.setD1( dj );
            _versionServices.update( version );
        }

        return JSP_REDIRECT_TO_MANAGE_PLU;
    }

    /**
     * Generates a HTML form for apply a Plu
     * @param request the Http request
     * @return HTML
     */
    public String getApplicablePlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_APPLICABLE_PLU );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        Type type = _typeServices.findByPrimaryKey( plu.getType( ).getId( ) );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_TYPE, type );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_APPLICABLE_PLU, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a message of confirmation of cancel for apply a PLU
     * @param request the Http request
     * @return message
     */
    public String getConfirmCancelApplicablePlu( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String strCause = request.getParameter( PARAMETER_PLU_CAUSE );

        UrlItem url = new UrlItem( JSP_MANAGE_PLU );

        Object[] args = { nIdPlu, strCause };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_APPLICABLE_PLU, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Generates a message of confirmation for apply a PLU
     * @param request the Http request
     * @throws ParseException ParseException
     * @return message
     */
    public String getConfirmApplicablePlu( HttpServletRequest request ) throws ParseException
    {
        if ( request.getParameter( PARAMETER_DATE_APPLICATION ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_APPLICATION, AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String strCause = request.getParameter( PARAMETER_PLU_CAUSE );
        String strDj = request.getParameter( PARAMETER_DATE_JURIDIQUE );
        String strDa = request.getParameter( PARAMETER_DATE_APPLICATION );
        Date dj;
        Date da;

        try
        {
            dj = stringToDate( request.getParameter( PARAMETER_DATE_JURIDIQUE ), "dd/MM/yyyy" );
            da = stringToDate( request.getParameter( PARAMETER_DATE_APPLICATION ), "dd/MM/yyyy" );
        }
        catch ( ParseException e )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_FORMAT, AdminMessage.TYPE_STOP );
        }

        Object[] argsDate = { strDa, strDj };

        if ( da.before( dj ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_APPLICATION_LOWER, argsDate,
                    AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_DO_APPLICABLE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        
        try
        {
	        url.addParameter( PARAMETER_DATE_JURIDIQUE, URIUtil.encodeAll( strDj ) );
	        url.addParameter( PARAMETER_DATE_APPLICATION, URIUtil.encodeAll( strDa ) );
        }
        catch ( URIException e )
        {
			new AppException( "An error occured while encoding request parameters" );
		}

        Object[] args = { nIdPlu, strCause, strDa };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_APPLICABLE_PLU, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Apply a Plu and return to the Plu manage
     * @param request the Http request
     * @throws ParseException ParseException
     * @return HTML
     */
    public String doApplicablePlu( HttpServletRequest request ) throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );
        Date dj = stringToDate( request.getParameter( PARAMETER_DATE_JURIDIQUE ), "dd/MM/yyyy" );
        Date da = stringToDate( request.getParameter( PARAMETER_DATE_APPLICATION ), "dd/MM/yyyy" );

        Etat etat = _etatServices.findByPrimaryKey( 1 );

        plu.setEtat( etat );
        plu.setDa( da );
        _pluServices.update( plu );

        // Iso iso = new Iso( );
        // iso.setPlu( plu );
        // _isoServices.create( iso );

        List<Version> versionList = _versionServices.selectApplication( nIdPlu, da );

        for ( Version version : versionList )
        {
            version.setD2( da );
            _versionServices.update( version );
        }

        versionList.clear( );

        versionList = _versionServices.selectEvolution( );

        for ( Version version : versionList )
        {
            version.setD3( dj );
            _versionServices.update( version );
        }

        GregorianCalendar calendar = new GregorianCalendar( );
        calendar.setTime( da );
        calendar.add( Calendar.DATE, -1 );

        versionList.clear( );
        versionList = _versionServices.selectArchive( );

        for ( Version version : versionList )
        {
            version.setD4( dj );
            version.setArchive( 'N' );
            _versionServices.update( version );
        }

        Plu plu2 = new Plu( );
        _pluServices.create( plu2 );

        plu2 = _pluServices.findPluWork( );

        List<Folder> folderList = _folderServices.findByPluId( nIdPlu );
        Map<Integer, Integer> mapIdOldIdNew = new Hashtable<Integer, Integer>( );

        for ( Folder folder : folderList )
        {
            Folder folder2 = new Folder( );
            folder2.setPlu( plu2.getId( ) );

            if ( mapIdOldIdNew.containsKey( folder.getParentFolder( ) ) )
            {
                folder2.setParentFolder( mapIdOldIdNew.get( folder.getParentFolder( ) ) );
            }
            else
            {
                folder2.setParentFolder( folder.getParentFolder( ) );
            }

            folder2.setTitle( folder.getTitle( ) );
            folder2.setDescription( folder.getDescription( ) );
            folder2.setImg( folder.getImg( ) );
            folder2.setHtml( folder.getHtml( ) );
            _folderServices.create( folder2 );

            folder2 = _folderServices.findLastFolder( );
            mapIdOldIdNew.put( folder.getId( ), folder2.getId( ) );

            List<FolderVersion> folderVersionList = _folderVersionServices.findByFolder( folder );

            for ( FolderVersion folderVersion : folderVersionList )
            {
                FolderVersion folderVersion2 = new FolderVersion( );
                folderVersion2.setVersion( folderVersion.getVersion( ) );
                folderVersion2.setFolder( folder2 );
                _folderVersionServices.create( folderVersion2 );
            }
        }

        return JSP_REDIRECT_TO_MANAGE_PLU;
    }

    /**
     * Generates a HTML form for modify Plu
     * @param request the Http request
     * @return HTML
     */
    public String getModifyPlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_PLU );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        Type type = _typeServices.findByPrimaryKey( plu.getType( ).getId( ) );

        List<Type> typeList = _typeServices.findAll( );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_TYPE, type );
        model.put( MARK_LIST_TYPE_LIST, typeList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PLU, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a message of confirmation for modify a PLU
     * @param request the Http request
     * @return message
     * @throws ParseException ParseException
     */
    public String getConfirmModifyPlu( HttpServletRequest request ) throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdType = Integer.parseInt( request.getParameter( PARAMETER_PLU_TYPE ) );
        String strCause = request.getParameter( PARAMETER_PLU_CAUSE );
        String strReference = request.getParameter( PARAMETER_PLU_REFERENCE );
        String strDate = request.getParameter( PARAMETER_DATE_JURIDIQUE );

        if ( StringUtils.isEmpty( request.getParameter( PARAMETER_PLU_CAUSE ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_DO_MODIFY_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_PLU_TYPE, nIdType );
	    try
	    {
	    	url.addParameter( PARAMETER_PLU_CAUSE, URIUtil.encodeAll( strCause ) );
	        url.addParameter( PARAMETER_PLU_REFERENCE, URIUtil.encodeAll( strReference ) );
	        url.addParameter( PARAMETER_DATE_JURIDIQUE, URIUtil.encodeAll( strDate ) );
	    }
	    catch ( URIException e )
        {
			new AppException( "An error occured while encoding request parameters" );
		}

        Date dj;

        try
        {
            dj = stringToDate( strDate, "dd/MM/yyyy" );
        }
        catch ( ParseException e )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_FORMAT, AdminMessage.TYPE_STOP );
        }

        Date date = new Date( );

        if ( dj.after( date ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_APPROVE, AdminMessage.TYPE_STOP );
        }

        Object[] args = { nIdPlu, strCause };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_MODIFY_PLU, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Modify a Plu and return to the Plu manage
     * @param request the Http request
     * @throws ParseException ParseException
     * @return HTML
     */
    public String doModifyPlu( HttpServletRequest request ) throws ParseException
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

    /**
     * Generates a HTML form for correct Plu
     * @param request the Http request
     * @return HTML
     */
    public String getCorrectPlu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CORRECT_PLU );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        Type type = _typeServices.findByPrimaryKey( plu.getType( ).getId( ) );

        List<Type> typeList = _typeServices.findAll( );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_TYPE, type );
        model.put( MARK_LIST_TYPE_LIST, typeList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CORRECT_PLU, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a message of confirmation for correct a PLU
     * @param request the Http request
     * @return message
     */
    public String getConfirmCorrectPlu( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdType = Integer.parseInt( request.getParameter( PARAMETER_PLU_TYPE ) );
        String strCause = request.getParameter( PARAMETER_PLU_CAUSE );
        String strReference = request.getParameter( PARAMETER_PLU_REFERENCE );
        String strDescription = request.getParameter( PARAMETER_HISTORY_DESCRIPTION );

        if ( StringUtils.isEmpty( strCause ) || StringUtils.isEmpty( strDescription ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_DO_CORRECT_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_PLU_TYPE, nIdType );
        try {
			url.addParameter( PARAMETER_PLU_CAUSE, URIUtil.encodeAll( strCause ) );
	        url.addParameter( PARAMETER_PLU_REFERENCE, URIUtil.encodeAll( strReference ) );
	        url.addParameter( PARAMETER_HISTORY_DESCRIPTION, URIUtil.encodeAll( strDescription ) );
		}
        catch ( URIException e )
        {
			throw new AppException( "An error occured while parsing request parameters" );
		}

        Object[] args = { nIdPlu, strCause };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CORRECT_PLU, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Correct a Plu and return to the Plu manage
     * @param request the Http request
     * @throws ParseException ParseException
     * @return HTML
     */
    public String doCorrectPlu( HttpServletRequest request ) throws ParseException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdType = Integer.parseInt( request.getParameter( PARAMETER_PLU_TYPE ) );
        Type type = _typeServices.findByPrimaryKey( nIdType );



        if ( plu.getDa( ) != null )
        {
            Etat etat = _etatServices.findByPrimaryKey( 5 );
            plu.setEtat( etat );
        }
        plu.setType( type );
        plu.setCause( request.getParameter( PARAMETER_PLU_CAUSE ) );
        plu.setReference( request.getParameter( PARAMETER_PLU_REFERENCE ) );
        _pluServices.update( plu );

        History history = new History( );
        history.setPlu( nIdPlu );

        Date date = new Date( );
        history.setDc( date );
        history.setDescription( request.getParameter( PARAMETER_HISTORY_DESCRIPTION ) );
        _historyServices.create( history );

        return JSP_REDIRECT_TO_MANAGE_PLU;
    }

    /**
     * Generates a message of confirmation for generate the PLU
     * @param request the Http request
     * @return message
     */
    public String getConfirmIsoPlu( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        UrlItem url = new UrlItem( JSP_DO_ISO_PLU );
        url.addParameter( PARAMETER_PLU_ID, plu.getId( ) );

        Object[] args = { plu.getId( ), plu.getCause( ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_ISO_PLU, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Request of generation of the PLU and return to the Plu manage
     * @param request the Http request
     * @return HTML
     */
    public String doIsoPlu( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        Etat etat = _etatServices.findByPrimaryKey( 1 );

        plu.setEtat( etat );
        _pluServices.update( plu );

        // FIXME pourquoi cr�er l'iso en base maintenant ?? plutot dans le batch
        // ?
        // Iso iso = new Iso( );
        // iso.setPlu( plu.getId( ) );
        // _isoServices.create( iso );

        return JSP_REDIRECT_TO_MANAGE_PLU;
    }

    /**
     * Generates a HTML form that displays the Folder manage
     * @param request the Http request
     * @return HTML
     */
    public String getTreePlu( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        if ( plu == null )
        {
            plu = new Plu( );
        }

        Plu pluWork = _pluServices.findPluWork( );
        List<Plu> pluList = _pluServices.findAll( );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_RESULT_PER_PAGE, 10 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_PLU_WORK, pluWork );
        model.put( MARK_LIST_PLU_LIST, pluList );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_TREE_FOLDER );

        String isCancelCreateFolder = request.getParameter( PARAMETER_CANCEL_CREATE_FOLDER );

        //Si on vient de l'annulation de la creation d'un dossier on ne renseigner pas le crière
        // titre du dossier dans le champ de recherche.
        if ( ( StringUtils.isBlank( isCancelCreateFolder ) || !isCancelCreateFolder.equals( "true" ) )
                && request.getParameter( PARAMETER_FOLDER_TITLE ) != null )
        {
            String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );

            FolderFilter folderFilter = new FolderFilter( );

            if ( nIdPlu != 0 )
            {
                folderFilter.setPlu( nIdPlu );
            }

            if ( !folderTitle.equals( "" ) )
            {
                folderFilter.setTitle( folderTitle );
            }

            List<Folder> folderList = _folderServices.findByFilter( folderFilter );

            Paginator<Folder> paginatorDossier = new Paginator<Folder>( (List<Folder>) folderList, _nItemsPerPage,
                    JSP_TREE_PLU + "?id_plu=" + nIdPlu + "&folder_title=" + folderTitle, PARAMETER_PAGE_INDEX,
                    _strCurrentPageIndex );

            model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
            model.put( MARK_PAGINATOR_DOSSER, paginatorDossier );
            model.put( MARK_LIST_FOLDER_LIST, paginatorDossier.getPageItems( ) );
            model.put( PARAMETER_FOLDER_TITLE, folderTitle );
        }
        else
        {
            List<Folder> folderList = _folderServices.findByPluId( nIdPlu );
            Paginator<Folder> paginator = new Paginator<Folder>( (List<Folder>) folderList, _nItemsPerPage,
                    JSP_TREE_PLU + "?id_plu=" + nIdPlu, PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
            model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
            model.put( MARK_PAGINATOR_DOSSER, paginator );
            model.put( MARK_LIST_FOLDER_LIST, paginator.getPageItems( ) );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TREE_PLU, getLocale( ), model );

        _fileList.clear( );
        _folderHtml.setHtml( null );
        _folderHtml.setHtmlImpression( null );
        _folderImage.setImg( null );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a HTML form that displays the Atome manage
     * @param request the Http request
     * @return HTML
     */
    public String getTreePluAtome( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        if ( plu == null )
        {
            plu = new Plu( );
        }

        Plu pluWork = _pluServices.findPluWork( );
        List<Plu> pluList = _pluServices.findAll( );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_RESULT_PER_PAGE, 10 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_PLU_WORK, pluWork );
        model.put( MARK_LIST_PLU_LIST, pluList );

        //gestion des atomes
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_FOLDER_ID ) ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_TREE_ATOME );

            int nIdFolder = 0; 

            if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_FOLDER_ID ) ) )
            {
                nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
            }

            Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

            if ( folder == null )
            {
                folder = new Folder( );
                folder.setPlu( nIdPlu );
            }

            model.put( MARK_FOLDER, folder );

            if ( request.getParameter( PARAMETER_FOLDER_TITLE ) != null )
            {
                model.put( PARAMETER_FOLDER_TITLE, request.getParameter( PARAMETER_FOLDER_TITLE ) );
            }

            if ( ( request.getParameter( PARAMETER_ATOME_NAME ) != null )
                    || ( request.getParameter( PARAMETER_ATOME_TITLE ) != null )
                    || ( request.getParameter( PARAMETER_ATOME_ID ) != null )
                    || ( request.getParameter( PARAMETER_VERSION_NUM ) != null )
                    || ( request.getParameter( PARAMETER_VERSION_D1 ) != null )
                    || ( request.getParameter( PARAMETER_VERSION_D2 ) != null )
                    || ( request.getParameter( PARAMETER_VERSION_D3 ) != null )
                    || ( request.getParameter( PARAMETER_VERSION_D4 ) != null ) )
            {
                String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
                String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
                String strAtomeId = request.getParameter( PARAMETER_ATOME_ID );
                String strNumVersion = request.getParameter( PARAMETER_VERSION_NUM );
                String strD1 = request.getParameter( PARAMETER_VERSION_D1 );
                String strD2 = request.getParameter( PARAMETER_VERSION_D2 );
                String strD3 = request.getParameter( PARAMETER_VERSION_D3 );
                String strD4 = request.getParameter( PARAMETER_VERSION_D4 );
                
                if ( request.getParameter( PARAMETER_CLEAR ) != null )
                {
                    atomeName = "";
                    atomeTitle = "";
                    strAtomeId = "";
                    strNumVersion = "";                	
                }

                AtomeFilter atomeFilter = new AtomeFilter( );
                VersionFilter versionFilter = new VersionFilter( );

                if ( !atomeName.equals( "" ) )
                {
                    atomeFilter.setName( atomeName );
                }

                if ( !atomeTitle.equals( "" ) )
                {
                    atomeFilter.setTitle( atomeTitle );
                }

                if ( !strAtomeId.equals( "" ) )
                {
                    Boolean p = Pattern.matches( "[0-9]+?", strAtomeId );

                    if ( p )
                    {
                        int atomeId = Integer.parseInt( strAtomeId );
                        atomeFilter.setId( atomeId );
                    }
                    else
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ATOME_ID_NUMBER, AdminMessage.TYPE_STOP );
                    }
                }

                if ( !strNumVersion.equals( "" ) )
                {
                    Boolean p = Pattern.matches( "[0-9]+?", strNumVersion );

                    if ( p )
                    {
                        int numVersion = Integer.parseInt( strNumVersion );
                        versionFilter.setVersion( numVersion );
                    }
                    else
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_VERSION_NUMBER, AdminMessage.TYPE_STOP );
                    }
                }

                if ( !strD1.equals( "" ) )
                {
                    Date d1;

                    try
                    {
                        d1 = stringToDate( strD1, "dd/MM/yyyy" );
                        versionFilter.setD1( d1 );
                    }
                    catch ( ParseException e )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_FORMAT, AdminMessage.TYPE_STOP );
                    }
                }

                if ( !strD2.equals( "" ) )
                {
                    Date d2;

                    try
                    {
                        d2 = stringToDate( strD2, "dd/MM/yyyy" );
                        versionFilter.setD2( d2 );
                    }
                    catch ( ParseException e )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_FORMAT, AdminMessage.TYPE_STOP );
                    }
                }

                if ( !strD3.equals( "" ) )
                {
                    Date d3;

                    try
                    {
                        d3 = stringToDate( strD3, "dd/MM/yyyy" );
                        versionFilter.setD3( d3 );
                    }
                    catch ( ParseException e )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE_FORMAT, AdminMessage.TYPE_STOP );
                    }
                }

                if ( !strD4.equals( "" ) )
                {
                    Date d4;

                    try
                    {
                        d4 = stringToDate( strD4, "dd/MM/yyyy" );
                        versionFilter.setD4( d4 );
                    }
                    catch ( ParseException e )
                    {
                        //throw new AppException( "An exception occured during date parsing", e );
                    }
                }

                List<Version> versionList = _versionServices.findByFilter( atomeFilter, versionFilter );

                Paginator<Version> paginatorAtome = new Paginator<Version>( (List<Version>) versionList,
                        _nItemsPerPage,
 JSP_TREE_PLU_ATOME + "?id_plu=" + plu.getId( ) + "&id_folder=" + folder.getId( )
                                + "&atome_name="
 + atomeName + "&atome_title=" + atomeTitle + "&id_atome=" + strAtomeId
                                + "&num_version=" + strNumVersion + "&version_d1=" + strD1 + "&version_d2=" + strD2
                                + "&version_d3=" + strD3 + "&version_d4=" + strD4, PARAMETER_PAGE_INDEX,
                        _strCurrentPageIndex );

                model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
                model.put( MARK_PAGINATOR_ATOME, paginatorAtome );
                model.put( MARK_LIST_VERSION_LIST, paginatorAtome.getPageItems( ) );
                model.put( PARAMETER_ATOME_NAME, atomeName );
                model.put( PARAMETER_ATOME_TITLE, atomeTitle );
                model.put( PARAMETER_ATOME_ID, strAtomeId );
                model.put( PARAMETER_VERSION_NUM, strNumVersion );
                model.put( PARAMETER_VERSION_D1, strD1 );
                model.put( PARAMETER_VERSION_D2, strD2 );
                model.put( PARAMETER_VERSION_D3, strD3 );
                model.put( PARAMETER_VERSION_D4, strD4 );
            }
            else
            {
                if ( request.getParameter( PARAMETER_ATOME_ALL ) != null )
                {
                    List<Version> versionList = _versionServices.findAll( );

                    Paginator<Version> paginatorAtomeAll = new Paginator<Version>( (List<Version>) versionList,
                            _nItemsPerPage,
 JSP_TREE_PLU_ATOME + "?id_plu=" + plu.getId( ) + "&id_folder="
                                    + folder.getId( ) + "&atome_all=1",
                            PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

                    model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
                    model.put( MARK_PAGINATOR_ATOME, paginatorAtomeAll );
                    model.put( MARK_LIST_VERSION_LIST, paginatorAtomeAll.getPageItems( ) );
                }
                else
                {
                    List<Version> versionList = _versionServices.findByPluAndFolder( folder.getPlu( ), nIdFolder );

                    Paginator<Version> paginatorAtomeFind = new Paginator<Version>( (List<Version>) versionList,
                            _nItemsPerPage, JSP_TREE_PLU_ATOME + "?id_plu=" + plu.getId( ) + "&id_folder="
                                    + folder.getId( ) + "&atome_all=1", PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

                    model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
                    model.put( MARK_PAGINATOR_ATOME, paginatorAtomeFind );
                    model.put( MARK_LIST_VERSION_LIST, paginatorAtomeFind.getPageItems( ) );
                }

            }
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TREE_PLU_ATOME, getLocale( ), model );

        _fileList.clear( );
        _folderHtml.setHtml( null );
        _folderHtml.setHtmlImpression( null );
        _folderImage.setImg( null );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a HTML form for create a new folder
     * @param request the Http request
     * @return HTML
     */
    public String getCreateFolder( HttpServletRequest request )
    {
    	//Clean _listFile
    	this.reinitListFile( request );
    	
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_FOLDER );

        Plu plu = _pluServices.findPluWork( );

        if ( plu.getId( ) == 0 )
        { // FIXME renvoie l'url au lieu du message d'erreur
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_PLU_WORK, AdminMessage.TYPE_STOP );
        }

        Collection<Folder> folderList = _folderServices.findByPluId( plu.getId( ) );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ) );
        model.put( PARAMETER_FOLDER_PARENT_ID, request.getParameter( PARAMETER_FOLDER_PARENT_ID ) );
        model.put( PARAMETER_FOLDER_TITLE, request.getParameter( PARAMETER_FOLDER_TITLE ) );
        model.put( PARAMETER_FOLDER_IMAGE, request.getParameter( PARAMETER_FOLDER_IMAGE ) );
        model.put( PARAMETER_FOLDER_DESCRIPTION, request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );

        if ( request.getParameter( PARAMETER_FOLDER_ID ) != null 
        		&& request.getParameter( PARAMETER_FOLDER_HTML_NOT_EMPTY ) != null 
        		&& request.getParameter( PARAMETER_FOLDER_HTML_NOT_EMPTY ).equals( "true" )
        		&& !request.getParameter( PARAMETER_FOLDER_ID ).equals( "0" ) )
        {
            String utilisation = "";
            if ( request.getParameter( PARAMETER_FOLDER_HTML_UTILISATION ) != null )
            {
                utilisation = request.getParameter( PARAMETER_FOLDER_HTML_UTILISATION );
            }

	        if ( request.getParameter( PARAMETER_FOLDER_ID_DUPLICATE ) != null )
	        {
	            int idFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID_DUPLICATE ) );
	            Folder folderDuplicate = _folderServices.findByPrimaryKey( idFolder );
	            
	            if ( folderDuplicate != null )
	            {
		            if ( utilisation.equals( "C" ) )
		            {
		                _folderHtml.setHtml( folderDuplicate.getHtml( ) );
		            }
		            else if ( utilisation.equals( "I" ) )
		            {
		                _folderHtml.setHtmlImpression( folderDuplicate.getHtmlImpression( ) );
		            }
		            else
		            {
		                _folderHtml.setHtml( folderDuplicate.getHtml( ) );
		                _folderHtml.setHtmlImpression( folderDuplicate.getHtmlImpression( ) );
		            }
	            }
	        }
            else
            {
                if ( request instanceof MultipartHttpServletRequest )
                {
                    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                    FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_HTML );

                    if ( fileItem != null && fileItem.getSize( ) != 0 )
                    {
                        if ( utilisation.equals( "C" ) )
                        {
                            _folderHtml.setHtml( fileItem.getString( ) );
                        }
                        else if ( utilisation.equals( "I" ) )
                        {
                            _folderHtml.setHtmlImpression( fileItem.getString( ) );
                        }
                    }
                }
                else
                {
                    if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_FOLDER_HTML ) ) )
                    {
                        String strHtml = request.getParameter( PARAMETER_FOLDER_HTML );
                        if ( utilisation.equals( "C" ) )
                        {
                            _folderHtml.setHtml( strHtml );
                        }
                        else if ( utilisation.equals( "I" ) )
                        {
                            _folderHtml.setHtmlImpression( strHtml );
                        }
                    }
                }
            }
        }

        if ( StringUtils.isNotEmpty( _folderHtml.getHtml( ) ) )
        {
            model.put( MARK_HTML, 1 );
        }
        if ( StringUtils.isNotEmpty( _folderHtml.getHtmlImpression( ) ) )
        {
            model.put( MARK_HTML_IMPRESSION, 1 );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_FOLDER, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a message of confirmation of cancel for create a new folder
     * @param request the Http request
     * @return message
     */
    public String getConfirmCancelCreateFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        UrlItem url = new UrlItem( JSP_TREE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_CANCEL_CREATE_FOLDER, "true" );

        Object[] args = {};


        return this.getMessageJsp( request, MESSAGE_CONFIRM_CANCEL_CREATE_FOLDER, args, "jsp/admin/plugins/plu/folder/CreateFolder.jsp", url.getUrl( ) );
    }

    /**
     * Generates a message of confirmation for create a new folder
     * @param request the Http request
     * @return message
     */
    public String getConfirmCreateFolder( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_TITLE ).equals( "" )
                || request.getParameter( PARAMETER_FOLDER_DESCRIPTION ).equals( "" ) )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_REQUIRED_FIELD, null, "jsp/admin/plugins/plu/folder/CreateFolder.jsp", null );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );

        UrlItem url = new UrlItem( JSP_DO_CREATE_FOLDER );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        try
        {
	        url.addParameter( PARAMETER_FOLDER_PARENT_ID, URIUtil.encodeAll( request.getParameter( PARAMETER_FOLDER_PARENT_ID ) ) );
	        url.addParameter( PARAMETER_FOLDER_TITLE, URIUtil.encodeAll( folderTitle ) );
	        url.addParameter( PARAMETER_FOLDER_DESCRIPTION, URIUtil.encodeAll( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) ) );
        }
	    catch ( URIException e )
        {
			throw new AppException( "An error occured while parsing request parameters" );
		}

        if ( request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK ) != null )
        {
            String[] check = request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK );

            for ( int j = 0; j < check.length; ++j )
            {
                url.addParameter( PARAMETER_FOLDER_HTML_CHECK, check[j] );
            }
        }

        if ( request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK_IMPRESSION ) != null )
        {
            String[] checkImpression = request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK_IMPRESSION );

            for ( int j = 0; j < checkImpression.length; ++j )
            {
                url.addParameter( PARAMETER_FOLDER_HTML_CHECK_IMPRESSION, checkImpression[j] );
            }
        }

        Object[] args = { folderTitle };

        Folder folder = _folderServices.findForTestTitle( folderTitle );

        if ( folder != null )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_FOLDER_CREATE, args, "jsp/admin/plugins/plu/folder/CreateFolder.jsp", null );
        }

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_IMAGE );

            if ( fileItem.getSize( ) > 0 )
            {
                String name = fileItem.getName( );
                String type = name.substring( name.lastIndexOf( "." ) );

                if ( !type.equalsIgnoreCase( ".jpg" ) && !type.equalsIgnoreCase( ".png" )
                        && !type.equalsIgnoreCase( ".gif" ) )
                {
                    return this.getMessageJsp( request, MESSAGE_ERROR_FOLDER_IMAGE_TYPE, args, "jsp/admin/plugins/plu/folder/CreateFolder.jsp", null );
                }

                PhysicalFile physicalFile = new PhysicalFile( );
                physicalFile.setValue( fileItem.get( ) );

                _folderImage.setImg( physicalFile.getValue( ) );
            }
        }

        return this.getMessageJsp( request, MESSAGE_CONFIRM_CREATE_FOLDER, args, "jsp/admin/plugins/plu/folder/CreateFolder.jsp", url.getUrl( ) );
    }

    /**
     * Create a folder and return to the folder manage
     * @param request the Http request
     * @return HTML
     */
    public String doCreateFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        int idParentFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_PARENT_ID ) );

        Folder folder = new Folder( );
        folder.setPlu( nIdPlu );
        folder.setParentFolder( idParentFolder );
        folder.setTitle( request.getParameter( PARAMETER_FOLDER_TITLE ) );
        folder.setDescription( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );

        if ( _folderImage.getImg( ) != null )
        {
            folder.setImg( _folderImage.getImg( ) );
        }
        else
        {
            folder.setImg( null );
        }

        if ( "true".equals( request.getParameter( PARAMETER_FOLDER_HTML_CHECK ) ) )
        {
            folder.setHtml( _folderHtml.getHtml( ) );
        }
        else
        {
            folder.setHtml( null );
        }

        if ( "true".equals( request.getParameter( PARAMETER_FOLDER_HTML_CHECK_IMPRESSION ) ) )
        {
            folder.setHtmlImpression( _folderHtml.getHtmlImpression( ) );
        }
        else
        {
            folder.setHtmlImpression( null );
        }

        _folderServices.create( folder );

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + nIdPlu;
    }

    /**
     * Generates a message of confirmation for remove a folder
     * @param request the Http request
     * @return message
     */
    public String getConfirmRemoveFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

        UrlItem url = new UrlItem( JSP_DO_REMOVE_FOLDER );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );

        Object[] args = { request.getParameter( PARAMETER_FOLDER_TITLE ) };

        Folder folder = _folderServices.findForDelete( nIdFolder );

        if ( folder != null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FOLDER_DELETE, args,
                    AdminMessage.TYPE_STOP );
        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_FOLDER, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Remove a folder and return to the folder manage
     * @param request the Http request
     * @return HTML
     */
    public String doRemoveFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        _folderServices.remove( folder );

        /* Set the PLU's state to "à relancer" */
        if ( plu.getDa( ) != null )
        {
            Etat etat = _etatServices.findByPrimaryKey( 5 );
            plu.setEtat( etat );
            _pluServices.update( plu );
        }

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId( );
    }

    /**
     * Generates a HTML form for modify a folder
     * @param request the Http request
     * @return HTML
     */
    public String getModifyFolder( HttpServletRequest request )
    {
    	//Clean _listFile
    	this.reinitListFile( request );
    	
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_FOLDER );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = 0;
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_FOLDER_ID_RETURN ) ) )
        {
            nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID_RETURN ) );
        }
        else
        {
            nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        }

        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        Folder folderParent = _folderServices.findByPrimaryKey( folder.getParentFolder( ) );

        if ( folderParent == null )
        {
            folderParent = new Folder( );
        }

        Collection<Folder> folderList = _folderServices.findByPluId( nIdPlu );

        if ( request.getParameter( PARAMETER_FOLDER_PARENT_ID ) != null )
        {
            folderParent.setId( Integer.parseInt( request.getParameter( PARAMETER_FOLDER_PARENT_ID ) ) );
        }
        if ( request.getParameter( PARAMETER_FOLDER_TITLE ) != null )
        {
            folder.setTitle( request.getParameter( PARAMETER_FOLDER_TITLE ) );
        }
        if ( request.getParameter( PARAMETER_FOLDER_IMAGE ) != null )
        {
            folder.setImg( request.getParameter( PARAMETER_FOLDER_IMAGE ).getBytes( ) );
        }
        if ( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) != null )
        {
            folder.setDescription( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        }

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_FOLDER_PARENT, folderParent );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ) );

        String utilisation = "";
        if ( request.getParameter( PARAMETER_FOLDER_HTML_UTILISATION ) != null )
        {
            utilisation = request.getParameter( PARAMETER_FOLDER_HTML_UTILISATION );
        }
        
        if ( request.getParameter( PARAMETER_FOLDER_HTML_NOT_EMPTY ) != null && request.getParameter( PARAMETER_FOLDER_HTML_NOT_EMPTY ).equals( "true" ) )
        {
	        if ( request.getParameter( PARAMETER_FOLDER_HTML ) != null )
	        {
	            String strHtml = request.getParameter( PARAMETER_FOLDER_HTML );
	            if ( utilisation.equals( "C" ) )
	            {
	                _folderHtml.setHtml( strHtml );
	            }
	            else if ( utilisation.equals( "I" ) )
	            {
	                _folderHtml.setHtmlImpression( strHtml );
	            }
	        }
	        else if ( request.getParameter( PARAMETER_FOLDER_ID_DUPLICATE ) != null )
	        {
	            int idFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID_DUPLICATE ) );
	            Folder folderDuplicate = _folderServices.findByPrimaryKey( idFolder );
	            
	            if ( folderDuplicate != null )
	            {
		            if ( utilisation.equals( "C" ) )
		            {
		                _folderHtml.setHtml( folderDuplicate.getHtml( ) );
		            }
		            else if ( utilisation.equals( "I" ) )
		            {
		                _folderHtml.setHtmlImpression( folderDuplicate.getHtmlImpression( ) );
		            }
		            else
		            {
		                _folderHtml.setHtml( folderDuplicate.getHtml( ) );
		                _folderHtml.setHtmlImpression( folderDuplicate.getHtmlImpression( ) );
		            }
	            }
	        }
	        else if ( request instanceof MultipartHttpServletRequest )
	        {
	            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	            FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_HTML );
	
                if ( fileItem != null && fileItem.getSize( ) != 0 )
                {
		            if ( utilisation.equals( "C" ) )
		            {
		                _folderHtml.setHtml( fileItem.getString( ) );
		            }
		            else if ( utilisation.equals( "I" ) )
		            {
		                _folderHtml.setHtmlImpression( fileItem.getString( ) );
		            }
                }
	        }
        }
        else
        {
        	if ( _folderHtml.getHtml( ) == null && _folderHtml.getHtmlImpression( ) == null )
        	{
	        	_folderHtml.setHtml( folder.getHtml( ) );
	        	_folderHtml.setHtmlImpression( folder.getHtmlImpression( ) );
        	}
        }

        if ( StringUtils.isNotEmpty( _folderHtml.getHtml( ) ) )
        {
            model.put( MARK_HTML, 1 );
        }
        if ( StringUtils.isNotEmpty( _folderHtml.getHtmlImpression( ) ) )
        {
            model.put( MARK_HTML_IMPRESSION, 1 );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_FOLDER, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a message of confirmation of cancel for modify a folder
     * @param request the Http request
     * @return message
     */
    public String getConfirmCancelModifyFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );

        UrlItem url = new UrlItem( JSP_TREE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, "" );
        url.addParameter( PARAMETER_FOLDER_TITLE, "" );

        Object[] args = { folderTitle };

        return this.getMessageJsp( request, MESSAGE_CONFIRM_CANCEL_MODIFY_FOLDER, args, "jsp/admin/plugins/plu/folder/ModifyFolder.jsp", url.getUrl( ) );
    }

    /**
     * Generates a message of confirmation for modify a folder
     * @param request the Http request
     * @return message
     */
    public String getConfirmModifyFolder( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_TITLE ).equals( "" )
                || request.getParameter( PARAMETER_FOLDER_DESCRIPTION ).equals( "" ) )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_REQUIRED_FIELD, null, "jsp/admin/plugins/plu/folder/ModifyFolder.jsp", null );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );
        String description = request.getParameter( PARAMETER_FOLDER_DESCRIPTION ).replaceAll( "\"", "\'" );

        UrlItem url = new UrlItem( JSP_DO_MODIFY_FOLDER );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        try
        {
            url.addParameter( PARAMETER_FOLDER_PARENT_ID, URIUtil.encodeAll( request.getParameter( PARAMETER_FOLDER_PARENT_ID ) ) );
            url.addParameter( PARAMETER_FOLDER_TITLE, URIUtil.encodeAll( folderTitle ) );
            url.addParameter( PARAMETER_FOLDER_DESCRIPTION, URIUtil.encodeAll( description ) );
        }
	    catch ( URIException e )
        {
			throw new AppException( "An error occured while parsing request parameters" );
		}

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

        if ( request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK_IMPRESSION ) != null )
        {
            String[] check = request.getParameterValues( PARAMETER_FOLDER_HTML_CHECK_IMPRESSION );

            for ( int j = 0; j < check.length; ++j )
            {
                url.addParameter( PARAMETER_FOLDER_HTML_CHECK_IMPRESSION, check[j] );
            }
        }

        Object[] args = { folderTitle };
        Folder folder = _folderServices.findForTestTitle( folderTitle );

        if ( folder != null )
        {
            if ( ( folder.getTitle( ) != null )
                    && !folder.getTitle( ).equals( request.getParameter( PARAMETER_FOLDER_TITLE_OLD ) ) )
            {
                return this.getMessageJsp( request, MESSAGE_ERROR_FOLDER_CREATE, args, "jsp/admin/plugins/plu/folder/ModifyFolder.jsp", null );
            }
        }

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_IMAGE );

            if ( fileItem.getSize( ) > 0 )
            {
                String name = fileItem.getName( );
                String type = name.substring( name.lastIndexOf( "." ) );

                if ( !type.equalsIgnoreCase( ".jpg" ) && !type.equalsIgnoreCase( ".png" )
                        && !type.equalsIgnoreCase( ".gif" ) )
                {
                    return this.getMessageJsp( request, MESSAGE_ERROR_FOLDER_IMAGE_TYPE, args, "jsp/admin/plugins/plu/folder/ModifyFolder.jsp", null );
                }

                PhysicalFile physicalFile = new PhysicalFile( );
                physicalFile.setValue( fileItem.get( ) );
                _folderImage.setImg( physicalFile.getValue( ) );
                _folderImage.setNomImage( fileItem.getName( ) );
            }
        }


        return this.getMessageJsp( request, MESSAGE_CONFIRM_MODIFY_FOLDER, args, "jsp/admin/plugins/plu/folder/ModifyFolder.jsp", url.getUrl( ) );
    }

    /**
     * Modify a folder and return to the folder manage
     * @param request the Http request
     * @return HTML
     */
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
            folder.setImg( _folderImage.getImg( ) );
            folder.setNomImage( null );
        }

        if ( _folderImage.getImg( ) != null )
        {
            folder.setImg( _folderImage.getImg( ) );
            folder.setNomImage( _folderImage.getNomImage( ) );
        }


        if ( "true".equals( request.getParameter( PARAMETER_FOLDER_HTML_CHECK ) ) )
        {
            folder.setHtml( _folderHtml.getHtml( ) );
        }
        else
        {
            folder.setHtml( null );
        }

        if ( "true".equals( request.getParameter( PARAMETER_FOLDER_HTML_CHECK_IMPRESSION ) ) )
        {
            folder.setHtmlImpression( _folderHtml.getHtmlImpression( ) );
        }
        else
        {
            folder.setHtmlImpression( null );
        }

        /* Set the PLU's state to "à relancer" */
        if ( plu.getDa( ) != null )
        {
            Etat etat = _etatServices.findByPrimaryKey( 5 );
            plu.setEtat( etat );
            _pluServices.update( plu );
        }

        _folderServices.update( folder );

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId( );
    }

    /**
     * Generates a HTML form for correct a folder
     * @param request the Http request
     * @return HTML
     */
    public String getCorrectFolder( HttpServletRequest request )
    {
    	//Clean _listFile
    	this.reinitListFile( request );
    	
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CORRECT_FOLDER );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        Folder folderParent = _folderServices.findByPrimaryKey( folder.getParentFolder( ) );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_FOLDER_PARENT, folderParent );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ) );

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_HTML );

            PhysicalFile physicalFile = new PhysicalFile( );
            physicalFile.setValue( fileItem.get( ) );
            _folderHtml.setImg( physicalFile.getValue( ) );
            model.put( MARK_HTML, 1 );
        }
        else
        {
            if ( request.getParameter( PARAMETER_FOLDER_HTML ) != null )
            {
                String strHtml = request.getParameter( PARAMETER_FOLDER_HTML );
                _folderHtml.setHtml( strHtml );
                model.put( MARK_HTML, 1 );
            }
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CORRECT_FOLDER, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a message of confirmation of cancel for correct a folder
     * @param request the Http request
     * @return message
     */
    public String getConfirmCancelCorrectFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );

        UrlItem url = new UrlItem( JSP_TREE_PLU );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );

        Object[] args = { folderTitle };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_CORRECT_FOLDER, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Generates a message of confirmation for correct a folder
     * @param request the Http request
     * @return message
     */
    public String getConfirmCorrectFolder( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_TITLE ).equals( "" )
                || request.getParameter( PARAMETER_FOLDER_DESCRIPTION ).equals( "" )
                || request.getParameter( PARAMETER_HISTORY_DESCRIPTION ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        String folderTitle = request.getParameter( PARAMETER_FOLDER_TITLE );

        UrlItem url = new UrlItem( JSP_DO_CORRECT_FOLDER );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        try
        {
        	url.addParameter( PARAMETER_FOLDER_ID, URIUtil.encodeAll( request.getParameter( PARAMETER_FOLDER_ID ) ) );
	        url.addParameter( PARAMETER_FOLDER_PARENT_ID, URIUtil.encodeAll( request.getParameter( PARAMETER_FOLDER_PARENT_ID ) ) );
	        url.addParameter( PARAMETER_FOLDER_TITLE, URIUtil.encodeAll( folderTitle ) );
	        url.addParameter( PARAMETER_FOLDER_DESCRIPTION, URIUtil.encodeAll( request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) ) );
	        url.addParameter( PARAMETER_HISTORY_DESCRIPTION, URIUtil.encodeAll( request.getParameter( PARAMETER_HISTORY_DESCRIPTION ) ) );
	    }
	    catch ( URIException e )
	    {
			throw new AppException( "An error occured while parsing request parameters" );
		}

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

        if ( ( folder != null ) && !folder.getTitle( ).equals( request.getParameter( PARAMETER_FOLDER_TITLE_OLD ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FOLDER_CREATE, args,
                    AdminMessage.TYPE_STOP );
        }

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            FileItem fileItem = multipartRequest.getFile( PARAMETER_FOLDER_IMAGE );

            if ( fileItem.getSize( ) > 0 )
            {
                String name = fileItem.getName( );
                String type = name.substring( name.lastIndexOf( "." ) );

                if ( !type.equalsIgnoreCase( ".jpg" ) && !type.equalsIgnoreCase( ".png" )
                        && !type.equalsIgnoreCase( ".gif" ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_FOLDER_IMAGE_TYPE, args,
                            AdminMessage.TYPE_STOP );
                }

                PhysicalFile physicalFile = new PhysicalFile( );
                physicalFile.setValue( fileItem.get( ) );
                _folderImage.setImg( physicalFile.getValue( ) );
                _folderImage.setNomImage( fileItem.getName( ) );
            }
        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CORRECT_FOLDER, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Correct a folder and return to the folder manage
     * @param request the Http request
     * @return HTML
     */
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
            folder.setImg( _folderImage.getImg( ) );
            folder.setNomImage( null );
        }

        if ( _folderImage.getImg( ) != null )
        {
            folder.setImg( _folderImage.getImg( ) );
            folder.setNomImage( _folderImage.getNomImage( ) );
        }

        if ( !"true".equals( request.getParameter( PARAMETER_FOLDER_HTML_CHECK ) ) )
        {
            folder.setHtml( _folderHtml.getHtml( ) );
        }

        _folderServices.update( folder );

        History history = new History( );
        history.setPlu( nIdPlu );
        history.setFolder( nIdFolder );

        Date date = new Date( );
        history.setDc( date );
        history.setDescription( request.getParameter( PARAMETER_HISTORY_DESCRIPTION ) );
        _historyServices.create( history );

        /* Set the PLU's state to "à relancer" */
        if ( plu.getDa( ) != null )
        {
            Etat etat = _etatServices.findByPrimaryKey( 5 );
            plu.setEtat( etat );
            _pluServices.update( plu );
        }

        return JSP_REDIRECT_TO_TREE_PLU + "?id_plu=" + plu.getId( );
    }

    /**
     * Generates a HTML page that visualizes the folder
     * @param request the Http request
     * @return HTML
     */
    public String getViewFolder( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
        List<Folder> folderChildList = _folderServices.findByParent( folder.getId( ) );

        Folder folderParent = _folderServices.findByPrimaryKey( folder.getParentFolder( ) );
        List<Folder> folderParentChildList = _folderServices.findByParent( folder.getParentFolder( ) );

        List<Version> listVersion = _versionServices.findByPluAndFolder( nIdPlu, nIdFolder );

        List<File> fileList = new ArrayList<File>( );

        for ( Version version : listVersion )
        {
            fileList.addAll( _fileServices.findByVersion( version.getId( ) ) );
        }

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_FOLDER_PARENT, folderParent );
        model.put( MARK_LIST_FOLDER_CHILD_LIST, folderChildList );
        model.put( MARK_LIST_FOLDER_PARENT_CHILD_LIST, folderParentChildList );
        model.put( MARK_LIST_VERSION_LIST, listVersion );
        model.put( MARK_LIST_FILE_LIST, fileList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VIEW_FOLDER, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a HTML form that displays the choices of creation of atome
     * @param request the Http request
     * @return HTML
     */
    public String getChoiceCreateAtome( HttpServletRequest request )
    {
        Plu plu = _pluServices.findPluWork( );

        if ( plu.getId( ) == 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_PLU_WORK, AdminMessage.TYPE_STOP );
        }

        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_ATOME );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        if ( folder == null )
        {
            folder = new Folder( );
        }

        List<Atome> atomeList = _atomeServices.findAll( );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_ATOME_LIST, atomeList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CHOICE_CREATE_ATOME, getLocale( ), model );

        _fileList.clear( );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a HTML form for create a new atome
     * @param request the Http request
     * @return HTML
     */
    public String getCreateAtome( HttpServletRequest request )
    {
    	//Clean _listFile
    	this.reinitListFile( request );
    	
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_ATOME );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        if ( folder == null )
        {
            folder = new Folder( );
        }

        Collection<Folder> folderList = _folderServices.findByPluId( nIdPlu );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( PARAMETER_FOLDER_ID_ATOME, request.getParameter( PARAMETER_FOLDER_ID_ATOME ) );
        model.put( PARAMETER_ATOME_NAME, request.getParameter( PARAMETER_ATOME_NAME ) );
        model.put( PARAMETER_ATOME_TITLE, request.getParameter( PARAMETER_ATOME_TITLE ) );
        model.put( PARAMETER_ATOME_NUM, request.getParameter( PARAMETER_ATOME_NUM ) );
        String versionNum = request.getParameter( PARAMETER_VERSION_NUM );
        if ( versionNum == null )
        {
            versionNum = "1";
        }
        model.put( PARAMETER_VERSION_NUM, versionNum );
        model.put( PARAMETER_ATOME_DESCRIPTION, request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ) );
        if ( request.getParameter( PARAMETER_REINIT ) != null )
        {
        	model.put( PARAMETER_REINIT, request.getParameter( PARAMETER_REINIT ) );
        }

        if ( request.getParameterValues( PARAMETER_FILE_CHECK ) != null )
        {
        	model.put( PARAMETER_FILE_CHECK, request.getParameterValues( PARAMETER_FILE_CHECK ) );
        }
        
        if ( request instanceof MultipartHttpServletRequest )
        {
        	String ret = addFileToFileList( request );
        	if ( StringUtils.isNotEmpty( ret ) )
        	{
        		return ret;
        	}
        }
    	getFileCheck( request, model );

        if ( !_fileList.isEmpty( ) )
        {
            model.put( MARK_LIST_FILE_LIST, _fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_ATOME, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }
    
    /**
     * Add a file in _fileList
     * @param request HttpServletRequest
     * @return ret errorMessage
     */
	private String addFileToFileList( HttpServletRequest request )
	{
		String ret = "";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );

		if ( ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_FILE_TITLE ) ) )
		        && ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_FILE_NAME ) ) )
		        && ( fileItem.get( ) != null ) )
		{
		    File file = new File( );
		    PhysicalFile physicalFile = new PhysicalFile( );
		    physicalFile.setValue( fileItem.get( ) );

		    String name = fileItem.getName( );
            String type = name.substring( name.lastIndexOf( "." ) + 1 ).toUpperCase( );
		    
            //Search files with same name
            int nNumVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) );
            String strFileDBName = PluUtils.getFileNameForDB( request.getParameter( PARAMETER_FILE_NAME ),
                    String.valueOf( nNumVersion ) );
            FileFilter fileFilter = new FileFilter( );
            fileFilter.setName( strFileDBName );
            List<File> listFileByName = _fileServices.findByFilter( fileFilter, new AtomeFilter( ) );

		    for ( File fileTest : _fileList )
		    {
                //If a file with the same name exist in DB or a new atome file have the same name : error
                if ( !listFileByName.isEmpty( )
                        || fileTest.getName( ).equals( request.getParameter( PARAMETER_FILE_NAME ) ) )
		        {
		        	Object[] args = {"", request.getParameter( PARAMETER_FILE_NAME )};
	                ret = this.getMessageJsp( request, MESSAGE_ERROR_FILE_CREATE_NAME, args,
	                        "jsp/admin/plugins/plu/file/JoinFile.jsp", null );
		        }
		    }
		    if ( StringUtils.isEmpty( ret ) )
		    {
			    file.setName( request.getParameter( PARAMETER_FILE_NAME ).replace( " ", "-" ) );
			    file.setTitle( request.getParameter( PARAMETER_FILE_TITLE ) );
			    file.setUtilisation( request.getParameter( PARAMETER_FILE_UTILISATION ).charAt( 0 ) );
			    file.setFile( physicalFile.getValue( ) );
			    file.setMimeType( type );
			    file.setSize( (int) fileItem.getSize( ) );
			    _fileList.add( file );
		    }
		}
		else if (  StringUtils.isNotEmpty( request.getParameter( "joinFile" ) ) )
		{
			if ( request.getParameter( "joinFile" ).equals( "true" ) )
			{
				ret = this.getMessageJsp( request, MESSAGE_ERROR_REQUIRED_FIELD, null,
						"jsp/admin/plugins/plu/file/JoinFile.jsp", null );
			}
		}
		
		return ret;
	}

    /**
     * Generates a HTML form for create a new atome with an existing atome
     * @param request the Http request
     * @return HTML
     * @throws IOException IOException 
     */
    public String getCreateAtomeWithOld( HttpServletRequest request ) throws IOException
    {
    	//Clean _listFile
    	this.reinitListFile( request );
    	
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        Collection<Folder> folderList = _folderServices.findByPluId( nIdPlu );

        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        Atome atome = _atomeServices.findByPrimaryKey( nIdAtome );
        Folder folder = _folderServices.findByAtome( nIdAtome );

        int numVersion = _versionServices.findMaxVersion( nIdAtome );
        Version version = _versionServices.findByAtomeAndNumVersion( nIdAtome, numVersion );

        Map<String, Object> model = new HashMap<String, Object>( );
        
        if ( _fileList.isEmpty( ) )
        {
        	setFileList( version.getId( ), model, request, true );
        }

        model.put( MARK_PLU, plu );
        if ( request.getParameter( PARAMETER_FOLDER_ID_ATOME ) != null )
        {
            model.put( PARAMETER_FOLDER_ID_ATOME, request.getParameter( PARAMETER_FOLDER_ID_ATOME ) );
        }
        else
        {
            model.put( PARAMETER_FOLDER_ID_ATOME, folder.getId( ) );
        }
        model.put( PARAMETER_ATOME_NAME, request.getParameter( PARAMETER_ATOME_NAME ) );
        model.put( PARAMETER_ATOME_TITLE, request.getParameter( PARAMETER_ATOME_TITLE ) );
        model.put( PARAMETER_ATOME_NUM, request.getParameter( PARAMETER_ATOME_NUM ) );
        String versionNum = request.getParameter( PARAMETER_VERSION_NUM );
        if ( versionNum == null )
        {
            versionNum = "1";
        }
        model.put( PARAMETER_VERSION_NUM, versionNum );
        model.put( PARAMETER_ATOME_DESCRIPTION, request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_ATOME, atome );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ) );
        if ( request.getParameter( PARAMETER_REINIT ) != null )
        {
        	model.put( PARAMETER_REINIT, request.getParameter( PARAMETER_REINIT ) );
        }

        if ( request.getParameterValues( PARAMETER_FILE_CHECK ) != null )
        {
        	model.put( PARAMETER_FILE_CHECK, request.getParameterValues( PARAMETER_FILE_CHECK ) );
        }
        
        if ( request instanceof MultipartHttpServletRequest && request.getParameter( PARAMETER_REINIT ) == null )
        {
        	String ret = addFileToFileList( request );
        	if ( StringUtils.isNotEmpty( ret ) )
        	{
        		return ret;
        	}
        }
    	getFileCheck( request, model );

        if ( !_fileList.isEmpty( ) )
        {
            model.put( MARK_LIST_FILE_LIST, _fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_ATOME_WITH_OLD, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a message of confirmation of cancel for create a new atome
     * @param request the Http request
     * @return message
     */
    public String getConfirmCancelCreateAtome( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );

        UrlItem url = new UrlItem( JSP_CHOICE_CREATE_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );

        return this.getMessageJsp( request, MESSAGE_CONFIRM_CANCEL_CREATE_ATOME, null, JSP_CREATE_ATOME, url.getUrl( ) ); 
    }

    /**
     * Generates a message of confirmation for create a new atome
     * @param request the Http request
     * @return message
     */
    public String getConfirmCreateAtome( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_ID_ATOME ) == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REQUIRED_FIELD, AdminMessage.TYPE_STOP );
        }
        if ( request.getParameter( PARAMETER_FOLDER_ID_ATOME ).equals( "" )
                || request.getParameter( PARAMETER_ATOME_NUM ).equals( "" )
                || request.getParameter( PARAMETER_VERSION_NUM ).equals( "" )
                || request.getParameter( PARAMETER_ATOME_NAME ).equals( "" )
                || request.getParameter( PARAMETER_ATOME_TITLE ).equals( "" )
                || request.getParameter( PARAMETER_ATOME_DESCRIPTION ).equals( "" )
                || request.getParameter( PARAMETER_ATOME_NAME ).matches( "[ \']+?" )
                || request.getParameter( PARAMETER_ATOME_TITLE ).matches( "[ \']+?" )
                || request.getParameterValues( PARAMETER_FILE_CHECK ) == null )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_REQUIRED_FIELD, null,
                    JSP_CREATE_ATOME, null );
        }

        int nIdAtome = 0;
        int numVersion = 0;
        try
        {
            nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_NUM ) );
        }
        catch ( NumberFormatException e )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_ATOME_ID_NUMBER, null,
                    JSP_CREATE_ATOME, null );
        }
        try
        {
            numVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) );
        }
        catch ( NumberFormatException e )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_VERSION_NUMBER, null,
                    JSP_CREATE_ATOME, null );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID_ATOME ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
        String atomeDescription = request.getParameter( PARAMETER_ATOME_DESCRIPTION );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE_ATOME );

        Object[] argsAtome = { atomeName, atomeTitle };

        if ( check == null )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_ATOME_CREATE_FILE_CHECK, null,
                    JSP_CREATE_ATOME, null );
        }

        UrlItem url = new UrlItem( JSP_DO_CREATE_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID_ATOME, nIdFolder );
        url.addParameter( PARAMETER_ATOME_NUM, nIdAtome );
        url.addParameter( PARAMETER_VERSION_NUM, numVersion );
        try
        {
	        url.addParameter( PARAMETER_ATOME_NAME, URIUtil.encodeAll( atomeName ) );
	        url.addParameter( PARAMETER_ATOME_TITLE, URIUtil.encodeAll( atomeTitle ) );
	        url.addParameter( PARAMETER_ATOME_DESCRIPTION, URIUtil.encodeAll( atomeDescription ) );
	    }
	    catch ( URIException e )
	    {
			throw new AppException( "An error occured while parsing request parameters" );
		}


        for ( Atome atome : _atomeServices.findAll( ) )
        {
            if ( atome.getId( ) == nIdAtome )
            {
                return this.getMessageJsp( request, MESSAGE_ERROR_ATOME_CREATE_ID, argsAtome,
                        JSP_CREATE_ATOME, null );
            }

            if ( atome.getName( ).equals( atomeName ) )
            {
                return this.getMessageJsp( request, MESSAGE_ERROR_ATOME_CREATE_NAME, argsAtome,
                        JSP_CREATE_ATOME, null );
            }

            if ( atome.getTitle( ).equals( atomeTitle ) )
            {
                return this.getMessageJsp( request, MESSAGE_ERROR_ATOME_CREATE_TITLE, argsAtome,
                        JSP_CREATE_ATOME, null );
            }
        }


        for ( int j = 0; j < check.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_CHECK, check[j] );
        }

        for ( int j = 0; j < fileTitle.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_TITLE_ATOME, fileTitle[j] );
        }

        // Check atome's file
        String confirmFile = "";
        confirmFile = getConfirmAtomeFile( request, numVersion, atomeName, atomeTitle, check,
				fileTitle, JSP_CREATE_ATOME );
        if ( StringUtils.isNotEmpty( confirmFile ) )
        {
        	return confirmFile;
        }

        return this.getMessageJsp( request, MESSAGE_CONFIRM_CREATE_ATOME, argsAtome,
                JSP_CREATE_ATOME, url.getUrl( ) );
    }

	/**
	 * Confirm method for atome files
	 * @param request HttpServletRequest
	 * @param numVersion num version
	 * @param atomeName atome name
	 * @param atomeTitle atome title
	 * @param check list of checkbox for atome file
	 * @param fileTitle file title
	 * @param pageReturn String pageReturn
	 * @return ret error message
	 */
	private String getConfirmAtomeFile( HttpServletRequest request,
			int numVersion, String atomeName, String atomeTitle,
			String[] check, String[] fileTitle, String pageReturn ) 
	{
		String ret = "";		
		int i = 0;

        Object[] argsEps = { atomeName, atomeTitle, numVersion };
        boolean consultation = false;
        boolean impression = false;
        String testName = _strVide;
        List<String> listTitle = new ArrayList<String>( );
        
        for ( File file : _fileList )
        {
            for ( int j = 0; j < check.length; ++j )
            {
                int c = Integer.parseInt( check[j] );

                if ( c == i )
                {
                    Object[] argsFile = { fileTitle[j], file.getName( ) };

                    if ( file.getSize( ) <= 0 )
                    {
                    	ret =  this.getMessageJsp( request, MESSAGE_ERROR_FILE_CREATE_SIZE, argsFile,
                    			pageReturn, null );
                    }

                    if ( testName.equals( file.getName( ) ) )
                    {
                    	ret =  this.getMessageJsp( request, MESSAGE_ERROR_FILE_CREATE_NAME, argsFile,
                    			pageReturn, null );
                    }

                    if ( listTitle.contains( fileTitle[j] + file.getUtilisation( ) ) )
                    {
                    	ret =  this.getMessageJsp( request, MESSAGE_ERROR_FILE_CREATE_TITLE, argsFile,
                    			pageReturn, null );
                    }
                    
                    if ( StringUtils.isEmpty( fileTitle[j] ) )
                    {
                    	ret =  this.getMessageJsp( request, MESSAGE_ERROR_FILE_CREATE_TITLE_EMPTY, argsFile,
                    			pageReturn, null );
                    }

                    int endIndex = file.getName( ).lastIndexOf( "-V" );

                    if ( endIndex != -1 )
                    {
                        testName = file.getName( ).substring( 0, endIndex );
                    }
                    else
                    {
                        testName = file.getName( );
                    }

                    listTitle.add( fileTitle[j] + file.getUtilisation( ) );

                    if ( file.getUtilisation( ) == 'I' )
                    {
                        impression = true;
                    }
                    else
                    {
                        consultation = true;
                    }
                }
            }

            i++;
        }

        if ( !impression )
        {
        	ret =  this.getMessageJsp( request, MESSAGE_ERROR_REQUIRED_FILE_EPS, argsEps,
        			pageReturn, null );
        }

        if ( !consultation )
        {
        	ret = this.getMessageJsp( request, MESSAGE_ERROR_REQUIRED_FILE_NO_EPS, argsEps,
        			pageReturn, null );
        }
        
        return ret;
	}

    /**
     * Create an atome and return to the choice create atome
     * @param request the Http request
     * @throws ParseException ParseException
     * @return HTML
     * @throws IOException IOException
     */
    public String doCreateAtome( HttpServletRequest request ) throws ParseException, IOException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID_ATOME ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_NUM ) );
        int numVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) );

        Atome atome = new Atome( );
        atome.setId( nIdAtome );
        atome.setName( request.getParameter( PARAMETER_ATOME_NAME ) );
        atome.setTitle( request.getParameter( PARAMETER_ATOME_TITLE ) );
        atome.setDescription( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );

        _atomeServices.create( atome );

        Version version = new Version( );
        version.setAtome( atome );
        version.setVersion( numVersion );

        _versionServices.create( version );

        Version version2 = _versionServices.findByAtomeAndNumVersion( nIdAtome, numVersion );

        FolderVersion folderVersion = new FolderVersion( );
        folderVersion.setVersion( version2 );
        folderVersion.setFolder( folder );

        _folderVersionServices.create( folderVersion );

        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE_ATOME );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        int i = 0;
        int order = 1;
        String strNumVersion;

        if ( numVersion < 10 )
        {
            strNumVersion = "-V0" + numVersion;
        }
        else
        {
            strNumVersion = "-V" + numVersion;
        }

        for ( File file : _fileList )
        {
            for ( int j = 0; j < check.length; ++j )
            {
                int c = Integer.parseInt( check[j] );

                if ( c == i )
                {
                    if ( !file.getTitle( ).equals( fileTitle[i] ) )
                    {
                        file.setTitle( fileTitle[i] );
                    }

                    file.setAtome( nIdAtome );
                    file.setOrder( order );
                    file.setVersion( version2.getId( ) );
                    
            		// Test if file name contains extension, if yes remove extension to file name

                    // Test if file name contains extension, if yes remove
                    // extension to file name
                    int a = file.getName( ).lastIndexOf( "." );
                    String nameWithoutExt = file.getName( );
                    String nameExt = "";
                    if ( a > 0 )
                    {
                        nameWithoutExt = file.getName( ).substring( 0, a );
                        nameExt = file.getName( ).substring( a );
                    }

                    a = nameWithoutExt.lastIndexOf( "-V" );
                    if ( a > 0 )
                    {
                        nameWithoutExt = nameWithoutExt.substring( 0, a ) + strNumVersion;
                    }
                    else
                    {
                        nameWithoutExt = nameWithoutExt + strNumVersion;
                    }

                    file.setName( nameWithoutExt + nameExt );
            		
                    file.setId( 0 );
                    _fileServices.create( file );
                    
                    FileFilter fileFilter = new FileFilter( );
                    fileFilter.setName( file.getName( ) );
                    fileFilter.setTitle( file.getTitle( ) );
                    AtomeFilter atomeFilter = new AtomeFilter( );
                    atomeFilter.setId( atome.getId( ) );
                    
                    List<File> fileCreate = _fileServices.findByFilter( fileFilter , atomeFilter );
                    java.io.File fileDest = new java.io.File( new java.io.File(
                            AppPropertiesService.getProperty( "plu.docs.path" ) ), fileCreate.get( fileCreate.size( ) - 1 ).getId( ) + "_" + file.getName( ) );
                    FileUtils.writeByteArrayToFile( fileDest, file.getFile( ) );

                    order++;
                }
            }

            i++;
        }

        Date date = new Date( );
        Plu newPlu = _pluServices.findPluWork( );

        if ( newPlu.getDa( ) != null )
        {
            newPlu.setDj( date );
            _pluServices.create( newPlu );
        }

        return JSP_REDIRECT_TO_TREE_PLU_ATOME + "?id_plu=" + plu.getId( ) + "&id_folder=" + folder.getId( )
                + "&atome_all=1";
    }

    /**
     * Generates a Date from a String
     * @param sDate the string date
     * @param sFormat the date format
     * @throws ParseException ParseException
     * @return Date the date converted
     */
    private Date stringToDate( String sDate, String sFormat ) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat( sFormat );

        return (Date) sdf.parse( sDate );
    }

    /**
     * Generates a HTML page that visualizes the files of the atome
     * @param request the Http request
     * @return HTML
     */
    public String getViewAtome( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_VIEW_ATOME );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        if ( plu == null )
        {
            plu = new Plu( );
        }

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        if ( folder == null )
        {
            folder = new Folder( );
        }

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion );

        List<File> fileAll = _fileServices.findAll( );
        List<String> fileAllFormat = _fileServices.findAllMimeType( );

        Plu pluWork = _pluServices.findPluWork( );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PLU, plu );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_PLU_WORK, pluWork );
        model.put( MARK_VERSION, version );
        model.put( MARK_LIST_FILE_ALL, fileAll );
        model.put( MARK_LIST_FILE_ALL_FORMAT, fileAllFormat );
        java.io.File fileDest = new java.io.File( AppPropertiesService.getProperty( "plu.docs.path" ) );
        model.put( "atomeLink", fileDest.toString( ) + "/" );

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

            FileFilter fileFilter = new FileFilter( );
            AtomeFilter atomeFilter = new AtomeFilter( );

            if ( !fileTitle.equals( "" ) )
            {
                fileFilter.setTitle( fileTitle );
            }

            if ( !fileName.equals( "" ) )
            {
                fileFilter.setName( fileName );
            }

            if ( fileFormat != null && !fileFilter.equals( "0" ) )
            {
                fileFilter.setMimeType( fileFormat );
            }

            if ( !atomeName.equals( "" ) )
            {
                atomeFilter.setName( atomeName );
            }

            List<File> fileList = _fileServices.findByFilter( fileFilter, atomeFilter );

            Paginator<File> paginator = new Paginator<File>( (List<File>) fileList, _nItemsPerPage,
                    JSP_REDIRECT_TO_VIEW_ATOME + "?id_plu=" + nIdPlu + "&id_folder=" + nIdFolder + "&id_version="
                            + nIdVersion + "&file_title=" + fileTitle + "&file_name=" + fileName + "&file_type="
                            + fileFormat + "&atome_name=" + atomeName, PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

            model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
            model.put( MARK_PAGINATOR, paginator );
            model.put( MARK_LIST_FILE_LIST, paginator.getPageItems( ) );
            model.put( PARAMETER_FILE_TITLE, fileTitle );
            model.put( PARAMETER_FILE_NAME, fileName );
            model.put( PARAMETER_FILE_FORMAT, fileFormat );
        }
        else
        {
            List<File> fileList = _fileServices.findByVersion( nIdVersion );

            Paginator<File> paginator = new Paginator<File>( (List<File>) fileList, _nItemsPerPage,
                    JSP_REDIRECT_TO_VIEW_ATOME + "?id_plu=" + plu.getId( ) + "&id_folder=" + folder.getId( )
                            + "&id_version=" + version.getId( ), PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

            model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
            model.put( MARK_PAGINATOR, paginator );
            model.put( MARK_LIST_FILE_LIST, paginator.getPageItems( ) );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VIEW_ATOME, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a HTML form for modify an atome
     * @param request the Http request
     * @return HTML
     * @throws IOException IOException
     */
    public String getModifyAtome( HttpServletRequest request ) throws IOException
    {
    	//Clean _listFile
    	this.reinitListFile( request );
    	
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATOME );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion );

        if ( request.getParameter( PARAMETER_ATOME_NUM ) != null )
        {
        	try
        	{
        		version.getAtome( ).setId( Integer.parseInt( request.getParameter( PARAMETER_ATOME_NUM ) ) );
        	}
        	catch( NumberFormatException n )
        	{
        		//If request.getParameter( PARAMETER_ATOME_NUM ) isn't Integer, keep the original id
        	}
        }
        if ( request.getParameter( PARAMETER_ATOME_NAME ) != null )
        {
            version.getAtome( ).setName( request.getParameter( PARAMETER_ATOME_NAME ) );
        }
        if ( request.getParameter( PARAMETER_ATOME_TITLE ) != null )
        {
            version.getAtome( ).setTitle( request.getParameter( PARAMETER_ATOME_TITLE ) );
        }
        if ( request.getParameter( PARAMETER_VERSION_NUM ) != null )
        {
        	try
        	{
        		version.setVersion( Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) ) );
	    	}
	    	catch( NumberFormatException n )
	    	{
	    		//If request.getParameter( PARAMETER_VERSION_NUM ) isn't Integer, keep the original version
	    	}
        }
        if ( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) != null )
        {
            version.getAtome( ).setDescription( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
        }

        Folder folder = _folderServices.findByVersion( nIdVersion );
        if ( request.getParameter( PARAMETER_FOLDER_ID_ATOME ) != null )
        {
            folder.setId( Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID_ATOME ) ) );
        }

        Collection<Folder> folderList = _folderServices.findByPluId( folder.getPlu( ) );

        Plu plu = _pluServices.findByPrimaryKey( folder.getPlu( ) );

        Map<String, Object> model = new HashMap<String, Object>( );
        
        if ( _fileList.isEmpty( ) )
        {
        	setFileList( nIdVersion, model, request, false );
        }

        model.put( MARK_PLU, plu );
        model.put( MARK_VERSION, version );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ) );
        if ( request.getParameter( PARAMETER_REINIT ) != null )
        {
        	model.put( PARAMETER_REINIT, request.getParameter( PARAMETER_REINIT ) );
        }

        if ( request.getParameterValues( PARAMETER_FILE_CHECK ) != null )
        {
        	model.put( PARAMETER_FILE_CHECK, request.getParameterValues( PARAMETER_FILE_CHECK ) );
        }
        
        if ( request instanceof MultipartHttpServletRequest )
        {
        	String ret = addFileToFileList( request );
        	if ( StringUtils.isNotEmpty( ret ) )
        	{
        		return ret;
        	}
        }
    	getFileCheck( request, model );

        if ( !_fileList.isEmpty( ) )
        {
            model.put( MARK_LIST_FILE_LIST, _fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_ATOME, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

	/**
	 * Get file check
	 * @param request HttpServletRequest
	 * @param model model
	 */
	@SuppressWarnings("unchecked")
	private void getFileCheck( HttpServletRequest request,
			Map<String, Object> model )
	{
		List<String> tmp = new ArrayList<String>( );
		// if PARAMETER_FILE_CHECK exists, set tmp
		if ( request.getParameterValues( PARAMETER_FILE_CHECK ) != null )
		{
			tmp = new ArrayList<String>( Arrays.asList( request.getParameterValues( PARAMETER_FILE_CHECK ) ) );
        }
        else
        {
            tmp = new ArrayList<String>( );
        }
		// if a new file is add, add new entry in tmp, else restore tmp
		if ( request.getParameter( "joinFile" ) != null )
		{
			if ( request.getParameter( "joinFile" ).equals( "true" )  )
			{
				tmp.add( Integer.toString( _fileList.size( ) - 1 ) );
			}
		}
		else if ( request.getParameter( PARAMETER_REINIT ) == null )
		{
			tmp = (List<String>) request.getSession( ).getAttribute( PARAMETER_FILE_CHECK );
		}
		if ( request.getParameter( PARAMETER_REINIT ) == null )
		{
			model.put( PARAMETER_FILE_CHECK, tmp );
		}
		request.getSession( ).setAttribute( PARAMETER_FILE_CHECK, tmp );
	}

    /**
     * Set the fileList for an atome version
     * @param nIdVersion version id
     * @throws IOException IOException
     */
	private void setFileList( int nIdVersion, Map<String, Object> model, HttpServletRequest request, boolean createWithOld ) throws IOException
	{
		List<String> checkFileList = new ArrayList<String>( );		
		List<File> listFile = _fileServices.findByVersion( nIdVersion );
		
		for ( File file : listFile )
		{
			if ( createWithOld )
			{
            	java.io.File fileDest = new java.io.File( new java.io.File(
                        AppPropertiesService.getProperty( "plu.docs.path" ) ), file.getId( ) + "_" + file.getName( ) );
				file.setFile( FileUtils.readFileToByteArray( fileDest ) );
			}
			_fileList.add( file );
			checkFileList.add( Integer.toString( _fileList.size( ) - 1 ) );
		}
		model.put( PARAMETER_FILE_CHECK, checkFileList );
		request.getSession( ).setAttribute( PARAMETER_FILE_CHECK, checkFileList );
	}

    /**
     * Generates a message of confirmation of cancel for modify atome
     * @param request the Http request
     * @return message
     */
    public String getConfirmCancelModifyAtome( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );

        UrlItem url = new UrlItem( JSP_TREE_PLU_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );

        Object[] args = { atomeName, atomeTitle };

        return this.getMessageJsp( request, MESSAGE_CONFIRM_CANCEL_MODIFY_ATOME, args, JSP_MODIFY_ATOME, url.getUrl( ) ); 
    }

    /**
     * Generates a message of confirmation for modify atome
     * @param request the Http request
     * @return message
     */
    public String getConfirmModifyAtome( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_ID_ATOME ).equals( "" )
                || request.getParameter( PARAMETER_ATOME_NUM ).equals( "" )
                || request.getParameter( PARAMETER_VERSION_NUM ).equals( "" )
                || request.getParameter( PARAMETER_ATOME_NAME ).equals( "" )
                || request.getParameter( PARAMETER_ATOME_TITLE ).equals( "" )
                || request.getParameter( PARAMETER_ATOME_DESCRIPTION ).equals( "" )
                || request.getParameterValues( PARAMETER_FILE_CHECK ) == null )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_REQUIRED_FIELD, null, JSP_MODIFY_ATOME, null );
        }
        
        int nIdAtome = 0;
        int numVersion = 0;
        try
        {
            nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_NUM ) );
        }
        catch ( NumberFormatException e )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_ATOME_ID_NUMBER, null, JSP_MODIFY_ATOME, null );
        }
        try
        {
            numVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) );
        }
        catch ( NumberFormatException e )
        {
			return this.getMessageJsp( request, MESSAGE_ERROR_VERSION_NUMBER, null, JSP_MODIFY_ATOME, null );
        }
        
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID_ATOME ) );
        int nIdAtomeOld = Integer.parseInt( request.getParameter( PARAMETER_ATOME_OLD_ID ) );
        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
        String atomeDescription = request.getParameter( PARAMETER_ATOME_DESCRIPTION );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE_ATOME );

        UrlItem url = new UrlItem( JSP_DO_MODIFY_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID_ATOME, nIdFolder );
        url.addParameter( PARAMETER_ATOME_NUM, nIdAtome );
        url.addParameter( PARAMETER_ATOME_OLD_ID, nIdAtomeOld );
        url.addParameter( PARAMETER_VERSION_ID, nIdVersion );
        url.addParameter( PARAMETER_VERSION_NUM, numVersion );
        try
        {
	        url.addParameter( PARAMETER_ATOME_NAME, URIUtil.encodeAll( atomeName ) );
	        url.addParameter( PARAMETER_ATOME_TITLE, URIUtil.encodeAll( atomeTitle ) );
	        url.addParameter( PARAMETER_ATOME_DESCRIPTION, URIUtil.encodeAll( atomeDescription ) );
	    }
	    catch ( URIException e )
	    {
			throw new AppException( "An error occured while parsing request parameters" );
		}

        for ( int j = 0; j < check.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_CHECK, check[j] );
        }

        for ( int j = 0; j < fileTitle.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_TITLE_ATOME, fileTitle[j] );
        }

        // Check atome's file
        String confirmFile = "";
        confirmFile = getConfirmAtomeFile( request, numVersion, atomeName, atomeTitle, check,
				fileTitle, JSP_MODIFY_ATOME );
        if ( StringUtils.isNotEmpty( confirmFile ) )
        {
        	return confirmFile;
        }

        Object[] args = { atomeName, atomeTitle };


        return this.getMessageJsp( request, MESSAGE_CONFIRM_MODIFY_ATOME, args,
                JSP_MODIFY_ATOME, url.getUrl( ) );
    }

    /**
     * Modify an atome and return to the atome manage
     * @param request the Http request
     * @throws ParseException ParseException
     * @return HTML
     * @throws IOException IOException
     */
    public String doModifyAtome( HttpServletRequest request ) throws ParseException, IOException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID_ATOME ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_NUM ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
        String atomeDescription = request.getParameter( PARAMETER_ATOME_DESCRIPTION );

        int nIdAtomeOld = Integer.parseInt( request.getParameter( PARAMETER_ATOME_OLD_ID ) );
        Atome atomeOld = _atomeServices.findByPrimaryKey( nIdAtomeOld );
        _atomeServices.remove( atomeOld );

        Atome atome = new Atome( );
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

        FolderVersion folderVersion = _folderVersionServices.findByMaxFolderAndVersion( version );
        version = _versionServices.findByPrimaryKey( nIdVersion );

        folderVersion.setVersion( version );
        folderVersion.setFolder( folder );
        _folderVersionServices.update( folderVersion );

        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE_ATOME );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        int i = 0;
        int order = 1;
        String strNumVersion;

        if ( numVersion < 10 )
        {
            strNumVersion = "-V0" + numVersion;
        }
        else
        {
            strNumVersion = "-V" + numVersion;
        }

        boolean toDelete = true;
        
        for ( File file : _fileList )
        {
            for ( int j = 0; j < check.length; ++j )
            {
                int c = Integer.parseInt( check[j] );

                if ( c == i )
                {
                	toDelete = false;
                	
                    updateFile( nIdAtome, atome, version, fileTitle, i, order,
 strNumVersion, file, nIdVersion );

                    order++;
                }
            }
            if ( toDelete )
            {
            	java.io.File fileDest = new java.io.File( new java.io.File(
                        AppPropertiesService.getProperty( "plu.docs.path" ) ), file.getId( ) + "_" + file.getName( ) );
                if ( fileDest.exists( ) )
                {
                	if ( !fileDest.delete( ) )
                	{
                		throw new AppException( "An error occured when trying to delete file" );
                	}
                }
                if ( file.getId( ) != 0 )
                {
                	_fileServices.remove( file );
                }
            }
            else
            {
            	toDelete = true;
            }

            i++;
        }

        return JSP_REDIRECT_TO_TREE_PLU_ATOME + "?id_plu=" + plu.getId( ) + "&id_folder=" + folder.getId( ) + "&atome_all=1";
    }

	/**
	 * @param nIdAtome nIdAtome
	 * @param atome atome
	 * @param version version
	 * @param fileTitle fileTitle
	 * @param i i 
	 * @param order order
	 * @param strNumVersion strNumVersion
	 * @param file file
	 * @throws IOException IOException
	 */
    private void updateFile( int nIdAtome, Atome atome, Version version, String[] fileTitle, int i, int order,
            String strNumVersion, File file, int nIdVersion ) throws IOException
	{

        List<File> oldFileList = new ArrayList<File>( );
        oldFileList = _fileServices.findByVersion( nIdVersion );

		if ( !file.getTitle( ).equals( fileTitle[i] ) )
		{
		    file.setTitle( fileTitle[i] );
		}

		file.setAtome( nIdAtome );
		file.setOrder( order );
		file.setVersion( version.getId( ) );
		
		// Test if file name contains extension, if yes remove extension to file name
        int a = file.getName( ).lastIndexOf( "." );
        String nameWithoutExt = file.getName( );
        String nameExt = "";
		if ( a > 0 )
		{
            nameWithoutExt = file.getName( ).substring( 0, a );
            nameExt = file.getName( ).substring( a );
		}
		
        a = nameWithoutExt.lastIndexOf( "-V" );
		if ( a > 0 )
		{
            if ( !oldFileList.contains( file ) )
            {
                nameWithoutExt = nameWithoutExt.substring( 0, a ) + strNumVersion;
            }
            else
            {
                String strNumOldVersion = PluUtils.getFileVersion( nameWithoutExt );
                nameWithoutExt = nameWithoutExt.substring( 0, a ) + strNumOldVersion;
            }

		}
		else
		{
            nameWithoutExt = nameWithoutExt + strNumVersion;
		}

        file.setName( nameWithoutExt + nameExt );
		
		if ( file.getId( ) != 0 )
		{
			_fileServices.update( file );
		}
		else
		{
			_fileServices.create( file );
		    FileFilter fileFilter = new FileFilter( );
		    fileFilter.setName( file.getName( ) );
		    fileFilter.setTitle( file.getTitle( ) );
		    AtomeFilter atomeFilter = new AtomeFilter( );
		    atomeFilter.setId( atome.getId( ) );
		    
		    List<File> fileCreate = _fileServices.findByFilter( fileFilter , atomeFilter );
		    file.setId( fileCreate.get( fileCreate.size( ) -1 ).getId( ) );
		}
		                    
		java.io.File fileDest = new java.io.File( new java.io.File(
		        AppPropertiesService.getProperty( "plu.docs.path" ) ), file.getId( ) + "_" + file.getName( ) );

		if ( !fileDest.exists( ) )
		{
            System.out.println( "Fichier pas existant : " + fileDest.getAbsolutePath( ) );
            FileUtils.writeByteArrayToFile( fileDest, file.getFile( ) );
		}
	}

    /**
     * Generates a HTML form for correct an atome
     * @param request the Http request
     * @return HTML
     * @throws IOException IOException
     */
    public String getCorrectAtome( HttpServletRequest request ) throws IOException
    {
    	//Clean _listFile
    	this.reinitListFile( request );
    	
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CORRECT_ATOME );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion );
        
        Map<String, Object> model = new HashMap<String, Object>( );
        
        // Restore correct description and atome title
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_HISTORY_DESCRIPTION ) ) )
        {
        	model.put( PARAMETER_HISTORY_DESCRIPTION, request.getParameter( PARAMETER_HISTORY_DESCRIPTION ) );
        }
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_ATOME_TITLE ) ) )
        {
        	version.getAtome( ).setTitle( request.getParameter( PARAMETER_ATOME_TITLE ) );
        }
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) ) )
        {
        	version.getAtome( ).setDescription( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
        }

        Folder folder = _folderServices.findByVersion( nIdVersion );
        
        Plu plu = _pluServices.findByPrimaryKey( folder.getPlu( ) );
        
        if ( _fileList.isEmpty( ) )
        {
        	setFileList( nIdVersion, model, request, false );
        }

        model.put( MARK_PLU, plu );
        model.put( MARK_VERSION, version );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ) );
        if ( request.getParameter( PARAMETER_REINIT ) != null )
        {
        	model.put( PARAMETER_REINIT, request.getParameter( PARAMETER_REINIT ) );
        }

        if ( request.getParameterValues( PARAMETER_FILE_CHECK ) != null )
        {
        	model.put( PARAMETER_FILE_CHECK, request.getParameterValues( PARAMETER_FILE_CHECK ) );
        }
        
        if ( request instanceof MultipartHttpServletRequest )
        {
        	String ret = addFileToFileList( request );
        	if ( StringUtils.isNotEmpty( ret ) )
        	{
        		return ret;
        	}
        }
    	getFileCheck( request, model );

        if ( !_fileList.isEmpty( ) )
        {
            model.put( MARK_LIST_FILE_LIST, _fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CORRECT_ATOME, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a message of confirmation of cancel for correct atome
     * @param request the Http request
     * @return message
     */
    public String getConfirmCancelCorrectAtome( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );

        UrlItem url = new UrlItem( JSP_TREE_PLU_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );

        Object[] args = { atomeName, atomeTitle };

        return this.getMessageJsp( request, MESSAGE_CONFIRM_CANCEL_CORRECT_ATOME, args, JSP_CORRECT_ATOME, url.getUrl( ) );
    }

    /**
     * Generates a message of confirmation for correct atome
     * @param request the Http request
     * @return message
     */
    public String getConfirmCorrectAtome( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_ATOME_TITLE ).equals( "" )
                || request.getParameter( PARAMETER_ATOME_DESCRIPTION ).equals( "" )
                || request.getParameter( PARAMETER_HISTORY_DESCRIPTION ).equals( "" )
                || request.getParameterValues( PARAMETER_FILE_CHECK ) == null )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_REQUIRED_FIELD, null, JSP_CORRECT_ATOME, null );
        }

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        int numVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_NUM ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );
        String atomeDescription = request.getParameter( PARAMETER_ATOME_DESCRIPTION );
        String strDescription = request.getParameter( PARAMETER_HISTORY_DESCRIPTION );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE_ATOME );

        UrlItem url = new UrlItem( JSP_DO_CORRECT_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        url.addParameter( PARAMETER_ATOME_ID, nIdAtome );
        url.addParameter( PARAMETER_VERSION_ID, nIdVersion );
        try
        {
        	url.addParameter( PARAMETER_ATOME_TITLE, URIUtil.encodeAll( atomeTitle ) );
	        url.addParameter( PARAMETER_ATOME_DESCRIPTION, URIUtil.encodeAll( atomeDescription ) );
	        url.addParameter( PARAMETER_HISTORY_DESCRIPTION, URIUtil.encodeAll( strDescription ) );
	    }
	    catch ( URIException e )
	    {
			throw new AppException( "An error occured while parsing request parameters" );
		}

        for ( int j = 0; j < check.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_CHECK, check[j] );
        }

        for ( int j = 0; j < fileTitle.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_TITLE_ATOME, fileTitle[j] );
        }

        // Check atome's file
        String confirmFile = "";
        confirmFile = getConfirmAtomeFile( request, numVersion, atomeName, atomeTitle, check,
				fileTitle, JSP_CORRECT_ATOME );
        if ( StringUtils.isNotEmpty( confirmFile ) )
        {
        	return confirmFile;
        }

        Object[] args = { atomeName, atomeTitle };

        return this.getMessageJsp( request, MESSAGE_CONFIRM_CORRECT_ATOME, args, JSP_CORRECT_ATOME, url.getUrl( ) );
    }

    /**
     * Correct an atome and return to the atome manage
     * @param request the Http request
     * @throws ParseException ParseException
     * @return HTML
     * @throws IOException IOException
     */
    public String doCorrectAtome( HttpServletRequest request ) throws ParseException, IOException
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

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion );

        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE_ATOME );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        int i = 0;
        int order = 1;
        String strNumVersion;

        if ( version.getVersion( ) < 10 )
        {
            strNumVersion = "-V0" + version.getVersion( );
        }
        else
        {
            strNumVersion = "-V" + version.getVersion( );
        }

        boolean toDelete = true;
        
        for ( File file : _fileList )
        {
            for ( int j = 0; j < check.length; ++j )
            {
                int c = Integer.parseInt( check[j] );

                if ( c == i )
                {
                	toDelete = false;
                	
                    updateFile( nIdAtome, atome, version, fileTitle, i, order, strNumVersion, file, nIdVersion );
                    
                    order++;
                }
            }
            if ( toDelete )
            {
            	java.io.File fileDest = new java.io.File( new java.io.File(
                        AppPropertiesService.getProperty( "plu.docs.path" ) ), file.getId( ) + "_" + file.getName( ) );
                if ( fileDest.exists( ) )
                {
                	if ( !fileDest.delete( ) )
                	{
                		throw new AppException( "An error occured when trying to delete file" );
                	}
                }
                if ( file.getId( ) != 0 )
                {
                	_fileServices.remove( file );
                }
            }
            else
            {
            	toDelete = true;
            }

            i++;
        }

        History history = new History( );
        history.setPlu( folder.getPlu( ) );
        history.setFolder( nIdFolder );
        history.setAtome( nIdAtome );

        Date date = new Date( );
        history.setDc( date );
        history.setDescription( request.getParameter( PARAMETER_HISTORY_DESCRIPTION ) );
        _historyServices.create( history );

        Plu plu = _pluServices.findByPrimaryKey( folder.getPlu( ) );
        if ( plu.getDa( ) != null )
        {
            Etat etat = _etatServices.findByPrimaryKey( 5 );
            plu.setEtat( etat );
            _pluServices.update( plu );
        }

        return JSP_REDIRECT_TO_TREE_PLU_ATOME + "?id_plu=" + folder.getPlu( ) + "&id_folder=" + folder.getId( ) + "&atome_all=1";
    }

    /**
     * Generates a HTML form for evolve an atome
     * @param request the Http request
     * @return HTML
     * @throws IOException IOException
     */
    public String getEvolveAtome( HttpServletRequest request ) throws IOException
    {
    	//Clean _listFile
    	this.reinitListFile( request );
    	
        setPageTitleProperty( PROPERTY_PAGE_TITLE_EVOLVE_ATOME );

        Plu plu = _pluServices.findPluWork( );

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        if ( folder == null )
        {
            folder = new Folder( );
        }

        Collection<Folder> folderList = _folderServices.findByPluId( plu.getId( ) );

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion );

        if ( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) != null )
        {
            version.getAtome( ).setDescription( request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
        }

        Map<String, Object> model = new HashMap<String, Object>( );
        
        if ( _fileList.isEmpty( ) )
        {
            setFileList( nIdVersion, model, request, true );
        }

        model.put( MARK_PLU, plu );
        model.put( MARK_VERSION, version );
        model.put( MARK_FOLDER, folder );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ) );
        if ( request.getParameter( PARAMETER_VERSION_NUM ) != null )
        {
            model.put( MARK_NEW_VERSION, request.getParameter( PARAMETER_VERSION_NUM ) );
        }
        else
        {
            model.put( MARK_NEW_VERSION, version.getVersion( ) + 1 );
        }
        if ( request.getParameter( PARAMETER_REINIT ) != null )
        {
        	model.put( PARAMETER_REINIT, request.getParameter( PARAMETER_REINIT ) );
        }

        if ( request.getParameterValues( PARAMETER_FILE_CHECK ) != null )
        {
        	model.put( PARAMETER_FILE_CHECK, request.getParameterValues( PARAMETER_FILE_CHECK ) );
        }
        
        if ( request instanceof MultipartHttpServletRequest )
        {
        	String ret = addFileToFileList( request );
        	if ( StringUtils.isNotEmpty( ret ) )
        	{
        		return ret;
        	}
        }
    	getFileCheck( request, model );

        if ( !_fileList.isEmpty( ) )
        {
            model.put( MARK_LIST_FILE_LIST, _fileList );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EVOLVE_ATOME, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a message of confirmation of cancel for evolve atome
     * @param request the Http request
     * @return message
     */
    public String getConfirmCancelEvolveAtome( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        String atomeName = request.getParameter( PARAMETER_ATOME_NAME );
        String atomeTitle = request.getParameter( PARAMETER_ATOME_TITLE );

        UrlItem url = new UrlItem( JSP_TREE_PLU_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );

        Object[] args = { atomeName, atomeTitle };

        return this.getMessageJsp( request, MESSAGE_CONFIRM_CANCEL_EVOLVE_ATOME, args, JSP_EVOLVE_ATOME, url.getUrl( ) );
    }

    /**
     * Generates a message of confirmation for evolve atome
     * @param request the Http request
     * @return message
     */
    public String getConfirmEvolveAtome( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_FOLDER_ID ).equals( "" )
                || request.getParameter( PARAMETER_VERSION_NUM ).equals( "" ) 
                || request.getParameterValues( PARAMETER_FILE_CHECK ) == null )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_REQUIRED_FIELD, null,
                    JSP_EVOLVE_ATOME, null );
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
        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE_ATOME );

        UrlItem url = new UrlItem( JSP_DO_EVOLVE_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        url.addParameter( PARAMETER_ATOME_ID, nIdAtome );
        url.addParameter( PARAMETER_VERSION_ID, nIdVersion );
        url.addParameter( PARAMETER_VERSION_NUM, numVersion );

        Object[] argsVersion = { atomeName, atomeTitle, numVersion, numVersionOld };

        if ( numVersion < numVersionOld )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_ATOME_CREATE_NUM_VERSION_SUP, argsVersion,
            JSP_EVOLVE_ATOME, null );
        }
        
        // Return error message if an atome allready exists on this version
        AtomeFilter atomeFilter = new AtomeFilter( );
        atomeFilter.setName( atomeName );
        atomeFilter.setTitle( atomeTitle );
        atomeFilter.setId( nIdAtome );
        VersionFilter versionFilter = new VersionFilter( );
        versionFilter.setVersion( numVersion );
        if ( !_versionServices.findByFilter( atomeFilter, versionFilter ).isEmpty( ) )
        {
            return this.getMessageJsp( request, MESSAGE_ERROR_ATOME_CREATE_NUM_VERSION_EXISTS, argsVersion,
                    JSP_EVOLVE_ATOME, null );        	
        }

        for ( int j = 0; j < check.length; ++j )
        {
            url.addParameter( PARAMETER_FILE_CHECK, check[j] );
        }

        for ( int j = 0; j < fileTitle.length; ++j )
        {
            try
            {
        		url.addParameter( PARAMETER_FILE_TITLE_ATOME, URIUtil.encodeAll( fileTitle[j] ) );
		    }
		    catch ( URIException e )
		    {
				throw new AppException( "An error occured while parsing request parameters" );
			}
        }

        // Check atome's file
        String confirmFile = "";
        confirmFile = getConfirmAtomeFile( request, numVersion, atomeName, atomeTitle, check,
				fileTitle, JSP_EVOLVE_ATOME );
        if ( StringUtils.isNotEmpty( confirmFile ) )
        {
        	return confirmFile;
        }

        Object[] args = { atomeName, atomeTitle, numVersionOld, numVersion };


        return this.getMessageJsp( request, MESSAGE_CONFIRM_EVOLVE_ATOME, args,
                JSP_EVOLVE_ATOME, url.getUrl( ) );
    }

    /**
     * Evolve an atome and return to the atome manage
     * @param request the Http request
     * @throws ParseException ParseException
     * @return HTML
     * @throws IOException IOException
     */
    public String doEvolveAtome( HttpServletRequest request ) throws ParseException, IOException
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

        Date d3 = stringToDate( "01/01/0001", "dd/MM/yyyy" );
        versionOld.setD3( d3 );
        versionOld.setArchive( 'O' );
        _versionServices.update( versionOld );

        FolderVersion folderVersion = _folderVersionServices.findByMaxFolderAndVersion( versionOld );
        _folderVersionServices.remove( folderVersion );

        Version version = new Version( );
        version.setAtome( atome );
        version.setVersion( numVersion );
        _versionServices.create( version );

        version = _versionServices.findByAtomeAndNumVersion( nIdAtome, numVersion );

        folderVersion = new FolderVersion( );
        folderVersion.setVersion( version );
        folderVersion.setFolder( folder );
        _folderVersionServices.create( folderVersion );

        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE_ATOME );
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        int i = 0;
        int order = 1;
        String strNumVersion;

        if ( version.getVersion( ) < 10 )
        {
            strNumVersion = "-V0" + version.getVersion( );
        }
        else
        {
            strNumVersion = "-V" + version.getVersion( );
        }

        boolean toDelete = true;
        
        for ( File file : _fileList )
        {
            for ( int j = 0; j < check.length; ++j )
            {
                int c = Integer.parseInt( check[j] );

                if ( c == i )
                {
                	toDelete = false;

                    file.setId( 0 );
                    updateFile( nIdAtome, atome, version, fileTitle, i, order, strNumVersion, file, nIdVersion );

                    order++;
                }
            }
            if ( toDelete )
            {
            	java.io.File fileDest = new java.io.File( new java.io.File(
                        AppPropertiesService.getProperty( "plu.docs.path" ) ), file.getId( ) + "_" + file.getName( ) );
                if ( fileDest.exists( ) )
                {
                	if ( !fileDest.delete( ) )
                	{
                		throw new AppException( "An error occured when trying to delete file" );
                	}
                }
                if ( file.getId( ) != 0 )
                {
                	_fileServices.remove( file );
                }
            }
            else
            {
            	toDelete = true;
            }
            i++;
        }

        return JSP_REDIRECT_TO_TREE_PLU_ATOME + "?id_plu=" + plu.getId( ) + "&id_folder=" + folder.getId( ) + "&atome_all=1";
    }

    /**
     * Generates a message of confirmation for archivage atome
     * @param request the Http request
     * @return message
     */
    public String getConfirmArchiveAtome( HttpServletRequest request ) // throws
                                                                       // AccessDeniedException
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );

        UrlItem url = new UrlItem( JSP_DO_ARCHIVE_ATOME );
        url.addParameter( PARAMETER_PLU_ID, nIdPlu );
        url.addParameter( PARAMETER_FOLDER_ID, nIdFolder );
        url.addParameter( PARAMETER_VERSION_ID, nIdVersion );

        Version version = _versionServices.findByPrimaryKey( nIdVersion );

        Object[] args = { version.getAtome( ).getName( ), version.getAtome( ).getTitle( ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_ARCHIVE_ATOME, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Archive an atome and return to the atome manage
     * @param request the Http request
     * @return HTML
     */
    public String doArchiveAtome( HttpServletRequest request )
    {
        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        if ( plu == null )
        {
            plu = new Plu( );
        }

        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
        Folder folder = _folderServices.findByPrimaryKey( nIdFolder );

        if ( folder == null )
        {
            folder = new Folder( );
        }

        int nIdVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_ID ) );
        Version version = _versionServices.findByPrimaryKey( nIdVersion );

        version.setArchive( 'O' );
        _versionServices.update( version );

        FolderVersion folderVersion = _folderVersionServices.findByMaxFolderAndVersion( version );
        _folderVersionServices.remove( folderVersion );

        return JSP_REDIRECT_TO_TREE_PLU_ATOME + "?id_plu=" + plu.getId( ) + "&id_folder=" + folder.getId( );
    }


    /**
     * @param request the request
     * @return the confirmation
     */
    public String getConfirmUploadAtome( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_DO_UPLOAD_ATOME );

        Object[] args = { request.getParameter( PARAMETER_ATOME_TITLE ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_UPLOAD_ATOME, args, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * @param request the request
     * @return the jsp
     */
    public String doUploadAtome( HttpServletRequest request )
    {
        return JSP_REDIRECT_TO_TREE_PLU_ATOME;
    }

    /**
     * Generates a HTML form for join of files to an atome
     * @param request the Http request
     * @return HTML
     */
    public String getJoinFile( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_JOIN_FILE );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        String page = request.getParameter( "page" );
        
        String[] check = request.getParameterValues( PARAMETER_FILE_CHECK );
        String[] fileTitle = request.getParameterValues( PARAMETER_FILE_TITLE_ATOME );
        int i = 0;
        
        // Save file title
        if ( check != null && fileTitle != null )
        {        
	        for ( File file : _fileList )
	        {
	            for ( int j = 0; j < check.length; ++j )
	            {
	                int c = Integer.parseInt( check[j] );
	
	                if ( c == i )
	                {
	                    file.setTitle( fileTitle[i] );
	                }
	            }
	            i++;
	        }
        }

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( "page", page );
        model.put( MARK_PLU, plu );
        if ( page.equals( "atome/CreateAtome" ) || page.equals( "atome/CreateAtomeWithOld" )
                || page.equals( "atome/ModifyAtome" ) || page.equals( "atome/EvolveAtome" )
                || page.equals( "atome/CorrectAtome" ) )
        {
            model.put( PARAMETER_FOLDER_ID_ATOME, request.getParameter( PARAMETER_FOLDER_ID_ATOME ) );
            model.put( PARAMETER_FOLDER_ID, request.getParameter( PARAMETER_FOLDER_ID ) );
            model.put( PARAMETER_ATOME_NAME, request.getParameter( PARAMETER_ATOME_NAME ) );
            model.put( PARAMETER_ATOME_TITLE, request.getParameter( PARAMETER_ATOME_TITLE ) );
            model.put( PARAMETER_VERSION_NUM, request.getParameter( PARAMETER_VERSION_NUM ) );
            model.put( PARAMETER_VERSION_NUM_OLD, request.getParameter( PARAMETER_VERSION_NUM_OLD ) );
            model.put( PARAMETER_ATOME_DESCRIPTION, request.getParameter( PARAMETER_ATOME_DESCRIPTION ) );
            model.put( PARAMETER_ATOME_NUM, request.getParameter( PARAMETER_ATOME_NUM ) );
            model.put( PARAMETER_ATOME_ID, request.getParameter( PARAMETER_ATOME_ID ) );
            model.put( PARAMETER_VERSION_ID, request.getParameter( PARAMETER_VERSION_ID ) );
            model.put( PARAMETER_FILE_TITLE, request.getParameter( PARAMETER_FILE_TITLE ) );
            model.put( PARAMETER_FILE_TITLE_ATOME, request.getParameter( PARAMETER_FILE_TITLE_ATOME ) );
            model.put( PARAMETER_FILE_NAME, request.getParameter( PARAMETER_FILE_NAME ) );
            model.put( PARAMETER_FILE, request.getParameter( PARAMETER_FILE ) );
        	model.put( PARAMETER_FILE_CHECK, request.getParameterValues( PARAMETER_FILE_CHECK ) );
        	model.put( PARAMETER_HISTORY_DESCRIPTION, request.getParameter( PARAMETER_HISTORY_DESCRIPTION ) );
        }

        if ( request.getParameter( PARAMETER_FOLDER_ID ) != null )
        {
            int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID ) );
            Folder folder;

            if ( nIdFolder != 0 )
            {
                folder = _folderServices.findByPrimaryKey( nIdFolder );
            }
            else
            {
                folder = new Folder( );
            }

            model.put( MARK_FOLDER, folder );
        }

        if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_ATOME_ID ) ) && !page.equals( "atome/CreateAtome" )
                && !page.equals( "atome/ModifyAtome" ) && !page.equals( "atome/CreateAtomeWithOld" ) )
        {
            int nIdAtome = Integer.parseInt( request.getParameter( PARAMETER_ATOME_ID ) );
            Atome atome = _atomeServices.findByPrimaryKey( nIdAtome );
            int maxVersion = _versionServices.findMaxVersion( nIdAtome );
            Version version = _versionServices.findByAtomeAndNumVersion( nIdAtome, maxVersion );
            model.put( MARK_ATOME, atome );
            model.put( MARK_VERSION, version );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_JOIN_FILE, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a HTML form for create a new html page
     * @param request the Http request
     * @return HTML
     */
    public String getCreateHtml( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_HTML );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        String page = request.getParameter( "page" );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( "page", page );
        model.put( MARK_PLU, plu );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ) );
        model.put( PARAMETER_FOLDER_PARENT_ID, request.getParameter( PARAMETER_FOLDER_PARENT_ID ) );
        model.put( PARAMETER_FOLDER_TITLE, request.getParameter( PARAMETER_FOLDER_TITLE ) );
        model.put( PARAMETER_FOLDER_IMAGE, request.getParameter( PARAMETER_FOLDER_IMAGE ) );
        model.put( PARAMETER_FOLDER_DESCRIPTION, request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        model.put( PARAMETER_FOLDER_ID, request.getParameter( PARAMETER_FOLDER_ID ) );
        model.put( PARAMETER_FOLDER_HTML_UTILISATION, request.getParameter( PARAMETER_FOLDER_HTML_UTILISATION ) );

        if ( request.getParameter( PARAMETER_FOLDER_ID_RETURN ) != null )
        {
            int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID_RETURN ) );
            Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
            model.put( MARK_FOLDER, folder );
        }

        if ( _folderHtml != null && !request.getParameter( "action" ).equals( "Cr�er" ) )
        {
            model.put( MARK_HTML, _folderHtml );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_HTML, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a HTML form for import a html page
     * @param request the Http request
     * @return HTML
     */
    public String getImportHtml( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_HTML );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        String page = request.getParameter( "page" );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( "page", page );
        model.put( MARK_PLU, plu );
        model.put( PARAMETER_FOLDER_PARENT_ID, request.getParameter( PARAMETER_FOLDER_PARENT_ID ) );
        model.put( PARAMETER_FOLDER_TITLE, request.getParameter( PARAMETER_FOLDER_TITLE ) );
        model.put( PARAMETER_FOLDER_IMAGE, request.getParameter( PARAMETER_FOLDER_IMAGE ) );
        model.put( PARAMETER_FOLDER_DESCRIPTION, request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        model.put( PARAMETER_FOLDER_ID, request.getParameter( PARAMETER_FOLDER_ID ) );
        model.put( PARAMETER_FOLDER_HTML_UTILISATION, request.getParameter( PARAMETER_FOLDER_HTML_UTILISATION ) );

        if ( request.getParameter( PARAMETER_FOLDER_ID_RETURN ) != null )
        {
            int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID_RETURN ) );
            Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
            model.put( MARK_FOLDER, folder );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_IMPORT_HTML, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Generates a HTML form for duplicate a html page
     * @param request the Http request
     * @return HTML
     */
    public String getDuplicateHtml( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_HTML );

        int nIdPlu = Integer.parseInt( request.getParameter( PARAMETER_PLU_ID ) );
        Plu plu = _pluServices.findByPrimaryKey( nIdPlu );

        List<Folder> folderList = _folderServices.findByPluId( nIdPlu );
        String page = request.getParameter( "page" );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( "page", page );
        model.put( MARK_PLU, plu );
        model.put( MARK_LIST_FOLDER_LIST, folderList );
        model.put( PARAMETER_FOLDER_PARENT_ID, request.getParameter( PARAMETER_FOLDER_PARENT_ID ) );
        model.put( PARAMETER_FOLDER_TITLE, request.getParameter( PARAMETER_FOLDER_TITLE ) );
        model.put( PARAMETER_FOLDER_IMAGE, request.getParameter( PARAMETER_FOLDER_IMAGE ) );
        model.put( PARAMETER_FOLDER_DESCRIPTION, request.getParameter( PARAMETER_FOLDER_DESCRIPTION ) );
        model.put( PARAMETER_FOLDER_ID, request.getParameter( PARAMETER_FOLDER_ID ) );
        model.put( PARAMETER_FOLDER_HTML_UTILISATION, request.getParameter( PARAMETER_FOLDER_HTML_UTILISATION ) );

        if ( request.getParameter( PARAMETER_FOLDER_ID_RETURN ) != null )
        {
            int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_FOLDER_ID_RETURN ) );
            Folder folder = _folderServices.findByPrimaryKey( nIdFolder );
            model.put( MARK_FOLDER, folder );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DUPLICATE_HTML, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }


    /**
     * @param request the request
     * @return the message
     */
    public String getMessage( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( "LISTE_NOM_CHAMPS", LISTE_NOM_CHAMP );

        Map<String, Object> listeChamps = new HashMap<String, Object>( LISTE_NOM_CHAMP.length );
        for ( String nomChamp : LISTE_NOM_CHAMP )
        {
            listeChamps.put( nomChamp, request.getParameter( nomChamp ) );
        }
        model.put( "listeChamps", listeChamps );
        
        String strText = "";
        if ( request.getSession( ).getAttribute( MARK_ERROR_ARGS ) != null )
        {
            strText = I18nService.getLocalizedString( request.getSession( ).getAttribute( MARK_ERROR_MESSAGE ).toString( ), 
            		(Object[]) request.getSession( ).getAttribute( MARK_ERROR_ARGS ) , request.getLocale( ) );
        }
        else
        {
            strText = I18nService.getLocalizedString( request.getSession( ).getAttribute( MARK_ERROR_MESSAGE ).toString( ), 
           		 request.getLocale( ) );
        }
        
        if ( request.getSession( ).getAttribute( MARK_PAGE_TARGET ) != null )
        {
        	model.put( MARK_PAGE_TARGET, AppPathService.getBaseUrl( request ) + request.getSession( ).getAttribute( MARK_PAGE_TARGET ) );
            request.getSession( ).removeAttribute( MARK_PAGE_TARGET );
        }
        
        
        model.put( MARK_ERROR_MESSAGE, strText );

        request.getSession( ).removeAttribute( MARK_ERROR_ARGS );
        request.getSession( ).removeAttribute( MARK_ERROR_MESSAGE );
        model.put( MARK_PAGE_RETURN, request.getSession( ).getAttribute( MARK_PAGE_RETURN ) );
        request.getSession( ).removeAttribute( MARK_PAGE_RETURN );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MESSAGE, getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * @param request the request
     * @param errorMessage the error message
     * @param args the args
     * @param pageReturn the page return
     * @param pageTarget the page target
     * @return the message jsp
     * @throws UnsupportedEncodingException
     */
    public String getMessageJsp( HttpServletRequest request, String errorMessage, Object[] args, String pageReturn,
            String pageTarget )
    {
        request.getSession( ).setAttribute( MARK_ERROR_MESSAGE, errorMessage );
        request.getSession( ).setAttribute( MARK_ERROR_ARGS, args );
        request.getSession( ).setAttribute( MARK_PAGE_RETURN, pageReturn );
        
        if ( request.getParameterValues( PARAMETER_FILE_CHECK ) != null )
        {
        	request.getSession( ).setAttribute( PARAMETER_FILE_CHECK, new ArrayList<String>( Arrays.asList( request.getParameterValues( PARAMETER_FILE_CHECK ) ) ) );
        }
        
        if ( pageTarget != null )
        {
            request.getSession( ).setAttribute( MARK_PAGE_TARGET, pageTarget );
        }

        StringBuilder parameters = new StringBuilder( "?" );
        boolean first = true;

        @SuppressWarnings( "unchecked" )
		Enumeration<String> en = request.getParameterNames( );
        while ( en.hasMoreElements( ) )
        {
            if ( !first )
            {
            	parameters.append( "&" );
            }
            first = false;
            String pName = (String) en.nextElement( );
            String paramValue = request.getParameter( pName );
            try
            {
                parameters.append( pName ).append( "=" ).append( StringUtil.URLEnc( paramValue, "UTF-8" ) );
            }
            catch ( UnsupportedEncodingException e )
            {
                throw new AppException( "Probl�me lors de l'encoding.", e );
            }
        }

        return AppPathService.getBaseUrl( request ) + JSP_MESSAGE + parameters;
    }
    
    /**
     * Clean _fileList, _folderHtml and _folderImage
     * @param request HttpServletRequest
     */
    public void reinitListFile( HttpServletRequest request )
    {
    	if ( request.getParameter( PARAMETER_REINIT ) != null )
    	{
            _fileList.clear( );
            _folderHtml.setHtml( null );
            _folderHtml.setHtmlImpression( null );
            _folderImage.setImg( null );
            request.getSession( ).getAttribute( PARAMETER_FILE_CHECK );
    	}
    }

}
