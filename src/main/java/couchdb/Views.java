/* **************************************************************************
 *             Copyright 2014 Applied Card Technologies Ltd
 *
 * What : Views
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

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import couchdb.SimpleEntity;

/**
 * Views. CouchDb container for views
 *
 * @param <EntityType> The type of entity for which the views are for.
 * @author aedwards
 * @version $Revision: 91803 $ $Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov 2012) $
 */
public class Views< EntityType extends SimpleEntity > extends SimpleEntity
{
	protected static final String		DEFAULT_LANGUAGE	= "javascript";
	private final Map< String, View >	views				= new HashMap<>();
	private String						language;

	/**
	 * @return the language of the views, by default this is javascript.
	 */
	@JsonProperty
	public String getLanguage()
	{
		return language;
	}

	/**
	 * @param languageParameter the language of the views.
	 */
	@JsonIgnore
	public void setLanguage( final String languageParameter )
	{
		this.language = languageParameter;
	}

	/**
	 * @param view the view to add to this set of views
	 */
	@JsonIgnore
	public void addView( final View view )
	{
		views.put( view.getName(), view );
	}

	/**
	 * @return the map of views
	 */
	@JsonProperty
	public Map< String, View > getViews()
	{
		return views;
	}

}
