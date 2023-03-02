package ru.clevertec.knyazev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.clevertec.knyazev.json.JsonToObjectParserImpl;
import ru.clevertec.knyazev.json.ObjectToJsonParserImpl;
import ru.clevertec.knyazev.json.exception.ParserException;

public class App {

	public static void main(String[] args) {
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
			Vano vano = jsonToObjectParserImpl.parseToObject(jSon, Vano.class);
		} catch (ParserException e) {
//			e.printStackTrace();
		}
		
	}

	public static class Vano {
		private Integer name;
		private String family;
		private int age;
		private boolean isGod;
		private Sergo sergo;
		private Sergo[][] strArr = {{new Sergo(12, "5"), new Sergo()}, {new Sergo(4, "8")}};
		private Set<List <Sergo>> strList = new HashSet<>() {
			private static final long serialVersionUID = -7284698454651738976L;

		{
			add(new ArrayList<>() {
				private static final long serialVersionUID = -9011891526631972492L;

			{
				add(new Sergo(13, "8"));
			}});
			add(new ArrayList<>() {
				private static final long serialVersionUID = -2103776024826338978L;

			{
				add(new Sergo(14, "6"));
				add(new Sergo(17, "10"));
			}});
		}};
		private Map<String, List<Sergo>> strMap = new HashMap<>() {
			private static final long serialVersionUID = -4692465108802060124L;

		{
			put("Something", new ArrayList<>() {
				private static final long serialVersionUID = -5852282862506285420L;

			{
				add(new Sergo(155, "self"));
				add(new Sergo(215, "hero"));
			}});
			put("Something more", new ArrayList<>() {
				private static final long serialVersionUID = -7929685995486498383L;

			{
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