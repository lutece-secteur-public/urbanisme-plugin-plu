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
package fr.paris.lutece.plugins.plu.business.atome;

import fr.paris.lutece.plugins.plu.business.folder.Folder;


/**
 * AtomeFilter
 * @author vLopez
 */
public class AtomeFilter
{
    private Integer _id;
    private String _title;
    private String _description;
    private Folder _folder;

    /**
     * Get the atome id filtered
     * @return the atome id
     */
    public Integer get_id(  )
    {
        return _id;
    }

    /**
     * Set the atome id to filter
     * @param _id the atome id
     */
    public void set_id( Integer _id )
    {
        this._id = _id;
    }

    /**
     * Get the atome title filtered
     * @return the atome title
     */
    public String get_title(  )
    {
        return _title;
    }

    /**
     * Set the atome title to filter
     * @param _title the atome title
     */
    public void set_title( String _title )
    {
        this._title = _title;
    }

    /**
     * Get the atome description filtered
     * @return the atome description
     */
    public String get_description(  )
    {
        return _description;
    }

    /**
     * Set the atome description to filter
     * @param _description the atome description
     */
    public void set_description( String _description )
    {
        this._description = _description;
    }

    /**
     * Get the atome folder filtered
     * @return the atome folder
     */
    public Folder get_folder(  )
    {
        return _folder;
    }

    /**
     * Set the atome folder to filter
     * @param _folder the atome folder
     */
    public void set_folder( Folder _folder )
    {
        this._folder = _folder;
    }
}
