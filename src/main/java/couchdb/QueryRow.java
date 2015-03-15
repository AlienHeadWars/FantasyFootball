/* **************************************************************************
 *             Copyright 2014 Applied Card Technologies Ltd
 *
 * What : QueryRow
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

import com.fasterxml.jackson.annotation.JsonProperty;

import couchdb.SimpleEntity;

/**
 * QueryRow.
 * @param <EntityType> the type of the embedded entity
 * @author aedwards
 * @version $Revision: 91803 $ $Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 */
public class QueryRow< EntityType extends SimpleEntity >
{
	private String		id, key;
	private EntityType	entity;

	/**
	 * @return the id of the entity
	 */
	@JsonProperty
	public String getId()
	{
		return id;
	}

	/**
	 * @param idParameter the id of the entity
	 */
	public void setId( final String idParameter )
	{
		this.id = idParameter;
	}

	/**
	 * @return the key from the query
	 */
	@JsonProperty
	public String getKey()
	{
		return key;
	}

	/**
	 * @param keyParameter the key from the query
	 */
	public void setKey( final String keyParameter )
	{
		this.key = keyParameter;
	}

	/**
	 * @return the embedded entity
	 */
	@JsonProperty( "value" )
	public EntityType getEntity()
	{
		return entity;
	}

	/**
	 * @param entityParameter the embedded entity
	 */
	public void setEntity( final EntityType entityParameter )
	{
		this.entity = entityParameter;
	}
}
