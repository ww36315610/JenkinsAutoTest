package finup.feather.utils.file;

import java.util.*;

/**
 * 继承了Properties类，保证按照文件顺序保存到Map中
 * @author WangJian：2017-02-19
 */

public class OrderedProperties extends Properties {

	private static final long serialVersionUID = -4627607243846121965L;
	private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

	public Enumeration<Object> keys() {
		return Collections.<Object> enumeration(keys);
	}

	public Object put(Object key, Object value) {
		keys.add(key);
		return super.put(key, value);
	}

	public Set<Object> keySet() {
		return keys;
	}

	public Set<String> stringPropertyNames() {
		Set<String> set = new LinkedHashSet<String>();
		for (Object key : this.keys) {
			set.add((String) key);
		}
		return set;
	}
}
