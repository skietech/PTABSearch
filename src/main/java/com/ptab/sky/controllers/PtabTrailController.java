package com.ptab.sky.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptab.sky.dao.PtabTrailDAO;
@PropertySource("classpath:application.properties")
@RestController
public class PtabTrailController {

	@Autowired
	private PtabTrailDAO ptabTrailDAO;

	@RequestMapping(value = "/ptab-trail", method = GET)
	public void getPtabTrail() {
		
		ptabTrailDAO.getTrails();
	}
}
