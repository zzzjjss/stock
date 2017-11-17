package com.uf.store.service.cache;

public interface CacheService {
	public void putObject(String key,Object value);
	public Object getCachedObject(String key); 
}
