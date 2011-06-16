package fr.paris.lutece.plugins.plu.business.history;

import fr.paris.lutece.plugins.plu.services.PluPlugin;
import fr.paris.lutece.portal.service.jpa.JPALuteceDAO;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for History objects
 * @author vLopez
 */
public class HistoryDAO extends JPALuteceDAO<Integer, History> implements IHistoryDAO
{
	private static final String SQL_QUERY_CORRECT_PLU = "INSERT INTO historique (id_plu, date_correction, description) VALUE (?, ?, ?)";
	private static final String SQL_QUERY_CORRECT_FOLDER = "INSERT INTO historique (id_plu, id_dossier, date_correction, description) VALUE (?, ?, ?, ?)";
	private static final String SQL_QUERY_CORRECT_ATOME = "INSERT INTO historique (id_plu, id_dossier, id_atome, date_correction, description) VALUE (?, ?, ?, ?, ?)";
	
	@Override
	public String getPluginName(  )
	{
		return PluPlugin.PLUGIN_NAME;
	}

	public void correctPlu( History history )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CORRECT_PLU );
    	java.sql.Date sqlDc = new java.sql.Date( history.getDc(  ).getTime(  ) );
        daoUtil.setInt( 1, history.getPlu(  ) );
        daoUtil.setDate( 2, sqlDc );
        daoUtil.setString( 3, history.getDescription(  ) );
        daoUtil.executeUpdate(  );
        
        daoUtil.free(  );
    }
	
	public void correctFolder( History history )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CORRECT_FOLDER );
    	java.sql.Date sqlDc = new java.sql.Date( history.getDc(  ).getTime(  ) );
        daoUtil.setInt( 1, history.getPlu(  ) );
        daoUtil.setInt( 2, history.getFolder(  ) );
        daoUtil.setDate( 3, sqlDc );
        daoUtil.setString( 4, history.getDescription(  ) );
        daoUtil.executeUpdate(  );
        
        daoUtil.free(  );
    }
	
	public void correctAtome( History history )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CORRECT_ATOME );
    	java.sql.Date sqlDc = new java.sql.Date( history.getDc(  ).getTime(  ) );
        daoUtil.setInt( 1, history.getPlu(  ) );
        daoUtil.setInt( 2, history.getFolder(  ) );
        daoUtil.setInt( 3, history.getAtome(  ) );
        daoUtil.setDate( 4, sqlDc );
        daoUtil.setString( 5, history.getDescription(  ) );
        daoUtil.executeUpdate(  );
        
        daoUtil.free(  );
    }
}
