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
package fr.paris.lutece.plugins.plu.business.history;

import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * This class represents business object History
 * @author vLopez
 */
@Entity
@Table( name = "historique" )
public class History
{
    /** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_historique_sequence";

    /** Unique value */
    private static final String JPA_COLUMN_NAME = "plu_historique_id";

    //Constants
    public static final String RESOURCE_TYPE = "HISTORY_RESOURCE";
    private int _id;
    private int _plu;
    private int _folder;
    private int _atome;
    private Date _dc;
    private String _description;

    /**
     * Returns the identifier of this history
     * @return the history identifier
     */
    @TableGenerator( table = PluJPAUtils.SEQUENCE_TABLE_NAME, name = JPA_SEQUENCE_NAME, pkColumnValue = JPA_COLUMN_NAME, allocationSize = 1 )
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = JPA_SEQUENCE_NAME )
    @Column( name = "id_histo" )
    public int getId(  )
    {
        return _id;
    }

    /**
     * Sets the identifier of the history to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the identifier of the plu
     * @return the plu identifier
     */
    @Column( name = "id_plu" )
    public int getPlu(  )
    {
        return _plu;
    }

    /**
     * Sets the identifier of the plu to the specified integer
     * @param plu the new plu identifier
     */
    public void setPlu( int plu )
    {
        _plu = plu;
    }

    /**
     * Returns the identifier of the folder
     * @return the folder identifier
     */
    @Column( name = "id_dossier" )
    public int getFolder(  )
    {
        return _folder;
    }

    /**
     * Sets the identifier of the folder to the specified integer
     * @param folder the new folder identifier
     */
    public void setFolder( int folder )
    {
        _folder = folder;
    }

    /**
     * Returns the identifier of the atome
     * @return the atome identifier
     */
    @Column( name = "id_atome" )
    public int getAtome(  )
    {
        return _atome;
    }

    /**
     * Sets the identifier of the atome to the specified integer
     * @param atome the new atome identifier
     */
    public void setAtome( int atome )
    {
        _atome = atome;
    }

    /**
     * Returns the dc of this history
     * @return the history dc
     */
    @Column( name = "date_correction" )
    public Date getDc(  )
    {
        return _dc;
    }

    /**
     * Sets the dc of the history to the specified date
     * @param dc the new dc
     */
    public void setDc( Date dc )
    {
        _dc = dc;
    }

    /**
     * Returns the name of this history
     * @return the history name
     */
    @Column( name = "description" )
    public String getDescription(  )
    {
        return _description;
    }

    /**
     * Sets the description of the history to the specified string
     * @param description the new description
     */
    public void setDescription( String description )
    {
        _description = description;
    }
}
