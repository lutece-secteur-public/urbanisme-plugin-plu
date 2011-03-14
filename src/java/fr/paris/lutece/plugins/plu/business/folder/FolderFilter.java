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
package fr.paris.lutece.plugins.plu.business.folder;


/**
 * FolderFilter
 * @author vLopez
 */
public class FolderFilter
{
    private Integer _id;
    private String _title;
    private String _description;
    private Folder _parentFolder;

    /**
     * Get the folder id filtered
     * @return the folder id
     */
    public Integer get_id(  )
    {
        return _id;
    }

    /**
     * Set the folder id to filter
     * @param _id the folder id
     */
    public void set_id( Integer _id )
    {
        this._id = _id;
    }

    /**
     * Get the folder title filtered
     * @return the folder title
     */
    public String get_title(  )
    {
        return _title;
    }

    /**
     * Set the folder title to filter
     * @param _title the folder title
     */
    public void set_title( String _title )
    {
        this._title = _title;
    }

    /**
     * Get the folder description filtered
     * @return the folder description
     */
    public String get_description(  )
    {
        return _description;
    }

    /**
     * Set the folder description to filter
     * @param _description the folder description
     */
    public void set_description( String _description )
    {
        this._description = _description;
    }

    /**
     * Get the parent folder filtered
     * @return the parent folder
     */
    public Folder get_parentFolder(  )
    {
        return _parentFolder;
    }

    /**
    * Set the parent folder to filter
    * @param _parentFolder the parent folder
    */
    public void set_parentFolder( Folder _parentFolder )
    {
        this._parentFolder = _parentFolder;
    }
}
