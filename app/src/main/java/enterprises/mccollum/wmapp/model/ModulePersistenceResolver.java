package enterprises.mccollum.wmapp.model;

import enterprises.mccollum.wmapp.model.GenericEntityManager;

/**
 * Created by smccollum on 03.05.17.
 */

public interface ModulePersistenceResolver {
	GenericEntityManager<?, ?> getEntityManager(Class<?> typeOfEntity);
}
