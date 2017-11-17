package com.uf.store.service.cache;

import java.util.concurrent.TimeUnit;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.springframework.stereotype.Service;

@Service
public class EhcacheService implements CacheService {
	private Cache<String, Object> cache;
	public EhcacheService() {
		long tti=10l;
		String cacheName="session";
		CacheConfiguration<String, Object> configuration=CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class,Object.class,ResourcePoolsBuilder.heap(10000l))
				.withExpiry(Expirations.timeToIdleExpiration(Duration.of(tti, TimeUnit.MINUTES))).build();
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache(cacheName, configuration).build(true);
		cache=cacheManager.getCache(cacheName, String.class, Object.class);
	}

	public Object getCachedObject(String key) {
		return cache.get(key);
	}

	@Override
	public void putObject(String key, Object value) {
		cache.put(key, value);
	}
}
