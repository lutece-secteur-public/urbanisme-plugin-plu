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
package fr.paris.lutece.plugins.plu.business;

import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;

//import java.sql.Date;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class VersionDAO extends JPALuteceDAO<Integer, Version> implements IVersionDAO
{
    private static final String SQL_QUERY_SELECT_BY_DATE = "SELECT A.id, A.title, A.description, V.id, V.version, V.d1, V.d2, V.d3, V.d4 FROM plu_version V INNER JOIN plu_atome A ON (V.atome = A.id) INNER JOIN plu_folder F ON (A.folder = F.id) WHERE V.d2 <= ? AND (V.d4 > ? OR V.d4 = 0) AND A.folder = ?";
    private static final String SQL_QUERY_SELECT_ID_BY_D3_D4 = "SELECT V.id FROM plu_version V INNER JOIN plu_atome A ON (V.atome = A.id) INNER JOIN plu_folder F ON (A.folder = F.id) WHERE V.d3 < ? AND V.d4 > ?";
    private static final String SQL_QUERY_SELECT_ID_BY_D2 = "SELECT V.id FROM plu_version V INNER JOIN plu_atome A ON (V.atome = A.id) INNER JOIN plu_folder F ON (A.folder = F.id) WHERE V.d2 > ?";
    
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    public List<Version> findByDateAndParent( Date date, int idParent )
    {
        List<Version> versionList = new ArrayList<Version>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DATE );
        daoUtil.setDate( 1, (java.sql.Date) date );
        daoUtil.setDate( 2, (java.sql.Date) date );
        daoUtil.setInt( 3, idParent );
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
    
    public List<Version> findByD3D4( Date da )
    {
    	List<Version> versionList = new ArrayList<Version>(  );
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ID_BY_D3_D4 );
        daoUtil.setDate( 1, (java.sql.Date) da );
        daoUtil.setDate( 2, (java.sql.Date) da );
        daoUtil.executeQuery(  );
    	
        while ( daoUtil.next(  ) )
        {
        	Version version = new Version( );
        	version.setId( daoUtil.getInt( 1 ) );
        	versionList.add( version );
        }
        
        daoUtil.free(  );
        
    	return versionList;
    }
    
    
    public List<Version> findByD2( Date da )
    {
    	List<Version> versionList = new ArrayList<Version>(  );
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ID_BY_D2 );
        daoUtil.setDate( 1, (java.sql.Date) da );
        daoUtil.setDate( 2, (java.sql.Date) da );
        daoUtil.executeQuery(  );
    	
        while ( daoUtil.next(  ) )
        {
        	Version version = new Version( );
        	version.setId( daoUtil.getInt( 1 ) );
        	versionList.add( version );
        }
        
        daoUtil.free(  );
    	
    	return versionList;
    }

    /*   public List<Version> findByFilter( VersionFilter filter )
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