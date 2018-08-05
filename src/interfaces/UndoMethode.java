package interfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import editor.DefaultEditorPanel.Matrix;

public class UndoMethode extends UndoObject {
	static HashMap<Method, Integer> hmId = new HashMap<>();
	// TODO keep id by same Method
	Method set;
	String name;

	public UndoMethode(Object old, Object neu, String string, Object obj) {
		this(old, neu, string, obj, string);
	}

	public UndoMethode(Object old, Object neu, String setString, Object obj,
			String name) {
		super(0, old, neu, obj);
		try {
			this.set = obj.getClass().getMethod(setString, neu.getClass(),
					Boolean.class);
			if (hmId.containsKey(set)) {
				id = hmId.get(set);
			} else {
				id = Matrix.undoID++;
				hmId.put(set, id);
			}
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
		}
		this.name = name;
	}

	@Override
	public void doRedo() {
		try {
			set.invoke(obj, neu, false);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doUndo() {
		try {
			set.invoke(obj, old, false);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return name;
	}

	public void setNeu(Object i) {
		neu = i;
	}

}
