/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
package fr.paris.lutece.plugins.plu.business.version;

import java.util.Date;


/**
 * VersionFilter
 * @author vLopez
 */
public class VersionFilter
{
    private Integer _version = -1;
    private Date _d1;
    private Date _d2;
    private Date _d3;
    private Date _d4;

    /**
     * Get the version version filtered
     * @return the version version
     */
    public Integer getVersion(  )
    {
        return _version;
    }

    /**
     * Set the version version to filter
     * @param version the version version
     */
    public void setVersion( Integer version )
    {
        this._version = version;
    }

    /**
     *
     * @return true if the filter contain a version
     */
    public boolean containsVersion(  )
    {
        return ( _version != -1 );
    }

    /**
     * Get the version d1 filtered
     * @return the version d1
     */
    public Date getD1(  )
    {
        return _d1;
    }

    /**
     * Set the version d1 to filter
     * @param d1 the version d1
     */
    public void setD1( Date d1 )
    {
        this._d1 = d1;
    }

    /**
     *
     * @return true if the filter contain a d1
     */
    public boolean containsD1(  )
    {
        return ( _d1 != null );
    }

    /**
     * Get the version d2 filtered
     * @return the version d2
     */
    public Date getD2(  )
    {
        return _d2;
    }

    /**
     * Set the version d2 to filter
     * @param d2 the version d2
     */
    public void setD2( Date d2 )
    {
        this._d2 = d2;
    }

    /**
     *
     * @return true if the filter contain a d2
     */
    public boolean containsD2(  )
    {
        return ( _d2 != null );
    }

    /**
     * Get the version d3 filtered
     * @return the version d3
     */
    public Date getD3(  )
    {
        return _d3;
    }

    /**
     * Set the version d3 to filter
     * @param d3 the version d3
     */
    public void setD3( Date d3 )
    {
        this._d3 = d3;
    }

    /**
     *
     * @return true if the filter contain a d3
     */
    public boolean containsD3(  )
    {
        return ( _d3 != null );
    }

    /**
     * Get the version d4 filtered
     * @return the version d4
     */
    public Date getD4(  )
    {
        return _d4;
    }

    /**
     * Set the version d4 to filter
     * @param d4 the version d4
     */
    public void setD4( Date d4 )
    {
        this._d4 = d4;
    }

    /**
     *
     * @return true if the filter contain a d4
     */
    public boolean containsD4(  )
    {
        return ( _d4 != null );
    }
}
