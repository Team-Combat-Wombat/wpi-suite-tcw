/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    twack
 *    mpdelladonna
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.core.models;

import com.google.gson.*;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
/**
 * The Data Model representation of a User. Implements
 * 	database interaction and serializing.
 * @author mdelladonna, twack, bgaffey
 */

public class User extends AbstractModel
{

	private String name;
	private String username;
	private int idNum;
	private Role role;
	private String sms;
	private String email;
	
	transient private String password; // excluded from serialization, still stored.
	
	/**
	 * The primary constructor for a User
	 * @param name	User's full name
	 * @param username	User's username (nickname)
	 * @param idNum	User's ID number
	 * @param aim User's AIM screen name
	 * @param email User's Email address
	 */
	public User(String name, String username, String password, String sms, String email, int idNum)
	{
		this.name = name;
		this.username = username;
		this.password = password;
		this.sms = sms;
		this.email = email;
		this.idNum = idNum;
		this.role = Role.USER;
	}
	
	/**
	 * The secondary constructor for a User. Allows users to be created using old constructor after
	 * adding aim and email support to User. 
	 * @param name	User's full name
	 * @param username	User's username (nickname)
	 * @param idNum	User's ID number
	 */
	public User(String name, String username, String password, int idNum)
	{
		this(name, username, password, "", "", idNum);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof User)
		{
			if( ((User)other).idNum == this.idNum)
			{
				//things that can be null
				if(this.name != null && !this.name.equals(((User)other).name))
				{
					return false;
				}
				
				if(this.username != null && !this.username.equals(((User)other).username))
				{
					return false;
				}
				
				if(this.password != null && !this.password.equals(((User)other).password))
				{
					return false;
				}
				
				// Check new aim field
				if(this.sms != null && !this.sms.equals(((User)other).sms))
				{
					return false;
				}
				
				// Check new email field
				if(this.email != null && !this.email.equals(((User)other).email))
				{
					return false;
				}
				
				if(this.role != null && !this.role.equals(((User)other).role))
				{
					return false;
				}
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Performs password checking logic. Fails if password field is null, which happens
	 * 	when User is deserialized so as to protect the password.
	 * @param pass	the password String to compare
	 * @return	True if the password matches, False otherwise.
	 */
	public boolean matchPassword(String pass)
	{
		return (this.password == null) ? false : password.equals(pass);
	}
	
	/**
	 * Sets password (please encrypt before using this method)
	 * @param pass
	 */
	public void setPassword(String pass)
	{
		this.password = pass;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	/**
	 *  Gets the users AIM
	 */
	public String getSMS()
	{
		return this.sms;
	}
	
	/**
	 *  Gets the users Email
	 */
	public String getEmail()
	{
		return this.email;
	}
	
	/* Accessors */
	public String getName()
	{
		return name;
	}
	
	public int getIdNum()
	{
		return idNum;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	
	/* database interaction */
	public void save()
	{
		return;
	}
	
	public void delete()
	{
		return;
	}
	
	/* Serializing */
	
	/**
	 * Serializes this User model into a JSON string.
	 * 
	 * @return	the JSON representation of this User
	 */
	public String toJSON()
	{
		String json;
		
		Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserSerializer()).create();
		
		json = gson.toJson(this, User.class);
		
		return json;	
	}
	
	/**
	 * Static method offering comma-delimited JSON
	 * 	serializing of User lists
	 * @param u	an array of Users
	 * @return	the serialized array of Users
	 */
	public static String toJSON(User[] u)
	{
		String json ="[";
		
		for(User a : u)
		{
			json += a.toJSON() + ", ";
		}
		
		json += "]";
				
		return json;
		
	}
	
	/* Built-in overrides/overloads */
	
	/**
	 * Override of toString() to return a JSON string for now.
	 * 	May override in the future.
	 */
	public String toString()
	{
		return this.toJSON();
	}

	@Override
	public Boolean identify(Object o)
	{
		Boolean b  = false;
		
		if(o instanceof User)
			if(((User) o).username.equalsIgnoreCase(this.username))
				b = true;
		
		if(o instanceof String)
			if(((String) o).equalsIgnoreCase(this.username))
				b = true;
		return b;
	}
	
	/**
	 * Determines if this is equal to another user
	 * @param anotherUser
	 * @return true if this and anotherUser are equal
	 */
	public boolean equals(User anotherUser){
		return this.name.equalsIgnoreCase(anotherUser.getName()) &&
				this.username.equalsIgnoreCase(anotherUser.getUsername()) &&
				this.idNum == anotherUser.getIdNum();
	}
	
	public User setName(String newName){
		this.name = newName;
		return this;
	}
	
	public User setUserName(String newUserName){
		this.username = newUserName;
		return this;
	}
	
	public User setIdNum(int newidNum){
		this.idNum = newidNum;
		return this;
	}

	
	
	public Role getRole()
	{
		return this.role;
	}
	
	public void setRole(Role r)
	{
		this.role = r;
	}

	
	public static User[] fromJSONArray(String body) {
		final Gson parser = new Gson();
		return parser.fromJson(body, User[].class);
	}
	
	public static User fromJSON(String json) {
		// build the custom serializer/deserializer
		Gson gson;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(User.class, new UserDeserializer());

		gson = builder.create();
		
		return gson.fromJson(json, User.class);
	}

	@Override
	public Project getProject() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setProject(Project aProject){
		//Users are not currently Associated with projects directly 
	}



	
}
