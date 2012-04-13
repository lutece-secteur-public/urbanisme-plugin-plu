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
import javax.persistence.TypedQuery;


/**
 * This class provides Data Access methods for Version objects
 * @author vLopez
 */
public class VersionDAO extends JPALuteceDAO<Integer, Version> implements IVersionDAO
{
    private static final String SQL_QUERY_SELECT_APPROVE = "SELECT v FROM FolderVersion fv JOIN fv.version v WHERE v.d1 IS NULL AND v.d2 IS NULL AND v.d3 IS NULL AND v.d4 IS NULL AND fv.folder.plu = :idPlu";
    private static final String SQL_QUERY_SELECT_APPLICATION = "SELECT v FROM FolderVersion fv JOIN fv.version v WHERE v.d1 <= :d2 AND v.d2 IS NULL AND v.d3 IS NULL AND v.d4 IS NULL AND fv.folder.plu = :idPlu";
    private static final String SQL_QUERY_SELECT_EVOLUTION = "SELECT v FROM Version v WHERE v.d3 = '0001-01-01'";
    private static final String SQL_QUERY_SELECT_ARCHIVE = "SELECT v FROM Version v  WHERE v.archive = 'O'";
    private static final String SQL_QUERY_SELECT_MAX_VERSION = "SELECT MAX(v.version) FROM Version v WHERE v.atome.id = :idAtome";
    private static final String SQL_QUERY_SELECT_BY_ATOME_AND_VERSION = "SELECT v FROM Version v WHERE v.atome.id = :idAtome AND v.version = :numVersion";
    private static final String SQL_QUERY_SELECT_BY_PLU_AND_FOLDER = "SELECT v FROM FolderVersion fv JOIN fv.version v JOIN v.atome a WHERE fv.folder.plu = :idPlu AND fv.folder.id = :idFolder";
    private static final String SQL_QUERY_SELECT_BY_PLU = "SELECT v FROM FolderVersion fv JOIN fv.version v JOIN v.atome a WHERE fv.folder.plu = :idPlu";
    private static final String SQL_QUERY_SELECT_ALL = "SELECT v FROM Version v";
    private static final String SQL_FILTER_ATOME_ID = "v.atome.id = :idAtome";
    private static final String SQL_FILTER_ATOME_NAME = "v.atome.name like :nameAtome";
    private static final String SQL_FILTER_ATOME_TITLE = "v.atome.title like :titleAtome";
    private static final String SQL_FILTER_VERSION_NUMBER = "v.version = :numVersion";
    private static final String SQL_FILTER_VERSION_D1 = "v.d1 = :d1";
    private static final String SQL_FILTER_VERSION_D2 = "v.d2 = :d2";
    private static final String SQL_FILTER_VERSION_D3 = "v.d3 = :d3";
    private static final String SQL_FILTER_VERSION_D4 = "v.d4 = :d4";

    /**
     * @return the plugin name
     */
    @Override
    public String getPluginName( )
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
        TypedQuery<Version> q = this.getEM( ).createQuery( SQL_QUERY_SELECT_APPROVE, Version.class );
        q.setParameter( "idPlu", idPlu );

        List<Version> versionList = q.getResultList( );

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
        java.sql.Date sqlD2 = new java.sql.Date( date.getTime( ) );

        TypedQuery<Version> q = this.getEM( ).createQuery( SQL_QUERY_SELECT_APPLICATION, Version.class );
        q.setParameter( "d2", sqlD2 );
        q.setParameter( "idPlu", idPlu );

        List<Version> versionList = q.getResultList( );

