package com.java1234.entity;

public class People {

	private int id;
	private String name;
	private int age;
	private Dog dog;//byName方式自动装配和配置文件中保持一致	
	private Cat cat;

	public People() {
		super();
		System.out.println("默认的无参构造方法");
	}
	
	public People(Dog dog,Cat cat) {
		super();
		System.out.println("constructor:::带参的构造方法");
		this.dog = dog;
		this.cat = cat;
	}	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public Dog getDog() {
		return dog;
	}
	public void setDog(Dog dog) {
		this.dog = dog;
	}
	
	public Cat getCat() {
		return cat;
	}
	public void setCat(Cat cat) {
		this.cat = cat;
	}
	
	@Override
	public String toString() {
		return "People [id=" + id + ", name=" + name + ", age=" + age
				+ ", dog=" + dog.getName() + ", cat=" + cat.getName() + "]";
	}

}
