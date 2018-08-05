package editor.tabelle;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;



public class Rhino {
	public ScriptEngine engine;

	public Rhino(Object o) {
		engine = new ScriptEngineManager().getEngineByName("JavaScript");
		try {
			engine.put("object", o);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void set(String setScript, Object[] value, Object target) {
		setObject(target);
		set(setScript, value);
	}

	public void set(String setScript, Object[] value) {
		try {
			if (setScript == null) {
				return;
			}
			String inKlammern = "";
			int cont = 0;
			for (Object o : value) {
				engine.put("value" + cont, o);
				if (cont != 0) {
					inKlammern += ",";
				}
				inKlammern += "value" + cont;
				cont++;
			}
			String s = "object." + setScript + "(" + inKlammern + ");";
			exe(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object get(String getScript, Object target) {
		setObject(target);
		return get(getScript);
	}

	public Object get(String getScript) {
		try {
			String s;
			if (getScript.contains("(")) {
				s = "wiedergabe = object." + getScript + ";";
			} else {
				s = "wiedergabe = object." + getScript + "();";
			}

			return exe(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	public Object exe(String getScript) {
		Object s = null;
		try {

			engine.eval(getScript);
			s = engine.get("wiedergabe");
//			System.out.println(s);
//			if (s instanceof NativeJavaObject) {
//				engine.eval("wiedergabe = String(wiedergabe)");
//				s = engine.get("wiedergabe");
//			}
		} catch (ScriptException e) {
			s = "<html><b>Error<html>";
			System.err.println("Rhino 81: " + e + "\n" + getScript);
		}
		return s;
	}

	public void setObject(Object o) {
		engine.put("object", o);
	}

}
