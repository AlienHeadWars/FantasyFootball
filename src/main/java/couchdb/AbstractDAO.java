/* **************************************************************************
 *             Copyright 2014 Applied Card Technologies Ltd
 *
 * What : AbstractDAO
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

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import couchdb.SimpleEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * AbstractDAO.
 *
 * @param <EntityType>
 *            entity type for the DAO
 * @author aedwards
 * @version $Revision: 91803 $ $Date: 2012-11-12 14:00:56 +0000 (Mon, 12 Nov
 *          2012) $
 */
public abstract class AbstractDAO<EntityType extends SimpleEntity> {

	private static final String VIEWS_PREFIX = "_design/";
	private static final String VIEW_ACCESS_SUFFIX = "/_view/";
	private static final String REPLICA_SUFFIX = "";
	private static final String REPLICATE = "_replicate";

	private final WebResource replicatorResource;
	private final WebResource ourDbResource;
	private final WebResource ourBackupDbResource;
	private boolean mainDbDown = false;
	private final Replicator pushReplicator;
	private final Replicator pullReplicator;
	private final ObjectMapper mapper;
	private final Class<EntityType> entityType;
	private final String viewsId;

	/**
	 * @param entityTypeParameter
	 *            the type of entity this dao manages
	 * @param clientParameter
	 *            jerseyClient used for communication with couchDb
	 * @param couchDbUrl
	 *            the location of our db
	 * @param replicatorDbUrl
	 *            the location of a replicating database
	 */
	public AbstractDAO(
			final Class<EntityType> entityTypeParameter,
			final Client clientParameter,
			final String couchDbUrl,
			final Optional<String> replicatorDbUrl) {
		this.entityType = entityTypeParameter;
		replicatorResource = clientParameter.resource(couchDbUrl + REPLICATE);
		String databaseName = CouchDbUtilities.getDatabaseNameForEntity(entityTypeParameter);
		ourDbResource = clientParameter.resource(couchDbUrl + databaseName);
		ourDbResource.put(ClientResponse.class);
		String replicatorTargetUrl =
				(replicatorDbUrl.isPresent() ? replicatorDbUrl.get() : couchDbUrl) + databaseName + REPLICA_SUFFIX;
		ourBackupDbResource = clientParameter.resource(replicatorTargetUrl);
		mapper = new ObjectMapper();
		pushReplicator = new Replicator(databaseName, replicatorTargetUrl, true, true);
		pullReplicator = new Replicator(replicatorTargetUrl, databaseName, true, true);
		replicate(pullReplicator);
		replicate(pushReplicator);
		viewsId = VIEWS_PREFIX + entityTypeParameter.getSimpleName();

	}

