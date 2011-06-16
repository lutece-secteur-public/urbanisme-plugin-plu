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
package fr.paris.lutece.plugins.plu.business.folderVersion;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.plu.business.folder.Folder;
import fr.paris.lutece.plugins.plu.business.version.Version;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * This class provides Data Access methods for FolderVersion objects
 * @author vLopez
 */
public class FolderVersionDAO extends JPALuteceDAO<Integer, FolderVersion> implements IFolderVersionDAO
{
	private static final String SQL_QUERY_CREATE = "INSERT INTO dossier_version_atome (id_version, id_dossier) VALUE (?, ?)";
	private static final String SQL_QUERY_UPDATE = "UPDATE dossier_version_atome SET id_version = ?, id_dossier = ? WHERE id_dossier_version = ?";
	private static final String SQL_QUERY_REMOVE = "DELETE FROM dossier_version_atome WHERE id_version = ? AND id_dossier = ?";
	private static final String SQL_QUERY_SELECT_BY_FOLDER = "SELECT id_dossier_version, id_version FROM dossier_version_atome WHERE id_dossier = ?";
	private static final String SQL_QUERY_SELECT_BY_FOLDER_AND_VERSION = "SELECT id_dossier_version FROM dossier_version_atome WHERE id_dossier = ? AND id_version = ?";
	
    /**
    * @return the plugin name
    */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }
    
    public void create( FolderVersion folderVersion )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CREATE );
        daoUtil.setInt( 1, folderVersion.getVersion(  ).getId(  ) );
        daoUtil.setInt( 2, folderVersion.getFolder(  ).getId(  ) );
        daoUtil.executeUpdate(  );
        
        daoUtil.free(  );
    }
    
    public void update( FolderVersion folderVersion )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, folderVersion.getVersion(  ).getId(  ) );
        daoUtil.setInt( 2, folderVersion.getFolder(  ).getId(  ) );
        daoUtil.setInt( 3, folderVersion.getId(  ) );
        daoUtil.executeUpdate(  );
        
        daoUtil.free(  );
    }
	
	public void remove( Folder folder, Version versionOld )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE );
        daoUtil.setInt( 1, versionOld.getId(  ) );
        daoUtil.setInt( 2, folder.getId(  ) );
        daoUtil.executeUpdate(  );
        
        daoUtil.free(  );
	}
	
    public List<FolderVersion> findByFolder( Folder folder )
    {
    	List<FolderVersion> folderVersionList = new ArrayList<FolderVersion>(  );
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_FOLDER );
        daoUtil.setInt( 1, folder.getId(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
        	Version version = new Version(  );
        	version.setId( daoUtil.getInt( 2 ) );
        	
        	FolderVersion folderVersion = new FolderVersion(  );
        	folderVersion.setId( daoUtil.getInt( 1 ) );
        	folderVersion.setVersion( version );
        	folderVersionList.add( folderVersion );
        }
        
        daoUtil.free(  );
        
    	return folderVersionList;	
    }
    
	public FolderVersion findByFolderAndVersion( Folder folder, Version version )
	{
		FolderVersion folderVersion = new FolderVersion(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_FOLDER_AND_VERSION );
        daoUtil.setInt( 1, folder.getId(  ) );
        daoUtil.setInt( 2, version.getId(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {        	
        	folderVersion.setId( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return folderVersion;
	}
}
