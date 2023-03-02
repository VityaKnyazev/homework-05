package ru.clevertec.knyazev.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import ru.clevertec.knyazev.json.util.Car;
import ru.clevertec.knyazev.json.util.Dealer;
import ru.clevertec.knyazev.json.util.Empty;
import ru.clevertec.knyazev.json.util.Family;
import ru.clevertec.knyazev.json.util.Human;
import ru.clevertec.knyazev.json.util.ManPassport;

public class ObjectToJsonParserImplTest {
	private ObjectToJsonParserImpl objectToJsonParserImpl;
	private Gson gson;
	
	@BeforeEach
	public void setUp() {
		objectToJsonParserImpl = new ObjectToJsonParserImpl();
		gson = new Gson();
	}

	@Test
	public void checkParseToJsonOnSimpleObjectShouldReturnJSONString() {
		Human human = new Human.Builder()
				.setName("Zafar")
				.setFamily("Khalid")
				.setAge(29)
				.setIsGod(false)
				.setChildrenQuantity(3)
				.build();
		
		String expectedJson = gson.toJson(human);
		String actualJson = objectToJsonParserImpl.parseToJson(human);
		
		assertThat(actualJson).isEqualTo(expectedJson);
	}
	
	@Test
	public void checkParseToJsonOnCompositeObjectShouldReturnJSONString() {
		Family family = new Family.Builder()
				.setName("Standard")
				.setAgeTogether(15)
				.setMan(new Human.Builder()
						.setName("Miko")
						.setFamily("Veter")
						.setAge(45)
						.setIsGod(false)
						.setChildrenQuantity(3)
						.build())
				.setWoman(new Human.Builder()
						.setName("Margo")
						.setFamily("Veter")
						.setAge(37)
						.setIsGod(true)
						.setChildrenQuantity(2)
						.build())
				.build();
		
		String expectedJson = gson.toJson(family);
		String actualJson = objectToJsonParserImpl.parseToJson(family);
		
		assertThat(actualJson).isEqualTo(expectedJson);
	}
	
	@Test
	public void checkParseToJsonOnCompositeObjectWithArrayShouldReturnJSONString() {
		Human[][] humans = {{new Human.Builder()
							.setName("Anna")
							.setFamily("Vera")
							.setAge(27)
							.setIsGod(false)
							.setChildrenQuantity(0)
							.build()}};
		
		Car car = new Car.Builder()
				.setProducers(new String[]{"Alex Antonov", "Andre Bogomazov", "Paul Liney"})
				.setIsExclusive(false)
				.setProductionYear(1958)
				.setPassengers(humans)
				.build();
		
		String expectedJson = gson.toJson(car);
		String actualJson = objectToJsonParserImpl.parseToJson(car);
		
		assertThat(actualJson).isEqualTo(expectedJson);
	}
	
	@Test
	public void checkParseToJsonOnCompositeObjectWithCollectionShouldReturnJSONString() {
		ManPassport manPassport = new ManPassport.Builder()
				.setName("Sergo")
				.setFamily("Alkin")
				.setChildrens(new HashSet<>() {
					
				private static final long serialVersionUID = -7399310972513077651L;

				{
					add(new Human.Builder()
							.setName("Anton")
							.setFamily("Alkin")
							.setAge(0)
							.setChildrenQuantity(0)
							.setIsGod(false)
							.build());
					add(new Human.Builder()
							.setName("Dasha")
							.setFamily("Alkin")
							.setChildrenQuantity(0)
							.setIsGod(false)
							.setAge(0)
							.build());
				}})
				.setAddresses(new ArrayList<>() {
					
				private static final long serialVersionUID = 7043119155940843813L;

				{
					add("Minsk, ul. Plehanova, 5, 125");
					add("Minsk, ul. Lenina, 12, 18");
					add("Gomel, ul. Lepeshinskogo, 12, 38");
				}})
				.setWives(new LinkedList<>() {
				
				private static final long serialVersionUID = -3167322156514520118L;

				{
					add(new Human.Builder()
							.setName("Galka")
							.setFamily("Alkin")
							.setChildrenQuantity(3)
							.setIsGod(true)
							.setAge(35)
							.build());
					add(new Human.Builder()
							.setName("Valya")
							.setFamily("Alkin")
							.setChildrenQuantity(1)
							.setIsGod(false)
							.setAge(19)
							.build());
				}})
				.build();
		
		String expectedJson = gson.toJson(manPassport);
		String actualJson = objectToJsonParserImpl.parseToJson(manPassport);
		
		assertThat(actualJson).isEqualTo(expectedJson);
	}
	
	@Test
	public void checkParseToJsonOnCompositeObjectWithMapShouldReturnJSONString() {
		Human[][] passengers = {{new Human.Builder()
			.setName("Manya")
			.setFamily("Galya")
			.setAge(27)
			.setIsGod(true)
			.setChildrenQuantity(0)
			.build()}};
		
		Dealer dealer = new Dealer.Builder()
				.setName("Alex")
				.setAge(34)
				.setIsTop(true)
				.setSellingHistory(new HashMap<>() {
					private static final long serialVersionUID = -4280431847393918672L;

				{
					put(1, 
					new Car.Builder()
					.setProducers(new String[]{"Alex Antonov", "Andre Bogomazov", "Paul Liney"})
					.setIsExclusive(false)
					.setProductionYear(1958)
					.setPassengers(passengers)
					.build());
				}})
				.build();
		
		String expectedJson = gson.toJson(dealer);
		String actualJson = objectToJsonParserImpl.parseToJson(dealer);
		
		assertThat(actualJson).isEqualTo(expectedJson);
	}
	
	@Test
	public void checkParseToJsonWhenGivingNullShouldReturnEmptyJSONString() {
		
		String actualJson = objectToJsonParserImpl.parseToJson(null);
		
		assertThat(actualJson).isEqualTo("{}");
	}
	
	@Test
	public void checkParseToJsonWhenGivingEmptyFieldsObjectShouldReturnEmptyJSONString() {
		Empty objectWithEmptyFields = new Empty();
		
		String actualJson = objectToJsonParserImpl.parseToJson(objectWithEmptyFields);
		
		assertThat(actualJson).isEqualTo("{}");
	}

}
