package interfaces;

import java.util.Collection;

public class Destroyer {
	public static void destroy(Object c) {
		if (c == null) {
			return;
		}
		if (c instanceof Collection<?>) {
			destroyList((Collection<?>) c);
		} else {
			destroyObject(c);
		}
	}

	public static void destroyList(Collection<?> c) {
		if (c == null) {
			return;
		}
		for (Object o : c) {
			if (o instanceof Collection<?>) {
				destroyList((Collection<?>) o);
			}
			destroyObject(o);
		}
		c.clear();
		c = null;
	}

	public static void destroyObject(Object o) {
		if (o instanceof DestroyAble) {
			((DestroyAble) o).destroy();
		}
		o = null;
	}
}
