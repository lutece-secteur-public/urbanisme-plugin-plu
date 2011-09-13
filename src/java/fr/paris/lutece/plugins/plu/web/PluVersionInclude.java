package fr.paris.lutece.plugins.plu.web;

import fr.paris.lutece.plugins.plu.business.plu.IPluServices;
import fr.paris.lutece.plugins.plu.business.plu.Plu;
import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class PluVersionInclude implements PageInclude
{
    public static final DateFormat DATE_FORMAT_PLU = new SimpleDateFormat( "dd/MM/yyyy" );

    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        IPluServices pluServices = (IPluServices) SpringContextService.getPluginBean( PluPlugin.PLUGIN_NAME,
                "plu.pluServices" );
        Plu pluApplied = pluServices.findPluApplied( );
        rootModel.put( "current_plu_version", pluApplied.getId( ) );
        rootModel.put( "current_plu_date", DATE_FORMAT_PLU.format( pluApplied.getDa( ) ) );
    }

}