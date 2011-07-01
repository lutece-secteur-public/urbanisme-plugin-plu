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

import fr.paris.lutece.plugins.plu.business.atome.AtomeFilter;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.plugins.plu.utils.PluUtils;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 * This class provides Data Access methods for Version objects
 * @author vLopez
 */
public class VersionDAO extends JPALuteceDAO<Integer, Version> implements IVersionDAO
{
	private static final String SQL_QUERY_SELECT_APPROVE = "SELECT v FROM FolderVersion fv JOIN fv.version v WHERE v.d1 IS NULL AND v.d2 IS NULL AND v.d3 IS NULL AND v.d4 IS NULL AND fv.folder.plu = :idPlu";
    private static final String SQL_QUERY_SELECT_APPLICATION = "SELECT v FROM FolderVersion fv JOIN fv.version v WHERE v.d1 < :d2 AND v.d2 IS NULL AND v.d3 IS NULL AND v.d4 IS NULL AND fv.folder.plu = :idPlu";
    private static final String SQL_QUERY_SELECT_EVOLUTION = "SELECT v FROM FolderVersion fv JOIN fv.version v WHERE v.d2 < :d3 AND v.d3 = '0000-00-00' AND v.d4 IS NULL AND fv.folder.id = :idPlu";
    private static final String SQL_QUERY_SELECT_ARCHIVE = "SELECT v FROM FolderVersion fv JOIN fv.version v  WHERE v.d3 < :d4 AND v.d4 IS NULL AND fv.folder.id = :idPlu";
//    private static final String SQL_QUERY_FOR_EVOLUTION = "UPDATE version_atome SET date_evolution = '0000-00-00' WHERE id_version = ?";
    
    private static final String SQL_QUERY_SELECT_MAX_VERSION = "SELECT MAX(v.version) FROM Version v WHERE v.atome.id = :idAtome";
    private static final String SQL_QUERY_SELECT_BY_ATOME_AND_VERSION = "SELECT v FROM Version v WHERE v.atome.id = :idAtome AND v.version = :numVersion";
    private static final String SQL_QUERY_SELECT_BY_PLU_AND_FOLDER = "SELECT v FROM FolderVersion fv JOIN fv.version v JOIN v.atome a WHERE fv.folder.plu = :idPlu AND fv.folder.id = :idFolder";
    private static final String SQL_QUERY_SELECT_ALL = "SELECT v FROM Version v";
    private static final String SQL_FILTER_ATOME_NAME = "v.atome.name = :nameAtome";
    private static final String SQL_FILTER_ATOME_TITLE = "v.atome.title = :titleAtome";
    private static final String SQL_FILTER_VERSION_NUMBER = "v.version = :numVersion";
    private static final String SQL_FILTER_VERSION_D1 = "v.d1 = :d1";
    private static final String SQL_FILTER_VERSION_D2 = "v.d2 = :d2";
    private static final String SQL_FILTER_VERSION_D3 = "v.d3 = :d3";
    private static final String SQL_FILTER_VERSION_D4 = "v.d4 = :d4";

