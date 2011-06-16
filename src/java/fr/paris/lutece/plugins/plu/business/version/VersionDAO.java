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

import fr.paris.lutece.plugins.plu.business.version.IVersionDAO;
import fr.paris.lutece.plugins.plu.business.atome.Atome;
import fr.paris.lutece.plugins.plu.business.atome.AtomeFilter;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.plugins.plu.utils.PluUtils;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;


/**
 * This class provides Data Access methods for Version objects
 * @author vLopez
 */
public class VersionDAO extends JPALuteceDAO<Integer, Version> implements IVersionDAO
{
    private static final String SQL_QUERY_CREATE = "INSERT INTO version_atome (id_atome, num_version) VALUE (?, ?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE version_atome SET id_atome = ?, num_version = ? WHERE id_version = ?";
    private static final String SQL_QUERY_UPDATE_APPROVE = "UPDATE version_atome VA INNER JOIN dossier_version_atome DVA ON (VA.id_version = DVA.id_version) INNER JOIN dossier D ON (DVA.id_dossier = D.id_dossier) SET VA.date_approbation = ? WHERE VA.date_approbation IS NULL AND VA.date_application IS NULL AND VA.date_evolution IS NULL AND VA.date_archivage IS NULL AND D.id_plu = ?";
    private static final String SQL_QUERY_UPDATE_APPLICATION = "UPDATE version_atome VA INNER JOIN dossier_version_atome DVA ON (VA.id_version = DVA.id_version) INNER JOIN dossier D ON (DVA.id_dossier = D.id_dossier) SET VA.date_application = ? WHERE VA.date_approbation < ? AND VA.date_application IS NULL AND VA.date_evolution IS NULL AND VA.date_archivage IS NULL AND D.id_plu = ?";
    private static final String SQL_QUERY_UPDATE_EVOLUTION = "UPDATE version_atome VA INNER JOIN dossier_version_atome DVA ON (VA.id_version = DVA.id_version) INNER JOIN dossier D ON (DVA.id_dossier = D.id_dossier) SET VA.date_evolution = ? WHERE VA.date_application < ? AND VA.date_evolution = '0000-00-00' AND VA.date_archivage IS NULL AND D.id_plu = ?";
    private static final String SQL_QUERY_UPDATE_ARCHIVE = "UPDATE version_atome VA INNER JOIN dossier_version_atome DVA ON (VA.id_version = DVA.id_version) INNER JOIN dossier D ON (DVA.id_dossier = D.id_dossier) SET VA.date_archivage = ? WHERE VA.date_evolution < ? AND VA.date_archivage IS NULL AND D.id_plu = ?";
    private static final String SQL_QUERY_FOR_EVOLUTION = "UPDATE version_atome SET date_evolution = '0000-00-00' WHERE id_version = ?";
    private static final String SQL_QUERY_SELECT_MAX_VERSION = "SELECT max(num_version) FROM version_atome WHERE id_atome = ?";
    private static final String SQL_QUERY_SELECT_BY_KEY = "SELECT VA.id_version, VA.num_version, VA.date_approbation, VA.date_application, VA.date_evolution, VA.date_archivage, A.id_atome, A.nom, A.titre, A.description FROM version_atome VA INNER JOIN atome A ON (VA.id_atome = A.id_atome) WHERE VA.id_version = ?";
    private static final String SQL_QUERY_SELECT_BY_ATOME_AND_VERSION = "SELECT VA.id_version, VA.num_version, VA.date_approbation, VA.date_application, VA.date_evolution, VA.date_archivage, A.id_atome, A.nom, A.titre, A.description FROM atome A INNER JOIN version_atome VA ON (A.id_atome = VA.id_atome) WHERE VA.id_atome = ? AND VA.num_version = ?";
    private static final String SQL_QUERY_SELECT_BY_PLU_AND_FOLDER = "SELECT VA.id_version, VA.num_version, VA.date_approbation, VA.date_application, VA.date_evolution, VA.date_archivage, A.id_atome, A.nom, A.titre, A.description FROM atome A INNER JOIN version_atome VA ON (A.id_atome = VA.id_atome) INNER JOIN dossier_version_atome DVA ON (VA.id_version = DVA.id_version) INNER JOIN dossier D ON (DVA.id_dossier = D.id_dossier) WHERE D.id_plu = ? AND D.id_dossier = ?";
    private static final String SQL_QUERY_SELECT_ALL = "SELECT VA.id_version, VA.num_version, VA.date_approbation, VA.date_application, VA.date_evolution, VA.date_archivage, A.id_atome, A.nom, A.titre, A.description FROM atome A INNER JOIN version_atome VA ON (A.id_atome = VA.id_atome)";
    private static final String SQL_FILTER_ATOME_NAME = "A.nom = ?";
    private static final String SQL_FILTER_ATOME_TITLE = "A.titre = ?";
    private static final String SQL_FILTER_VERSION_NUMBER = "VA.num_version = ?";
    private static final String SQL_FILTER_VERSION_D1 = "VA.date_approbation = ?";
    private static final String SQL_FILTER_VERSION_D2 = "VA.date_application = ?";
    private static final String SQL_FILTER_VERSION_D3 = "VA.date_evolution = ?";
    private static final String SQL_FILTER_VERSION_D4 = "VA.date_archivage = ?";
    
