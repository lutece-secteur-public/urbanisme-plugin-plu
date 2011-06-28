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

import fr.paris.lutece.plugins.plu.business.atome.AtomeFilter;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.plugins.plu.utils.PluUtils;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for File objects
 * @author vLopez
 */
public class FileDAO extends JPALuteceDAO<Integer, File> implements IFileDAO
{
    private static final String SQL_QUERY_DELETE = "DELETE FROM fichier WHERE id_atome = ? AND ordre_fichier = ? AND id_version_atome = ?";

    private static final String SQL_QUERY_SELECT_ALL = "SELECT id_atome, ordre_fichier, id_version_atome, nom_fichier, titre_fichier, format, est_eps, taille FROM fichier";
    private static final String SQL_QUERY_SELECT_ALL_FORMAT = "SELECT format FROM fichier GROUP BY format";
    private static final String SQL_QUERY_SELECT_BY_VERSION = "SELECT id_atome, ordre_fichier, id_version_atome, nom_fichier, titre_fichier, format, est_eps, taille FROM fichier WHERE id_version_atome = ?";
    private static final String SQL_SEARCH = "SELECT F.id_atome, F.ordre_fichier, F.id_version_atome, F.nom_fichier, F.titre_fichier, F.format, F.est_eps, F.taille FROM fichier F INNER JOIN version_atome VA ON (F.id_version_atome = VA.id_version) INNER JOIN atome A ON (VA.id_atome = A.id_atome)";
    private static final String SQL_FILTER_FILE_TITLE = "F.titre_fichier = ?";
    private static final String SQL_FILTER_FILE_NAME = "F.nom_fichier = ?";
    private static final String SQL_FILTER_FILE_TYPE = "F.format = ?";
    private static final String SQL_FILTER_ATOME_NAME = "A.nom = ?";

    /**
    * @return the plugin name
    */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

    public void remove( File file )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, file.getId(  ) );
        daoUtil.setInt( 2, file.getOrder(  ) );
        daoUtil.setInt( 3, file.getVersion(  ) );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    public List<File> findAll(  )
    {
        List<File> fileList = new ArrayList<File>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            File file = new File(  );
            file.setId( daoUtil.getInt( 1 ) );
            file.setOrder( daoUtil.getInt( 2 ) );
            file.setVersion( daoUtil.getInt( 3 ) );
            file.setName( daoUtil.getString( 4 ) );
            file.setTitle( daoUtil.getString( 5 ) );
            file.setMimeType( daoUtil.getString( 6 ) );
            file.setEPS( daoUtil.getString( 7 ) );
            file.setSize( daoUtil.getInt( 8 ) );
            fileList.add( file );
        }

        daoUtil.free(  );

        return fileList;
    }

    public List<File> findAllMimeType(  )
    {
        List<File> fileList = new ArrayList<File>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_FORMAT );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            File file = new File(  );
            file.setMimeType( daoUtil.getString( 1 ) );
            fileList.add( file );
        }

        daoUtil.free(  );

        return fileList;
    }

    public List<File> findByVersion( int nIdVersion )
    {
        List<File> fileList = new ArrayList<File>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_VERSION );
        daoUtil.setInt( 1, nIdVersion );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            File file = new File(  );
            file.setId( daoUtil.getInt( 1 ) );
            file.setOrder( daoUtil.getInt( 2 ) );
            file.setVersion( daoUtil.getInt( 3 ) );
            file.setName( daoUtil.getString( 4 ) );
            file.setTitle( daoUtil.getString( 5 ) );
            file.setMimeType( daoUtil.getString( 6 ) );
            file.setEPS( daoUtil.getString( 7 ) );
            file.setSize( daoUtil.getInt( 8 ) );
            fileList.add( file );
        }

        daoUtil.free(  );

        return fileList;
    }

    public List<File> findByFilter( FileFilter fileFilter, AtomeFilter atomeFilter )
    {
        List<File> fileList = new ArrayList<File>(  );
        List<String> listStrFilter = new ArrayList<String>(  );

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

        DAOUtil daoUtil = new DAOUtil( strSQL );
        int nIndex = 1;

        if ( fileFilter.containsTitle(  ) )
        {
            daoUtil.setString( nIndex, fileFilter.get_title(  ) );
            nIndex++;
        }

        if ( fileFilter.containsName(  ) )
        {
            daoUtil.setString( nIndex, fileFilter.get_name(  ) );
            nIndex++;
        }

        if ( fileFilter.containsMimeType(  ) )
        {
            daoUtil.setString( nIndex, fileFilter.get_mimeType(  ) );
            nIndex++;
        }

        if ( atomeFilter.containsName(  ) )
        {
            daoUtil.setString( nIndex, atomeFilter.get_name(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            File file = new File(  );
            file.setId( daoUtil.getInt( 1 ) );
            file.setOrder( daoUtil.getInt( 2 ) );
            file.setVersion( daoUtil.getInt( 3 ) );
            file.setName( daoUtil.getString( 4 ) );
            file.setTitle( daoUtil.getString( 5 ) );
            file.setMimeType( daoUtil.getString( 6 ) );
            file.setEPS( daoUtil.getString( 7 ) );
            file.setSize( daoUtil.getInt( 8 ) );
            fileList.add( file );
        }

        daoUtil.free(  );

        return fileList;
    }
}
