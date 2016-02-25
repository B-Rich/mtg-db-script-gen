package com.west.scripts; 

class Driver {

	static main(args) {
		def input = "C:\\Users\\Ian Westfall\\Desktop\\springMTG\\cardData\\AllSets.json"
		def setScriptOutput = "C:\\Users\\Ian Westfall\\Desktop\\springMTG\\dbScripts\\upgraded\\setTable.sql"
		def cardScriptOutput = "C:\\Users\\Ian Westfall\\Desktop\\springMTG\\dbScripts\\upgraded\\cardTable.sql"
		/*
		def ssg = new SetScriptGenerator()
		ssg.generate(input, setScriptOutput)
		*/
		
		/*
		def csg = new CardScriptGenerator()
		csg.generate(input, cardScriptOutput)
		*/
		
		def upgradedScriptGenerator = new UpgradedScriptGenerator(input)
		upgradedScriptGenerator.generate(setScriptOutput, cardScriptOutput); 
	}
}