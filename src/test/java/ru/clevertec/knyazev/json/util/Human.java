package ru.clevertec.knyazev.json.util;

public class Human {
	private String name;
	private String family;
	private int age;
	private boolean isGod;
	private Integer childrenQuantity;

	public Human() {
	}

	public static class Builder {
		Human human;

		public Builder() {
			human = new Human();
		}

		public Builder setName(String name) {
			human.name = name;
			return this;
		}

		public Builder setFamily(String family) {
			human.family = family;
			return this;
		}

		public Builder setAge(int age) {
			human.age = age;
			return this;
		}

		public Builder setIsGod(boolean isGod) {
			human.isGod = isGod;
			return this;
		}

		public Builder setChildrenQuantity(Integer childrenQuantity) {
			human.childrenQuantity = childrenQuantity;
			return this;
		}

		public Human build() {
			return human;
		}

	}

}
