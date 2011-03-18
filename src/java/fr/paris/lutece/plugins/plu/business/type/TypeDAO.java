package fr.paris.lutece.plugins.plu.business.type;

import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;

/**
 * This class provides Data Access methods for Type objects
 * @author vLopez
 */
public class TypeDAO extends JPALuteceDAO<Integer, Type> implements ITypeDAO
{
	/**
     * @return the plugin name
     */
    @Override
    public String getPluginName(  )
    {
        return PluPlugin.PLUGIN_NAME;
    }

}
