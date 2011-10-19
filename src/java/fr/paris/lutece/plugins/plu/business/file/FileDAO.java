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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import fr.paris.lutece.plugins.plu.business.atome.AtomeFilter;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.plugins.plu.utils.PluUtils;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;


/**
 * This class provides Data Access methods for File objects
 * @author vLopez
 */
public class FileDAO extends JPALuteceDAO<Integer, File> implements IFileDAO
{
    private static final String SQL_QUERY_SELECT_ALL_FORMAT = "SELECT f.mimeType FROM File f GROUP BY f.mimeType";
    private static final String SQL_QUERY_SELECT_BY_VERSION = "SELECT f FROM File f WHERE f.version = :idVersion";
    private static final String SQL_SEARCH = "SELECT f FROM File f, Version v";
    private static final String SQL_FILTER_FILE_TITLE = "f.title = :titleFile";
    private static final String SQL_FILTER_FILE_NAME = "f.name = :nameFile";
    private static final String SQL_FILTER_FILE_TYPE = "f.mimeType = :typeFile";
    private static final String SQL_FILTER_FILE_ID = "f.version = v.id";
    private static final String SQL_FILTER_ATOME_NAME = "v.atome.name = :nameAtome";

    /**
    * @return the plugin name
    */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    /**
     * Returns a list of file objects
     * @return A list of all mime type file
     */
    public List<File> findAllMimeType(  )
    {
        EntityManager em = getEM(  );
        TypedQuery<File> q = em.createQuery( SQL_QUERY_SELECT_ALL_FORMAT, File.class );

        List<File> fileList = q.getResultList(  );

        return fileList;
    }

    /**
     * Returns a list of file objects
     * @param nIdVersion the version id
     * @return A list of file associated with the same version id
     */
    public List<File> findByVersion( int nIdVersion )
    {
        EntityManager em = getEM(  );
        TypedQuery<File> q = em.createQuery( SQL_QUERY_SELECT_BY_VERSION, File.class );
        q.setParameter( "idVersion", nIdVersion );

        List<File> fileList = q.getResultList(  );

        return fileList;
    }

    /**
     * Finds by filter
     * @param fileFilter the file filter
     * @param atomeFilter the atome filter
     * @return the folder list
     */
    public List<File> findByFilter( FileFilter fileFilter, AtomeFilter atomeFilter )
    {
        List<String> listStrFilter = new ArrayList<String>(  );
        
        listStrFilter.add( ( SQL_FILTER_FILE_ID ) );
        
        if ( fileFilter.containsTitle(  ) )
        {
            listStrFilter.add( SQL_FILTER_FILE_TITLE );
        }

        if ( fileFilter.containsName(  ) )
        {
            listStrFilter.add( SQL_FILTER_FILE_NAME );
        }

        if ( fileFilter.containsMimeType(  ) )
        {
            listStrFilter.add( SQL_FILTER_FILE_TYPE );
        }

        if ( atomeFilter.containsName(  ) )
        {
            listStrFilter.add( SQL_FILTER_ATOME_NAME );
        }

        String strSQL = PluUtils.buildRequetteWithFilter( SQL_SEARCH, listStrFilter );

        EntityManager em = getEM(  );
        TypedQuery<File> q = em.createQuery( strSQL, File.class );

        if ( fileFilter.containsTitle(  ) )
        {
            q.setParameter( "titleFile", fileFilter.getTitle(  ) );
        }

        if ( fileFilter.containsName(  ) )
        {
            q.setParameter( "nameFile", fileFilter.getName(  ) );
        }

        if ( fileFilter.containsMimeType(  ) )
        {
            q.setParameter( "typeFile", fileFilter.getMimeType(  ) );
        }

        if ( atomeFilter.containsName(  ) )
        {
            q.setParameter( "nameAtome", atomeFilter.getName(  ) );
        }

        List<File> fileList = q.getResultList(  );

        return fileList;
    }
}
