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

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;


/**
 * VersionServices
 * @author vLopez
 */
@Transactional
public class VersionServices implements IVersionServices
{
    private IVersionHome _home;

    /**
         * @return the _home
         */
    public IVersionHome getHome(  )
    {
        return _home;
    }

    /**
     * @param home the _home to set
     */
    public void setHome( IVersionHome home )
    {
        this._home = home;
    }

    /**
     * Create a new version object
     * @param version the new version object
     */
    public void create( Version version )
    {
        _home.create( version );
    }

    /**
     * Update a version object
     * @param version the version object
     */
    public void update( Version version )
    {
        _home.update( version );
    }

    /**
     * Returns a list of version objects for the approve
     * @param idPlu The plu identifier
     * @return A list of version
     */
    public List<Version> selectApprove( int idPlu )
    {
        return _home.selectApprove( idPlu );
    }

    /**
     * Returns a list of version objects for the application
     * @param idPlu The plu identifier
     * @param date The date of application
     * @return A list of version
     */
    public List<Version> selectApplication( int idPlu, Date date )
    {
        return _home.selectApplication( idPlu, date );
    }

    /**
     * Returns a list of version objects for the evolution
     * @return A list of version
     */
    public List<Version> selectEvolution( )
    {
        return _home.selectEvolution( );
    }

    /**
     * Returns a list of version objects for the archive
     * @return A list of version
     */
    public List<Version> selectArchive( )
    {
        return _home.selectArchive( );
    }

    /**
     * Returns the largest num version
     * @param nIdAtome The atome identifier
     * @return The largest num version
     */
    public int findMaxVersion( int nIdAtome )
    {
        return _home.findMaxVersion( nIdAtome );
    }

    /**
     * Returns a version object
     * @param nKey the version id
     * @return A version object with the id nKey
     */
    public Version findByPrimaryKey( int nKey )
    {
        return _home.findByPrimaryKey( nKey );
    }

    /**
     * Returns a list of version objects
     * @param nIdAtome The atome identifier
     * @param numVersion The number version
     * @return A list of version
     */
    public Version findByAtomeAndNumVersion( int nIdAtome, int numVersion )
    {
        return _home.findByAtomeAndNumVersion( nIdAtome, numVersion );
    }

    /**
     * Returns a list of version objects
     * @param nIdPlu The plu identifier
     * @param nIdFolder The folder identifier
     * @return A list of version
     */
    public List<Version> findByPluAndFolder( int nIdPlu, int nIdFolder )
    {
        return _home.findByPluAndFolder( nIdPlu, nIdFolder );
    }

    /**
     * Finds by filter
     * @param atomeFilter the atome filter
     * @param versionFilter the version filter
     * @return the version list
     */
    public List<Version> findByFilter( AtomeFilter atomeFilter, VersionFilter versionFilter )
    {
        return _home.findByFilter( atomeFilter, versionFilter );
    }

    /**
     * Finds all versions
     * @return the version list
     */
    public List<Version> findAll( )
    {
        return _home.findAll( );
    }

    /**
     * Return a list of Version having an Atome having only one Version
     * @return list of version
     */
    public List<Version> findVersionWithAtomeWithSingleVersion( )
    {
        return _home.findVersionWithAtomeWithSingleVersion( );
    }

    /**
     * Return the id of the oldest Plu with this version atome
     * @param nIdVersion the id of the version atome
     * @return id of the plu
     */
    public int findOldestPluWithVersion( int nIdVersion )
    {
        return _home.findOldestPluWithVersion( nIdVersion );
    }
}
