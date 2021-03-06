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
package fr.paris.lutece.plugins.plu.business.atome;


/**
 * AtomeFilter
 * @author vLopez
 */
public class AtomeFilter
{
    private Integer _id = -1;
    private String _name;
    private String _title;

    /**
     * Get the atome id filtered
     * @return the atome title
     */
    public Integer getId(  )
    {
        return _id;
    }

    /**
     * Set the atome id to filter
     * @param id the atome id
     */
    public void setId( Integer id )
    {
        this._id = id;
    }

    /**
     *
     * @return true if the filter contain an id
     */
    public boolean containsId(  )
    {
        return ( _id != -1 );
    }

    /**
     * Get the atome name filtered
     * @return the atome name
     */
    public String getName(  )
    {
        return _name;
    }

    /**
     * Set the atome name to filter
     * @param name the atome name
     */
    public void setName( String name )
    {
        this._name = name;
    }

    /**
     *
     * @return true if the filter contain a name
     */
    public boolean containsName(  )
    {
        return ( _name != null );
    }

    /**
     * Get the atome title filtered
     * @return the atome title
     */
    public String getTitle(  )
    {
        return _title;
    }

    /**
     * Set the atome title to filter
     * @param title the atome title
     */
    public void setTitle( String title )
    {
        this._title = title;
    }

    /**
     *
     * @return true if the filter contain a title
     */
    public boolean containsTitle(  )
    {
        return ( _title != null );
    }
}
