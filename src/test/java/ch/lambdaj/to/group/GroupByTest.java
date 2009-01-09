// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.to.group;

import static ch.lambdaj.group.Groups.*;
import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.Strings.*;
import static ch.lambdaj.mock.ExposureBy.*;
import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.jmock.*;
import org.junit.*;

import ch.lambdaj.mock.*;

/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
public class GroupByTest {
	private Mockery context = new Mockery();

	@Before
	public void setUp() {
		FexFrance = new Exposure() {
			{
				setCountryName("France");
				setInsuredName("Fex France");
				setCountryIso("FR");
				setCountryFlag("/flags/fr.jpg");
			}
		};

		FexCanada = new Exposure() {
			{
				setCountryName("Canada");
				setInsuredName("Fex Canada");
				setCountryIso("CA");
				setCountryFlag("/flags/ca.jpg");
			}
		};

		exposures = asList(FexFrance, FexCanada);
	}

	private Exposure FexFrance, FexCanada;

	private List<Exposure> exposures;

	@Test
	public void testGroupByInsuredName() {
		String string = json(group(exposures, by(Exposure).insuredName()));

		string = removeApos(string);
		assertThat(string, containsString("insuredName:Fex France"));
		assertThat(string, containsString("insuredName:Fex Canada"));
	}

	@Test
	public void testGroupByInsuredNameAndCountryName() {
		String string = json(group(exposures, by(Exposure).insuredName().countryName()));

		string = removeApos(string);
		assertThat(string, containsString("insuredName:Fex France,children:"));
		assertThat(string, containsString("countryName:France,children:"));
		assertThat(string, containsString("insuredName:Fex Canada,children:"));
		assertThat(string, containsString("countryName:Canada,children:"));
	}

	@Test
	public void testGroupByInsuredNameAsExposures() {
		String string = json(group(exposures, by(Exposure).insuredName().asExposures()));
	}

	@Test
	public void testGroupByCountryName() {
		String string = json(group(exposures, by(Exposure).countryName().asInsureds().headCountryIso()));

		string = removeApos(string);
		assertThat(string, allOf(containsString("insureds:[{"), containsString("countryName:France"), containsString("insuredName:Fex France"), containsString("countryFlag:/flags/fr.jpg"),
				containsString("countryIso:FR")));
		assertThat(string, allOf(containsString("insureds:[{"), containsString("countryName:Canada"), containsString("insuredName:Fex Canada"), containsString("countryFlag:/flags/fr.jpg"),
				containsString("countryIso:FR")));

		string = removeString(string, removeApos(json(FexFrance)));
		string = removeString(string, removeApos(json(FexCanada)));
		assertThat(string, containsString("countryIso:FR"));
		assertThat(string, containsString("countryIso:CA"));
	}

	@Test
	public void testGroupByCountryNameAndInsuredName() {
		String string = json(group(exposures, by(Exposure).countryName().insuredName()));
	}

	@Test
	public void testGroupByCountryNameAsCountries() {
		String string = json(group(exposures, by(Exposure).countryName().asCountries()));
		string = removeApos(string);
	}

	@Test
	public void testGroupByCountryNameAsExposures() {
		String string = json(group(exposures, by(Exposure).countryName().asExposures()));
		string = removeApos(string);
	}
}