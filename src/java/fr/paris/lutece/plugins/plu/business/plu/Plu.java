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
package fr.paris.lutece.plugins.plu.business.plu;

import fr.paris.lutece.plugins.plu.business.etat.Etat;
import fr.paris.lutece.plugins.plu.business.type.Type;
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


/**
 * This class represents business object Plu
 * @author vLopez
 */
@Entity
@Table( name = "plu" )
public class Plu
{
	/** Constants */
	
    public static final String RESOURCE_TYPE = "PLU_RESOURCE";
    /** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_sequence";
    /** Unique value */
    private static final String JPA_COLUMN_NAME = "plu_id";
    private int _id;
    private Type _type;
    private String _cause;
    private String _reference;
    private Date _dj;
    private Date _da;
    private Date _dg;
    private Etat _etat;

    /**
     * Returns the identifier of this plu
     * @return the plu identifier
     */
    @TableGenerator( table = PluJPAUtils.SEQUENCE_TABLE_NAME, name = JPA_SEQUENCE_NAME, pkColumnValue = JPA_COLUMN_NAME, allocationSize = 1 )
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = JPA_SEQUENCE_NAME )
    @Column( name = "id_plu" )
    public int getId(  )
    {
        return _id;
    }

    /**
     * Sets the identifier of the plu to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the type of this plu
     * @return the plu type
     */
    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "id_type_acte_juridique" )
    public Type getType(  )
    {
        return _type;
    }

    /**
     * Sets the type of the plu to the specified Type
     * @param type the new type
     */
    public void setType( Type type )
    {
        _type = type;
    }

    /**
     * Returns the cause of this plu
     * @return the plu cause
     */
    @Column( name = "nom_acte_juridique" )
    public String getCause(  )
    {
        return _cause;
    }

    /**
     * Sets the cause of the plu to the specified string
     * @param cause the new cause
     */
    public void setCause( String cause )
    {
        _cause = cause;
    }

    /**
     * Returns the reference of this plu
     * @return the plu reference
     */
    @Column( name = "ref_deliberation" )
    public String getReference(  )
    {
        return _reference;
    }

    /**
    * Sets the reference of the plu to the specified String
    * @param reference the new reference
    */
    public void setReference( String reference )
    {
        _reference = reference;
    }

    /**
     * Returns the dj of this plu
     * @return the plu dj
     */
    @Column( name = "dj" )
    public Date getDj(  )
    {
        return _dj;
    }

    /**
     * Sets the dj of the plu to the specified date
     * @param dj the new dj
     */
    public void setDj( Date dj )
    {
        _dj = dj;
    }

    /**
     * Returns the da of this plu
     * @return the plu da
     */
    @Column( name = "da" )
    public Date getDa(  )
    {
        return _da;
    }

    /**
     * Sets the date generation of the plu to the specified date
     * @param dg the new date generation
     */
    public void setDg( Date dg )
    {
        _dg = dg;
    }

    /**
     * Returns the date generation of this plu
     * @return the plu dg
     */
    @Column( name = "date_generation" )
    public Date getDg(  )
    {
        return _dg;
    }

    /**
     * Sets the da of the plu to the specified date
     * @param da the new da
     */
    public void setDa( Date da )
    {
        _da = da;
    }

    /**
     * Returns the etat of this plu
     * @return the plu etat
     */
    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "id_etat_generation" )
    public Etat getEtat(  )
    {
        return _etat;
    }

    /**
     * Sets the etat of the plu to the specified Etat
     * @param etat the new etat
     */
    public void setEtat( Etat etat )
    {
        _etat = etat;
    }
}
