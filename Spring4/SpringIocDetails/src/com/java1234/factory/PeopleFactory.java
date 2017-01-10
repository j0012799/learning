package com.java1234.factory;

import com.java1234.entity.People;

public class PeopleFactory {

	public People createPeople(){
		People p=new People();
		p.setId(5);
		p.setName("老七");
		p.setAge(77);
		return p;
	}
}
