package ru.prsolution.winstrike.domain.models

import ru.prsolution.winstrike.datasource.model.EndEntity
import ru.prsolution.winstrike.datasource.model.StartEntity

class Wall(
		val start: StartEntity? = null,
		val end: EndEntity? = null
)
