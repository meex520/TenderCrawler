package com.redis.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {

	private static String JEDIS_IP;
	private static int JEDIS_PORT;
	private static String JEDIS_PASSWORD;
	//private static String JEDIS_SLAVE;

	private static JedisPool jedisPool=null;

	public void init()
	{
		//Configuration conf = Configuration.getInstance();
		//JEDIS_IP = conf.getString("jedis.ip", "127.0.0.1");
		//JEDIS_PORT = conf.getInt("jedis.port", 6379);
		//JEDIS_PASSWORD = conf.getString("jedis.password", null);
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(5000);
		config.setMaxIdle(256);//20
		config.setMaxWait(5000L);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		config.setTestWhileIdle(true);
		config.setMinEvictableIdleTimeMillis(60000l);
		config.setTimeBetweenEvictionRunsMillis(3000l);
		config.setNumTestsPerEvictionRun(-1);
		String ip=com.util.PropertyUtil.getvalue("redisip");
		String port=com.util.PropertyUtil.getvalue("redisport");
		jedisPool = new JedisPool(config,ip, Integer.parseInt(port), 60000);
	}
	/**
	 * 判断是否有该条链接
	 * @param url
	 * @param taskid
	 * @return  返回ture说明redis中存在该条数据，返回false则说明是新产生的链接
	 */
	public  boolean hasUrlget(String url,int taskid) {

		boolean value = false;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			value = jedis.exists(url+"_"+taskid);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}

		return value;
	}
	/**
	 * 
	 * @param url
	 * @param taskid
	 * @return 返回ture说明存入成功，返回false说明存入失败
	 */
	public  boolean setUrl(String url,int taskid) {
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			jedis.setex(url+"_"+taskid,3600*24*3600,"");
			return true;
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}

		return false;
	}
	
	/**
	 * 获取数据
	 * @param key
	 * @return
	 */
	public  String get(String key) {

		String value = null;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}

		return value;
	}

	public static void close(Jedis jedis) {
		try {
			jedisPool.returnResource(jedis);

		} catch (Exception e) {
			if (jedis.isConnected()) {
				jedis.quit();
				jedis.disconnect();
			}
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public  byte[] get(byte[] key) {

		byte[] value = null;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}

		return value;
	}

	public  void set(byte[] key, byte[] value) {

		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	public  void set(byte[] key, byte[] value, int time) {

		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			jedis.expire(key, time);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	public  void hset(byte[] key, byte[] field, byte[] value) {
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	public  void hset(String key, String field, String value) {
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public  String hget(String key, String field) {

		String value = null;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}

		return value;
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public  byte[] hget(byte[] key, byte[] field) {

		byte[] value = null;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}

		return value;
	}

	public  void hdel(byte[] key, byte[] field) {

		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			jedis.hdel(key, field);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	/**
	 * 存储REDIS队列 顺序存储
	 * @param byte[] key reids键名
	 * @param byte[] value 键值
	 */
	public  void lpush(byte[] key, byte[] value) {

		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {

			jedis = jedisPool.getResource();
			jedis.lpush(key, value);

		} catch (Exception e) {

			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			//返还到连接池
			close(jedis);

		}
	}

	/**
	 * 存储REDIS队列 反向存储
	 * @param byte[] key reids键名
	 * @param byte[] value 键值
	 */
	public  void rpush(byte[] key, byte[] value) {

		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {

			jedis = jedisPool.getResource();
			jedis.rpush(key, value);

		} catch (Exception e) {

			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			//返还到连接池
			close(jedis);

		}
	}

	/**
	 * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
	 * @param byte[] key reids键名
	 * @param byte[] value 键值
	 */
	public  void rpoplpush(byte[] key, byte[] destination) {

		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {

			jedis = jedisPool.getResource();
			jedis.rpoplpush(key, destination);

		} catch (Exception e) {

			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			//返还到连接池
			close(jedis);

		}
	}

	/**
	 * 获取队列数据
	 * @param byte[] key 键名
	 * @return
	 */
	public  List<byte[]> lpopList(byte[] key) {

		List<byte[]> list = null;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {

			jedis = jedisPool.getResource();
			list = jedis.lrange(key, 0, -1);

		} catch (Exception e) {

			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			//返还到连接池
			close(jedis);

		}
		return list;
	}

	/**
	 * 获取队列数据
	 * @param byte[] key 键名
	 * @return
	 */
	public  byte[] rpop(byte[] key) {

		byte[] bytes = null;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {

			jedis = jedisPool.getResource();
			bytes = jedis.rpop(key);

		} catch (Exception e) {

			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			//返还到连接池
			close(jedis);

		}
		return bytes;
	}

	public  void hmset(Object key, Map<String, String> hash) {
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			jedis.hmset(key.toString(), hash);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);

		}
	}

	public  void hmset(Object key, Map<String, String> hash, int time) {
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {

			jedis = jedisPool.getResource();
			jedis.hmset(key.toString(), hash);
			jedis.expire(key.toString(), time);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);

		}
	}

	public  List<String> hmget(Object key, String... fields) {
		List<String> result = null;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {

			jedis = jedisPool.getResource();
			result = jedis.hmget(key.toString(), fields);

		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);

		}
		return result;
	}

	public  Set<String> hkeys(String key) {
		Set<String> result = null;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			result = jedis.hkeys(key);

		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);

		}
		return result;
	}

	public  List<byte[]> lrange(byte[] key, int from, int to) {
		List<byte[]> result = null;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			result = jedis.lrange(key, from, to);

		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);

		}
		return result;
	}

	public  Map<byte[], byte[]> hgetAll(byte[] key) {
		Map<byte[], byte[]> result = null;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			result = jedis.hgetAll(key);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);
		}
		return result;
	}

	public  void del(byte[] key) {

		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	public  long llen(byte[] key) {

		long len = 0;
		Jedis jedis = null;
		if(jedisPool==null)
		{
			init();
		}
		try {
			jedis = jedisPool.getResource();
			jedis.llen(key);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
		return len;
	}

}