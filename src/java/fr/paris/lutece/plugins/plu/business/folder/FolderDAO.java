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


/**
 * This class provides Data Access methods for Folder objects
 * @author vLopez
 */
public class FolderDAO extends JPALuteceDAO<Integer, Folder> implements IFolderDAO
{
    //    private static final String SQL_QUERY_CREATE = "INSERT INTO dossier (id_plu, id_dossier_parent, titre, description, image, html_specifique) VALUE (?, ?, ?, ?, ?, ?)";
    //    private static final String SQL_QUERY_REMOVE = "DELETE FROM dossier WHERE id_dossier = ?";
    //    private static final String SQL_QUERY_UPDATE = "UPDATE dossier SET id_dossier_parent = ?, titre = ?, description = ?, image = ?, html_specifique = ? WHERE id_dossier = ?";
    //    private static final String SQL_QUERY_SELECT_BY_KEY = "SELECT * FROM dossier WHERE id_dossier = ?";
    private static final String SQL_QUERY_SELECT_LAST_FOLDER = "SELECT * FROM dossier WHERE id_dossier = ( SELECT max(id_dossier) FROM dossier )";
    private static final String SQL_QUERY_SELECT_BY_TITLE = "SELECT * FROM dossier WHERE titre = ?";
    private static final String SQL_QUERY_SELECT_BY_ATOME = "SELECT * FROM dossier D INNER JOIN dossier_version_atome DVA ON (D.id_dossier = DVA.id_dossier) INNER JOIN version_atome VA ON (DVA.id_version = VA.id_version) WHERE num_version = (SELECT max(num_version) FROM version_atome WHERE id_atome = ?)";
    private static final String SQL_QUERY_SELECT_BY_VERSION = "SELECT * FROM dossier D INNER JOIN dossier_version_atome DVA ON (D.id_dossier = DVA.id_dossier) WHERE DVA.id_version = ?";
    private static final String SQL_QUERY_SELECT_FOR_DELETE = "SELECT * FROM dossier D LEFT OUTER JOIN dossier_version_atome DVA ON (D.id_dossier=DVA.id_dossier) WHERE D.id_dossier_parent = ? OR DVA.id_dossier = ?";
    private static final String SQL_QUERY_SELECT_BY_PLU_ID = "SELECT * FROM dossier WHERE id_plu = ? ORDER BY id_dossier_parent";
    private static final String SQL_QUERY_SELECT_BY_PARENT = "SELECT * FROM dossier WHERE id_dossier_parent = ?";
    private static final String SQL_QUERY_SELECT_ALL = "SELECT * FROM dossier";
    private static final String SQL_FILTER_ID_PLU = "id_plu = ?";
    private static final String SQL_FILTER_TITLE = "titre = ?";
    private static final String SQL_QUERY_SELECT_IMAGE = "SELECT image FROM dossier WHERE id_dossier = ?";

    /**
     * @return the plugin name
     */
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    //    public void create( Folder folder )
    //    {
    //        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CREATE );
    //        daoUtil.setInt( 1, folder.getPlu(  ) );
    //        daoUtil.setInt( 2, folder.getParentFolder(  ) );
    //        daoUtil.setString( 3, folder.getTitle(  ) );
    //        daoUtil.setString( 4, folder.getDescription(  ) );
    //        daoUtil.setBytes( 5, folder.getImg(  ) );
    //        daoUtil.setBytes( 6, folder.getHtml(  ) );
    //        daoUtil.executeUpdate(  );
    //
    //        daoUtil.free(  );
    //    }
    //
    //    public void update( Folder folder )
    //    {
    //        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
    //        daoUtil.setInt( 1, folder.getParentFolder(  ) );
    //        daoUtil.setString( 2, folder.getTitle(  ) );
    //        daoUtil.setString( 3, folder.getDescription(  ) );
    //        daoUtil.setBytes( 4, folder.getImg(  ) );
    //        daoUtil.setBytes( 5, folder.getHtml(  ) );
    //        daoUtil.setInt( 6, folder.getId(  ) );
    //
    //        daoUtil.executeUpdate(  );
    //
    //        daoUtil.free(  );
    //    }
    //
    //    public void remove( int nKey )
    //    {
    //        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE );
    //        daoUtil.setInt( 1, nKey );
    //        daoUtil.executeUpdate(  );
    //
    //        daoUtil.free(  );
    //    }
    //
    //    public Folder findByPrimaryKey( int nKey )
    //    {
    //        Folder folder = new Folder(  );
    //        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_KEY );
    //        daoUtil.setInt( 1, nKey );
    //        daoUtil.executeQuery(  );
    //
    //        while ( daoUtil.next(  ) )
    //        {
    //            folder.setId( daoUtil.getInt( 1 ) );
    //            folder.setPlu( daoUtil.getInt( 2 ) );
    //            folder.setParentFolder( daoUtil.getInt( 3 ) );
    //            folder.setTitle( daoUtil.getString( 4 ) );
    //            folder.setDescription( daoUtil.getString( 5 ) );
    //            folder.setImg( daoUtil.getBytes( 6 ) );
    //            folder.setHtml( daoUtil.getBytes( 7 ) );
    //        }
    //
    //        daoUtil.free(  );
    //
    //        return folder;
    //    }
    public Folder findLastFolder(  )
    {
        Folder folder = new Folder(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_FOLDER );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            folder.setId( daoUtil.getInt( 1 ) );
            folder.setPlu( daoUtil.getInt( 2 ) );
            folder.setParentFolder( daoUtil.getInt( 3 ) );
            folder.setTitle( daoUtil.getString( 4 ) );
            folder.setDescription( daoUtil.getString( 5 ) );
            folder.setImg( daoUtil.getBytes( 6 ) );
            folder.setHtml( daoUtil.getBytes( 7 ) );
        }

