package enterprises.mccollum.wmapp.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by smccollum on 02.05.17.
 */
public class CollectionFieldDeserializer<T, K> implements JsonDeserializer<ForeignCollection<T>> {
	GenericEntityManager<T, K> em;
	
	public CollectionFieldDeserializer(GenericEntityManager<T, K> em){
		this.em = em;
	}
	
	@Override
	public ForeignCollection<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		List<Reference<K>> list = new Gson().fromJson(json, new TypeToken<List<Reference<K>>>(){}.getType());
		return null;
	}
}
