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
import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

//import java.sql.Date;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * This class provides Data Access methods for Folder objects
 * @author vLopez
 */
public class FolderDAO extends JPALuteceDAO<Integer, Folder> implements IFolderDAO
{
    private static final String SQL_QUERY_SELECT_BY_DATE = "SELECT DISTINCT F.id, F.title, F.description, F.parentFolder FROM plu_version V INNER JOIN plu_atome A ON (V.atome = A.id) INNER JOIN plu_folder F ON (A.folder = F.id) WHERE V.d2 <= ? AND V.d4 > ? ";
    private static final String SQL_QUERY_SELECT_WORK_PLU = "SELECT DISTINCT F.id, F.title, F.description, F.parentFolder FROM plu_version V INNER JOIN plu_atome A ON (V.atome = A.id) INNER JOIN plu_folder F ON (A.folder = F.id) WHERE V.d1 >= ? ";

    /**
     * @return the plugin name
     */
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    /*
        public List<Folder> findByDateAndParent( Date da, int idFolderParent )
        {
            EntityManager em = getEM(  );
            CriteriaBuilder cb = em.getCriteriaBuilder(  );
    
            CriteriaQuery<Folder> cq = cb.createQuery( Folder.class );
    
            //Root<Folder> rootFolder = cq.from( Folder.class );
            Root<Version> rootVersion = cq.from( Version.class );
            rootVersion.fetch( Version_.atome, JoinType.LEFT ).fetch( Atome_.folder, JoinType.LEFT )
                .fetch( Folder_.parentFolder, JoinType.LEFT );
    
            Predicate conditionD2 = cb.equal( rootVersion.get( Version_.d2 ), da );
            //Predicate conditionD2 = cb.greaterThanOrEqualTo( rootVersion.get( Version_.d2 ), da );
            Predicate conditionD4 = cb.greaterThan( rootVersion.get( Version_.d4 ), da );
            //Predicate conditionD4 = cb.lessThan( rootVersion.get( Version_.d4 ), da );
            Predicate conditionIdFolder = cb.equal( rootVersion.get( Version_.atome).get( Atome_.folder).get( Folder_.parentFolder), idFolderParent );
    
            cq.where( conditionD2, conditionD4, conditionIdFolder );
            //cq.where(conditionD2).where(conditionD4).where(conditionIdFolder);
    
            TypedQuery<Folder> query = em.createQuery( cq );
    
            try
            {
                return query.getResultList(  );
            }
            catch ( NoResultException e )
            {
                return null;
            }
        }
        */

    /**
     * Load the list of folder
     *
     * @param date The date for the query
     * @return The list of the Folder
     */
    public List<Folder> findByDate( Date date )
    {
        List<Folder> folderList = new ArrayList<Folder>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DATE );
        java.sql.Date sqlDate = new java.sql.Date( date.getTime(  ) );
        daoUtil.setDate( 1, sqlDate );
        daoUtil.setDate( 2, sqlDate );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Folder parentFolder = new Folder(  );
            parentFolder.setId( daoUtil.getInt( 4 ) );

            Folder folder = new Folder(  );
            folder.setId( daoUtil.getInt( 1 ) );
            folder.setTitle( daoUtil.getString( 2 ) );
            folder.setDescription( daoUtil.getString( 3 ) );
            folder.setParentFolder( parentFolder );
            folderList.add( folder );
        }

        daoUtil.free(  );

        return folderList;
    }

    /**
     * Find folder list by filter
     * @param filter the filter
     * @return the folder list
     */
    public List<Folder> findByFilter( FolderFilter filter )
    {
        EntityManager em = getEM(  );
        CriteriaBuilder cb = em.getCriteriaBuilder(  );

        CriteriaQuery<Folder> cq = cb.createQuery( Folder.class );

        Root<Folder> root = cq.from( Folder.class );

        buildCriteriaQuery( filter, root, cq, cb );

        cq.distinct( true );

        TypedQuery<Folder> query = em.createQuery( cq );

        return query.getResultList(  );
    }

    /**
     * Build the criteria query from the filter
     * @param filter the filter
     * @param root the folder root
     * @param query the criteria query
     * @param builder the criteria builder
     */
    private void buildCriteriaQuery( FolderFilter filter, Root<Folder> root, CriteriaQuery<Folder> cq,
        CriteriaBuilder cb )
    {
        List<Predicate> listPredicates = new ArrayList<Predicate>(  );

        if ( StringUtils.isNotBlank( filter.get_title(  ) ) )
        {
            listPredicates.add( cb.like( root.get( Folder_.title ),
                    PluJPAUtils.buildCriteriaLikeString1( filter.get_title(  ) ) ) );
        }

        /*if ( StringUtils.isNotBlank( filter.get_description(  ) ) )
        {
            listPredicates.add( cb.like( root.get( Folder_.description ),
                    PluJPAUtils.buildCriteriaLikeString2( filter.get_description(  ) ) ) );
        }*/
        if ( !listPredicates.isEmpty(  ) )
        {
            // add existing predicates to Where clause
            cq.where( listPredicates.toArray( new Predicate[0] ) );
        }
    }

    public Collection<Folder> findWorkPlu( Date date )
    {
        List<Folder> folderList = new ArrayList<Folder>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_WORK_PLU );
        java.sql.Date sqlDate = new java.sql.Date( date.getTime(  ) );
        daoUtil.setDate( 1, sqlDate );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Folder parentFolder = new Folder(  );
            parentFolder.setId( daoUtil.getInt( 4 ) );

            Folder folder = new Folder(  );
            folder.setId( daoUtil.getInt( 1 ) );
            folder.setTitle( daoUtil.getString( 2 ) );
            folder.setDescription( daoUtil.getString( 3 ) );
            folder.setParentFolder( parentFolder );
            folderList.add( folder );
        }

        daoUtil.free(  );

        return folderList;
    }
}
