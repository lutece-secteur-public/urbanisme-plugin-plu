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

import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * This class provides Data Access methods for Plu objects
 * @author vLopez
 */
public class PluDAO extends JPALuteceDAO<Integer, Plu> implements IPluDAO
{
    //    private static final String SQL_QUERY_CREATE = "INSERT INTO plu VALUE ()";
	//    private static final String SQL_QUERY_SELECT_ALL = "SELECT * FROM plu";
    //    private static final String SQL_QUERY_SELECT_BY_KEY = "SELECT * FROM plu WHERE id_plu = ?";
    private static final String SQL_QUERY_SELECT_PLU_APPLIED = "SELECT id_plu FROM plu WHERE id_plu=(SELECT max(id_plu) FROM plu)-1";
    private static final String SQL_QUERY_SELECT_PLU_WORK = "SELECT id_plu FROM plu WHERE dj IS NULL AND da IS NULL";
    private static final String SQL_QUERY_UPDATE_APPROVE = "UPDATE plu set id_type_acte_juridique = ?, nom_acte_juridique = ?, ref_deliberation = ?, dj = ? WHERE id_plu = ?";
    private static final String SQL_QUERY_UPDATE_APPLICATION = "UPDATE plu set da = ? WHERE id_plu = ?";

    /**
     * @return the plugin name
     */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    //    public void create( Plu plu )
    //    {
    //        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CREATE );
    //        daoUtil.executeUpdate(  );
    //
    //        daoUtil.free(  );
    //    }
    //
    //    public List<Plu> findAll(  )
    //    {
    //        List<Plu> pluList = new ArrayList<Plu>(  );
    //        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );
    //        daoUtil.executeQuery(  );
    //
    //        while ( daoUtil.next(  ) )
    //        {
    //            Plu plu = new Plu(  );
    //            plu.setId( daoUtil.getInt( 1 ) );
    //            plu.setType( daoUtil.getInt( 2 ) );
    //            plu.setCause( daoUtil.getString( 3 ) );
    //            plu.setReference( daoUtil.getString( 4 ) );
    //            plu.setDj( daoUtil.getDate( 5 ) );
    //            plu.setDa( daoUtil.getDate( 6 ) );
    //            plu.setDg( daoUtil.getDate( 7 ) );
    //            plu.setEtat( daoUtil.getInt( 8 ) );
    //            pluList.add( plu );
    //        }
    //
    //        daoUtil.free(  );
    //
    //        return pluList;
    //    }
    //
    //    public Plu findByPrimaryKey( int nKey )
    //    {
    //        Plu plu = new Plu(  );
    //        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_KEY );
    //        daoUtil.setInt( 1, nKey );
    //        daoUtil.executeQuery(  );
    //
    //        while ( daoUtil.next(  ) )
    //        {
    //            plu.setId( daoUtil.getInt( 1 ) );
    //            plu.setType( daoUtil.getInt( 2 ) );
    //            plu.setCause( daoUtil.getString( 3 ) );
    //            plu.setReference( daoUtil.getString( 4 ) );
    //            plu.setDj( daoUtil.getDate( 5 ) );
    //            plu.setDa( daoUtil.getDate( 6 ) );
    //            plu.setDg( daoUtil.getDate( 7 ) );
    //            plu.setEtat( daoUtil.getInt( 8 ) );
    //        }
    //
    //        daoUtil.free(  );
    //
    //        return plu;
    //    }
    public void updateApprove( Plu plu )
    {
        java.sql.Date sqlDj = new java.sql.Date( plu.getDj(  ).getTime(  ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_APPROVE );
        daoUtil.setInt( 1, plu.getType(  ).getId(  ) );
        daoUtil.setString( 2, plu.getCause(  ) );
        daoUtil.setString( 3, plu.getReference(  ) );
        daoUtil.setDate( 4, sqlDj );
        daoUtil.setInt( 5, plu.getId(  ) );

        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    public void updateApplication( Plu plu )
    {
        java.sql.Date sqlDa = new java.sql.Date( plu.getDa(  ).getTime(  ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_APPLICATION );
        daoUtil.setDate( 1, sqlDa );
        daoUtil.setInt( 2, plu.getId(  ) );

        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    public Plu findPluWork(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PLU_WORK );
        daoUtil.executeQuery(  );

        Plu plu = new Plu(  );

        while ( daoUtil.next(  ) )
        {
            plu.setId( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return plu;
    }

    /**
     * Load the list of plu
     *
     * @return The list of the Plu
     */
    public Plu findPluApplied(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PLU_APPLIED );
        daoUtil.executeQuery(  );

        Plu plu = new Plu(  );

        while ( daoUtil.next(  ) )
        {
            plu.setId( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return plu;
    }
}
