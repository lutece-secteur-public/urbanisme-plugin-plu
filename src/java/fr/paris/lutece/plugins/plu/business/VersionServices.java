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

import fr.paris.lutece.portal.service.plugin.Plugin;

//import java.sql.Date;
import java.util.Date;
import java.util.Collection;
import java.util.List;


public class VersionServices implements IVersionServices
{
    IVersionHome _home;

    public IVersionHome getHome(  )
    {
        return _home;
    }

    public void setHome( IVersionHome _home )
    {
        this._home = _home;
    }

    public void create( Version version, Plugin plugin )
    {
        _home.create( version );
    }

    public void remove( Version version, Plugin plugin )
    {
        int versionId = version.getId(  );
        _home.remove( versionId );
    }

    public void update( Version version, Plugin plugin )
    {
        _home.update( version );
    }

    public Collection<Version> findAll( Plugin plugin )
    {
        return _home.findAll(  );
    }

    public Version findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _home.findByPrimaryKey( nKey );
    }

    public List<Version> findByDateAndParent( Date date, int idParent )
    {
        return _home.findByDateAndParent( date, idParent );
    }

    public List<Version> findByD3D4( Date da )
    {
    	return _home.findByD3D4( da );
    }
    
    public List<Version> findByD2( Date da )
    {
    	return _home.findByD2( da );
    }
    
    /*public List<Version> findByFilter( VersionFilter filter )
    {
        return _home.findByFilter( filter );
    }*/
}