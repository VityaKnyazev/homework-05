package ru.clevertec.knyazev.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {

	public static void main(String[] args) {
		JsonParser jsonParser = new JsonParserImpl();
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