package fr.paris.lutece.plugins.plu.business.file.content;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;

/**
 * This class represents business object FileContent
 * @author vLopez
 */
@Entity
@Table( name = "PLU_FICHIER_CONTENU" )
public class FileContent
{
	/** Constants */
	
    public static final String RESOURCE_TYPE = "DOSSIER_VERSION_RESOURCE";
    /** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_fichier_contenu_sequence";
    /** Unique value */
    private static final String JPA_COLUMN_NAME = "plu_fichier_contenu_id";
    private int _id;
    private byte[] _file;
    
    /**
     * Returns the identifier of this file content
     * @return the file content identifier
     */
    @TableGenerator( table = PluJPAUtils.SEQUENCE_TABLE_NAME, name = JPA_SEQUENCE_NAME, pkColumnValue = JPA_COLUMN_NAME, allocationSize = 1 )
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = JPA_SEQUENCE_NAME )
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