    /**
     * @return the plugin name
     */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    public void create( Version version )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CREATE );
        daoUtil.setInt( 1, version.getAtome(  ).getId(  ) );
        daoUtil.setInt( 2, version.getVersion(  ) );
        daoUtil.executeUpdate(  );
        
        daoUtil.free(  );
    }
    
    public void update( Version version )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, version.getAtome(  ).getId(  ) );
        daoUtil.setInt( 2, version.getVersion(  ) );
        daoUtil.setInt( 3, version.getId(  ) );
        daoUtil.executeUpdate(  );
        
        daoUtil.free(  );
    }
    
    /**
     * Update the version objects for the approve
     * @param idPlu The plu id for the query
     * @param date The date for the query
     */
    public void updateApprove( int idPlu, Date date )
    {
    	java.sql.Date sqlD1 = new java.sql.Date( date.getTime(  ) );
    	
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_APPROVE );
        daoUtil.setDate( 1, sqlD1 );
        daoUtil.setInt( 2, idPlu );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }
    
    /**
     * Update the version objects for the application
     * @param idPlu The plu id for the query
     * @param date The date for the query
     */
    public void updateApplication( int idPlu, Date date )
    {
    	java.sql.Date sqlD2 = new java.sql.Date( date.getTime(  ) );
    	
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_APPLICATION );
        daoUtil.setDate( 1, sqlD2 );
        daoUtil.setDate( 2, sqlD2 );
        daoUtil.setInt( 3, idPlu );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }
    
    /**
     * Update the version objects for the evolution
     * @param idPlu The plu id for the query
     * @param date The date for the query
     */
    public void updateEvolution( int idPlu, Date date )
    {
    	java.sql.Date sqlD3 = new java.sql.Date( date.getTime(  ) );
    	
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_EVOLUTION );
        daoUtil.setDate( 1, sqlD3 );
        daoUtil.setDate( 2, sqlD3 );
        daoUtil.setInt( 3, idPlu );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }
    
    /**
     * Update the version objects for the archive
     * @param idPlu The plu id for the query
     * @param date The date for the query
     */
    public void updateArchive( int idPlu, Date date )
    {
    	java.sql.Date sqlD4 = new java.sql.Date( date.getTime(  ) );
    	
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_ARCHIVE );
        daoUtil.setDate( 1, sqlD4 );
        daoUtil.setDate( 2, sqlD4 );
        daoUtil.setInt( 3, idPlu );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }
    
    public void updateForEvolution( int nKey)
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FOR_EVOLUTION );
    	daoUtil.setInt( 1, nKey );
    	daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }
    
    
    /**
     * Load the largest num version
     * @param nIdAtome The atome identifier
     * @return The largest num version
     */
    public int findMaxVersion( int nIdAtome )
    {
        int nId = 0;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MAX_VERSION );
        daoUtil.setInt( 1, nIdAtome );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nId = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nId;
    }
    
    public Version findByPrimaryKey( int nKey )
    {
    	Version version = new Version(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_KEY );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
        	Atome atome = new Atome(  );
        	atome.setId( daoUtil.getInt( 7 ) );
        	atome.setName( daoUtil.getString( 8 ) );
        	atome.setTitle( daoUtil.getString( 9 ) );
        	atome.setDescription( daoUtil.getString( 10 ) );
        	            
            version.setId( daoUtil.getInt( 1 ) );
            version.setAtome( atome );
            version.setVersion( daoUtil.getInt( 2 ) );
            version.setD1( daoUtil.getDate( 3 ) );
            version.setD2( daoUtil.getDate( 4 ) );
            version.setD3( daoUtil.getDate( 5 ) );
            version.setD4( daoUtil.getDate( 6 ) );
        }

        daoUtil.free(  );

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
        Version version = new Version(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ATOME_AND_VERSION );
        daoUtil.setInt( 1, nIdAtome );
        daoUtil.setInt( 2, numVersion );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Atome atome = new Atome(  );
            atome.setId( daoUtil.getInt( 7 ) );
            atome.setName( daoUtil.getString( 8 ) );
            atome.setTitle( daoUtil.getString( 9 ) );
            atome.setDescription( daoUtil.getString( 10 ) );

            version.setId( daoUtil.getInt( 1 ) );
            version.setAtome( atome );
            version.setVersion( daoUtil.getInt( 2 ) );
            version.setD1( daoUtil.getDate( 3 ) );
            version.setD2( daoUtil.getDate( 4 ) );
            version.setD3( daoUtil.getDate( 5 ) );
            version.setD4( daoUtil.getDate( 6 ) );
        }

        daoUtil.free(  );

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
    	List<Version> versionList = new ArrayList<Version>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PLU_AND_FOLDER );
        daoUtil.setInt( 1, nIdPlu );
        daoUtil.setInt( 2, nIdFolder );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Atome atome = new Atome(  );
            atome.setId( daoUtil.getInt( 7 ) );
            atome.setName( daoUtil.getString( 8 ) );
            atome.setTitle( daoUtil.getString( 9 ) );
            atome.setDescription( daoUtil.getString( 10 ) );

            Version version = new Version(  );
            version.setId( daoUtil.getInt( 1 ) );
            version.setAtome( atome );
            version.setVersion( daoUtil.getInt( 2 ) );
            version.setD1( daoUtil.getDate( 3 ) );
            version.setD2( daoUtil.getDate( 4 ) );
            version.setD3( daoUtil.getDate( 5 ) );
            version.setD4( daoUtil.getDate( 6 ) );
            versionList.add( version );
        }

        daoUtil.free(  );

        return versionList;
    }

	public List<Version> findByFilter(AtomeFilter atomeFilter, VersionFilter versionFilter)
	{
		List<Version> versionList = new ArrayList<Version>(  );
        List<String> listStrFilter = new ArrayList<String>(  );

        if( atomeFilter.containsName(  ) )
        {
            listStrFilter.add( SQL_FILTER_ATOME_NAME );
        }
        if( atomeFilter.containsTitle(  ) )
        {
            listStrFilter.add( SQL_FILTER_ATOME_TITLE );
        }      
        if( versionFilter.containsVersion(  ) )
        {
            listStrFilter.add( SQL_FILTER_VERSION_NUMBER );
        }
        if( versionFilter.containsD1(  ) )
        {
        	listStrFilter.add( SQL_FILTER_VERSION_D1 );
        }
        if( versionFilter.containsD2(  ) )
        {
        	listStrFilter.add( SQL_FILTER_VERSION_D2 );
        }
        if( versionFilter.containsD3(  ) )
        {
        	listStrFilter.add( SQL_FILTER_VERSION_D3 );
        }
        if( versionFilter.containsD4(  ) )
        {
        	listStrFilter.add( SQL_FILTER_VERSION_D4 );
        }
        
        String strSQL = PluUtils.buildRequetteWithFilter( SQL_QUERY_SELECT_ALL, listStrFilter );

        DAOUtil daoUtil = new DAOUtil( strSQL );
        int nIndex = 1;
        
        if( atomeFilter.containsName(  ) )
        {
            daoUtil.setString( nIndex, atomeFilter.get_name(  ) );
            nIndex++;
        }
        if( atomeFilter.containsTitle(  ) )
        {
            daoUtil.setString( nIndex, atomeFilter.get_title(  ) );
            nIndex++;
        }
        if( versionFilter.containsVersion(  ) )
        {
            daoUtil.setInt( nIndex, versionFilter.get_version(  ) );
            nIndex++;
        }
        if( versionFilter.containsD1(  ) )
        {
        	java.sql.Date sqlD1 = new java.sql.Date( versionFilter.get_d1(  ).getTime(  ) );
        	daoUtil.setDate( nIndex, sqlD1 );
        	nIndex++;
        }
        if( versionFilter.containsD2(  ) )
        {
        	java.sql.Date sqlD2 = new java.sql.Date( versionFilter.get_d2(  ).getTime(  ) );
        	daoUtil.setDate( nIndex, sqlD2 );
        	nIndex++;
        }
        if( versionFilter.containsD3(  ) )
        {
        	java.sql.Date sqlD3 = new java.sql.Date( versionFilter.get_d3(  ).getTime(  ) );
        	daoUtil.setDate( nIndex, sqlD3 );
        	nIndex++;
        }
        if( versionFilter.containsD4(  ) )
        {
        	java.sql.Date sqlD4 = new java.sql.Date( versionFilter.get_d4(  ).getTime(  ) );
        	daoUtil.setDate( nIndex, sqlD4 );
        	nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
        	Atome atome = new Atome(  );
            atome.setId( daoUtil.getInt( 7 ) );
            atome.setName( daoUtil.getString( 8 ) );
            atome.setTitle( daoUtil.getString( 9 ) );
            atome.setDescription( daoUtil.getString( 10 ) );

            Version version = new Version(  );
            version.setId( daoUtil.getInt( 1 ) );
            version.setAtome( atome );
            version.setVersion( daoUtil.getInt( 2 ) );
            version.setD1( daoUtil.getDate( 3 ) );
            version.setD2( daoUtil.getDate( 4 ) );
            version.setD3( daoUtil.getDate( 5 ) );
            version.setD4( daoUtil.getDate( 6 ) );
            versionList.add( version );
        }

        daoUtil.free(  );

        return versionList;
	}
    
    
    
