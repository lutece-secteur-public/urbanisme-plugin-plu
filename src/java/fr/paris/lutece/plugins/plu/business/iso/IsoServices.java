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
package fr.paris.lutece.plugins.plu.business.iso;

import fr.paris.lutece.plugins.plu.business.plu.IPluHome;
import fr.paris.lutece.plugins.plu.business.plu.Plu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.transaction.annotation.Transactional;


/**
 * IsoServices
 * @author vLopez
 */
@Transactional
public class IsoServices implements IIsoServices
{
    private IIsoHome _home;
    private IPluHome _pluHome;

    /**
     * @return the _home
     */
    public IIsoHome getHome( )
    {
        return _home;
    }

    /**
     * @param home the _home to set
     */
    public void setHome( IIsoHome home )
    {
        this._home = home;
    }

    /**
     * @return the pluHome
     */
    public IPluHome getPluHome( )
    {
        return _pluHome;
    }

    /**
     * @param pluHome the pluHome to set
     */
    public void setPluHome( IPluHome pluHome )
    {
        this._pluHome = pluHome;
    }

    /**
     * Create a new Iso object
     * @param iso the new Iso object
     */
    public void create( Iso iso )
    {
        _home.create( iso );
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Iso> findList( )
    {
        List<Iso> isoList = this.getHome( ).findList( );
        List<Iso> isoListToReturn = new ArrayList<Iso>( );

        // Eliminates doublons by getting last iso generated
        Map<Integer, Iso> isoPerPlu = new TreeMap<Integer, Iso>( );
        for ( Iso iso : isoList )
        {
            Plu plu = iso.getPlu( );
            if ( isoPerPlu.get( plu.getId( ) ) == null || isoPerPlu.get( plu.getId( ) ).getId( ) < iso.getId( ) )
            {
                if ( !isoPerPlu.containsKey( plu.getId( ) ) )
                {
                    isoListToReturn.add( null ); // Init list to return
                }
                isoPerPlu.put( plu.getId( ), iso );
            }
        }

        Map<Integer, Date> listDateFinApplication = getListDateFinApplication( );

        // Get date of end of application and reverse order of list
        int i = isoPerPlu.size( );
        for ( Iso iso : isoPerPlu.values( ) )
        {
            // Set the date of end with date application of next plu
            iso.getPlu( ).setDateFin( listDateFinApplication.get( iso.getPlu( ).getId( ) ) );
            isoListToReturn.set( i - 1, iso );
            i--;
        }

        return isoListToReturn;
    }

    /**
     * Get a map of dates of end of application (key : id plu, value : date)
     * @return map of dates of end of application (key : id plu, value : date)
     */
    private Map<Integer, Date> getListDateFinApplication( )
    {
        List<Plu> listPlu = this.getPluHome( ).findAll( );
        Map<Integer, Date> listDateFinApplication = new HashMap<Integer, Date>( );

        int i = 1;
        for ( Plu plu : listPlu )
        {
            if ( i < listPlu.size( ) )
            {
                // La date de fin d'application du plu courant et celle de debut
                // du plu suivant
                listDateFinApplication.put( plu.getId( ), listPlu.get( i ).getDa( ) );
            }
            i++;
        }
        return listDateFinApplication;
    }

}
