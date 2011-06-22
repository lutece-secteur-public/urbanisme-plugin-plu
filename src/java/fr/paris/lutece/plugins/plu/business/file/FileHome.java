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
package fr.paris.lutece.plugins.plu.business.file;

import fr.paris.lutece.plugins.plu.business.atome.AtomeFilter;
import fr.paris.lutece.portal.service.jpa.AbstractLuteceHome;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for File objects
 * @author vLopez
 */
public class FileHome extends AbstractLuteceHome<Integer, File, IFileDAO> implements IFileHome
{
    //    /**
    //     * Create a new file object
    //     * @param file the new file object
    //     */
    //    public void create( File file )
    //    {
    //        getDao(  ).create( file );
    //    }
    //
    //    /**
    //     * Update a new file object
    //     * @param file the file object
    //     */
    //    public void update( File file )
    //    {
    //        getDao(  ).update( file );
    //    }

    /**
     * Remove a new file object
     * @param file the file object
     */
    public void remove( File file )
    {
        getDao(  ).remove( file );
    }

    /**
     * Returns a list of file objects
     * @return A list of all file
     */
    public List<File> findAll(  )
    {
        return getDao(  ).findAll(  );
    }

    /**
     * Returns a list of file objects
     * @return A list of all mime type file
     */
    public List<File> findAllMimeType(  )
    {
        return getDao(  ).findAllMimeType(  );
    }

    /**
     * Returns a list of file objects
     * @param nIdVersion the version id
     * @return A list of file associated with the same version id
     */
    public List<File> findByVersion( int nIdVersion )
    {
        return getDao(  ).findByVersion( nIdVersion );
    }

    /**
     * Finds by filter
     * @param fileFilter the file filter
     * @param atomeFilter the atome filter
     * @return the folder list
     */
    public List<File> findByFilter( FileFilter fileFilter, AtomeFilter atomeFilter )
    {
        return getDao(  ).findByFilter( fileFilter, atomeFilter );
    }
}
