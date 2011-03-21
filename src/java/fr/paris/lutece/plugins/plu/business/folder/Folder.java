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
 * This class represents business object Folder
 * @author vLopez
 */
@Entity
@Table( name = "plu_folder" )
public class Folder
{
    /** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_folder_sequence";

    //constantes
    private static final String JPA_COLUMN_NAME = "plu_folder_id";
    public static final String RESOURCE_TYPE = "FOLDER_RESOURCE";
    private int _id;
    private String _title;
    private String _description;
    private byte[] _img;
    private Folder _parentFolder;

    /**
     * Returns the identifier of this folder
     * @return the folder identifier
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
     * Sets the identifier of the folder to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the title of this folder
     * @return the folder title
     */
    @Column( name = "title" )
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
     * Returns the parent folder of this folder
     * @return the parent folder
     */
    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "parentFolder", columnDefinition = "INT NOT NULL DEFAULT '0'" )
    public Folder getParentFolder(  )
    {
        return _parentFolder;
    }

    /**
     * Sets the parent folder of the folder to the specified Folder
     * @param parentFolder the new parent folder
     */
    public void setParentFolder( Folder parentFolder )
    {
        _parentFolder = parentFolder;
    }

    /**
     * Returns the image of this folder
     * @return the folder image
     */
    @Lob
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
     * Returns the photo Input Stream.
     * @return InputStream
     * @throws SQLException e
     */

    /*        public InputStream getPhotoContent() throws SQLException
            {
                    if (getImg() == null)
                    {
                            return null;
                    }
    
                    return getImg().getBinaryStream();
            }
    
            /**
             *
             * @param sourceStream - Photo source input stream
             * @throws IOException e
             */

    /*        public void setPhotoContent(InputStream sourceStream) throws IOException
            {
                    Blob createBlob = Hibernate.createBlob(sourceStream);
                    setImg(createBlob);
            }
    */
}
