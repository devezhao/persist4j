package cn.devezhao.persist4j.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 大小写不敏感 Map
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 08/17/2019
 */
public class CaseInsensitiveMap<V> implements Map<String, V> {
	
	private Map<String, String> originalKeys = new HashMap<>();
	
	private Map<String, V> delegate = new org.apache.commons.collections4.map.CaseInsensitiveMap<>();
	
	public CaseInsensitiveMap() {
		super();
	}
	
	public CaseInsensitiveMap(Map<? extends String, ? extends V> m) {
		this();
		this.putAll(m);
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return delegate.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return delegate.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return delegate.get(key);
	}
	
	@Override
	public V put(String key, V value) {
		originalKeys.put(convertKey(key), key);
		return delegate.put(key, value);
	}

	@Override
	public V remove(Object key) {
		originalKeys.remove(convertKey(key));
		return delegate.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends V> m) {
		for (String key : m.keySet()) {
			originalKeys.put(convertKey(key), key);
		}
		delegate.putAll(m);
	}

	@Override
	public void clear() {
		originalKeys.clear();
		delegate.clear();
	}

	@Override
	public Set<String> keySet() {
		return new HashSet<String>(originalKeys.values());
	}

	@Override
	public Collection<V> values() {
		return delegate.values();
	}

	@Override
	public Set<Entry<String, V>> entrySet() {
		Set<Entry<String, V>> es = new HashSet<>();
		for (Entry<String, V> e : delegate.entrySet()) {
			String oKey = originalKeys.get(convertKey(e.getKey()));
			es.add(new CIEntry(oKey, e.getValue()));
		}
		return es;
	}
	
	private String convertKey(final Object key) {
		return key.toString().toUpperCase();
    }
	
	/**
	 * @param <V>
	 */
	class CIEntry implements Entry<String, V> {
		
		private String key;
		private V value;
		
		protected CIEntry(String key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public String getKey() {
			return key;
		}
		@Override
		public V getValue() {
			return value;
		}
		@Override
		public V setValue(V value) {
			this.value = value;
			return value;
		}
		@Override
		public String toString() {
			return String.format("%s=%s", key, value);
		}
	}
}
