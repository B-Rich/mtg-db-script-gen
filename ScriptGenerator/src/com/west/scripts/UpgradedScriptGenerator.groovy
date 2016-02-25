package com.west.scripts

import groovy.json.JsonSlurper

import com.west.scripts.model.Card
import com.west.scripts.model.CardSet

class UpgradedScriptGenerator {

	def sets = []
	
	UpgradedScriptGenerator(input){
		def inputFile = new File(input)
		def jsonSlurper = new JsonSlurper()
		def data = jsonSlurper.parseText(inputFile.getText())
		
		
		data.keySet().each { setCode ->
			def setData = data[setCode]
			def set = new CardSet(setData)
			
			//Build the cards list.
			setData.cards.each { cardData ->
				set.cards.add(new Card(cardData, set.mciCode))
			}
			
			sets.add(set)
		}
	}
	
	def generate(outputSets, outputCards){
		def setStatements = []
		def cardStatements = []
		//def cardStatementSingle = "INSERT INTO cards(id, mciNumber, imageName, setCode, name, text, flavorText, color, convertedManaCost, manaCost, power, toughness, typeText, types, subtypes, rarity) VALUES "
		
		sets.each { set -> 
			def includeSet = false
			set.cards.each { card -> 
				if(card.mciNumber){
					//There's at least one valid card, so include the set. 
					includeSet = true
					cardStatements.add(card.getInsertStatement())
					//cardStatementSingle += sprintf("(%s),", card.buildValueString())
				} 
			}
			
			def setInsertStatement = set.getInsertStatement()
			if(includeSet && setInsertStatement){
				setStatements.add(setInsertStatement)
			}
		}
		//cardStatementSingle = cardStatementSingle.substring(0, cardStatementSingle.length() - 1) + ";"
		
		//At this point, the two statement lists should be populated. 
		def outputFileSets = new File(outputSets)
		outputFileSets.withWriter('UTF-8'){ writer ->
			setStatements.each { setStatement -> 
				writer.append(setStatement)
				writer.newLine()
			}
		}
		
		def outputFileCards = new File(outputCards)
		outputFileCards.withWriter('UTF-8'){ writer ->
			cardStatements.each { cardStatement -> 
				writer.append(cardStatement)
				writer.newLine()
			}
		}
		
		/*
		def outputFileCards = new File(outputCards)
		outputFileCards.withWriter('UTF-8'){ writer ->
			writer.append(cardStatementSingle)
		}*/
		
		println "Done generating. "
	}
}
