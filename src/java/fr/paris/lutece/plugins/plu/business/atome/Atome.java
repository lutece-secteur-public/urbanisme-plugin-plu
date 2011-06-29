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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * This class represents business object Atome
 * @author vLopez
 */
@Entity
@Table( name = "atome" )
public class Atome
{
	/** Constants */
    public static final String RESOURCE_TYPE = "ATOME_RESOURCE";
    private int _id;
    private String _name;
    private String _title;
    private String _description;

    /**
     * Returns the identifier of this atome
     * @return the atome identifier
     */
    @Id
    @Column( name = "id_atome" )
    public int getId(  )
    {
        return _id;
    }

    /**
     * Sets the identifier of the atome to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the name of this atome
     * @return the atome name
     */
    @Column( name = "nom" )
    public String getName(  )
    {
        return _name;
    }

    /**
     * Sets the name of the atome to the specified string
     * @param name the new name
     */
    public void setName( String name )
    {
        _name = name;
    }

    /**
     * Returns the title of this atome
     * @return the atome title
     */
    @Column( name = "titre" )
    public String getTitle(  )
    {
        return _title;
    }

    /**
     * Sets the title of the atome to the specified string
     * @param title the new title
     */
    public void setTitle( String title )
    {
        _title = title;
    }

    /**
     * Returns the description of this atome
     * @return the atome description
     */
    @Column( name = "description" )
    public String getDescription(  )
    {
        return _description;
    }

    /**
     * Sets the description of the atome to the specified string
     * @param description the new description
     */
    public void setDescription( String description )
    {
        _description = description;
    }
}