    /**
     * @return the plugin name
     */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }
    
    /**
     * Returns a list of version objects for the approve
     * @param idPlu The plu identifier
     * @return A list of version
     */
    public List<Version> selectApprove( int idPlu )
    {
        EntityManager em = getEM(  );
    	Query q = em.createQuery( SQL_QUERY_SELECT_APPROVE );
    	q.setParameter( "idPlu", idPlu );

    	List<Version> versionList = (List<Version>) q.getResultList(  );
    	
    	return versionList;
    }

    /**
     * Returns a list of version objects for the application
     * @param idPlu The plu identifier
     * @param date The date of application
     * @return A list of version
     */
    public List<Version> selectApplication( int idPlu, Date date )
    {
        java.sql.Date sqlD2 = new java.sql.Date( date.getTime(  ) );

        EntityManager em = getEM(  );
    	Query q = em.createQuery( SQL_QUERY_SELECT_APPLICATION );
    	q.setParameter( "d2", sqlD2 );
    	q.setParameter( "idPlu", idPlu );

    	List<Version> versionList = (List<Version>) q.getResultList(  );
    	
    	return versionList;
    }

    /**
     * Returns a list of version objects for the evolution
     * @param idPlu The plu identifier
     * @param date The date of evolution
     * @return A list of version
     */
    public List<Version> selectEvolution( int idPlu, Date date )
    {
        java.sql.Date sqlD3 = new java.sql.Date( date.getTime(  ) );

        EntityManager em = getEM(  );
    	Query q = em.createQuery( SQL_QUERY_SELECT_EVOLUTION );
    	q.setParameter( "d3", sqlD3 );
    	q.setParameter( "idPlu", idPlu );

    	List<Version> versionList = (List<Version>) q.getResultList(  );
    	
    	return versionList;
    }

    /**
     * Returns a list of version objects for the archive
     * @param idPlu The plu identifier
     * @param date The date of archivage
     * @return A list of version
     */
    public List<Version> selectArchive( int idPlu, Date date )
    {
        java.sql.Date sqlD4 = new java.sql.Date( date.getTime(  ) );

        EntityManager em = getEM(  );
    	Query q = em.createQuery( SQL_QUERY_SELECT_ARCHIVE );
    	q.setParameter( "d4", sqlD4 );
    	q.setParameter( "idPlu", idPlu );
    	
    	List<Version> versionList = (List<Version>) q.getResultList(  );
    	
    	return versionList;
    }

