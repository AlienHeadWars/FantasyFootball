/* **************************************************************************
 *             Copyright 2014 Applied Card Technologies Ltd
 *
 * What : CouchDbUtilities
 * Who  : aedwards
 * When : 30 Oct 2014
 *
 * Source control
 *		$Revision: 91803 $
 *		$Author: markh2 $
 *		$Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 *
 ****************************************************************************/

package couchdb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import couchdb.SimpleEntity;

/**
 * CouchDbUtilities.
 *
 * @author aedwards
 * @version $Revision: 91803 $ $Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 */
public final class CouchDbUtilities
{
	private static final String		INDEX					= "function(doc) { if (doc.%1$s)  emit(doc.%1$s, doc) }";
	private static String			databasePrefix;

	/**
	 * Converts a parameter into a couchdb url parameter. This involves: *replacing all spaces with "%20" *enclosing the
	 * parameter in quotations
	 *
	 * @param parameter the parameter to be converted
	 * @return the converted parameter
	 */
	public static String parseParameter( final String parameter )
	{
		String spacesParsed = parameter.replace( " ", "%20" );
		return "\"" + spacesParsed + "\"";
	}

	/**
	 * Hiding default constructor.
	 */
	private CouchDbUtilities()
	{

	}

	/**
	 * Generates an index view for the provided fieldName.
	 *
	 * @param fieldName name of the field to generate an index on
	 * @return a view for the index
	 */
	public static View generateIndexForField( final String fieldName )
	{
		View view = new View( fieldName );
		String map = String.format( INDEX, fieldName );
		view.setMap( map );
		return view;
	}

	/**
	 * Generates indexes for a collection of fields.
	 *
	 * @param fields the fields for which we want to generate indexes
	 * @return a map of <fieldName, index>
	 */
	public static Map< String, View > generateIndexesForFields( final Collection< String > fields )
	{
		Map< String, View > views = new HashMap< String, View >();
		for ( String field : fields )
		{
			views.put( field, generateIndexForField( field ) );
		}
		return views;
	}

	/**
	 * Extracts a list of entities from a query response.
	 *
	 * @param <EntityType> the entity type to extract
	 * @param queryResponse the query response to extract entities from
	 * @return the list of extracted entities
	 */
	public static < EntityType extends SimpleEntity > List< EntityType > getEntitiesFromQueryReponse(
			final QueryResponse< EntityType > queryResponse )
	{
		ArrayList< EntityType > list = new ArrayList<>();
		for ( QueryRow< EntityType > row : queryResponse.getRows() )
		{
			list.add( row.getEntity() );
		}
		return list;
	}

	/**
	 * @return prefix for our databases
	 */
	public static String getDatabasePrefix()
	{
		return databasePrefix;
	}

	/**
	 * @param databasePrefixParameter prefix for our databases
	 */
	public static void setDatabasePrefix( final String databasePrefixParameter )
	{
		CouchDbUtilities.databasePrefix = databasePrefixParameter;
	}

	/**
	 * @param entityType the entity for which to generate a database name
	 * @return the generated database name
	 */
	public static String getDatabaseNameForEntity( final Class< ? extends SimpleEntity > entityType )
	{
		return getDatabasePrefix() + "_" + entityType.getSimpleName().toLowerCase();
	}
}
