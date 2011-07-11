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
package fr.paris.lutece.plugins.plu.business.atome;

import java.util.List;


/**
 * AtomeServices
 * @author vLopez
 */
public class AtomeServices implements IAtomeServices
{
    private IAtomeHome _home;

    /**
         * @return the _home
         */
    public IAtomeHome getHome(  )
    {
        return _home;
    }

    /**
     * @param home the _home to set
     */
    public void setHome( IAtomeHome home )
    {
        this._home = home;
    }

    /**
     * Create a new atome object
     * @param atome the new atome object
     */
    public void create( Atome atome )
    {
        _home.create( atome );
    }

    /**
     * remove an atome object
     * @param atome the atome object
     */
    public void remove( Atome atome )
    {
        int nIdAtome = atome.getId(  );
        _home.remove( nIdAtome );
    }

    /**
     * Update an atome object
     * @param atome the atome object
     */
    public void update( Atome atome )
    {
        _home.update( atome );
    }

    /**
     * Returns an atome object
     * @param nKey the atome id
     * @return An atome object with the id nKey
     */
    public Atome findByPrimaryKey( int nKey )
    {
        return _home.findByPrimaryKey( nKey );
    }

    /**
     * Returns a list of atome objects
     * @return A list of all atome
     */
    public List<Atome> findAll(  )
    {
        return _home.findAll(  );
    }
}
