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
package fr.paris.lutece.plugins.plu.business.plu;

import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.portal.service.util.AppException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


/**
 * This class provides Data Access methods for Plu objects
 * @author vLopez
 */
public class PluDAO extends JPALuteceDAO<Integer, Plu> implements IPluDAO
{
    private static final String SQL_QUERY_SELECT_PLU_WORK = "SELECT p FROM Plu p WHERE p.da IS NULL";
    private static final String SQL_QUERY_SELECT_PLU_APPLIED = "SELECT p FROM Plu p WHERE p.id = ( SELECT MAX(p.id) - 1 FROM Plu p )";
    private static final String SQL_QUERY_SELECT_PLU_SEARCH_BY_DATE_APPLICATION = "SELECT p FROM Plu p WHERE p.da > ? AND p.da < ?";
    private static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd" );

    /**
     * Returns the list of plu with find with filters
     * @param dateApplicationDebut the begin application date
     * @param dateApplicationFin the end application date
     * @return the list of plu
     */
    public List<Plu> findPluWithFilters( String dateApplicationDebut, String dateApplicationFin )
    {

        String query = SQL_QUERY_SELECT_PLU_SEARCH_BY_DATE_APPLICATION;
        TypedQuery<Plu> q = this.getEM( ).createQuery( query, Plu.class );

        List<Plu> listPlu;

        try
        {
            q.setParameter( 1, SDF.parse( dateApplicationDebut ) );
            q.setParameter( 2, SDF.parse( dateApplicationFin ) );
        }
        catch ( ParseException e1 )
        {
            throw new AppException( "Erreur lors de la récupération de la date d'application.", e1 );
        }

        try
        {
            listPlu = (ArrayList<Plu>) q.getResultList( );
        }
        catch ( NoResultException e )
        {
            listPlu = new ArrayList<Plu>( );
        }

        return listPlu;
    }

    /**
     * @return the plugin name
     */
    @Override
    public String getPluginName( )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    /**
     * Returns a plu object
     * @return A plu object which work
     */
    public Plu findPluWork( )
    {
        EntityManager em = getEM( );
        Query q = em.createQuery( SQL_QUERY_SELECT_PLU_WORK );

        Plu plu;

        try
        {
            plu = (Plu) q.getSingleResult( );
        }
        catch ( NoResultException e )
        {
            plu = new Plu( );
            plu.setId( 0 );
        }

        return plu;
    }

    /**
     * Returns a plu object
     * @return A plu object which is applied
     */
    public Plu findPluApplied( )
    {
        EntityManager em = getEM( );
        Query q = em.createQuery( SQL_QUERY_SELECT_PLU_APPLIED );
        Plu plu = null;
        plu = (Plu) q.getSingleResult( );
        return plu;
    }
}
