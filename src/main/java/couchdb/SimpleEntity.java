/* **************************************************************************
 *             Copyright 2014 Applied Card Technologies Ltd
 *
 * What : SimpleEntity
 * Who  : aedwards
 * When : 21 Oct 2014
 *
 * Source control
 *		$Revision: 91803 $
 *		$Author: markh2 $
 *		$Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 *
 ****************************************************************************/

package couchdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SimpleEntity. Simple entity for persisting in a couchDB database
 *
 * @author aedwards
 * @version $Revision: 91803 $ $Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 */
@JsonInclude( Include.NON_NULL )
public class SimpleEntity
{

	private String	id, revision;

	/**
	 * @return the uid of the entity
	 */
	@JsonIgnore
	public String getId()
	{
		return id;
	}

	/**
	 * @param idParameter the id of the object
	 */
	@JsonProperty( "_id" )
	public void setId( final String idParameter )
	{
		this.id = idParameter;
	}

	/**
	 * @return the revision of the document
	 */
	@JsonProperty( "_rev" )
	public String getRevision()
	{
		return revision;
	}

	/**
	 * @param revisionParameter - the revision of the document
	 */
	public void setRevision( final String revisionParameter )
	{
		this.revision = revisionParameter;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
		result = prime * result + ( ( revision == null ) ? 0 : revision.hashCode() );
		return result;
	}

	@Override
	public boolean equals( final Object obj )
	{
		if ( this == obj )
		{
			return true;
		}
		if ( obj == null )
		{
			return false;
		}
		if ( getClass() != obj.getClass() )
		{
			return false;
		}
		SimpleEntity other = ( SimpleEntity ) obj;
		if ( id == null )
		{
			if ( other.id != null )
			{
				return false;
			}
		}
		else if ( !id.equals( other.id ) )
		{
			return false;
		}
		if ( revision == null )
		{
			if ( other.revision != null )
			{
				return false;
			}
		}
		else if ( !revision.equals( other.revision ) )
		{
			return false;
		}
		return true;
	}

}
