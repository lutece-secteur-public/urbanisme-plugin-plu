package fr.paris.lutece.plugins.plu.business.history;

import fr.paris.lutece.util.jpa.IGenericHome;

/**
 * IHistoryHome the history home interface
 * @author vLopez
 */
public interface IHistoryHome extends IGenericHome<Integer, History>
{
	/**
	 * Create a history object after a plu correction
	 * @param history the history object
	 */
	 public void correctPlu( History history );

	 /**
	  * Create a history object after a folder correction
	  * @param history the history object
	  */
	 public void correctFolder( History history );
	 
	 /**
	  * Create a history object after an atome correction
	  * @param history the history object
	  */
	 public void correctAtome( History history );
}
