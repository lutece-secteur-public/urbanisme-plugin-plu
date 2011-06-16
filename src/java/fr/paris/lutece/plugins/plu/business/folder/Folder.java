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


/**
 * This class represents business object Folder
 * @author vLopez
 */
public class Folder
{
    //constantes
    public static final String RESOURCE_TYPE = "DOSSIER_RESOURCE";
    private int _id;
    private int _plu;
    private int _parentFolder;
    private String _title;
    private String _description;
    private byte[] _img;
    private byte[] _html;

    /**
     * Returns the identifier of this folder
     * @return the folder identifier
     */
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
    public byte[] getHtml(  )
    {
        return _html;
    }

    /**
    * Sets the html specific of the folder to the specified byte
    * @param html the new html specific
    */
    public void setHtml( byte[] html )
    {
        _html = html;
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