        return versionList;
    }

    /**
     * Returns a list of version objects for the evolution
     * @return A list of version
     */
    public List<Version> selectEvolution( )
    {
        TypedQuery<Version> q = this.getEM( ).createQuery( SQL_QUERY_SELECT_EVOLUTION, Version.class );

        List<Version> versionList = q.getResultList( );

        return versionList;
    }

    /**
     * Returns a list of version objects for the archive
     * @return A list of version
     */
    public List<Version> selectArchive( )
    {

        TypedQuery<Version> q = this.getEM( ).createQuery( SQL_QUERY_SELECT_ARCHIVE, Version.class );
        List<Version> versionList = q.getResultList( );

        return versionList;
    }

    /**
     * Returns a version objects
     * @param nIdAtome The atome identifier
     * @return A version
     */
    public int findMaxVersion( int nIdAtome )
    {
        EntityManager em = getEM( );
        Query q = em.createQuery( SQL_QUERY_SELECT_MAX_VERSION );
        q.setParameter( "idAtome", nIdAtome );

        int version = (Integer) q.getSingleResult( );

        return version;
    }

    /**
     * Returns a version objects
     * @param nIdAtome The atome identifier
     * @param numVersion The number version
     * @return A version
     */
    public Version findByAtomeAndNumVersion( int nIdAtome, int numVersion )
    {
        EntityManager em = getEM( );
        Query q = em.createQuery( SQL_QUERY_SELECT_BY_ATOME_AND_VERSION );
        q.setParameter( "idAtome", nIdAtome );
        q.setParameter( "numVersion", numVersion );

        Version version = (Version) q.getSingleResult( );

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
        TypedQuery<Version> q;
        
        if ( nIdFolder != 0 )
        {
        	q = this.getEM( ).createQuery( SQL_QUERY_SELECT_BY_PLU_AND_FOLDER, Version.class );
        	q.setParameter( "idFolder", nIdFolder );
        }
        else
        {
        	q = this.getEM( ).createQuery( SQL_QUERY_SELECT_BY_PLU, Version.class );
        }

        q.setParameter( "idPlu", nIdPlu );
        
        List<Version> versionList = q.getResultList( );

        return versionList;
    }

    /**
     * Finds by filter
     * @param atomeFilter the atome filter
     * @param versionFilter the version filter
     * @return the version list
     */
    public List<Version> findByFilter( AtomeFilter atomeFilter, VersionFilter versionFilter )
    {
        List<String> listStrFilter = new ArrayList<String>( );

        if ( atomeFilter.containsId( ) )
        {
            listStrFilter.add( SQL_FILTER_ATOME_ID );
        }

        if ( atomeFilter.containsName( ) )
        {
            listStrFilter.add( SQL_FILTER_ATOME_NAME );
        }

        if ( atomeFilter.containsTitle( ) )
        {
            listStrFilter.add( SQL_FILTER_ATOME_TITLE );
        }

        if ( versionFilter.containsVersion( ) )
        {
            listStrFilter.add( SQL_FILTER_VERSION_NUMBER );
        }

        if ( versionFilter.containsD1( ) )
        {
            listStrFilter.add( SQL_FILTER_VERSION_D1 );
        }

        if ( versionFilter.containsD2( ) )
        {
            listStrFilter.add( SQL_FILTER_VERSION_D2 );
        }

        if ( versionFilter.containsD3( ) )
        {
            listStrFilter.add( SQL_FILTER_VERSION_D3 );
        }

        if ( versionFilter.containsD4( ) )
        {
            listStrFilter.add( SQL_FILTER_VERSION_D4 );
        }

        String strSQL = PluUtils.buildRequetteWithFilter( SQL_QUERY_SELECT_ALL, listStrFilter );

        TypedQuery<Version> q = this.getEM( ).createQuery( strSQL, Version.class );

        if ( atomeFilter.containsId( ) )
        {
            q.setParameter( "idAtome", atomeFilter.getId( ) );
        }

        if ( atomeFilter.containsName( ) )
        {
            q.setParameter( "nameAtome", "%" + atomeFilter.getName( ) + "%" );
        }

        if ( atomeFilter.containsTitle( ) )
        {
            q.setParameter( "titleAtome", "%" + atomeFilter.getTitle( ) + "%" );
        }

        if ( versionFilter.containsVersion( ) )
        {
            q.setParameter( "numVersion", versionFilter.getVersion( ) );
        }

        if ( versionFilter.containsD1( ) )
        {
            java.sql.Date sqlD1 = new java.sql.Date( versionFilter.getD1( ).getTime( ) );
            q.setParameter( "d1", sqlD1 );
        }

        if ( versionFilter.containsD2( ) )
        {
            java.sql.Date sqlD2 = new java.sql.Date( versionFilter.getD2( ).getTime( ) );
            q.setParameter( "d2", sqlD2 );
        }

        if ( versionFilter.containsD3( ) )
        {
            java.sql.Date sqlD3 = new java.sql.Date( versionFilter.getD3( ).getTime( ) );
            q.setParameter( "d3", sqlD3 );
        }

        if ( versionFilter.containsD4( ) )
        {
            java.sql.Date sqlD4 = new java.sql.Date( versionFilter.getD4( ).getTime( ) );
            q.setParameter( "d4", sqlD4 );
        }

        List<Version> versionList = q.getResultList( );

        return versionList;
    }

    /**
     * Finds all
     * @return the version list
     */
    public List<Version> findAll( )
    {
        List<String> listFilters = new ArrayList<String>( );
        String strSQL = PluUtils.buildRequetteWithFilter( SQL_QUERY_SELECT_ALL, listFilters );
        TypedQuery<Version> q = this.getEM( ).createQuery( strSQL, Version.class );
        return q.getResultList( );
    }

}
