package cn.devezhao.persist4j.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 1.0, 12/21/2018
 * @version $Id$
 */
public class CaseInsensitiveMap<V> extends HashMap<String, V> {
	private static final long serialVersionUID = -675936187876527993L;

	private Map<String, String> keysMapping = new HashMap<>();

	public CaseInsensitiveMap() {
		super();
	}

	public CaseInsensitiveMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public CaseInsensitiveMap(int initialCapacity) {
		super(initialCapacity);
	}

	public CaseInsensitiveMap(Map<? extends String, ? extends V> m) {
		super(m);
	}

	@Override
	public V put(String key, V value) {
		V v = super.put(key, value);
		putKeyMapping(key);
		return v;
	}

	@Override
	public void putAll(Map<? extends String, ? extends V> m) {
		super.putAll(m);
		for (String key : m.keySet()) {
			putKeyMapping(key);
		}
	}

	@Override
	public V putIfAbsent(String key, V value) {
		key = getKeyMapping(key);
		return super.putIfAbsent(key, value);
	}

	@Override
	public V remove(Object key) {
		key = getKeyMapping((String) key);
		V v = super.remove(key);
		if (v != null) {
			removeKeyMapping((String) key);
		}
		return v;
	}

	@Override
	public boolean replace(String key, V oldValue, V newValue) {
		key = getKeyMapping(key);
		return super.replace(key, oldValue, newValue);
	}

	@Override
	public V replace(String key, V value) {
		key = getKeyMapping(key);
		return super.replace(key, value);
	}

	@Override
	public V get(Object key) {
		key = getKeyMapping((String) key);
		return super.get(key);
	}

	@Override
	public V getOrDefault(Object key, V defaultValue) {
		key = getKeyMapping((String) key);
		return super.getOrDefault(key, defaultValue);
	}
	
	@Override
	public boolean containsKey(Object key) {
		key = getKeyMapping((String) key);
		return super.containsKey(key);
	}

	@Override
	public void clear() {
		super.clear();
		keysMapping.clear();
	}

	// --

	private void putKeyMapping(String key) {
		keysMapping.put(key.toLowerCase(), key);
	}

	private String getKeyMapping(String key) {
		String keyLower = keysMapping.get(key.toLowerCase());
		return keyLower == null ? key.toString() : keyLower;
	}

	private void removeKeyMapping(String key) {
		keysMapping.remove(key.toLowerCase());
	}
}
