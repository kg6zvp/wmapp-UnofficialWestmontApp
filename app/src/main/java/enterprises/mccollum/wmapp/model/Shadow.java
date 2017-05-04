package enterprises.mccollum.wmapp.model;

import enterprises.mccollum.wmapp.model.GenericEntityManager;
import enterprises.mccollum.wmapp.model.ModulePersistenceResolver;

/**
 * Created by smccollum on 03.05.17.
 */

public interface Shadow<T, K> {
	K getId();
	void setId(K id);
	
	/**
	 * populate this stop and save to the database
	 * @param pm
	 * @return
	 */
	T populate(ModulePersistenceResolver pm);
}