	/**
	 * @param replicator
	 *            to run on server
	 */
	private void replicate(final Replicator replicator) {
		Builder builder = replicatorResource.accept(MediaType.APPLICATION_JSON);
		builder.type(MediaType.APPLICATION_JSON);
		try {
			builder.post(ClientResponse.class, getMapper().writeValueAsString(replicator));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @return couchDb web resource, returns backup source if primary is
	 *         unavailable.
	 */
	protected WebResource getResource() {
		WebResource resource;
		try {
			// simple get will throw a ClientHandlerException if resource is
			// unavailable
			ourDbResource.get(ClientResponse.class);
			resource = ourDbResource;
			// If the main db was previously down, restart replication service
			if (mainDbDown) {
				replicate(pullReplicator);
				replicate(pushReplicator);
				mainDbDown = false;
			}
		} catch (ClientHandlerException e) {
			mainDbDown = true;
			ourBackupDbResource.get(ClientResponse.class);
			resource = ourBackupDbResource;
		}
		return resource;
	}

	/**
	 * @return a mapper for mapping objects to JSON
	 */
	protected ObjectMapper getMapper() {
		return mapper;
	}

	/**
	 * @param entity
	 *            the entity we're saving
	 * @return the entity updated with new version & primary key
	 * @throws IOException
	 *             IO exception can be formed if object does not map correctly
	 */
	public EntityType saveEntity(final EntityType entity) throws IOException {
		return save(entity);
	}

	/**
	 * @param entity
	 *            the entity we're saving
	 * @return the entity updated with new version & primary key
	 * @throws IOException
	 *             IO exception can be formed if object does not map correctly
	 */
	public EntityType forceSaveEntity(final EntityType entity) throws IOException {
		EntityType existingResponse = getEntity(entity.getId());
		if (existingResponse != null) {
			injectIntoEntity(existingResponse, entity);
		}

		return save(entity);
	}

	/**
	 * @param entity
	 *            the entity we're saving
	 * @param <SaveEntityType>
	 *            the entity type for the entity
	 * @return the entity updated with new version & primary key
	 * @throws IOException
	 *             IOException can be formed if object does not map correctly
	 */
	private <SaveEntityType extends SimpleEntity> SaveEntityType save(final SaveEntityType entity) throws IOException {
		WebResource entityUrl = getResource().path(entity.getId());
		String string = getMapper().writeValueAsString(entity);
		ClientResponse response = entityUrl.put(ClientResponse.class, string);
		String respString = response.getEntity(String.class);
		EntityResponse entityResponse = getMapper().readValue(respString, EntityResponse.class);
		injectIntoEntity(entityResponse, entity);
		return entity;
	}

	/**
	 * @param entityId
	 *            the ID of the entity we're grabbing
	 * @return the grabbed person, returns null if no person was found for the
	 *         given id
	 */
	public EntityType getEntity(final String entityId) {
		return get(entityId, entityType);

	}

	/**
	 * @param entityId
	 *            the id of the entity to fetch
	 * @param entityTypeForResult
	 *            defines the type the result should be mapped to
	 * @param <GetEntityType>
	 *            the return type
	 * @return returns the result from the couchDB database mapped as the
	 *         provided entity type
	 */
	private <GetEntityType extends SimpleEntity> GetEntityType get(
			final String entityId,
			final Class<GetEntityType> entityTypeForResult) {
		WebResource entityUrl = getResource().path(entityId);
		ClientResponse response = entityUrl.get(ClientResponse.class);
		String string = response.getEntity(String.class);
		GetEntityType entity;
		try {
			entity = getMapper().readValue(string, entityTypeForResult);
		} catch (IOException ioException) {
			ioException.printStackTrace();
			entity = null;
		}
		return entity;
	}

	/**
	 * @return the current views on the database for the DAO
	 */
	protected Views<EntityType> getViews() {
		@SuppressWarnings("unchecked")
		Views<EntityType> views = get(viewsId, Views.class);
		if (views == null) {
			views = new Views<>();
			views.setId(viewsId);
			views.setLanguage(Views.DEFAULT_LANGUAGE);
		}
		return views;
	}

	/**
	 * @param views
	 *            the collection of views to save
	 * @return the views with updated
	 * @throws IOException
	 *             IOException can be formed if object does not map correctly,
	 *             presumably due to corrupted views
	 */
	protected Views<EntityType> saveViews(final Views<EntityType> views) throws IOException {
		return save(views);
	}

	/**
	 * @param fields
	 *            the fields to create indexes for
	 */
	protected void createIndexes(final Collection<String> fields) {
		Views<EntityType> views = getViews();
		views.getViews().putAll(CouchDbUtilities.generateIndexesForFields(fields));
		try {
			saveViews(views);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the resource from which views may be queried
	 */
	private WebResource getViewResource() {
		WebResource resource = getResource();
		WebResource viewResource = resource.path(viewsId + VIEW_ACCESS_SUFFIX);
		return viewResource;
	}

	/**
	 * @param field
	 *            the field on which the restriction is to be placed
	 * @param value
	 *            the expected value for the field
	 * @return the list of entities that match the expectation
	 * @throws IOException
	 *             an IO exception will be thrown if an object fails to map
	 *             properly
	 */
	public List<EntityType> getEntitiesByField(final String field, final String value) throws IOException {
		WebResource viewResource =
				getViewResource().path(field).queryParam("key", CouchDbUtilities.parseParameter(value));
		ClientResponse response = viewResource.get(ClientResponse.class);
		String string = response.getEntity(String.class);
		QueryResponse<EntityType> queryResponse = getMapper().readValue(string, getTypeReferenceForResponse());
		List<EntityType> entities = CouchDbUtilities.getEntitiesFromQueryReponse(queryResponse);
		return entities;
	}

	/**
	 * @param fieldMap
	 *            map of entities with their expected values
	 * @return set of entities that match all expected values
	 * @throws IOException
	 *             an IO exception will be thrown if an object fails to map
	 *             properly
	 */
	public Set<EntityType> getEntitiesByFieldMap(final Map<String, String> fieldMap) throws IOException {
		Set<EntityType> returnSet = null;
		// For every field ...
		for (String field : fieldMap.keySet()) {
			// ... get all the entities according to the restriction
			List<EntityType> list = getEntitiesByField(field, fieldMap.get(field));
			// ... if this is the first field, add all returned values to the
			// return set ...
			if (returnSet == null) {
				returnSet = new HashSet<>();
				returnSet.addAll(list);
			}
			// ... otherwise, remove anything from the return set that isn't
			// also in this new list
			else {
				Set<EntityType> andSet = new HashSet<>();
				for (EntityType entity : returnSet) {
					if (list.contains(entity)) {
						andSet.add(entity);
					}
				}
				returnSet = andSet;
			}
		}
		return returnSet;
	}

	/**
	 * @return as health if: either the database or the backup database are
	 *         available
	 */
	public Boolean isHealthy() {
		try {
			getResource();
			return true;
		} catch (ClientHandlerException clientHandlerException) {
			return false;
		}
	}

	/**
	 * @return an appropriate type reference for mapping query responses
	 */
	protected abstract TypeReference<QueryResponse<EntityType>> getTypeReferenceForResponse();

	/**
	 * Inject the response parameters into the provided entity.
	 *
	 */
	private void injectIntoEntity(final SimpleEntity injectingEntity, final SimpleEntity injectIntoEntity) {
		injectIntoEntity.setId(injectingEntity.getId());
		injectIntoEntity.setRevision(injectingEntity.getRevision());
	}
}
