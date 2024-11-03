package com.mayab.quality.doubles;

import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

class DependencyTest {
	
	private SubDependency subdependency;
	private Dependency dependency;
	public static final int TEST_NUMBER = 3;
	
	@BeforeEach
	void setup() {
		subdependency = mock(SubDependency.class);
		dependency = mock(Dependency.class);
	}
	
	@Test
	void getClassNameTest() {
		//Exercise
		String name = dependency.getSubDependencyClassName();
		//Assertion
		assertThat(null,is("SubDependency.class"));
		System.out.println(name);
	}
	
	@Test
	void addTwoTest() {
		int num = 10;
		int expected = 3;
		
		when(dependency.addTwo(num)).thenReturn(TEST_NUMBER);
		int result = dependency.addTwo(1);
		
		
		assertThat(expected, is(result));
	}
	
	@Test
	public void testAnswer() {
	    when(dependency.addTwo(anyInt())).thenAnswer(new Answer<Integer>() {
	        public Integer answer(InvocationOnMock invocation) throws Throwable {
	            int arg = (Integer) invocation.getArguments()[0];
	            return arg + 20;
	        }
	    });

	    assertEquals(30, dependency.addTwo(10));
	}


}
