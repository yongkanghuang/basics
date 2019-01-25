package com.basics;

import com.basics.es.entity.EsUser;
import com.basics.es.service.EsUserSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicsApplicationTests {

	@Autowired
	EsUserSearchService esUserSearchService;

	@Test
	public void contextLoads() {
		esUserSearchService.createIndex("user_20190124","content", EsUser.class);
	}

}
