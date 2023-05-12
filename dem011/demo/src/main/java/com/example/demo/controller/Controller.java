package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Response;
import com.example.demo.service.XpathService;

@RestController
public class Controller {

	@Autowired
	XpathService xpathService;

	@PostMapping("/validate")
	@CrossOrigin(origins = "http://localhost:4200")
	public void validData(@RequestBody Response response) {
		xpathService.execute(response);
	}

	@GetMapping("/test")
	public String testing() {
		return "Tested";
	}

}
