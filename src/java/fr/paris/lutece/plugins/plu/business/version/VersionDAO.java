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
package fr.paris.lutece.plugins.plu.business.version;

import fr.paris.lutece.plugins.plu.business.atome.Atome;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;

//import java.sql.Date;
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
 * This class provides Data Access methods for Version objects
 * @author vLopez
 */
public class VersionDAO extends JPALuteceDAO<Integer, Version> implements IVersionDAO
{
    private static final String SQL_QUERY_SELECT_BY_DATE_AND_PARENT = "SELECT A.id, A.title, A.description, V.id, V.version, V.d1, V.d2, V.d3, V.d4 FROM plu_version V INNER JOIN plu_atome A ON (V.atome = A.id) INNER JOIN plu_folder F ON (A.folder = F.id) WHERE V.d2 <= ? AND V.d4 > ? AND A.folder = ?";
    private static final String SQL_QUERY_SELECT_ID_BY_D3_D4 = "SELECT A.id, A.title, A.description, V.id, V.version, V.d1, V.d2, V.d3, V.d4 FROM plu_version V INNER JOIN plu_atome A ON (V.atome = A.id) WHERE V.d3 < ? AND V.d4 > ?";
    private static final String SQL_QUERY_SELECT_ID_BY_D2 = "SELECT A.id, A.title, A.description, V.id, V.version, V.d1, V.d2, V.d3, V.d4 FROM plu_version V INNER JOIN plu_atome A ON (V.atome = A.id) WHERE V.d1 < ? AND V.d2 > ? AND V.d3 > ?";

    /**
     * @return the plugin name
     */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    /*
        public List<Version> findByDateAndParent( Date da, int idFolder )
        {
            EntityManager em = getEM(  );
            CriteriaBuilder cb = em.getCriteriaBuilder(  );
    
            CriteriaQuery<Version> cq = cb.createQuery( Version.class );
    
            Root<Folder> rootFolder = cq.from( Folder.class );
            Root<Version> rootVersion = cq.from( Version.class );
            rootVersion.fetch( Version_.atome, JoinType.LEFT ).fetch( Atome_.folder, JoinType.LEFT )
                .fetch( Folder_.parentFolder, JoinType.LEFT );
    
            Predicate conditionD2 = cb.lessThanOrEqualTo( rootVersion.get( Version_.d2 ), da );
            Predicate conditionD4 = cb.greaterThan( rootVersion.get( Version_.d4 ), da );
            Predicate conditionIdFolder = cb.equal( rootFolder.get( Folder_.id ), idFolder );
    
            cq.where( conditionD2, conditionD4, conditionIdFolder );
    
            TypedQuery<Version> query = em.createQuery( cq );
    
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
     * Load the list of version
     *
     * @param date The date for the query
     * @param idFolder The folder identifier
     * @return The list of the Version
     */
    public List<Version> findByDateAndParent( Date date, int idFolder )
    {
        List<Version> versionList = new ArrayList<Version>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DATE_AND_PARENT );
        java.sql.Date sqlDate = new java.sql.Date( date.getTime(  ) );
        daoUtil.setDate( 1, sqlDate );
        daoUtil.setDate( 2, sqlDate );
        daoUtil.setInt( 3, idFolder );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Atome atome = new Atome(  );
            atome.setId( daoUtil.getInt( 1 ) );
            atome.setTitle( daoUtil.getString( 2 ) );
            atome.setDescription( daoUtil.getString( 3 ) );

