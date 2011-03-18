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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.paris.lutece.plugins.plu.business.version.Version;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * This class provides Data Access methods for File objects
 * @author vLopez
 */
public class FileDAO extends JPALuteceDAO<Integer, File> implements IFileDAO
{
    private static final String SQL_QUERY_SELECT_BY_VERSION = "SELECT F.name, F.title, F.mimeType, F.size, F.file, F.version FROM plu_file F INNER JOIN plu_version V ON (F.version = V.id) WHERE F.version = ?";
    private static final String SQL_QUERY_SELECT_BY_ATOME = "SELECT F.id, F.name, F.title, F.mimeType, F.size, F.file, F.version FROM plu_file F INNER JOIN plu_version V ON (F.version = V.id) WHERE V.atome = ?";
    
    /**
    * @return the plugin name
    */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

	public Collection<File> findByVersion(int nIdVersion)
	{
		List<File> fileList = new ArrayList<File>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_VERSION );
        daoUtil.setInt( 1, nIdVersion );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Version version = new Version(  );
            version.setId( daoUtil.getInt( 6 ) );

            File file = new File(  );
            //file.setId( daoUtil.getInt( 1 ) );
            file.setName( daoUtil.getString( 1 ) );
            file.setTitle( daoUtil.getString( 2 ) );
            file.setMimeType( daoUtil.getString( 3 ) );
            file.setSize( daoUtil.getInt( 4 ) );
            file.setFile( daoUtil.getBytes( 5 ) );
            file.setVersion( version );
            fileList.add( file );
        }

        daoUtil.free(  );

        return fileList;
	}
	
	public Collection<File> findByAtome(int nIdAtome)
	{
		List<File> fileList = new ArrayList<File>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ATOME );
        daoUtil.setInt( 1, nIdAtome );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Version version = new Version(  );
            version.setId( daoUtil.getInt( 7 ) );

            File file = new File(  );
            file.setId( daoUtil.getInt( 1 ) );
            file.setName( daoUtil.getString( 2 ) );
            file.setTitle( daoUtil.getString( 3 ) );
            file.setMimeType( daoUtil.getString( 4 ) );
            file.setSize( daoUtil.getInt( 5) );
            file.setFile( daoUtil.getBytes( 6 ) );
            file.setVersion( version );
            fileList.add( file );
        }

        daoUtil.free(  );

        return fileList;
	}
}
