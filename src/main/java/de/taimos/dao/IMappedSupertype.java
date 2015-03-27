package de.taimos.dao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Copyright 2015 Hoegernet<br>
 * <br>
 * denotes that the class is used as a superclass meant to be sub-classes. It advises Jongo to include type information into the JSON string
 * into the field <code>@class</code> to allow deserialization into the correct Java class
 * 
 * @author Thorsten Hoeger
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public interface IMappedSupertype {
	//
}
