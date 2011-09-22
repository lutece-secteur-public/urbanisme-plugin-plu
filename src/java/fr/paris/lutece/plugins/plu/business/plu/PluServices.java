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
package fr.paris.lutece.plugins.plu.business.plu;

import fr.paris.lutece.plugins.plu.business.iso.IIsoHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * PluServices
 * @author vLopez
 */
public class PluServices implements IPluServices
{
    private IPluHome _home;
    private IIsoHome isoHome;

    /**
     * @return the instance of the service
     */
    public static IPluServices getInstance( )
    {
        return (IPluServices) SpringContextService.getBean( "plu.pluServices" );
    }

    /**
     * @return the _home
     */
    public IPluHome getHome( )
    {
        return _home;
    }

    /**
     * @param home the _home to set
     */
    public void setHome( IPluHome home )
    {
        this._home = home;
    }

    /**
     * Create a new plu object
     * @param plu the new plu object
     */
    public void create( Plu plu )
    {
        _home.create( plu );
    }

    /**
     * Update a plu object
     * @param plu the plu object
     */
    public void update( Plu plu )
    {
        _home.update( plu );
    }

    /**
     * Returns a list of plu objects
     * @return A list of all plu
     */
    public List<Plu> findAll( )
    {
        return _home.findAll( );
    }

    /**
     * Returns a plu object
     * @param nKey the plu id
     * @return A plu object with the same id
     */
    public Plu findByPrimaryKey( int nKey )
    {
        return _home.findByPrimaryKey( nKey );
    }

    /**
     * Returns a plu object
     * @return A plu object which work
     */
    public Plu findPluWork( )
    {
        return _home.findPluWork( );
    }

    /**
     * Returns a plu object
     * @return A plu object which is applied
     */
    public Plu findPluApplied( )
    {
        return _home.findPluApplied( );
    }

    /**
     * Returns the list of plu with find with filters
     * @param dateDebut the begin application date
     * @param dateFin the end application date
     * @return the list of plu
     */
    public List<Plu> findWithFilters( String dateDebut, String dateFin )
    {
        List<Plu> pluList = PluHome.getInstance( ).findWithFilters( dateDebut, dateFin );
        for ( Plu plu : pluList )
        {
            plu.setLastIso( getIsoHome( ).findLastGenerated( plu.getId( ) ) );
        }
        return pluList;
    }

    /**
     * @return the isoHome
     */
    public IIsoHome getIsoHome( )
    {
        return isoHome;
    }

    /**
     * @param isoHome the isoHome to set
     */
    public void setIsoHome( IIsoHome isoHome )
    {
        this.isoHome = isoHome;
    }
}
