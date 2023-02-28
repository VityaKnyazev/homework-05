package ru.clevertec.knyazev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.clevertec.knyazev.json.JsonToObjectParserImpl;
import ru.clevertec.knyazev.json.ObjectToJsonParserImpl;
import ru.clevertec.knyazev.json.exception.ParserException;

public class App {

	public static void main(String[] args) {
		Pattern arrFieldPattern = Pattern.compile("\\s{0,}\"\\w+?\"\\s{0,}:\\s{0,}\\[");
		Matcher m = arrFieldPattern.matcher("255 \"strArr\":[");
		while (m.find()) {
			System.out.println(m.start());
		}
		
		
		ObjectToJsonParserImpl jsonParser = new ObjectToJsonParserImpl();
		Vano v = new Vano();
		v.name = 12;
		v.family = "Full";
		v.age = 18;
		v.isGod = false;
		v.sergo = new Sergo();
		v.sergo.name = 128;
		v.sergo.family = "Ser";

		String res = jsonParser.parseToJson(v);
		System.out.println(res);
		
		String jSon = "{\"name\":12,\"family\":\"Full\",\"age\":18,\"isGod\":false,\"sergo\":{\"name\":128,\"family\":\"Ser\"},\"strArr\":[[{\"name\":12,\"family\":\"5\"},{\"name\":null,\"family\":null}],[{\"name\":4,\"family\":\"8\"}]],\"strList\":[[{\"name\":13,\"family\":\"8\"}],[{\"name\":14,\"family\":\"6\"},{\"name\":17,\"family\":\"10\"}]],\"strMap\":{\"Something more\":[{\"name\":125,\"family\":\"herself\"},{\"name\":512,\"family\":\"history\"}],\"Something\":[{\"name\":155,\"family\":\"self\"},{\"name\":215,\"family\":\"hero\"}]}}";
		JsonToObjectParserImpl jsonToObjectParserImpl = new JsonToObjectParserImpl();
		try {
			jsonToObjectParserImpl.parseToObject(jSon, Vano.class);
		} catch (ParserException e) {
			e.printStackTrace();
		}
//		List<String> cob = jsonParser.getCompositeObjectsNames(v);
//		String fieldName = cob.get(0);
//		
//		try {
//			Field compField = jsonParser.getCompositeObject(v, fieldName);
//			Object obj = compField.get(v);
//			System.out.println(obj);
//			
//			Field[] compFields =  compField.getType().getFields();
//			Stream.of(compFields).forEach(compF -> {
//				try {
//					compF.setAccessible(true);
//					System.out.println(compF.get(obj));
//				} catch (IllegalArgumentException | IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
//		} catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
	}

	public static class Vano {
		private Integer name;
		private String family;
		private int age;
		private boolean isGod;
		private Sergo sergo;
		private Sergo[][] strArr = {{new Sergo(12, "5"), new Sergo()}, {new Sergo(4, "8")}};
		private Set<List <Sergo>> strList = new HashSet<>() {{
			add(new ArrayList<>() {{
				add(new Sergo(13, "8"));
			}});
			add(new ArrayList<>() {{
				add(new Sergo(14, "6"));
				add(new Sergo(17, "10"));
			}});
		}};
		private Map<String, List<Sergo>> strMap = new HashMap<>() {{
			put("Something", new ArrayList<>() {{
				add(new Sergo(155, "self"));
				add(new Sergo(215, "hero"));
			}});
			put("Something more", new ArrayList<>() {{
				add(new Sergo(125, "herself"));
				add(new Sergo(512, "history"));
			}});
		}};
	}

	public static class Sergo {
		private Integer name;
		private String family;
		
		Sergo() {}
		
		Sergo(Integer name, String family) {
			this.name = name;
			this.family = family;
		}
	}
}