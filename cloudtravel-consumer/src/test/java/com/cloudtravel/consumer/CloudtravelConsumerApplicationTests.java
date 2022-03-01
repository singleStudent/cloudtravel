package com.cloudtravel.consumer;


import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CloudtravelConsumerApplicationTests {

	@Test
	public void contextLoads() {
		String a = "123";
		a.lines().forEach(System.out::println);

		String b = a.repeat(2);
		System.out.println(a + "<===> " + b);
		String c= "  456  ";
		System.out.println(c.trim());
		System.out.println(c.stripLeading());;
		System.out.println(c.stripTrailing());;
	}

}
