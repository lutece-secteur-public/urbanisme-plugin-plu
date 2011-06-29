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
package fr.paris.lutece.plugins.plu.business.etat;

import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * This class represents business object Etat
 * @author vLopez
 */
@Entity
@Table( name = "etat_generation" )
public class Etat
{
	/** Constants */
	
    public static final String RESOURCE_TYPE = "ETAT_RESOURCE";
    /** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_etat_sequence";
    /** Unique value */
    private static final String JPA_COLUMN_NAME = "plu_etat_id";
    private int _id;
    private String _name;

    /**
     * Returns the identifier of this etat
     * @return the etat identifier
     */
    @TableGenerator( table = PluJPAUtils.SEQUENCE_TABLE_NAME, name = JPA_SEQUENCE_NAME, pkColumnValue = JPA_COLUMN_NAME, allocationSize = 1 )
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = JPA_SEQUENCE_NAME )
    @Column( name = "id_etat" )
    public int getId(  )
    {
        return _id;
    }

    /**
     * Sets the identifier of the etat to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the name of this etat
     * @return the etat name
     */
    @Column( name = "etat" )
    public String getName(  )
    {
        return _name;
    }

    /**
     * Sets the name of the etat to the specified string
     * @param name the new name
     */
    public void setName( String name )
    {
        _name = name;
    }
}
