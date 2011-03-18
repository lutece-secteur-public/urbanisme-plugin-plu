package fr.paris.lutece.plugins.plu.business.type;

import java.util.Collection;

import fr.paris.lutece.portal.service.plugin.Plugin;

public class TypeServices implements ITypeServices
{
	ITypeHome _home;

    public ITypeHome getHome(  )
    {
        return _home;
    }

    public void setHome( ITypeHome _home )
    {
        this._home = _home;
    }
	
	public Collection<Type> findAll(Plugin plugin)
	{
		return _home.findAll(  );
	}

	public Type findByPrimaryKey(int nKey, Plugin plugin)
	{
		return _home.findByPrimaryKey( nKey );
	}

}
