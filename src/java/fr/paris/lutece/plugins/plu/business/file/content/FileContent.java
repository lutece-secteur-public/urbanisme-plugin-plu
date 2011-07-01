package fr.paris.lutece.plugins.plu.business.file.content;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class represents business object FileContent
 * @author vLopez
 */
@Entity
@Table( name = "fichier_contenu" )
public class FileContent
{
	/** Constants */
    public static final String RESOURCE_TYPE = "FICHIER_RESOURCE";
    private int _id;
    private byte[] _file;
    
    /**
     * Returns the identifier of this file content
     * @return the file content identifier
     */
    @Id
    @Column( name = "id_fichier_contenu" )
    public int getId(  )
    {
        return _id;
    }

    /**
     * Sets the identifier of the file content to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the physical file of this file content
     * @return the physical file
     */
    @Column( name = "fichier" )
    public byte[] getFile(  )
    {
        return _file;
    }

    /**
     * Sets the physical file of the file content to the specified byte
     * @param file the new physical file
     */
    public void setFile( byte[] file )
    {
        _file = file;
    }
}
