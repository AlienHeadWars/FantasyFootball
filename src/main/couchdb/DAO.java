package main.couchdb;

import java.util.function.Function;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class DAO<T extends Entity> {

	private WebResource resource;
	private Class<T> entityType;

	public DAO(String couchdbUrl, Client client, Class<T> entityType) {
		resource = client.resource(couchdbUrl).path(
				entityType.getSimpleName().toLowerCase());
		ClientResponse response = resource.put(ClientResponse.class);
		// if (response.getStatus() != 201) {
		// ErrorResponse errorResponse = response
		// .getEntity(ErrorResponse.class);
		// if (!errorResponse.getError().equals("file_exists"))
		// throw new RuntimeException(errorResponse.getReason());
		// }
		this.entityType = entityType;
	};

	public void saveEntity(T entity) {
		saveEntity(entity, false);
	}

	public void saveEntity(T entity, Boolean forceUpdate) {

		WebResource path = resource.path(entity.getId());

		if (forceUpdate) {
			try {
				Entity existing = path.get(Entity.class);
				entity.setRev(existing.getRev());
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}

		path.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = path.put(ClientResponse.class, entity);
		if (response.getStatus() != 201 && response.getStatus() != 200) {
			ErrorResponse errorResponse = response
					.getEntity(ErrorResponse.class);
			throw new RuntimeException(errorResponse.getReason());
		}
	}

	public T getEntity(String id) {
		return resource.path(id).get(entityType);
	}

}
