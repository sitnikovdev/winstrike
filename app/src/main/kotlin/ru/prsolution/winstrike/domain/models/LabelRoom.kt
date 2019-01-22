package ru.prsolution.winstrike.domain.models

class LabelRoom(text: String?, dx: Int?, dy: Int?) {
	var text: String
	var dx: Int
	var dy: Int


	init {
		this.text = text.toString()
		this.dx = dx!!
		this.dy = dy!!
	}

}
