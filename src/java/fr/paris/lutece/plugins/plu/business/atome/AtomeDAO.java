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
package fr.paris.lutece.plugins.plu.business.atome;

import fr.paris.lutece.plugins.plu.business.folder.Folder;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Date;

import java.util.ArrayList;
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
 * This class provides Data Access methods for Atome objects
 * @author vLopez
 */
public class AtomeDAO extends JPALuteceDAO<Integer, Atome> implements IAtomeDAO
{
    private static final String SQL_QUERY_SELECT_BY_DATE = "SELECT A.id, A.title, A.description, A.folder FROM plu_version V INNER JOIN plu_atome A ON (V.atome = A.id) INNER JOIN plu_folder F ON (A.folder = F.id) WHERE V.d2 <= ? AND V.d4 > ? AND A.folder = ?";
    private static final String SQL_QUERY_SELECT_MAX_ID = "SELECT max(id) FROM plu_atome";

    /**
     * @return the plugin name
     */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    /*
        public List<Atome> findByDateAndParent( Date da, int idFolder )
        {
            EntityManager em = getEM(  );
            CriteriaBuilder cb = em.getCriteriaBuilder(  );
    
            CriteriaQuery<Atome> cq = cb.createQuery( Atome.class );
    
            Root<Folder> rootFolder = cq.from( Folder.class );
            Root<Version> rootVersion = cq.from( Version.class );
            rootVersion.fetch( Version_.atome, JoinType.LEFT ).fetch( Atome_.folder, JoinType.LEFT )
                .fetch( Folder_.parentFolder, JoinType.LEFT );
    
            Predicate conditionD2 = cb.lessThanOrEqualTo( rootVersion.get( Version_.d2 ), da );
            Predicate conditionD4 = cb.greaterThan( rootVersion.get( Version_.d4 ), da );
            Predicate conditionIdFolder = cb.equal( rootFolder.get( Folder_.id ), idFolder );
    
            cq.where( conditionD2, conditionD4, conditionIdFolder );
    
            TypedQuery<Atome> query = em.createQuery( cq );
    
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
     * Load the list of atome
     *
     * @param date The date for the query
     * @param idFolder The folder identifier
     * @return The list of the Atome
     */
    public List<Atome> findByDateAndParent( Date date, int idFolder )
    {
        List<Atome> atomeList = new ArrayList<Atome>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DATE );
        daoUtil.setDate( 1, date );
        daoUtil.setDate( 2, date );
        daoUtil.setInt( 3, idFolder );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Folder folder = new Folder(  );
            folder.setId( daoUtil.getInt( 4 ) );

            Atome atome = new Atome(  );
            atome.setId( daoUtil.getInt( 1 ) );
            atome.setTitle( daoUtil.getString( 2 ) );
            atome.setDescription( daoUtil.getString( 3 ) );
            atome.setFolder( folder );
            atomeList.add( atome );
        }

        daoUtil.free(  );

        return atomeList;
    }

	public int findMaxId()
	{
		int nId = 0;
		
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MAX_ID );
		daoUtil.executeQuery(  );
		
		while( daoUtil.next(  ) )
		{
			nId = daoUtil.getInt( 1 );
		}
		
		daoUtil.free(  );
		
		return nId;
	}
}
