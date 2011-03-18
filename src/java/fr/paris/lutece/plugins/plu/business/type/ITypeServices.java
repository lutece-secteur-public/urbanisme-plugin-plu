package fr.paris.lutece.plugins.plu.business.type;

import java.util.Collection;

import fr.paris.lutece.portal.service.plugin.Plugin;

public interface ITypeServices
{
	public Collection<Type> findAll( Plugin plugin );
	
	public Type findByPrimaryKey( int nKey, Plugin plugin );
	
}
