package cn.hpn.redis;

import java.util.List;


public class JedisClientCluster implements JedisClient {
	
	private JedisClient jedisClient;
	

	public JedisClient getjedisClient() {
		return jedisClient;
	}

	public void setjedisClient(JedisClient jedisClient) {
		this.jedisClient = jedisClient;
	}

	@Override
	public String set(String key, String value) {
		return jedisClient.set(key, value);
	}

	@Override
	public String get(String key) {
		return jedisClient.get(key);
	}

	@Override
	public Boolean exists(String key) {
		return jedisClient.exists(key);
	}

	@Override
	public Long expire(String key, int seconds) {
		return jedisClient.expire(key, seconds);
	}

	@Override
	public Long ttl(String key) {
		return jedisClient.ttl(key);
	}

	@Override
	public Long incr(String key) {
		return jedisClient.incr(key);
	}

	@Override
	public Long hset(String key, String field, String value) {
		return jedisClient.hset(key, field, value);
	}

	@Override
	public String hget(String key, String field) {
		return jedisClient.hget(key, field);
	}

	@Override
	public Long hdel(String key, String... field) {
		return jedisClient.hdel(key, field);
	}

	@Override
	public Boolean hexists(String key, String field) {
		return jedisClient.hexists(key, field);
	}

	@Override
	public List<String> hvals(String key) {
		return jedisClient.hvals(key);
	}

	@Override
	public Long del(String key) {
		return jedisClient.del(key);
	}

}
