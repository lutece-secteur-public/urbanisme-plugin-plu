package fr.paris.lutece.plugins.plu.business.history;

import fr.paris.lutece.portal.service.jpa.AbstractLuteceHome;

/**
 * This class provides instances management methods (create, find, ...) for History objects
 * @author vLopez
 */
public class HistoryHome extends AbstractLuteceHome<Integer, History, IHistoryDAO> implements IHistoryHome
{
	/**
	 * Create a history object after a plu correction
	 * @param history the history object
	 */
	public void correctPlu( History history )
	{
		getDao(  ).correctPlu( history );
	}
	
	/**
	 * Create a history object after a folder correction
	 * @param history the history object
	 */
	public void correctFolder( History history )
	{
		getDao(  ).correctFolder( history );
	}
	
	/**
	 * Create a history object after an atome correction
	 * @param history the history object
	 */
	public void correctAtome( History history )
	{
		getDao(  ).correctAtome( history );
	}
}
