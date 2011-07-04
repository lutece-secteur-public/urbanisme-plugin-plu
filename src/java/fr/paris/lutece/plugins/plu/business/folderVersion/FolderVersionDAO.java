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

import fr.paris.lutece.plugins.plu.business.folder.Folder;
import fr.paris.lutece.plugins.plu.business.version.Version;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 * This class provides Data Access methods for FolderVersion objects
 * @author vLopez
 */
public class FolderVersionDAO extends JPALuteceDAO<Integer, FolderVersion> implements IFolderVersionDAO
{
    private static final String SQL_QUERY_SELECT_BY_FOLDER = "SELECT fv.id, fv.version.id FROM FolderVersion fv WHERE fv.folder.id = :idFolder";
    private static final String SQL_QUERY_SELECT_BY_FOLDER_AND_VERSION = "SELECT fv.id FROM FolderVersion fv WHERE fv.folder.id = :idFolder AND fv.version.id = :idVersion";

    /**
    * @return the plugin name
    */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    /**
     * Returns a list of folderVersion objects
     * @param folder the folder associated
     * @return A list of folderVersion associated with the folder
     */
    public List<FolderVersion> findByFolder( Folder folder )
    {
    	EntityManager em = getEM(  );
    	Query q = em.createQuery( SQL_QUERY_SELECT_BY_FOLDER );
    	q.setParameter( "idFolder", folder.getId(  ) );
    	
    	List<FolderVersion> folderVersionList = (List<FolderVersion>) q.getResultList(  );
    	
    	return folderVersionList;
    }

    /**
     * Returns a folderVersion object
     * @param folder the folder associated
     * @param version the version associated
     * @return A folderVersion object associated with the folder and the version
     */
    public FolderVersion findByFolderAndVersion( Folder folder, Version version )
    {
    	EntityManager em = getEM(  );
    	Query q = em.createQuery( "SELECT fv FROM FolderVersion fv WHERE fv.folder.id = :idFolder AND fv.version.id = :idVersion" );
    	q.setParameter( "idFolder", folder.getId(  ) );
    	q.setParameter( "idVersion", version.getId(  ) );
    	
    	FolderVersion folderVersion = (FolderVersion) q.getSingleResult(  );
    	
    	return folderVersion;
    }
}
