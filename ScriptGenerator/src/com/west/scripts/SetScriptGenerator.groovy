package com.west.scripts

import groovy.json.JsonSlurper

class SetScriptGenerator {
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
				//set contains the set name.
				def name = data[set].name.replace('\'', '\'\'')
				def mciCode = data[set].magicCardsInfoCode
				def releaseDate = data[set].releaseDate
				def nameValid = false
				def mciCodeValid = false
				
				if(!name || name.isAllWhitespace()){
					println "Name is invalid: " + set
				}
				else{
					nameValid = true
				}
				
				if(!mciCode || mciCode.isAllWhitespace()){
					println "MCI Code is invalid: " + set
				}
				else{
					mciCodeValid = true
				}
				
				if(nameValid && mciCodeValid){
					def insertStatement = sprintf("INSERT INTO sets(name, mciCode, releaseDate) VALUES('%s','%s','%s');",
						[name, mciCode, releaseDate])
					writer.append(insertStatement)
					writer.newLine()
					//outputFile << insertStatement + ";\r\n"
				}
			}
		}
	}
}
