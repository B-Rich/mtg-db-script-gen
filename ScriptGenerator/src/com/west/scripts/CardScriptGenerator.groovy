package com.west.scripts

import groovy.json.JsonSlurper

class CardScriptGenerator {
	def missingMCI = 0
	
	def generate(input, output){
		def inputFile = new File(input)
		def outputFile = new File(output)
		def jsonSlurper = new JsonSlurper()
		def data = jsonSlurper.parseText(inputFile.getText())
		
		if(outputFile.exists()){
			outputFile.delete()
		}
		
		outputFile.withWriter('UTF-8'){ writer -> 
			data.keySet().each { set ->
				data[set].cards.each { card ->
					def valuesString = buildValues(data[set].magicCardsInfoCode, card)
					
					if(valuesString && !valuesString.isAllWhitespace()){
						def insertString = "INSERT INTO cards(id, mciNumber, imageName, setCode, name, text, flavorText, color, convertedManaCost, manaCost, power, toughness, typeText, types, subtypes, rarity) VALUES" + valuesString + ";"
						writer.append(insertString)
						writer.newLine()
					}
				}
			}
		}
		
		println missingMCI + " cards were missing mciCodes. " 
	}
	
	def buildValues(set, card){
		def valuesString = ""; 
		
		def id = card.id
		def mciNumber = card.mciNumber
		def imageName = card.imageName
		def setCode = buildSetCodeSubquery(set)
		def name = card.name
		def text = card.text
		def flavorText = card.flavor
		def color = flattenList(card.colors)
		def convertedManaCost = card.cmc
		def manaCost = card.manaCost
		def pwr = card.power
		def toughness = card.toughness
		def typeText = card.type
		def types = flattenList(card.types)
		def subtypes = flattenList(card.subtypes)
		def rarity = card.rarity
		
		if(!id || id.isAllWhitespace()){
			throw new Exception("Card is missing an id in set: " + set)
		}
		
		if(!mciNumber || mciNumber.isAllWhitespace()){
			println "Card missing an mciNumber: " + id
			missingMCI++
			return null; 
		}
		else if(mciNumber.contains("/")){
			mciNumber = mciNumber.substring(mciNumber.lastIndexOf('/') + 1)
		}
		
		if(!name || name.isAllWhitespace()){
			throw new Exception("Card is missing a name: " + id)
		}
		
		valuesString = sprintf("'%s', '%s', '%s', %s, '%s', '%s', '%s', '%s', %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s'", 
			[escapeSQL(id), 
			escapeSQL(mciNumber), 
			escapeSQL(imageName), 
			setCode, 
			escapeSQL(name), 
			escapeSQL(text), 
			escapeSQL(flavorText), 
			escapeSQL(color), 
			convertedManaCost, 
			escapeSQL(manaCost), 
			escapeSQL(pwr), 
			escapeSQL(toughness), 
			escapeSQL(typeText), 
			escapeSQL(types), 
			escapeSQL(subtypes), 
			escapeSQL(rarity)]) 
		
		return sprintf("(%s)", valuesString)
	}
	
	def buildSetCodeSubquery(set){
		return "(SELECT id FROM sets WHERE mciCode='" + set + "')"
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
	
	def escapeSQL(String input){
		if(input)
			return input.replace('\'', '\'\'')
		else
			return input
	}
}
