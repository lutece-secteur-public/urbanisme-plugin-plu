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
 * FileFilter
 * @author vLopez
 */
public class FileFilter
{
	private String _name = null;
    private String _title = null;
    private String _mimeType = null;
    
    /**
     * Get the file name filtered
     * @return the file name
     */
    public String get_name(  )
    {
        return _name;
    }

    /**
     * Set the file name to filter
     * @param _name the file name
     */
    public void set_name( String _name )
    {
        this._name = _name;
    }
    
    /**
     *
     * @return true if the filter contain a file name
     */
    public boolean containsName(  )
   	{
       return ( _name != null );
   	}
   
    /**
     * Get the file title filtered
     * @return the file title
     */
    public String get_title(  )
    {
        return _title;
    }

    /**
     * Set the file title to filter
     * @param _title the file title
     */
    public void set_title( String _title )
    {
        this._title = _title;
    }
    
    /**
     *
     * @return true if the filter contain a file title
     */
    public boolean containsTitle(  )
   	{
	   return ( _title != null );
   	}
   
   /**
    * Get the file mime type filtered
    * @return the file mime type
    */
   public String get_mimeType(  )
   {
       return _mimeType;
   }

   /**
    * Set the file mime type to filter
    * @param _mimeType the file mime type
    */
   public void set_mimeType( String _mimeType )
   {
       this._mimeType = _mimeType;
   }
   
   /**
    *
   	* @return true if the filter contain a file mime type
   	*/
   public boolean containsMimeType(  )
  	{
	  return ( _mimeType != null );
  	}
}
