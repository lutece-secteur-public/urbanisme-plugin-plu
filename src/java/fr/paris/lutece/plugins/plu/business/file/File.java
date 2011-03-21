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
package fr.paris.lutece.plugins.plu.business.file;

import fr.paris.lutece.plugins.plu.business.version.Version;
import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * This class represents business object File
 * @author vLopez
 */
@Entity
@Table( name = "plu_file" )
public class File
{
    /** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_file_sequence";

    //constantes
    private static final String JPA_COLUMN_NAME = "plu_file_id";
    public static final String RESOURCE_TYPE = "FILE_RESOURCE";
    private int _id;
    private String _name;
    private String _title;
    private String _mimeType;
    private long _size;
    private byte[] _file;
    private Version _version;

    /**
     * Returns the identifier of this file
     * @return the file identifier
     */
    @TableGenerator( table = PluJPAUtils.SEQUENCE_TABLE_NAME, name = JPA_SEQUENCE_NAME, pkColumnValue = JPA_COLUMN_NAME, allocationSize = 1 )
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = JPA_SEQUENCE_NAME )
    @Column( name = "id" )
    public int getId(  )
    {
        return _id;
    }

    /**
     * Sets the identifier of the file to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the name of this file
     * @return the file name
     */
    @Column( name = "name" )
    public String getName(  )
    {
        return _name;
    }

    /**
     * Sets the name of the file to the specified string
     * @param name the new name
     */
    public void setName( String name )
    {
        _name = name;
    }

    @Column( name = "title" )
    public String getTitle(  )
    {
        return _title;
    }

    /**
     * Sets the title of the file to the specified string
     * @param title the new title
     */
    public void setTitle( String title )
    {
        _title = title;
    }

    /**
     * Returns the mime type of this file
     * @return the file mime type
     */
    @Column( name = "mimeType" )
    public String getMimeType(  )
    {
        return _mimeType;
    }

    /**
     * Sets the mime type of the file to the specified string
     * @param mimeType the new mime type
     */
    public void setMimeType( String mimeType )
    {
        _mimeType = mimeType;
    }

    /**
     * Returns the size of this file
     * @return the file size
     */
    @Column( name = "size" )
    public long getSize(  )
    {
        return _size;
    }

    /**
     * Sets the size of the file to the specified long
     * @param size the new size
     */
    public void setSize( long size )
    {
        _size = size;
    }

    /**
     * Returns the physical file of this file
     * @return the physical file
     */
    @Lob
    @Column( name = "file" )
    public byte[] getFile(  )
    {
        return _file;
    }

    /**
     * Sets the physical file of the file to the specified byte
     * @param file the new physical file
     */
    public void setFile( byte[] file )
    {
        _file = file;
    }

    /**
     * Returns the version of this file
     * @return the file version
     */
    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "version" )
    public Version getVersion(  )
    {
        return _version;
    }

    /**
     * Sets the version of the file to the specified Version
     * @param version the new version
     */
    public void setVersion( Version version )
    {
        _version = version;
    }
}
