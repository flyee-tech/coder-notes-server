package com.peiel.notes;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan //配置filter
@MapperScan({"com.peiel.notes.dao", "com.peiel.notes.automation.mapper"})
public class CoderNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoderNotesApplication.class, args);
	}

}
