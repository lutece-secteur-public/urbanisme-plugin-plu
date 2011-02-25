package fr.paris.lutece.plugins.plu.business;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel( value = Plu.class )
public class Plu_
{
	public static volatile SingularAttribute<Plu, Integer> id;
    public static volatile SingularAttribute<Plu, Date> dj;
    public static volatile SingularAttribute<Plu, Date> da;

}