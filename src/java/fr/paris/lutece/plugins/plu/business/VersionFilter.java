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
package fr.paris.lutece.plugins.plu.business;

import java.sql.Date;


public class VersionFilter
{
    private Integer _id;
    private Integer _version;
    private Date _d1;
    private Date _d2;
    private Date _d3;
    private Date _d4;
    private Atome _atome;

    public Integer get_id(  )
    {
        return _id;
    }

    public void set_id( Integer _id )
    {
        this._id = _id;
    }

    public Integer get_version(  )
    {
        return _version;
    }

    public void set_version( Integer _version )
    {
        this._version = _version;
    }

    public Date get_d1(  )
    {
        return _d1;
    }

    public void set_d1( Date _d1 )
    {
        this._d1 = _d1;
    }

    public Date get_d2(  )
    {
        return _d2;
    }

    public void set_d2( Date _d2 )
    {
        this._d2 = _d2;
    }

    public Date get_d3(  )
    {
        return _d3;
    }

    public void set_d3( Date _d3 )
    {
        this._d3 = _d3;
    }

    public Date get_d4(  )
    {
        return _d4;
    }

    public void set_d4( Date _d4 )
    {
        this._d4 = _d4;
    }

    public Atome get_atome(  )
    {
        return _atome;
    }

    public void set_atome( Atome _atome )
    {
        this._atome = _atome;
    }
}
