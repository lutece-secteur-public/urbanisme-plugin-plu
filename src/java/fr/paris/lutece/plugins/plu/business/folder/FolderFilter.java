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


/**
 * FolderFilter
 * @author vLopez
 */
public class FolderFilter
{
    private Integer _plu = -1;
    private String _title;

    /**
     * Get the plu id filtered
     * @return the plu id
     */
    public Integer getPlu(  )
    {
        return _plu;
    }

    /**
     * Set the plu id to filter
     * @param plu the plu id
     */
    public void setPlu( Integer plu )
    {
        this._plu = plu;
    }

    /**
     *
     * @return true if the filter contain an id plu
     */
    public boolean containsPlu(  )
    {
        return ( _plu != -1 );
    }

    /**
     * Get the folder title filtered
     * @return the folder title
     */
    public String getTitle(  )
    {
        return _title;
    }

    /**
     * Set the folder title to filter
     * @param title the folder title
     */
    public void setTitle( String title )
    {
        this._title = title;
    }

    /**
     *
     * @return true if the filter contain a folder title
     */
    public boolean containsTitle(  )
    {
        return ( _title != null );
    }
}
