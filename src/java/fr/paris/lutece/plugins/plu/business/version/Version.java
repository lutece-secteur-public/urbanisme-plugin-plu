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
package fr.paris.lutece.plugins.plu.business.version;

import fr.paris.lutece.plugins.plu.business.atome.Atome;
import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;

//import java.sql.Date;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * This class represents business object Version
 * @author vLopez
 */
@Entity
@Table( name = "version_atome" )
public class Version
{
    /** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_version_atome_sequence";

    /** Unique value */
    private static final String JPA_COLUMN_NAME = "plu_version_atome_id";

    //Constants
    public static final String RESOURCE_TYPE = "VERSION_RESOURCE";
    private int _id;
    private Atome _atome;
    private int _version;
    private Date _d1;
    private Date _d2;
    private Date _d3;
    private Date _d4;

    /**
     * Returns the identifier of this version
     * @return the version identifier
     */
    @TableGenerator( table = PluJPAUtils.SEQUENCE_TABLE_NAME, name = JPA_SEQUENCE_NAME, pkColumnValue = JPA_COLUMN_NAME, allocationSize = 1 )
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = JPA_SEQUENCE_NAME )
    @Column( name = "id_version" )
    public int getId(  )
    {
        return _id;
    }

    /**
     * Sets the identifier of the version to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the atome of this version
     * @return the version atome
     */
    @OneToOne( fetch = FetchType.EAGER)
    @JoinColumn( name="id_atome", referencedColumnName="id_atome" )
    public Atome getAtome(  )
    {
        return _atome;
    }

    /**
     * Sets the atome of the version to the specified Atome
     * @param atome the new atome
     */
    public void setAtome( Atome atome )
    {
        _atome = atome;
    }

    /**
     * Returns the version of this version
     * @return the version version
     */
    @Column( name = "num_version" )
    public int getVersion(  )
    {
        return _version;
    }

    /**
     * Sets the version of the version to the specified integer
     * @param version the new version
     */
    public void setVersion( int version )
    {
        _version = version;
    }

    /**
     * Returns the d1 of this version
     * @return the version d1
     */
    @Column( name = "date_approbation" )
    public Date getD1(  )
    {
        return _d1;
    }

    /**
     * Sets the d1 of the version to the specified date
     * @param d1 the new d1
     */
    public void setD1( Date d1 )
    {
        _d1 = d1;
    }

    /**
     * Returns the d2 of this version
     * @return the version d2
     */
    @Column( name = "date_application" )
    public Date getD2(  )
    {
        return _d2;
    }

    /**
     * Sets the d2 of the version to the specified date
     * @param d2 the new d2
     */
    public void setD2( Date d2 )
    {
        _d2 = d2;
    }

    /**
     * Returns the d3 of this version
     * @return the version d3
     */
    @Column( name = "date_evolution" )
    public Date getD3(  )
    {
        return _d3;
    }

    /**
     * Sets the d3 of the version to the specified date
     * @param d3 the new d3
     */
    public void setD3( Date d3 )
    {
        _d3 = d3;
    }

    /**
     * Returns the d4 of this version
     * @return the version d4
     */
    @Column( name = "date_archivage" )
    public Date getD4(  )
    {
        return _d4;
    }

    /**
     * Sets the d4 of the version to the specified date
     * @param d4 the new d4
     */
    public void setD4( Date d4 )
    {
        _d4 = d4;
    }
}
