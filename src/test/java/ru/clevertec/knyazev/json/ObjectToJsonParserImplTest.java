package ru.clevertec.knyazev.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import ru.clevertec.knyazev.json.util.Car;
import ru.clevertec.knyazev.json.util.Family;
import ru.clevertec.knyazev.json.util.Human;

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

}
