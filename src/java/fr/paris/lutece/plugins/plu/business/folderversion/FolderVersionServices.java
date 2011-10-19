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
package fr.paris.lutece.plugins.plu.business.folderversion;

import fr.paris.lutece.plugins.plu.business.folder.Folder;
import fr.paris.lutece.plugins.plu.business.version.Version;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;


/**
 * FolderVersionServices
 * @author vLopez
 */
@Transactional
public class FolderVersionServices implements IFolderVersionServices
{
    private IFolderVersionHome _home;

    /**
         * @return the _home
         */
    public IFolderVersionHome getHome(  )
    {
        return _home;
    }

    /**
     * @param home the _home to set
     */
    public void setHome( IFolderVersionHome home )
    {
        this._home = home;
    }

    /**
     * Create a new folderVersion object
     * @param folderVersion the new folderVersion object
     */
    public void create( FolderVersion folderVersion )
    {
        _home.create( folderVersion );
    }

    /**
     * Update a folderVersion object
     * @param folderVersion the folderVersion object
     */
    public void update( FolderVersion folderVersion )
    {
        _home.update( folderVersion );
    }

    /**
     * Remove a folderVersion object
     * @param folderVersion the folderVersion object
     */
    public void remove( FolderVersion folderVersion )
    {
        int nIdFolderVersion = folderVersion.getId(  );
        _home.remove( nIdFolderVersion );
    }

    /**
     * Returns a list of folderVersion objects
     * @param folder the folder associated
     * @return A list of folderVersion associated with the folder
     */
    public List<FolderVersion> findByFolder( Folder folder )
    {
        return _home.findByFolder( folder );
    }

    /**
     * Returns a folderVersion object
     * @param version the version associated
     * @return A folderVersion object associated with the last folder of the version
     */
    public FolderVersion findByMaxFolderAndVersion( Version version )
    {
        return _home.findByMaxFolderAndVersion( version );
    }
}
