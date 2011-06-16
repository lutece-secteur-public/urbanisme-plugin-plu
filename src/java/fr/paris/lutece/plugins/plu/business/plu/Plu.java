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
package fr.paris.lutece.plugins.plu.business.plu;

import java.util.Date;


/**
 * This class represents business object Plu
 * @author vLopez
 */
public class Plu
{
    //constantes
    public static final String RESOURCE_TYPE = "PLU_RESOURCE";
    private int _id;
    private int _type;
    private String _cause;
    private String _reference;
    private Date _dj;
    private Date _da;
    private int _etat;

    /**
     * Returns the identifier of this plu
     * @return the plu identifier
     */
    public int getId(  )
    {
        return _id;
    }

    /**
     * Sets the identifier of the plu to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the type of this plu
     * @return the plu type
     */
    public int getType(  )
    {
        return _type;
    }

    /**
     * Sets the type of the plu to the specified integer
     * @param type the new type
     */
    public void setType( int type )
    {
        _type = type;
    }
    
    /**
     * Returns the cause of this plu
     * @return the plu cause
     */
    public String getCause(  )
    {
        return _cause;
    }

    /**
     * Sets the cause of the plu to the specified string
     * @param cause the new cause
     */
    public void setCause( String cause )
    {
        _cause = cause;
    }
    
    /**
     * Returns the reference of this plu
     * @return the plu reference
     */
    public String getReference(  )
    {
        return _reference;
    }

    /**
    * Sets the reference of the plu to the specified String
    * @param reference the new reference
    */
    public void setReference( String reference )
    {
        _reference = reference;
    }
    
    /**
     * Returns the dj of this plu
     * @return the plu dj
     */
    public Date getDj(  )
    {
        return _dj;
    }

    /**
     * Sets the dj of the plu to the specified date
     * @param dj the new dj
     */
    public void setDj( Date dj )
    {
        _dj = dj;
    }

    /**
     * Returns the da of this plu
     * @return the plu da
     */
    public Date getDa(  )
    {
        return _da;
    }

    /**
     * Sets the da of the plu to the specified date
     * @param da the new da
     */
    public void setDa( Date da )
    {
        _da = da;
    }

    /**
     * Returns the etat of this plu
     * @return the plu etat
     */
    public int getEtat(  )
    {
        return _etat;
    }

    /**
     * Sets the type of the plu to the specified integer
     * @param type the new type
     */
    public void setEtat( int etat )
    {
        _etat = etat;
    }
}
