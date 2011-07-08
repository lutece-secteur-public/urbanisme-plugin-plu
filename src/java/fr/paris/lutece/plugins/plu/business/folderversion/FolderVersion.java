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
package fr.paris.lutece.plugins.plu.business.folderVersion;

import fr.paris.lutece.plugins.plu.business.folder.Folder;
import fr.paris.lutece.plugins.plu.business.version.Version;
import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;

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
 * This class represents business object FolderVersion
 * @author vLopez
 */
@Entity
@Table( name = "PLU_DOSSIER_VERSION_ATOME" )
public class FolderVersion
{
	/** Constants */
	
    public static final String RESOURCE_TYPE = "DOSSIER_VERSION_RESOURCE";
    /** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_dossier_version_atome_sequence";
    /** Unique value */
    private static final String JPA_COLUMN_NAME = "plu_dossier_version_atome_id";    
    private int _id;
    private Version _version;
    private Folder _folder;

    /**
     * Returns the identifier of this file
     * @return the file identifier
     */
    @TableGenerator( table = PluJPAUtils.SEQUENCE_TABLE_NAME, name = JPA_SEQUENCE_NAME, pkColumnValue = JPA_COLUMN_NAME, allocationSize = 1 )
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = JPA_SEQUENCE_NAME )
    @Column( name = "id_dossier_version" )
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
     * Returns the version of this FolderVersion
     * @return the FolderVersion version
     */
    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "id_version" )
    public Version getVersion(  )
    {
        return _version;
    }

    /**
     * Sets the version of the FolderVersion to the specified Version
     * @param version the new version
     */
    public void setVersion( Version version )
    {
        _version = version;
    }

    /**
     * Returns the folder of this FolderVersion
     * @return the FolderVersion folder
     */
    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "id_dossier" )
    public Folder getFolder(  )
    {
        return _folder;
    }

    /**
     * Sets the folder of the FolderVersion to the specified Folder
     * @param folder the new folder
     */
    public void setFolder( Folder folder )
    {
        _folder = folder;
    }
}
