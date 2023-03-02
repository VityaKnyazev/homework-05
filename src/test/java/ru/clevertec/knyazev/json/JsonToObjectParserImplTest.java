package ru.clevertec.knyazev.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.json.exception.ParserException;
import ru.clevertec.knyazev.json.util.Human;

public class JsonToObjectParserImplTest {
	private JsonToObjectParserImpl jsonToObjectParserImpl;

	@BeforeEach
	public void setUp() {
		jsonToObjectParserImpl = new JsonToObjectParserImpl();
	}

	@Test
	public void checkParseToObjectOnSimpleFiedsShouldReturnInitializedObject() throws ParserException {
		String jSON = "{\"name\":\"Sanya\",\"family\":\"Krilov\",\"age\":36,\"isGod\":false,\"childrenQuantity\":2}";

		Human actualHuman = jsonToObjectParserImpl.parseToObject(jSON, Human.class);

		assertAll(
				() -> assertThat(actualHuman).isNotNull(),
				() -> assertThat(actualHuman.getName()).isEqualTo("Sanya"),
				() -> assertThat(actualHuman.getFamily()).isEqualTo("Krilov"),
				() -> assertThat(actualHuman.getAge()).isEqualTo(36),
				() -> assertThat(actualHuman.isGod()).isEqualTo(false),
				() -> assertThat(actualHuman.getChildrenQuantity()).isEqualTo(2)
		);
	}
}
