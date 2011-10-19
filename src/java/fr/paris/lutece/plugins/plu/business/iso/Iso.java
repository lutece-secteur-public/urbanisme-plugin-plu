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
package fr.paris.lutece.plugins.plu.business.iso;

import fr.paris.lutece.plugins.plu.business.plu.Plu;
import fr.paris.lutece.plugins.plu.utils.PluUtils;
import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;


/**
 * This class represents business object Iso
 * @author vLopez
 */
@Entity
@Table( name = "plu_iso" )
public class Iso
{
    /** Constants */
    public static final String RESOURCE_TYPE = "ISO_RESOURCE";

    /** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_iso_sequence";

    /** Unique value */
    private static final String JPA_COLUMN_NAME = "plu_iso_id";
    private int _id;
    private Plu _plu;
    private String _nomConsultation;
    private String _nomImpression;
    private Date _date;
    private Long _tailleConsultation;
    private Long _tailleImpression;

    /**
     * Returns the identifier of this iso
     * @return the iso identifier
     */
    @TableGenerator( table = PluJPAUtils.SEQUENCE_TABLE_NAME, name = JPA_SEQUENCE_NAME, pkColumnValue = JPA_COLUMN_NAME, allocationSize = 1 )
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = JPA_SEQUENCE_NAME )
    @Column( name = "id_iso" )
    public int getId( )
    {
        return _id;
    }

    /**
     * Sets the identifier of the iso to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the id plu of this iso
     * @return the iso id plu
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "id_plu" )
    public Plu getPlu( )
    {
        return _plu;
    }

    /**
     * Sets the id plu of the iso to the specified integer
     * @param plu the new id plu
     */
    public void setPlu( Plu plu )
    {
        _plu = plu;
    }

    /**
     * @return the nom
     */
    @Column( name = "iso_c_nom" )
    public String getNomConsultation( )
    {
        return _nomConsultation;
    }

    /**
     * @param nom the nom to set
     */
    public void setNomConsultation( String nom )
    {
        this._nomConsultation = nom;
    }

    /**
     * @return the nom
     */
    @Column( name = "iso_i_nom" )
    public String getNomImpression( )
    {
        return _nomImpression;
    }

    /**
     * @param nom the nom to set
     */
    public void setNomImpression( String nom )
    {
        this._nomImpression = nom;
    }

    /**
     * @return the date
     */
    @Column( name = "iso_date" )
    public Date getDate( )
    {
        return _date;
    }

    /**
     * @param date the date to set
     */
    public void setDate( Date date )
    {
        this._date = date;
    }

    /**
     * @return the taille
     */
    @Column( name = "iso_c_taille" )
    public Long getTailleConsultation( )
    {
        return _tailleConsultation;
    }

    /**
     * @param taille the taille to set
     */
    public void setTailleConsultation( Long taille )
    {
        this._tailleConsultation = taille;
    }

    /**
     * @return the taille
     */
    @Column( name = "iso_i_taille" )
    public Long getTailleImpression( )
    {
        return _tailleImpression;
    }

    /**
     * @param taille the taille to set
     */
    public void setTailleImpression( Long taille )
    {
        this._tailleImpression = taille;
    }

    /**
     * @return the size
     */
    @Transient
    public String getTailleImpressionString( )
    {
        return PluUtils.formatSize( this._tailleImpression );
    }

    /**
     * @return the size
     */
    @Transient
    public String getTailleConsultationString( )
    {
        return PluUtils.formatSize( this._tailleConsultation );
    }
}
