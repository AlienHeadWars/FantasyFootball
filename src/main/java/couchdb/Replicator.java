/* **************************************************************************
 *             Copyright 2014 Applied Card Technologies Ltd
 *
 * What : Replicator
 * Who  : aedwards
 * When : 24 Oct 2014
 *
 * Source control
 *		$Revision: 91803 $
 *		$Author: markh2 $
 *		$Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 *
 ****************************************************************************/

package couchdb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Replicator. A replication message for a couchDB.
 *
 * @author aedwards
 * @version $Revision: 91803 $ $Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 */
@JsonInclude( Include.NON_NULL )
public class Replicator
{
	private final String	source, target;
	private final Boolean	continuous, createTarget;

	/**
	 * Constructor for a replicator. Initialises replicator as not continuous and that it should create target (if not
	 * already existing)
	 *
	 * @param sourceParameter the source to be replicated
	 * @param targetParameter the target for the replication
	 */
	public Replicator( final String sourceParameter, final String targetParameter )
	{
		this( sourceParameter, targetParameter, false, true );
	}

	/**
	 * @param sourceParameter the source to be replicated
	 * @param targetParameter the target for the replication
	 * @param continuousParameter whether to replicate continuously (as opposed to a one shot replication)
	 * @param createTargetParameter whether or not to create the target if it does not yet exist
	 */
	public Replicator( final String sourceParameter, final String targetParameter, final Boolean continuousParameter,
			final Boolean createTargetParameter )
	{
		this.source = sourceParameter;
		this.target = targetParameter;
		this.continuous = continuousParameter;
		this.createTarget = createTargetParameter;
	}

	/**
	 * @return the source to replicate
	 */
	@JsonProperty
	public String getSource()
	{
		return source;
	}

	/**
	 * @return the target for the replication
	 */
	@JsonProperty
	public String getTarget()
	{
		return target;
	}

	/**
	 * @return whether to replicate continuously (as opposed to a one shot replication)
	 */
	@JsonProperty
	public Boolean getContinuous()
	{
		return continuous;
	}

	/**
	 * @return whether or not to create the target if it does not yet exist
	 */
	@JsonProperty( "create_target" )
	public Boolean getCreateTarget()
	{
		return createTarget;
	}

}
