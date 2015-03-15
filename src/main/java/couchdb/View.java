/* **************************************************************************
 *             Copyright 2014 Applied Card Technologies Ltd
 *
 * What : View
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * View.
 * @author aedwards
 * @version $Revision: 91803 $ $Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 */
@JsonInclude( Include.NON_NULL )
public class View
{
	private String				map, reduce;
	private final String		name;

	/**
	 * Default Constructor.
	 */
	public View()
	{
		this( null );
	}

	/**
	 * @param nameParameter the name attached to the view
	 */
	public View( final String nameParameter )
	{
		this.name = nameParameter;
	}

	/**
	 * @return the map function for couchdb
	 */
	@JsonProperty
	public String getMap()
	{
		return map;
	}

	/**
	 * @param mapParameter the map function for couchdb
	 */
	public void setMap( final String mapParameter )
	{
		this.map = mapParameter;
	}

	/**
	 * @return the reduce function for couchDb
	 */
	@JsonProperty
	public String getReduce()
	{
		return reduce;
	}

	/**
	 * @param reduceParameter the reduce function for couchDb
	 */
	public void setReduce( final String reduceParameter )
	{
		this.reduce = reduceParameter;
	}

	/**
	 * Not sent to database, used for internal mapping.
	 * @return the name of the view
	 */
	@JsonIgnore
	public String getName()
	{
		return name;
	}

}
