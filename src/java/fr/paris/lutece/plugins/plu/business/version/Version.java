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
package fr.paris.lutece.plugins.plu.business.version;

import fr.paris.lutece.plugins.plu.business.atome.Atome;
import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;

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
import javax.persistence.Transient;


/**
 * This class represents business object Version
 * @author vLopez
 */
@Entity
@Table( name = "plu_version_atome" )
public class Version implements Comparable
{
    /** Constants */
    public static final String RESOURCE_TYPE = "VERSION_RESOURCE";

    /** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_version_atome_sequence";

    /** Unique value */
    private static final String JPA_COLUMN_NAME = "plu_version_atome_id";
    private int _id;
    private Atome _atome;
    private int _version;
    private Date _d1;
    private Date _d2;
    private Date _d3;
    private Date _d4;
    private char _archive;
    //Set to false by default
    private boolean _atomeHaveSingleVersion = false;

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
    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "id_atome", referencedColumnName = "id_atome" )
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
     * Returns the date approbation of this version
     * @return the version date approbation
     */
    @Column( name = "date_approbation" )
    public Date getD1(  )
    {
        return _d1;
    }

    /**
     * Sets the date approbation of the version to the specified date
     * @param d1 the new date approbation
     */
    public void setD1( Date d1 )
    {
        _d1 = d1;
    }

    /**
     * Returns the date application of this version
     * @return the version date application
     */
    @Column( name = "date_application" )
    public Date getD2(  )
    {
        return _d2;
    }

    /**
     * Sets the date application of the version to the specified date
     * @param d2 the new date application
     */
    public void setD2( Date d2 )
    {
        _d2 = d2;
    }

    /**
     * Returns the date evolution of this version
     * @return the version date evolution
     */
    @Column( name = "date_evolution" )
    public Date getD3(  )
    {
        return _d3;
    }

    /**
     * Sets the date evolution of the version to the specified date
     * @param d3 the new date evolution
     */
    public void setD3( Date d3 )
    {
        _d3 = d3;
    }

    /**
     * Returns the date archivage of this version
     * @return the version date archivage
     */
    @Column( name = "date_archivage" )
    public Date getD4(  )
    {
        return _d4;
    }

    /**
     * Sets the date archivage of the version to the specified date
     * @param d4 the new date archivage
     */
    public void setD4( Date d4 )
    {
        _d4 = d4;
    }

    /**
     * Returns the archive value of this file
     * @return the file archive value
     */
    @Column( name = "a_archiver" )
    public char getArchive(  )
    {
        return _archive;
    }

    /**
     * Sets the archive value of the file to the specified char
     * @param archive the new archive value
     */
    public void setArchive( char archive )
    {
        _archive = archive;
    }

    /**
     * @return the _atomeHaveSingleVersion
     */
    @Transient
    public boolean getAtomeHaveSingleVersion( )
    {
        return _atomeHaveSingleVersion;
    }

    /**
     * @param _atomeHaveSingleVersion the _atomeHaveSingleVersion to set
     */
    public void setAtomeHaveSingleVersion( boolean atomeHaveSingleVersion )
    {
        this._atomeHaveSingleVersion = atomeHaveSingleVersion;
    }

    public int compareTo( Object o )
    {
        Version v = (Version) o;
        int retour = 0;
        if ( this.getAtome( ).getId( ) > v.getAtome( ).getId( ) )
        {
            retour += 100;
        }
        else if ( this.getAtome( ).getId( ) < v.getAtome( ).getId( ) )
        {
            retour -= 100;
        }
        if ( this.getAtome( ).getTitle( ).compareTo( v.getAtome( ).getTitle( ) ) > 0 )
        {
            retour += 10;
        }
        else if ( this.getAtome( ).getTitle( ).compareTo( v.getAtome( ).getTitle( ) ) < 0 )
        {
            retour -= 10;
        }
        if ( this.getVersion( ) > v.getVersion( ) )
        {
            retour += 1;
        }
        else if ( this.getVersion( ) < v.getVersion( ) )
        {
            retour -= 1;
        }

        return retour;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass( ) != obj.getClass( ) )
            return false;
        Version other = (Version) obj;
        if ( _archive != other._archive )
            return false;
        if ( _atome == null )
        {
            if ( other._atome != null )
                return false;
        }
        else if ( !_atome.equals( other._atome ) )
            return false;
        if ( _d1 == null )
        {
            if ( other._d1 != null )
                return false;
        }
        else if ( !_d1.equals( other._d1 ) )
            return false;
        if ( _d2 == null )
        {
            if ( other._d2 != null )
                return false;
        }
        else if ( !_d2.equals( other._d2 ) )
            return false;
        if ( _d3 == null )
        {
            if ( other._d3 != null )
                return false;
        }
        else if ( !_d3.equals( other._d3 ) )
            return false;
        if ( _d4 == null )
        {
            if ( other._d4 != null )
                return false;
        }
        else if ( !_d4.equals( other._d4 ) )
            return false;
        if ( _id != other._id )
            return false;
        if ( _version != other._version )
            return false;
        return true;
    }



}
