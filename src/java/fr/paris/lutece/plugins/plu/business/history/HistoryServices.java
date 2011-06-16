package fr.paris.lutece.plugins.plu.business.history;

public class HistoryServices implements IHistoryServices
{
	IHistoryHome _home;

    public IHistoryHome getHome(  )
    {
        return _home;
    }

    public void setHome( IHistoryHome _home )
    {
        this._home = _home;
    }

    /**
	 * Create a history object after a plu correction
	 * @param history the history object
	 */
    public void correctPlu( History history )
    {
        _home.correctPlu( history );
    }
    
    /**
	 * Create a history object after a folder correction
	 * @param history the history object
	 */
    public void correctFolder( History history )
    {
        _home.correctFolder( history );
    }
    
    /**
	 * Create a history object after an atome correction
	 * @param history the history object
	 */
    public void correctAtome( History history )
    {
        _home.correctAtome( history );
    }
}
