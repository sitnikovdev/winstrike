package ru.prsolution.winstrike.domain.models

class Arena(
		val publicId: String? = null,
		val activeLayoutPid: String? = null,
		val cityPid: String? = null,
		val name: String? = null,
		val metro: String? = null,
		val roomLayoutPid: String? = null,
		val description: String? = null,
		val imageUrl: String? = null,
		val commonDescription: String? = null,
		val vipDescription: String? = null,
		val commonImageUrl: String? = null,
		val vipImageUrl: String? = null,
		val locale: String? = null
)


enum class ArenaType {
	TWOROOMS, COMMON, VIP
}

