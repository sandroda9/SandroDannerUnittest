package com.mayab.quality.loginunittest.service;

//Import mockito
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.*;

//Import hamcrest
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertFalse;






import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.loginunittest.dao.IDAOUser;

class UserServiceTest {
	
	private UserService service;
	private IDAOUser dao;
	private HashMap<Integer , User> db;
	

	@BeforeEach
	void setUp() throws Exception {
		
		dao = mock(IDAOUser.class);
		service = new UserService(dao);
		db = new HashMap<Integer, User>();
		
	}

	@Test
	public void whenPasswordShort_test() {
		//initialize
		String shortPass ="123";
		String name = "user1";
		String email = "user1@email.com";
		User user = null;
		
		
		//Fake code for findUserByEmail & save models
		when(dao.findUserByEmail(anyString())).thenReturn(user);
		when(dao.save(any(User.class))).thenReturn(null);
		
		//Exercise
		user = service.createUser(name, email, shortPass);
		
		//verify
		assertThat(user,is(nullValue()));
	}
	
	void whenAllDataCorrect_saveUserTest() {
		
		//Initialition
		int sizeBefore =db.size();
		
		//Fake code for
		when(dao.findUserByEmail(anyString())).thenReturn(null);
		when(dao.save(any(User.class))).thenAnswer(new Answer<Integer>() {
		    // Method within the class
		    public Integer answer(InvocationOnMock invocation) throws Throwable {
		        // Set behavior in every invocation
		        User arg = (User) invocation.getArguments()[0];
		        db.put(1, arg);
		        System.out.println("Size despues=" + db.size() + "\n");
		        // Return the invoked value
		        return db.size()+1;
		    }
		});
		
		//Exercide
		User user = service.createUser("hola", "hola@email.com", "pass" );
		
		//verify
		assertThat(db.size(), is(sizeBefore+1));

	}
	
	

}
