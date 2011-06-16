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

import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for Atome objects
 * @author vLopez
 */
public class AtomeDAO extends JPALuteceDAO<Integer, Atome> implements IAtomeDAO
{
	private static final String SQL_QUERY_CREATE = "INSERT INTO atome VALUE (?, ?, ?, ?)";
	private static final String SQL_QUERY_UPDATE = "UPDATE atome SET id_atome = ?, nom = ?, titre = ?, description = ? WHERE id_atome = ?";
	private static final String SQL_QUERY_SELECT_ALL = "SELECT * FROM atome";
	private static final String SQL_QUERY_SELECT_BY_KEY = "SELECT * FROM atome WHERE id_atome = ?";

    /**
     * @return the plugin name
     */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    public void create( Atome atome )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CREATE );
        daoUtil.setInt( 1, atome.getId(  ) );
        daoUtil.setString( 2, atome.getName(  ) );
        daoUtil.setString( 3, atome.getTitle(  ) );
        daoUtil.setString( 4, atome.getDescription(  ) );
        daoUtil.executeUpdate(  );
        
        daoUtil.free(  );
    }
    
    public void update( Atome atome, int nIdAtomeOld )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, atome.getId(  ) );
        daoUtil.setString( 2, atome.getName(  ) );
        daoUtil.setString( 3, atome.getTitle(  ) );
        daoUtil.setString( 4, atome.getDescription(  ) );
        daoUtil.setInt( 5, nIdAtomeOld );
        daoUtil.executeUpdate(  );
        
        daoUtil.free(  );
    }
    
    public Atome findByPrimaryKey( int nKey )
    {
        Atome atome = new Atome(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_KEY );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            atome.setId( daoUtil.getInt( 1 ) );
            atome.setName( daoUtil.getString( 2 ) );
            atome.setTitle( daoUtil.getString( 3 ) );
            atome.setDescription( daoUtil.getString( 4 ) );
        }

        daoUtil.free(  );

        return atome;
    }
    

	
	public List<Atome> findAll(  )
	{
		List<Atome> atomeList = new ArrayList<Atome>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
        	Atome atome = new Atome(  );
            atome.setId( daoUtil.getInt( 1 ) );
            atome.setName( daoUtil.getString( 2 ) );
            atome.setTitle( daoUtil.getString( 3 ) );
            atome.setDescription( daoUtil.getString( 4 ) );
            atomeList.add( atome );
        }

        daoUtil.free(  );

        return atomeList;
	}
    
//    public List<Atome> findByDateAndParent( Date da, int idFolder )
//    {
//        EntityManager em = getEM(  );
//        CriteriaBuilder cb = em.getCriteriaBuilder(  );
//
//        CriteriaQuery<Atome> cq = cb.createQuery( Atome.class );
//
//        Root<Folder> rootFolder = cq.from( Folder.class );
//        Root<Version> rootVersion = cq.from( Version.class );
//        rootVersion.fetch( Version_.atome, JoinType.LEFT ).fetch( Atome_.folder, JoinType.LEFT )
//            .fetch( Folder_.parentFolder, JoinType.LEFT );
//
//        Predicate conditionD2 = cb.lessThanOrEqualTo( rootVersion.get( Version_.d2 ), da );
//        Predicate conditionD4 = cb.greaterThan( rootVersion.get( Version_.d4 ), da );
//        Predicate conditionIdFolder = cb.equal( rootFolder.get( Folder_.id ), idFolder );
//
//        cq.where( conditionD2, conditionD4, conditionIdFolder );
//
//        TypedQuery<Atome> query = em.createQuery( cq );
//
//        try
//        {
//            return query.getResultList(  );
//        }
//        catch ( NoResultException e )
//        {
//            return null;
//        }
//    }
}
