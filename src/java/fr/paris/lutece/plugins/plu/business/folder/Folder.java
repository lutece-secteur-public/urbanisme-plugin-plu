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
package fr.paris.lutece.plugins.plu.business.folder;

import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * This class represents business object Folder
 * @author vLopez
 */
@Entity
@Table( name = "PLU_DOSSIER" )
public class Folder
{
    /** Constants */
    public static final String RESOURCE_TYPE = "DOSSIER_RESOURCE";

    /** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_dossier_sequence";

    /** Unique value */
    private static final String JPA_COLUMN_NAME = "plu_dossier_id";
    private int _id;
    private int _plu;
    private int _parentFolder;
    private String _title;
    private String _description;
    private byte[] _img;
    private String _html;

    /**
     * Returns the identifier of this folder
     * @return the folder identifier
     */
    @TableGenerator( table = PluJPAUtils.SEQUENCE_TABLE_NAME, name = JPA_SEQUENCE_NAME, pkColumnValue = JPA_COLUMN_NAME, allocationSize = 1 )
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = JPA_SEQUENCE_NAME )
    @Column( name = "id_dossier" )
    public int getId(  )
    {
        return _id;
    }

    /**
     * Sets the identifier of the folder to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the plu of this folder
     * @return the plu
     */
    @Column( name = "id_plu" )
    public int getPlu(  )
    {
        return _plu;
    }

    /**
     * Sets the plu of the folder to the specified Folder
     * @param plu the new plu
     */
    public void setPlu( int plu )
    {
        _plu = plu;
    }

    /**
     * Returns the parent folder of this folder
     * @return the parent folder
     */
    @Column( name = "id_dossier_parent" )
    public int getParentFolder(  )
    {
        return _parentFolder;
    }

    /**
     * Sets the parent folder of the folder to the specified Folder
     * @param parentFolder the new parent folder
     */
    public void setParentFolder( int parentFolder )
    {
        _parentFolder = parentFolder;
    }

    /**
     * Returns the title of this folder
     * @return the folder title
     */
    @Column( name = "titre" )
    public String getTitle(  )
    {
        return _title;
    }

    /**
     * Sets the title of the folder to the specified string
     * @param title the new title
     */
    public void setTitle( String title )
    {
        _title = title;
    }

    /**
     * Returns the description of this folder
     * @return the folder description
     */
    @Column( name = "description" )
    public String getDescription(  )
    {
        return _description;
    }

    /**
     * Sets the description of the folder to the specified string
     * @param description the new description
     */
    public void setDescription( String description )
    {
        _description = description;
    }

    /**
     * Returns the image of this folder
     * @return the folder image
     */
    @Column( name = "image" )
    public byte[] getImg(  )
    {
        return _img;
    }

    /**
    * Sets the image of the folder to the specified byte
    * @param img the new image
    */
    public void setImg( byte[] img )
    {
        _img = img;
    }

    /**
     * Returns the html specific of this folder
     * @return the folder html specific
     */
    @Column( name = "html_specifique" )
    public String getHtml(  )
    {
        return _html;
    }

    /**
    * Sets the html specific of the folder to the specified byte
    * @param html the new html specific
    */
    public void setHtml( String html )
    {
        _html = html;
    }
}
