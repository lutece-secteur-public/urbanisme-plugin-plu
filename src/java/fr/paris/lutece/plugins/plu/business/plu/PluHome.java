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

import fr.paris.lutece.portal.service.jpa.AbstractLuteceHome;


/**
 * This class provides instances management methods (create, find, ...) for Plu objects
 * @author vLopez
 */
public class PluHome extends AbstractLuteceHome<Integer, Plu, IPluDAO> implements IPluHome
{
    //    public void create( Plu plu )
    //    {
    //        getDao(  ).create( plu );
    //    }
    //
    //    public List<Plu> findAll(  )
    //    {
    //        return getDao(  ).findAll(  );
    //    }
    //
    //    public Plu findByPrimaryKey( int nKey )
    //    {
    //        return getDao(  ).findByPrimaryKey( nKey );
    //    }

    /**
     * Update a plu object
     * @param plu the plu object
     */
    public void updateApprove( Plu plu )
    {
        getDao(  ).updateApprove( plu );
    }

    /**
     * Update a plu object
     * @param plu the plu object
     */
    public void updateApplication( Plu plu )
    {
        getDao(  ).updateApplication( plu );
    }

    /**
     * Returns a plu object
     * @return A plu object which work
     */
    public Plu findPluWork(  )
    {
        return getDao(  ).findPluWork(  );
    }

    /**
     * Returns a plu object
     * @return A plu object which is applied
     */
    public Plu findPluApplied(  )
    {
        return getDao(  ).findPluApplied(  );
    }
}
