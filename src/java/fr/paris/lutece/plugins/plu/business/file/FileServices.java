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
package fr.paris.lutece.plugins.plu.business.file;

import fr.paris.lutece.plugins.plu.business.atome.AtomeFilter;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;


/**
 * FileServices
 * @author vLopez
 */
@Transactional
public class FileServices implements IFileServices
{
    private IFileHome _home;

    /**
         * @return the _home
         */
    public IFileHome getHome(  )
    {
        return _home;
    }

    /**
     * @param home the _home to set
     */
    public void setHome( IFileHome home )
    {
        _home = home;
    }

    /**
     * Create a new file object
     * @param file the new file object
     */
    public void create( File file )
    {
        _home.create( file );
    }

    /**
     * Update a file object
     * @param file the file object
     */
    public void update( File file )
    {
        _home.update( file );
    }

    /**
     * Remove a file object
     * @param file the file object
     */
    public void remove( File file )
    {
        int key = file.getId(  );
        _home.remove( key );
    }

    /**
     * Returns a list of file objects
     * @return A list of all file
     */
    public List<File> findAll(  )
    {
        return _home.findAll(  );
    }

    /**
     * Returns a list of file objects
     * @return A list of all mime type file
     */
    public List<String> findAllMimeType(  )
    {
        return _home.findAllMimeType(  );
    }

    /**
     * Returns a list of file objects
     * @param nIdVersion the version id
     * @return A list of file associated with the same version id
     */
    public List<File> findByVersion( int nIdVersion )
    {
        return _home.findByVersion( nIdVersion );
    }

    /**
     * Finds by filter
     * @param fileFilter the file filter
     * @param atomeFilter the atome filter
     * @return the folder list
     */
    public List<File> findByFilter( FileFilter fileFilter, AtomeFilter atomeFilter )
    {
        return _home.findByFilter( fileFilter, atomeFilter );
    }
}