//    public void updateForEvolution( int nKey )
//    {
//        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FOR_EVOLUTION );
//        daoUtil.setInt( 1, nKey );
//        daoUtil.executeUpdate(  );
//
//        daoUtil.free(  );
//    }

    /**
     * Load the largest num version
     * @param nIdAtome The atome identifier
     * @return The largest num version
     */
    public int findMaxVersion( int nIdAtome )
    {
    	EntityManager em = getEM(  );
    	Query q = em.createQuery( SQL_QUERY_SELECT_MAX_VERSION );
    	q.setParameter( "idAtome", nIdAtome );
    	
    	int version = (Integer) q.getSingleResult(  );
    	
    	return version;
    }

    /**
     * Returns a list of version objects
     * @param nIdAtome The atome identifier
     * @param numVersion The number version
     * @return A list of version
     */
    public Version findByAtomeAndNumVersion( int nIdAtome, int numVersion )
    {
    	EntityManager em = getEM(  );
    	Query q = em.createQuery( SQL_QUERY_SELECT_BY_ATOME_AND_VERSION );
    	q.setParameter( "idAtome", nIdAtome );
    	q.setParameter( "numVersion", numVersion );
    	
    	Version version = (Version) q.getSingleResult(  );
    	
    	return version;
    }

    /**
     * Returns a list of version objects
     * @param nIdPlu The plu identifier
     * @param nIdFolder The folder identifier
     * @return A list of version
     */
    public List<Version> findByPluAndFolder( int nIdPlu, int nIdFolder )
    {
    	EntityManager em = getEM(  );
    	Query q = em.createQuery( SQL_QUERY_SELECT_BY_PLU_AND_FOLDER );
    	q.setParameter( "idPlu", nIdPlu );
    	q.setParameter( "idFolder", nIdFolder );
    	
    	List<Version> versionList = (List<Version>) q.getResultList(  );
    	
    	return versionList;
    }

    /**
     * Finds by filter
     * @param filter the filter
     * @return the version list
     */
    public List<Version> findByFilter( AtomeFilter atomeFilter, VersionFilter versionFilter )
    {
        List<String> listStrFilter = new ArrayList<String>(  );

        if ( atomeFilter.containsName(  ) )
        {
            listStrFilter.add( SQL_FILTER_ATOME_NAME );
        }

        if ( atomeFilter.containsTitle(  ) )
        {
            listStrFilter.add( SQL_FILTER_ATOME_TITLE );
        }

        if ( versionFilter.containsVersion(  ) )
        {
            listStrFilter.add( SQL_FILTER_VERSION_NUMBER );
        }

        if ( versionFilter.containsD1(  ) )
        {
            listStrFilter.add( SQL_FILTER_VERSION_D1 );
        }

        if ( versionFilter.containsD2(  ) )
        {
            listStrFilter.add( SQL_FILTER_VERSION_D2 );
        }

        if ( versionFilter.containsD3(  ) )
        {
            listStrFilter.add( SQL_FILTER_VERSION_D3 );
        }

        if ( versionFilter.containsD4(  ) )
        {
            listStrFilter.add( SQL_FILTER_VERSION_D4 );
        }

        String strSQL = PluUtils.buildRequetteWithFilter( SQL_QUERY_SELECT_ALL, listStrFilter );

        EntityManager em = getEM(  );
    	Query q = em.createQuery( strSQL );

        if ( atomeFilter.containsName(  ) )
        {
        	q.setParameter( "nameAtome", atomeFilter.get_name(  ) );
        }

        if ( atomeFilter.containsTitle(  ) )
        {
        	q.setParameter( "titleAtome", atomeFilter.get_title(  ) );
        }

        if ( versionFilter.containsVersion(  ) )
        {
        	q.setParameter( "numVersion", versionFilter.get_version(  ) );
        }

        if ( versionFilter.containsD1(  ) )
        {
            java.sql.Date sqlD1 = new java.sql.Date( versionFilter.get_d1(  ).getTime(  ) );
            q.setParameter( "d1", sqlD1 );
        }

        if ( versionFilter.containsD2(  ) )
        {
            java.sql.Date sqlD2 = new java.sql.Date( versionFilter.get_d2(  ).getTime(  ) );
            q.setParameter( "d2", sqlD2 );
        }

        if ( versionFilter.containsD3(  ) )
        {
            java.sql.Date sqlD3 = new java.sql.Date( versionFilter.get_d3(  ).getTime(  ) );
            q.setParameter( "d3", sqlD3 );
        }

        if ( versionFilter.containsD4(  ) )
        {
            java.sql.Date sqlD4 = new java.sql.Date( versionFilter.get_d4(  ).getTime(  ) );
            q.setParameter( "d4", sqlD4 );
        }

        List<Version> versionList = (List<Version>) q.getResultList(  );

        return versionList;
    }

    //    public List<Version> findByFilter( VersionFilter filter )
    //    {
    //            EntityManager em = getEM(  );
    //        CriteriaBuilder cb = em.getCriteriaBuilder(  );
    //    
    //        CriteriaQuery<Version> cq = cb.createQuery( Version.class );
    //    
    //        Root<Version> root = cq.from( Version.class );
    //    
    //        buildCriteriaQuery( filter, root, cq, cb );
    //    
    //        cq.distinct( true );
    //    
    //        TypedQuery<Version> query = em.createQuery( cq );
    //    
    //        return query.getResultList(  );
    //    }
    //    
    //    private void buildCriteriaQuery( VersionFilter filter, Root<Version> root, CriteriaQuery<Version> cq,
    //        CriteriaBuilder cb )
    //    {
    //            List<Predicate> listPredicates = new ArrayList<Predicate>(  );
    //    
    //        if ( StringUtils.isNotBlank( filter.get_d1(  ) ) )
    //        {
    //                listPredicates.add( cb.like( root.get( Version_.d1 ),
    //                       PluJPAUtils.buildCriteriaLikeString2( filter.get_d1(  ) ) ) );
    //        }
    //    
    //        if ( !listPredicates.isEmpty(  ) )
    //        {
    //            // add existing predicates to Where clause
    //            cq.where( listPredicates.toArray( new Predicate[0] ) );
    //        }
    //    }
}
