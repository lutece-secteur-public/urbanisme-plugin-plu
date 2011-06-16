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
package fr.paris.lutece.plugins.plu.business.type;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * This class provides Data Access methods for Type objects
 * @author vLopez
 */
public class TypeDAO extends JPALuteceDAO<Integer, Type> implements ITypeDAO
{
	private static final String SQL_QUERY_SELECT_BY_KEY = "SELECT * FROM type_acte_juridique WHERE id_type = ?";
	private static final String SQL_QUERY_SELECT_ALL = "SELECT * FROM type_acte_juridique";
	
    /**
    * @return the plugin name
    */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }
    
    public Type findByPrimaryKey( int nKey )
    {
    	Type type = new Type(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_KEY );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {;
        	type.setId( daoUtil.getInt( 1 ) );
        	type.setName( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return type;
    }
    
    public List<Type> findAll(  )
    {
    	List<Type> typeList = new ArrayList<Type>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
        	Type type = new Type(  );
        	type.setId( daoUtil.getInt( 1 ) );
        	type.setName( daoUtil.getString( 2 ) );
            typeList.add( type );
        }

        daoUtil.free(  );

        return typeList;
    }
}
