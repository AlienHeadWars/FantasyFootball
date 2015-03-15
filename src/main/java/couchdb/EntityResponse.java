/* **************************************************************************
 *             Copyright 2014 Applied Card Technologies Ltd
 *
 * What : EntityResponse
 * Who  : aedwards
 * When : 29 Oct 2014
 *
 * Source control
 *		$Revision: 91803 $
 *		$Author: markh2 $
 *		$Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 *
 ****************************************************************************/

package couchdb;

import couchdb.SimpleEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * EntityResponse. Captures the response from couchDB for a newly put entity. Response includes the ID and the new
 * revision of the document
 *
 * @author aedwards
 * @version $Revision: 91803 $ $Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 */
public class EntityResponse extends SimpleEntity
{
	private Boolean	ok;

	/**
	 * @return the "ok" response from couchDB to indicate everything is a-ok
	 */
	@JsonProperty
	public Boolean getOk()
	{
		return ok;
	}

	/**
	 * @param okParameter set the ok response from couchDB
	 */
	public void setOk( final Boolean okParameter )
	{
		this.ok = okParameter;
	}

	/**
	 * @param idParameter the id of the object
	 */
	@JsonProperty( "id" )
	@Override
	public void setId( final String idParameter )
	{
		super.setId( idParameter );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonProperty( "rev" )
	public String getRevision()
	{
		return super.getRevision();
	}

}
