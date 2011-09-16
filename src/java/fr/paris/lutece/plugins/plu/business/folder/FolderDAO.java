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
package fr.paris.lutece.plugins.plu.business.folder;

import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.plugins.plu.utils.PluUtils;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 * This class provides Data Access methods for Folder objects
 * @author vLopez
 */
public class FolderDAO extends JPALuteceDAO<Integer, Folder> implements IFolderDAO
{
    private static final String SQL_QUERY_SELECT_LAST_FOLDER = "SELECT f FROM Folder f WHERE f.id = (SELECT MAX(f.id) FROM Folder f)";
    private static final String SQL_QUERY_SELECT_BY_TITLE = "SELECT f FROM Folder f WHERE f.title = :title";
    private static final String SQL_QUERY_SELECT_BY_ATOME = "SELECT fv.folder FROM FolderVersion fv JOIN fv.version v JOIN fv.folder f WHERE v.atome.id = :idAtome AND v.version = (SELECT MAX(v.version) FROM Version v WHERE v.atome.id = :idAtome ) ORDER BY f.id DESC";
    private static final String SQL_QUERY_SELECT_BY_VERSION = "SELECT f FROM FolderVersion fv JOIN fv.folder f WHERE fv.version.id = :idVersion";
    private static final String SQL_QUERY_SELECT_FOR_DELETE_WITHOUT_PARENT = "SELECT f FROM Folder f WHERE f.parentFolder = :idParentFolder";
    private static final String SQL_QUERY_SELECT_FOR_DELETE_WITHOUT_ATOME = "SELECT f FROM FolderVersion fv JOIN fv.folder f WHERE fv.folder.id = :idFolder";
    private static final String SQL_QUERY_SELECT_BY_PLU_ID = "SELECT f FROM Folder f WHERE f.plu = :idPlu ORDER BY f.parentFolder";
    private static final String SQL_QUERY_SELECT_BY_PARENT = "SELECT f FROM Folder f WHERE f.parentFolder = :idParentFolder";
    private static final String SQL_QUERY_SELECT_ALL = "SELECT f FROM Folder f";
    private static final String SQL_FILTER_ID_PLU = "f.plu = :idPlu";
    private static final String SQL_FILTER_TITLE = "f.title = :title";

    //    private static final String SQL_QUERY_SELECT_IMAGE = "SELECT f.img FROM Folder f WHERE f.id = :idFolder";
    private static final String SQL_QUERY_SELECT_IMAGE = "SELECT d.image FROM plu_dossier d WHERE d.id_dossier = ?";
    private static final String SQL_QUERY_SELECT_HTML = "SELECT d.html_specifique FROM plu_dossier d WHERE d.id_dossier = ?";

    /**
     * @return the plugin name
     */
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    /**
     * Returns a folder object
     * @return The last folder created
     */
    public Folder findLastFolder(  )
    {
        EntityManager em = getEM(  );
        Query q = em.createQuery( SQL_QUERY_SELECT_LAST_FOLDER );

        Folder folder = (Folder) q.getSingleResult(  );

        return folder;
    }

    /**
     * Returns a folder object
     * @param title the folder title
     * @return A folder object with the same title
     */
    public Folder findForTestTitle( String title )
    {
        EntityManager em = getEM(  );
        Query q = em.createQuery( SQL_QUERY_SELECT_BY_TITLE );
        q.setParameter( "title", title );

        List<Folder> folderList = q.getResultList(  );
        Folder folder = null;

        if ( !folderList.isEmpty(  ) )
        {
            folder = folderList.get( 0 );
        }

        return folder;
    }

    /**
     * Returns a folder object
     * @param nIdAtome the atome id
     * @return A folder object associated with the atome id
     */
    public Folder findByAtome( int nIdAtome )
    {
        EntityManager em = getEM(  );
        Query q = em.createQuery( SQL_QUERY_SELECT_BY_ATOME );
        q.setParameter( "idAtome", nIdAtome );

        List<Folder> folderList = q.getResultList(  );
        Folder folder = null;

        if ( !folderList.isEmpty(  ) )
        {
            folder = folderList.get( 0 );
        }

        return folder;
    }

    /**
     * Returns a folder object
     * @param nIdVersion the version id
     * @return A folder object associated with the version id
     */
    public Folder findByVersion( int nIdVersion )
    {
        EntityManager em = getEM(  );
        Query q = em.createQuery( SQL_QUERY_SELECT_BY_VERSION );
        q.setParameter( "idVersion", nIdVersion );

        List<Folder> folderList = q.getResultList(  );

        Folder folder = folderList.get( 0 );

        return folder;
    }