        daoUtil.free(  );

        return folder;
    }

    public Folder findForTestTitle( String title )
    {
        Folder folder = new Folder(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_TITLE );
        daoUtil.setString( 1, title );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            folder.setId( daoUtil.getInt( 1 ) );
            folder.setPlu( daoUtil.getInt( 2 ) );
            folder.setParentFolder( daoUtil.getInt( 3 ) );
            folder.setTitle( daoUtil.getString( 4 ) );
            folder.setDescription( daoUtil.getString( 5 ) );
            folder.setImg( daoUtil.getBytes( 6 ) );
            folder.setHtml( daoUtil.getBytes( 7 ) );
        }

        daoUtil.free(  );

        return folder;
    }

    public Folder findByAtome( int nIdAtome )
    {
        Folder folder = new Folder(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ATOME );
        daoUtil.setInt( 1, nIdAtome );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            folder.setId( daoUtil.getInt( 1 ) );
            folder.setPlu( daoUtil.getInt( 2 ) );
            folder.setParentFolder( daoUtil.getInt( 3 ) );
            folder.setTitle( daoUtil.getString( 4 ) );
            folder.setDescription( daoUtil.getString( 5 ) );
            folder.setImg( daoUtil.getBytes( 6 ) );
            folder.setHtml( daoUtil.getBytes( 7 ) );
        }

        daoUtil.free(  );

        return folder;
    }

    public Folder findByVersion( int nIdVersion )
    {
        Folder folder = new Folder(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_VERSION );
        daoUtil.setInt( 1, nIdVersion );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            folder.setId( daoUtil.getInt( 1 ) );
            folder.setPlu( daoUtil.getInt( 2 ) );
            folder.setParentFolder( daoUtil.getInt( 3 ) );
            folder.setTitle( daoUtil.getString( 4 ) );
            folder.setDescription( daoUtil.getString( 5 ) );
            folder.setImg( daoUtil.getBytes( 6 ) );
            folder.setHtml( daoUtil.getBytes( 7 ) );
        }

        daoUtil.free(  );

        return folder;
    }

    public Folder findForDelete( int nKey )
    {
        Folder folder = new Folder(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_FOR_DELETE );
        daoUtil.setInt( 1, nKey );
        daoUtil.setInt( 2, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            folder.setId( daoUtil.getInt( 1 ) );
            folder.setPlu( daoUtil.getInt( 2 ) );
            folder.setParentFolder( daoUtil.getInt( 3 ) );
            folder.setTitle( daoUtil.getString( 4 ) );
            folder.setDescription( daoUtil.getString( 5 ) );
            folder.setImg( daoUtil.getBytes( 6 ) );
            folder.setHtml( daoUtil.getBytes( 7 ) );
        }

        daoUtil.free(  );

        return folder;
    }

    public List<Folder> findByPluId( int pluId )
    {
        List<Folder> folderList = new ArrayList<Folder>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PLU_ID );
        daoUtil.setInt( 1, pluId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Folder folder = new Folder(  );
            folder.setId( daoUtil.getInt( 1 ) );
            folder.setPlu( daoUtil.getInt( 2 ) );
            folder.setParentFolder( daoUtil.getInt( 3 ) );
            folder.setTitle( daoUtil.getString( 4 ) );
            folder.setDescription( daoUtil.getString( 5 ) );
            folder.setImg( daoUtil.getBytes( 6 ) );
            folder.setHtml( daoUtil.getBytes( 7 ) );
            folderList.add( folder );
        }

        daoUtil.free(  );

        return folderList;
    }

    public List<Folder> findByParent( int parentId )
    {
        List<Folder> folderList = new ArrayList<Folder>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PARENT );
        daoUtil.setInt( 1, parentId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Folder folder = new Folder(  );
            folder.setId( daoUtil.getInt( 1 ) );
            folder.setPlu( daoUtil.getInt( 2 ) );
            folder.setParentFolder( daoUtil.getInt( 3 ) );
            folder.setTitle( daoUtil.getString( 4 ) );
            folder.setDescription( daoUtil.getString( 5 ) );
            folder.setImg( daoUtil.getBytes( 6 ) );
            folder.setHtml( daoUtil.getBytes( 7 ) );
            folderList.add( folder );
        }

        daoUtil.free(  );

        return folderList;
    }

    public List<Folder> findByFilter( FolderFilter filter )
    {
        List<Folder> folderList = new ArrayList<Folder>(  );
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

        DAOUtil daoUtil = new DAOUtil( strSQL );
        int nIndex = 1;

        if ( filter.containsPlu(  ) )
        {
            daoUtil.setInt( nIndex, filter.get_plu(  ) );
            nIndex++;
        }

        if ( filter.containsTitle(  ) )
        {
            daoUtil.setString( nIndex, filter.get_title(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Folder folder = new Folder(  );
            folder.setId( daoUtil.getInt( 1 ) );
            folder.setPlu( daoUtil.getInt( 2 ) );
            folder.setParentFolder( daoUtil.getInt( 3 ) );
            folder.setTitle( daoUtil.getString( 4 ) );
            folder.setDescription( daoUtil.getString( 5 ) );
            folder.setImg( daoUtil.getBytes( 6 ) );
            folder.setHtml( daoUtil.getBytes( 7 ) );
            folderList.add( folder );
        }

        daoUtil.free(  );

        return folderList;
    }

    public ImageResource getImageResource( int nIdFolder )
    {
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

    //    /**
    //     * Find folder list by filter
    //     * @param filter the filter
    //     * @return the folder list
    //     */
    //    public List<Folder> findByFilter( FolderFilter filter )
    //    {
    //        EntityManager em = getEM(  );
    //        CriteriaBuilder cb = em.getCriteriaBuilder(  );
    //
    //        CriteriaQuery<Folder> cq = cb.createQuery( Folder.class );
    //
    //        Root<Folder> root = cq.from( Folder.class );
    //
    //        buildCriteriaQuery( filter, root, cq, cb );
    //
    //        cq.distinct( true );
    //
    //        TypedQuery<Folder> query = em.createQuery( cq );
    //
    //        return query.getResultList(  );
    //    }
    //
    //    /**
    //     * Build the criteria query from the filter
    //     * @param filter the filter
    //     * @param root the folder root
    //     * @param cq the criteria query
    //     * @param cb the criteria builder
    //     */
    //    private void buildCriteriaQuery( FolderFilter filter, Root<Folder> root, CriteriaQuery<Folder> cq,
    //        CriteriaBuilder cb )
    //    {
    //        List<Predicate> listPredicates = new ArrayList<Predicate>(  );
    //
    //        if ( StringUtils.isNotBlank( filter.get_title(  ) ) )
    //        {
    //            listPredicates.add( cb.like( root.get( Folder_.title ),
    //                    PluJPAUtils.buildCriteriaLikeString1( filter.get_title(  ) ) ) );
    //        }
    //
    //        /*if ( StringUtils.isNotBlank( filter.get_description(  ) ) )
    //        {
    //            listPredicates.add( cb.like( root.get( Folder_.description ),
    //                    PluJPAUtils.buildCriteriaLikeString2( filter.get_description(  ) ) ) );
    //        }*/
    //        if ( !listPredicates.isEmpty(  ) )
    //        {
    //            // add existing predicates to Where clause
    //            cq.where( listPredicates.toArray( new Predicate[0] ) );
    //        }
    //    }
    //
}
