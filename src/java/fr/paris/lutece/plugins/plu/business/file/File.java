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
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;

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
    private String _title;
    private String _mimeType;
    private PhysicalFile _file;
    private Version _version;

    @TableGenerator( table = PluJPAUtils.SEQUENCE_TABLE_NAME, name = JPA_SEQUENCE_NAME, pkColumnValue = JPA_COLUMN_NAME, allocationSize = 1 )
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = JPA_SEQUENCE_NAME )
    @Column( name = "id" )
    public int getId(  )
    {
        return _id;
    }

    public void setId( int id )
    {
        _id = id;
    }

    @Column( name = "title" )
    public String getTitle(  )
    {
        return _title;
    }

    public void setTitle( String title )
    {
        _title = title;
    }

    @Column( name = "mimeType" )
    public String getMimeType(  )
    {
        return _mimeType;
    }

    public void setMimeType( String mimeType )
    {
        _mimeType = mimeType;
    }

    /*  @Lob
      @Column( name = "file")
      public PhysicalFile getFile(  )
      {
          return _file;
      }
    
    
      public void setFile( PhysicalFile file )
      {
          _file = file;
      }
      */
    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "version" )
    public Version getVersion(  )
    {
        return _version;
    }

    public void setVersion( Version version )
    {
        _version = version;
    }
}
