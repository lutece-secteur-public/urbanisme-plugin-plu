package fr.paris.lutece.plugins.plu.utils;

import java.util.List;

public class PluUtils
{
	public static final String CONSTANT_WHERE = " WHERE ";
    public static final String CONSTANT_AND = " AND ";
	
	/**
     * Builds a query with filters placed in parameters.
     * Consider using {@link #buildQueryWithFilter(StringBuilder, List)} instead.
     * @param strSelect the select of the  query
     * @param listStrFilter the list of filter to add in the query
     * @return a query
     */
    public static String buildRequetteWithFilter( String strSelect, List<String> listStrFilter )
    {
        return buildQueryWithFilter( new StringBuilder( strSelect ), listStrFilter );
    }
    
    /**
     * Builds a query with filters placed in parameters
     * @param sbSQL the beginning of the  query
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

            if ( nCount != listFilter.size(  ) )
            {
            	sbSQL.append( CONSTANT_AND );
            }
        }

        return sbSQL.toString(  );
    }

}
