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
package fr.paris.lutece.plugins.plu.business.folder;

import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.jpa.AbstractLuteceHome;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for Folder objects
 * @author vLopez
 */
public class FolderHome extends AbstractLuteceHome<Integer, Folder, IFolderDAO> implements IFolderHome
{
    /**
     * Returns a folder object
     * @return The last folder created
     */
    public Folder findLastFolder(  )
    {
        return getDao(  ).findLastFolder(  );
    }

    /**
     * Returns a folder object
     * @param title the folder title
     * @return A folder object with the same title
     */
    public Folder findForTestTitle( String title )
    {
        return getDao(  ).findForTestTitle( title );
    }

    /**
     * Returns a folder object
     * @param nIdAtome the atome id
     * @return A folder object associated with the atome id
     */
    public Folder findByAtome( int nIdAtome )
    {
        return getDao(  ).findByAtome( nIdAtome );
    }

    /**
     * Returns a folder object
     * @param nIdVersion the version id
     * @return A folder object associated with the version id
     */
    public Folder findByVersion( int nIdVersion )
    {
        return getDao(  ).findByVersion( nIdVersion );
    }

    /**
     * Returns a folder object
     * @param nKey the folder id
     * @return A folder object which has a child or an atome
     */
    public Folder findForDelete( int nKey )
    {
        return getDao(  ).findForDelete( nKey );
    }

    /**
     * Returns a list of folder objects
     * @param pluId the plu id
     * @return A list of folder with the same plu id
     */
    public List<Folder> findByPluId( int pluId )
    {
        return getDao(  ).findByPluId( pluId );
    }

    /**
     * Returns a list of folder objects
     * @param parentId the folder parent id
     * @return A list of folder with the same folder parent id
     */
    public List<Folder> findByParent( int parentId )
    {
        return getDao(  ).findByParent( parentId );
    }

    /**
     * Finds by filter
     * @param filter the filter
     * @return the folder list
     */
    public List<Folder> findByFilter( FolderFilter filter )
    {
        return getDao(  ).findByFilter( filter );
    }

    /**
     * Loads the image representing the folder
     *
     * @param nIdFolder int identifier of the Folder to fetch
     * @return the image resource
     */
    public ImageResource getImageResource( int nIdFolder )
    {
        return getDao(  ).getImageResource( nIdFolder );
    }

    /**
     * Loads the html specifique representing the folder
     *
     * @param nIdFolder int identifier of the Folder to fetch
     * @return the html specifique
     */
    public ImageResource getHtmlResource( int nIdFolder )
    {
        return getDao(  ).getHtmlResource( nIdFolder );
    }
}
