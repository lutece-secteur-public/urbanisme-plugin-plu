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
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;


public class PluDAO extends JPALuteceDAO<Integer, Plu> implements IPluDAO
{
    //private static final String SQL_QUERY_SELECT_BY_DATE = "SELECT id FROM plu_plu WHERE da > ?";

    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    public Plu findByDaNull( Date date )
    {
        EntityManager em = getEM(  );
        CriteriaBuilder cb = em.getCriteriaBuilder(  );

        CriteriaQuery<Plu> cq = cb.createQuery( Plu.class );

        Root<Plu> root = cq.from( Plu.class );

        Predicate condition = cb.greaterThan( root.get( Plu_.da ), date );
        cq.where( condition );

        TypedQuery<Plu> query = em.createQuery( cq );

        try
        {
            return query.getSingleResult(  );
        }
        catch ( NoResultException e )
        {
            return null;
        }
    }
    /*
    public Plu findByDaNull( Date date )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DATE );
        java.sql.Date sqlDate = new java.sql.Date( date.getTime(  ) );
        daoUtil.setDate( 1, sqlDate );
        daoUtil.executeQuery(  );

        Plu plu = new Plu(  );

        while ( daoUtil.next(  ) )
        {
            plu.setId( daoUtil.getInt( 1 ) );
        }

        return plu;
    }
     */
}
