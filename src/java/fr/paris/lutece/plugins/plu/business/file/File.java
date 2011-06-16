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


/**
 * This class represents business object File
 * @author vLopez
 */
public class File
{
    //constantes
    public static final String RESOURCE_TYPE = "FICHIER_RESOURCE";
    private int _id;
    private int _order;
    private int _version;
    private String _name;
    private String _title;
    private String _mimeType;
    private int _size;
    private byte[] _file;
    private String _eps;

    /**
     * Returns the identifier of this file
     * @return the file identifier
     */
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
     * Returns the order of this file
     * @return the file order
     */
    public int getOrder(  )
    {
        return _order;
    }

    /**
     * Sets the order of the file to the specified integer
     * @param order the new order
     */
    public void setOrder( int order )
    {
    	_order = order;
    }

    /**
     * Returns the identifier version of this file
     * @return the identifier version
     */
    public int getVersion(  )
    {
        return _version;
    }

    /**
     * Sets the identifier version of the file to the specified integer
     * @param version the new identifier version
     */
    public void setVersion( int version )
    {
        _version = version;
    }
    
    /**
     * Returns the name of this file
     * @return the file name
     */
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

    /**
     * Returns the title of this file
     * @return the file title
     */
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
    public int getSize(  )
    {
        return _size;
    }

    /**
     * Sets the size of the file to the specified long
     * @param size the new size
     */
    public void setSize( int size )
    {
        _size = size;
    }

    /**
     * Returns the physical file of this file
     * @return the physical file
     */
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
     * Returns the eps value of this file
     * @return the file eps value
     */
    public String getEPS(  )
    {
        return _eps;
    }

    /**
     * Sets the eps value of the file to the specified varchar
     * @param eps the new eps value
     */
    public void setEPS( String eps )
    {
        _eps = eps;
    }
}
