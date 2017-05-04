package enterprises.mccollum.wmapp.model;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by smccollum on 02.05.17.
 */

public class GenericEntityManager<T, K> {
	Dao<T, K> sprig;
	
	public GenericEntityManager(Dao<T, K> sprig){
		this.sprig = sprig;
	}
	
	public boolean containsKey(K key){
		try{
			return sprig.idExists(key);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public T persist(T data){
		try{
			sprig.create(data);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
		return syncFromDB(data);
	}
	
	public T syncFromDB(T data){
		try {
			sprig.refresh(data);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
		return data;
	}
	
	public T save(T data){
		try{
			sprig.update(data);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
		return data;
	}
	
	public T get(K key){
		try{
			return sprig.queryForId(key);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get all T in database
	 * @return
	 */
	public List<T> getAll(){
		try{
			return sprig.queryForAll();
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public boolean isTableEmpty(){
		try{
			return (sprig.queryForAll().size() < 1);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void saveAll(List<T> data){
		for(T d : data){
			save(d);
		}
	}
	
	public void removeKey(K key){
		try{
			sprig.deleteById(key);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void remove(T data){
		try {
			sprig.delete(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void removeAll(List<T> data){
		for(T d : data){
			remove(d);
		}
	}
}
