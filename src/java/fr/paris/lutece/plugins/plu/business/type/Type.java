package fr.paris.lutece.plugins.plu.business.type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import fr.paris.lutece.plugins.plu.utils.jpa.PluJPAUtils;

/**
 * This class represents business object Type
 * @author vLopez
 */
@Entity
@Table( name = "plu_type" )
public class Type
{
	/** Sequence name */
    private static final String JPA_SEQUENCE_NAME = "plu_sequence";
    
    //constantes
    private static final String JPA_COLUMN_NAME = "plu_type_id";
    public static final String RESOURCE_TYPE = "TYPE_RESOURCE";
    private int _id;
    private String _name;
    
    /**
     * Returns the identifier of this type
     * @return the type identifier
     */
    @TableGenerator( table = PluJPAUtils.SEQUENCE_TABLE_NAME, name = JPA_SEQUENCE_NAME, pkColumnValue = JPA_COLUMN_NAME, allocationSize = 1 )
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = JPA_SEQUENCE_NAME )
    @Column( name = "id" )
    public int getId(  )
    {
        return _id;
    }

    /**
     * Sets the identifier of the type to the specified integer
     * @param id the new identifier
     */
    public void setId( int id )
    {
        _id = id;
    }

    /**
     * Returns the name of this type
     * @return the type name
     */
    @Column( name = "name" )
    public String getName(  )
    {
        return _name;
    }

    /**
     * Sets the name of the type to the specified string
     * @param name the new name
     */
    public void setName( String name )
    {
        _name = name;
    }

}
