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
import fr.paris.lutece.portal.service.jpa.AbstractLuteceHome;

import java.util.Date;
import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for Version objects
 * @author vLopez
 */
public class VersionHome extends AbstractLuteceHome<Integer, Version, IVersionDAO> implements IVersionHome
{
    /**
    * Returns a list of version objects for the approve
    * @param idPlu The plu identifier
    * @return A list of version
    */
    public List<Version> selectApprove( int idPlu )
    {
        return getDao(  ).selectApprove( idPlu );
    }

    /**
    * Returns a list of version objects for the application
    * @param idPlu The plu identifier
    * @param date The date of application
    * @return A list of version
    */
    public List<Version> selectApplication( int idPlu, Date date )
    {
        return getDao(  ).selectApplication( idPlu, date );
    }

    /**
     * Returns a list of version objects for the evolution
     * @return A list of version
     */
    public List<Version> selectEvolution( )
    {
        return getDao(  ).selectEvolution( );
    }

    /**
     * Returns a list of version objects for the archive
     * @return A list of version
     */
    public List<Version> selectArchive( )
    {
        return getDao(  ).selectArchive( );
    }

    /**
     * Returns the largest num version
     * @param nIdAtome The atome identifier
     * @return The largest num version
     */
    public int findMaxVersion( int nIdAtome )
    {
        return getDao(  ).findMaxVersion( nIdAtome );
    }

    /**
     * Return the id of the oldest Plu with this version atome
     * @param nIdVersion the id of the version atome
     * @return id of the plu
     */
    public int findOldestPluWithVersion( int nIdVersion )
    {
        return getDao( ).findOldestPluWithVersion( nIdVersion );
    }

    /**
     * Returns a list of version objects
     * @param nIdAtome The atome identifier
     * @param numVersion The number version
     * @return A list of version
     */
    public Version findByAtomeAndNumVersion( int nIdAtome, int numVersion )
    {
        return getDao(  ).findByAtomeAndNumVersion( nIdAtome, numVersion );
    }

    /**
     * Returns a list of version objects
     * @param nIdPlu The plu identifier
     * @param nIdFolder The folder identifier
     * @return A list of version
     */
    public List<Version> findByPluAndFolder( int nIdPlu, int nIdFolder )
    {
        return getDao(  ).findByPluAndFolder( nIdPlu, nIdFolder );
    }

    /**
     * Finds by filter
     * @param atomeFilter the atome filter
     * @param versionFilter the version filter
     * @return the version list
     */
    public List<Version> findByFilter( AtomeFilter atomeFilter, VersionFilter versionFilter )
    {
        return getDao(  ).findByFilter( atomeFilter, versionFilter );
    }

    /**
     * Finds all
     * @return the version list
     */
    public List<Version> findAll( )
    {
        return getDao( ).findAll( );
    }

    /**
     * Return a list of Version having an Atome having only one Version
     * @return list of version
     */
    public List<Version> findVersionWithAtomeWithSingleVersion( )
    {
        return getDao( ).findVersionWithAtomeWithSingleVersion( );
    }

    /**
     * Return the version in the state given
     * @param state 1,2,3 or 4, state of the version needed
     * @param pluWork id of the working plu
     * @return list of the version in the state given
     */
    public List<Version> findVersionState( int state, int pluWork )
    {

        return getDao( ).findVersionState( state, pluWork );
    }
}
