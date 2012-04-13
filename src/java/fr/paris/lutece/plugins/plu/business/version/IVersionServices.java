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


/**
 * IVersionServices the version services interface
 * @author vLopez
 */
public interface IVersionServices
{
    /**
     * Create a new version object
     * @param version the new version object
     */
    void create( Version version );

    /**
     * Update a version object
     * @param version the version object
     */
    void update( Version version );

    /**
     * Returns a list of version objects for the approve
     * @param idPlu The plu identifier
     * @return A list of version
     */
    List<Version> selectApprove( int idPlu );

    /**
     * Returns a list of version objects for the application
     * @param idPlu The plu identifier
     * @param date The date of application
     * @return A list of version
     */
    List<Version> selectApplication( int idPlu, Date date );

    /**
     * Returns a list of version objects for the evolution
     * @return A list of version
     */
    List<Version> selectEvolution( );

    /**
     * Returns a list of version objects for the archive
     * @return A list of version
     */
    List<Version> selectArchive( );

    /**
     * Returns an integer
     * @param nIdAtome the atome id
     * @return A maximum version number
     */
    int findMaxVersion( int nIdAtome );

    /**
     * Returns a version object
     * @param nKey the version id
     * @return A version object with the id nKey
     */
    Version findByPrimaryKey( int nKey );

    /**
     * Returns a version object
     * @param nIdAtome the atome id
     * @param numVersion the version number
     * @return A version object associated with the same atome id and version number
     */
    Version findByAtomeAndNumVersion( int nIdAtome, int numVersion );

    /**
     * Returns a list of version objects
     * @param nIdPlu the plu id
     * @param nIdFolder the folder id
     * @return A list of version associated with the same plu id and folder id
     */
    List<Version> findByPluAndFolder( int nIdPlu, int nIdFolder );

    /**
     * Finds by filter
     * @param atomeFilter the atome filter
     * @param versionFilter the version filter
     * @return the version list
     */
    List<Version> findByFilter( AtomeFilter atomeFilter, VersionFilter versionFilter );

    /**
     * Finds all versions
     * @return the version list
     */
    List<Version> findAll( );
}
