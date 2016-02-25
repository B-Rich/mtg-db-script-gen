package com.west.scripts.model

class CardSet {
	String name
	String mciCode
	String releaseDate
	List<Card> cards
	
	private final String INSERT_TEMPLATE = "INSERT INTO sets(name, mciCode, releaseDate) VALUES('%s','%s','%s');"
	
	CardSet(setData){
		this.name = setData.name
		this.mciCode = setData.magicCardsInfoCode
		this.releaseDate = setData.releaseDate
		this.cards = []
	}
	
	def getInsertStatement(){
		if(this.mciCode && this.cards.size() > 0){
			return sprintf(INSERT_TEMPLATE, [
				escapeQuotes(name), 
				mciCode, 
				releaseDate
			])
		}
		else{
			return null
		}
	}
	
	def escapeQuotes(field){
		return field.replace('\'', '\'\'')
	}
}
