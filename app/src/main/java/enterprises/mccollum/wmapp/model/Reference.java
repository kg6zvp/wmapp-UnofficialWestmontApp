package enterprises.mccollum.wmapp.model;

public class Reference<K> {
	K id;
	String url;
	
	public Reference(){}
	public Reference(K id, String url){
		setId(id);
		setUrl(url+String.valueOf(id));
	}
	public K getId() {
		return id;
	}
	public void setId(K id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
