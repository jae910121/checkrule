package com.guohualife.ebiz.bpm.base;

import java.io.FileNotFoundException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.Log4jConfigurer;

@ContextConfiguration(locations = "classpath:/config/applicationContext.xml")
public class BaseTester extends AbstractJUnit4SpringContextTests {

	public BaseTester() {

		try {
			Log4jConfigurer
					.initLogging("file:WebRoot/WEB-INF/log4j.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
