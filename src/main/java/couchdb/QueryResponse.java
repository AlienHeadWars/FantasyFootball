/* **************************************************************************
 *             Copyright 2014 Applied Card Technologies Ltd
 *
 * What : QueryResponse
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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import couchdb.SimpleEntity;

/**
 * QueryResponse.
 * @param <T> the expected returned response
 * @author aedwards
 * @version $Revision: 91803 $ $Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 */
public class QueryResponse< T extends SimpleEntity >
{
	private Integer					totalRows, offset;
	private List< QueryRow< T >>	rows;

	/**
	 * @return the number of rows returned by the query
	 */
	@JsonProperty( "total_rows" )
	public Integer getTotalRows()
	{
		return totalRows;
	}

	/**
	 * @param totalRowsParameter the number of rows returned by the query
	 */
	public void setTotalRows( final Integer totalRowsParameter )
	{
		this.totalRows = totalRowsParameter;
	}

	/**
	 * @return the offset of the rows returned by the query
	 */
	@JsonProperty
	public Integer getOffset()
	{
		return offset;
	}

	/**
	 * @param offsetParameter the offset of the rows returned by the query
	 */
	public void setOffset( final Integer offsetParameter )
	{
		this.offset = offsetParameter;
	}

	/**
	 * @return returns a list of rows encapsulating the entities
	 */
	@JsonProperty
	public List< QueryRow< T >> getRows()
	{
		return rows;
	}

	/**
	 * @param rowsParameter a list of rows encapsulating the entities
	 */
	public void setRows( final List< QueryRow< T >> rowsParameter )
	{
		this.rows = rowsParameter;
	}

}
