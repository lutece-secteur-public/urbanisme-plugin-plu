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
package fr.paris.lutece.plugins.plu.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import org.apache.commons.io.FileUtils;


/**
 * Utils methods
 */
public final class PluUtils
{
    public static final String CONSTANT_WHERE = " WHERE ";
    public static final String CONSTANT_AND = " AND ";

    public static final BigDecimal BD_ONE_GO = new BigDecimal( FileUtils.ONE_GB );
    public static final BigDecimal BD_ONE_MO = new BigDecimal( FileUtils.ONE_MB );
    public static final BigDecimal BD_ONE_KO = new BigDecimal( FileUtils.ONE_KB );

    public static final MathContext MC_2 = new MathContext( 2 );
    public static final MathContext MC_1 = new MathContext( 1 );

    /**
     * empty constructor
     */
    private PluUtils( )
    {
        // nothing
    }

    /**
     * Builds a query with filters placed in parameters.
     * Consider using {@link #buildQueryWithFilter(StringBuilder, List)}
     * instead.
     * @param strSelect the select of the query
     * @param listStrFilter the list of filter to add in the query
     * @return a query
     */
    public static String buildRequetteWithFilter( String strSelect, List<String> listStrFilter )
    {
        return buildQueryWithFilter( new StringBuilder( strSelect ), listStrFilter );
    }

    /**
     * Builds a query with filters placed in parameters
     * @param sbSQL the beginning of the query
     * @param listFilter the list of filter to add in the query
     * @return a query
     */
    public static String buildQueryWithFilter( StringBuilder sbSQL, List<String> listFilter )
    {
        int nCount = 0;

        for ( String strFilter : listFilter )
        {
            if ( ++nCount == 1 )
            {
                sbSQL.append( CONSTANT_WHERE );
            }

            sbSQL.append( strFilter );

            if ( nCount != listFilter.size( ) )
            {
                sbSQL.append( CONSTANT_AND );
            }
        }

        return sbSQL.toString( );
    }

    /**
     * Transform a byte size to a readable size including units
     * @param size size in bytes
     * @return size in string
     */
    public static String formatSize( Long size )
    {
        String displaySize;

        if ( size / FileUtils.ONE_GB > 0 )
        {
            displaySize = String.valueOf( new BigDecimal( size ).divide( BD_ONE_GO, BigDecimal.ROUND_CEILING ) )
                    + " GO";
        }
        else if ( size / FileUtils.ONE_MB > 0 )
        {
            displaySize = String.valueOf( new BigDecimal( size ).divide( BD_ONE_MO, BigDecimal.ROUND_CEILING ) )
                    + " MO";
        }
        else if ( size / FileUtils.ONE_KB > 0 )
        {
            displaySize = String.valueOf( new BigDecimal( size ).divide( BD_ONE_KO, BigDecimal.ROUND_CEILING ) )
                    + " KO";
        }
        else
        {
            displaySize = String.valueOf( size ) + " octets";
        }
        return displaySize;
    }


    /**
     * Return a file name without version (-Vxx) and without extension.
     * @param strfileName
     * @return file name without version and extension
     */
    public static String getFileNameForDB( String strfileName, String strVersion )
    {

        int a = strfileName.lastIndexOf( "." );
        String strNameWithoutExt = strfileName;
        String strNameExt = "";
        String strNameWithoutVersion = strfileName;
        String strNameForDB = strfileName;

        //Cut extension
        if ( a > 0 )
        {
            strNameForDB = strfileName.substring( 0, a );
            strNameExt = strfileName.substring( a );
        }

        //Cut version
        a = strNameWithoutExt.lastIndexOf( "-V" );
        if ( a > 0 )
        {
            strNameWithoutVersion = strNameWithoutExt.substring( 0, a );
        }

        //Delete spaces
        String strNameWithoutSpace = strNameWithoutVersion.replace( " ", "-" );

        if ( strVersion.length( ) == 1 )
        {
            strVersion = "0" + strVersion;
        }
        //Add version and extension
        strNameForDB = strNameWithoutSpace + "-V" + strVersion + strNameExt;

        return strNameForDB;

    }
}
