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

import java.util.List;

import fr.paris.lutece.portal.service.jpa.AbstractLuteceHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 * This class provides instances management methods (create, find, ...) for Plu objects
 * @author vLopez
 */
public class PluHome extends AbstractLuteceHome<Integer, Plu, IPluDAO> implements IPluHome
{    
	// Constants corresponding to the variables defined in the lutece.properties file
    private static PluHome _singleton;
  // private static PluDAO _dao = (PluDAO) SpringContextService.getPluginBean( "plu", "plu.pluDAO" );

    /**
     * @return the instance of the service
     */
    public static PluHome getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new PluHome(  );
        }

        return _singleton;
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

    /**
     * Returns the list of plu with find with filters
     * @param dateDebut the begin application date
     * @param dateFin the end application date
     * @return the list of plu
     */
	public List<Plu> findWithFilters(String dateDebut, String dateFin)
	{
		return PluDAO.getInstance( ).findPluWithFilters( dateDebut, dateFin );
	}
}