    /**
     * Returns a folder object
     * @param nKey the folder id
     * @return A folder object which has a child or an atome
     */
    public Folder findForDelete( int nKey )
    {
        EntityManager em = getEM(  );
        Query q = em.createQuery( SQL_QUERY_SELECT_FOR_DELETE_WITHOUT_PARENT );
        q.setParameter( "idParentFolder", nKey );

        List<Folder> folderList = q.getResultList(  );
        Folder folder = null;

        if ( !folderList.isEmpty(  ) )
        {
            folder = folderList.get( 0 );
        }
        else
        {
            q = em.createQuery( SQL_QUERY_SELECT_FOR_DELETE_WITHOUT_ATOME );
            q.setParameter( "idFolder", nKey );
            folderList = q.getResultList(  );

            if ( !folderList.isEmpty(  ) )
            {
                folder = folderList.get( 0 );
            }
        }

        return folder;
    }

    /**
     * Returns a list of folder objects
     * @param pluId the plu id
     * @return A list of folder with the same plu id
     */
    public List<Folder> findByPluId( int pluId )
    {
        EntityManager em = getEM(  );
        Query q = em.createQuery( SQL_QUERY_SELECT_BY_PLU_ID );
        q.setParameter( "idPlu", pluId );

        List<Folder> folderList = q.getResultList(  );

        return folderList;
    }

    /**
     * Returns a list of folder objects
     * @param parentId the folder parent id
     * @return A list of folder with the same folder parent id
     */
    public List<Folder> findByParent( int parentId )
    {
        EntityManager em = getEM(  );
        Query q = em.createQuery( SQL_QUERY_SELECT_BY_PARENT );
        q.setParameter( "idParentFolder", parentId );

        List<Folder> folderList = q.getResultList(  );

        return folderList;
    }

    /**
     * Finds by filter
     * @param filter the filter
     * @return the folder list
     */
    public List<Folder> findByFilter( FolderFilter filter )
    {
        List<String> listStrFilter = new ArrayList<String>(  );

        if ( filter.containsPlu(  ) )
        {
            listStrFilter.add( SQL_FILTER_ID_PLU );
        }

        if ( filter.containsTitle(  ) )
        {
            listStrFilter.add( SQL_FILTER_TITLE );
        }

        String strSQL = PluUtils.buildRequetteWithFilter( SQL_QUERY_SELECT_ALL, listStrFilter );
        strSQL += " ORDER BY f.parentFolder ";

        EntityManager em = getEM(  );
        Query q = em.createQuery( strSQL );

        if ( filter.containsPlu(  ) )
        {
            q.setParameter( "idPlu", filter.get_plu(  ) );
        }

        if ( filter.containsTitle(  ) )
        {
            q.setParameter( "title", filter.get_title(  ) );
        }

        List<Folder> folderList = q.getResultList(  );

        return folderList;
    }

    /**
     * Loads the image representing the folder
     *
     * @param nIdFolder int identifier of the Folder to fetch
     * @return the image resource
     */
    public ImageResource getImageResource( int nIdFolder )
    {
        //    	EntityManager em = getEM(  );
        //    	Query q = em.createQuery( SQL_QUERY_SELECT_IMAGE );
        //    	q.setParameter( "idFolder", nIdFolder );
        //    	
        //    	ImageResource image = null;
        //    	image = (ImageResource) q.getSingleResult(  );
        //    	
        //    	return image;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_IMAGE );
        daoUtil.setInt( 1, nIdFolder );
        daoUtil.executeQuery(  );

        ImageResource image = null;

        if ( daoUtil.next(  ) )
        {
            image = new ImageResource(  );
            image.setImage( daoUtil.getBytes( 1 ) );
        }

        daoUtil.free(  );

        return image;
    }

    /**
     * Loads the html specifique representing the folder
     *
     * @param nIdFolder int identifier of the Folder to fetch
     * @return the html specifique
     */
    public ImageResource getHtmlResource( int nIdFolder )
    {
        //    	EntityManager em = getEM(  );
        //    	Query q = em.createQuery( SQL_QUERY_SELECT_HTML );
        //    	q.setParameter( "idFolder", nIdFolder );
        //    	
        //    	ImageResource image = null;
        //    	image = (ImageResource) q.getSingleResult(  );
        //    	
        //    	return image;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_HTML );
        daoUtil.setInt( 1, nIdFolder );
        daoUtil.executeQuery(  );

        ImageResource image = null;

        if ( daoUtil.next(  ) )
        {
            image = new ImageResource(  );
            image.setImage( daoUtil.getBytes( 1 ) );
        }

        daoUtil.free(  );

        return image;
    }
}