            Version version = new Version(  );
            version.setId( daoUtil.getInt( 4 ) );
            version.setVersion( daoUtil.getInt( 5 ) );
            version.setD1( daoUtil.getDate( 6 ) );
            version.setD2( daoUtil.getDate( 7 ) );
            version.setD3( daoUtil.getDate( 8 ) );
            version.setD4( daoUtil.getDate( 9 ) );
            version.setAtome( atome );
            versionList.add( version );
        }

        daoUtil.free(  );

        return versionList;
    }

    /*
    public List<Version> findByD3D4( Date da )
    {
        EntityManager em = getEM(  );
        CriteriaBuilder cb = em.getCriteriaBuilder(  );
    
        CriteriaQuery<Version> cq = cb.createQuery( Version.class );
    
        Root<Version> root = cq.from( Version.class );
        root.fetch( Version_.atome, JoinType.LEFT ).fetch( Atome_.folder, JoinType.LEFT );
    
        Predicate conditionD3 = cb.lessThan( root.get( Version_.d3 ), da );
        Predicate conditionD4 = cb.greaterThan( root.get( Version_.d4 ), da );
    
        cq.where( conditionD3, conditionD4 );
    
        TypedQuery<Version> query = em.createQuery( cq );
    
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
     * Load the list of version
     *
     * @param date The date for the query
     * @return The list of the Version
     */
    public List<Version> findByD3D4( Date da )
    {
        List<Version> versionList = new ArrayList<Version>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ID_BY_D3_D4 );
        java.sql.Date sqlDa = new java.sql.Date( da.getTime(  ) );
        daoUtil.setDate( 1, sqlDa );
        daoUtil.setDate( 2, sqlDa );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Atome atome = new Atome(  );
            atome.setId( daoUtil.getInt( 1 ) );
            atome.setTitle( daoUtil.getString( 2 ) );
            atome.setDescription( daoUtil.getString( 3 ) );

            Version version = new Version(  );
            version.setId( daoUtil.getInt( 4 ) );
            version.setVersion( daoUtil.getInt( 5 ) );
            version.setD1( daoUtil.getDate( 6 ) );
            version.setD2( daoUtil.getDate( 7 ) );
            version.setD3( daoUtil.getDate( 8 ) );
            version.setD4( daoUtil.getDate( 9 ) );
            version.setAtome( atome );
            versionList.add( version );
        }

        daoUtil.free(  );

        return versionList;
    }

    /*
    public List<Version> findByD2( Date da )
    {
        EntityManager em = getEM(  );
        CriteriaBuilder cb = em.getCriteriaBuilder(  );
    
        CriteriaQuery<Version> cq = cb.createQuery( Version.class );
    
        Root<Version> root = cq.from( Version.class );
        root.fetch( Version_.atome, JoinType.LEFT ).fetch( Atome_.folder, JoinType.LEFT );
    
        Predicate condition = cb.greaterThan( root.get( Version_.d2 ), da );
    
        cq.where( condition );
    
        TypedQuery<Version> query = em.createQuery( cq );
    
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
     * Load the list of version
     *
     * @param date The date for the query
     * @return The list of the Version
     */
    public List<Version> findByD2( Date da )
    {
        List<Version> versionList = new ArrayList<Version>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ID_BY_D2 );
        java.sql.Date sqlDa = new java.sql.Date( da.getTime(  ) );
        daoUtil.setDate( 1, sqlDa );
        daoUtil.setDate( 2, sqlDa );
        daoUtil.setDate( 3, sqlDa );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Atome atome = new Atome(  );
            atome.setId( daoUtil.getInt( 1 ) );
            atome.setTitle( daoUtil.getString( 2 ) );
            atome.setDescription( daoUtil.getString( 3 ) );

            Version version = new Version(  );
            version.setId( daoUtil.getInt( 4 ) );
            version.setVersion( daoUtil.getInt( 5 ) );
            version.setD1( daoUtil.getDate( 6 ) );
            version.setD2( daoUtil.getDate( 7 ) );
            version.setD3( daoUtil.getDate( 8 ) );
            version.setD4( daoUtil.getDate( 9 ) );
            version.setAtome( atome );
            versionList.add( version );
        }

        daoUtil.free(  );

        return versionList;
    }

    /*
        public List<Version> findByFilter( VersionFilter filter )
    {
            EntityManager em = getEM(  );
        CriteriaBuilder cb = em.getCriteriaBuilder(  );
    
        CriteriaQuery<Version> cq = cb.createQuery( Version.class );
    
        Root<Version> root = cq.from( Version.class );
    
        buildCriteriaQuery( filter, root, cq, cb );
    
        cq.distinct( true );
    
        TypedQuery<Version> query = em.createQuery( cq );
    
        return query.getResultList(  );
    }
    
    private void buildCriteriaQuery( VersionFilter filter, Root<Version> root, CriteriaQuery<Version> cq,
        CriteriaBuilder cb )
    {
            List<Predicate> listPredicates = new ArrayList<Predicate>(  );
    
        if ( StringUtils.isNotBlank( filter.get_d1(  ) ) )
        {
                listPredicates.add( cb.like( root.get( Version_.d1 ),
                       PluJPAUtils.buildCriteriaLikeString2( filter.get_d1(  ) ) ) );
        }
    
        if ( !listPredicates.isEmpty(  ) )
        {
            // add existing predicates to Where clause
            cq.where( listPredicates.toArray( new Predicate[0] ) );
        }
    }
    */
}