//    /**
//     * Load the list of version
//     *
//     * @param date The date for the query
//     * @param idFolder The folder identifier
//     * @return The list of the Version
//     */
//    public List<Version> findByDateAndParent( Date date, int idFolder )
//    {
//        List<Version> versionList = new ArrayList<Version>(  );
//        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DATE_AND_PARENT );
//        java.sql.Date sqlDate = new java.sql.Date( date.getTime(  ) );
//        daoUtil.setDate( 1, sqlDate );
//        daoUtil.setDate( 2, sqlDate );
//        daoUtil.setInt( 3, idFolder );
//        daoUtil.executeQuery(  );
//
//        while ( daoUtil.next(  ) )
//        {
//        	Atome atome = new Atome(  );
//        	atome.setId( daoUtil.getInt( 7 ) );
//        	atome.setName( daoUtil.getString( 8 ) );
//        	atome.setTitle( daoUtil.getString( 9 ) );
//        	atome.setDescription( daoUtil.getString( 10 ) );
//        	
//            Version version = new Version(  );
//            version.setId( daoUtil.getInt( 1 ) );
//            version.setAtome( atome );
//            version.setVersion( daoUtil.getInt( 2 ) );
//            version.setD1( daoUtil.getDate( 3 ) );
//            version.setD2( daoUtil.getDate( 4 ) );
//            version.setD3( daoUtil.getDate( 5 ) );
//            version.setD4( daoUtil.getDate( 6 ) );
//            versionList.add( version );
//        }
//
//        daoUtil.free(  );
//
//        return versionList;
//    }


    
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
