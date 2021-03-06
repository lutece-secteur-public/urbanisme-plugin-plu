/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
package fr.paris.lutece.plugins.plu.business.folderversion;

import fr.paris.lutece.plugins.plu.business.folder.Folder;
import fr.paris.lutece.plugins.plu.business.version.Version;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


/**
 * This class provides Data Access methods for FolderVersion objects
 * @author vLopez
 */
public class FolderVersionDAO extends JPALuteceDAO<Integer, FolderVersion> implements IFolderVersionDAO
{
    private static final String SQL_QUERY_SELECT_BY_FOLDER = "SELECT fv FROM FolderVersion fv WHERE fv.folder.id = :idFolder";
    private static final String SQL_QUERY_SELECT_BY_VERSION = "SELECT fv FROM FolderVersion fv WHERE fv.version.id = :idVersion";
    private static final String SQL_QUERY_SELECT_BY_FOLDER_AND_VERSION = "SELECT fv FROM FolderVersion fv WHERE fv.version.id = :idVersion AND fv.folder.id = (SELECT MAX(fv.folder.id) FROM FolderVersion fv WHERE fv.version.id = :idVersion)";

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
        TypedQuery<FolderVersion> q = em.createQuery( SQL_QUERY_SELECT_BY_FOLDER, FolderVersion.class );
        q.setParameter( "idFolder", folder.getId(  ) );

        List<FolderVersion> folderVersionList = q.getResultList(  );

        return folderVersionList;
    }

    /**
     * Returns a folderVersion object
     * @param version the version associated
     * @return A folderVersion object associated with the last folder of the version
     */
    public FolderVersion findByMaxFolderAndVersion( Version version )
    {
        EntityManager em = getEM(  );
        Query q = em.createQuery( SQL_QUERY_SELECT_BY_FOLDER_AND_VERSION );
        q.setParameter( "idVersion", version.getId(  ) );

        FolderVersion folderVersion = (FolderVersion) q.getSingleResult(  );

        return folderVersion;
    }

    /**
     * Returns a list of folderVersion objects
     * @param version the version associated
     * @return A list of folderVersion associated with the versioon
     */
    public List<FolderVersion> findByVersion( Version version )
    {
        EntityManager em = getEM( );
        TypedQuery<FolderVersion> q = em.createQuery( SQL_QUERY_SELECT_BY_VERSION, FolderVersion.class );
        q.setParameter( "idVersion", version.getId( ) );

        List<FolderVersion> folderVersionList = q.getResultList( );

        return folderVersionList;
    }
}
