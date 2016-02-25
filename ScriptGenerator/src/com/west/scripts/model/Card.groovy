package com.west.scripts.model

class Card {
	String id
	String name
	String text
	String flavor
	List<String> colors
	String typeText
	List<String> types
	List<String> subtypes
	String manaCost
	int convertedManaCost
	String power
	String toughness
	String rarity
	String imageName
	String mciNumber 
	
	String setCode
	
	private static final String INSERT_TEMPLATE = "INSERT INTO cards(id, mciNumber, imageName, setCode, name, text, flavorText, color, convertedManaCost, manaCost, power, toughness, typeText, types, subtypes, rarity) VALUES (%s);"
	private static final String VALUES_TEMPLATE = "%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s"
	
	Card(cardData, setCode){
		this.id = cardData.id
		this.name = cardData.name
		this.text = cardData.text
		this.flavor = cardData.flavor
		this.colors = cardData.colors
		this.typeText = cardData.type
		this.types = cardData.types
		this.subtypes = cardData.subtypes
		this.manaCost = cardData.manaCost
		this.convertedManaCost = cardData.cmc? cardData.cmc: 0
		this.power = cardData.power
		this.toughness = cardData.toughness
		this.rarity = cardData.rarity
		this.imageName = cardData.imageName
		this.mciNumber = cardData.mciNumber
		if(this.mciNumber && this.mciNumber.contains("/")){
			this.mciNumber = this.mciNumber.substring(this.mciNumber.lastIndexOf('/') + 1)
		}
		this.setCode = setCode
	} 
	
	def getInsertStatement(){
		if(mciNumber){
			def valuesString = buildValueString()
			
			return sprintf(INSERT_TEMPLATE, valuesString)
		}
		else{
			return null
		}
	}
	
	def buildValueString(){
		sprintf(VALUES_TEMPLATE,
			escapeField(id),
			escapeField(mciNumber),
			escapeField(imageName),
			buildSetCodeSubquery(),
			escapeField(name),
			escapeField(text),
			escapeField(flavor),
			escapeField(flattenList(colors)),
			convertedManaCost,
			escapeField(manaCost),
			escapeField(power),
			escapeField(toughness),
			escapeField(typeText),
			escapeField(flattenList(types)),
			escapeField(flattenList(subtypes)),
			escapeField(rarity)
		)
	}
	
	def buildSetCodeSubquery(){
		return "(SELECT id FROM sets WHERE mciCode='" + setCode + "')"
	}
	
	def escapeField(field){
		if(field)
			return "'" + field.replace('\'', '\'\'') + "'"
		else return 'NULL'
	}
	
	def flattenList(list){
		def flattenedList = ""
		list.each{ item ->
			flattenedList += item + "|"
		}
		
		if(flattenedList.length() > 1 && flattenedList.substring(flattenedList.length()-1).equals("|")){
			flattenedList = flattenedList.substring(0, flattenedList.length()-1)
		}
		return flattenedList
	}
}
