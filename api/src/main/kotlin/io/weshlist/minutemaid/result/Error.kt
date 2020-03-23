package io.weshlist.minutemaid.result

interface BaseError {
	val message: String
}

sealed class ChannelError(override val message: String) : BaseError {
	data class NotFound(val name: String) :
		ChannelError("No such Channel Found: $name")

	data class MalformedName(val name: String) :
		ChannelError("Malformed Channel name: $name")

	data class DatabaseError(val name: String, val throwable: Throwable) :
		ChannelError("Error from Database: ${throwable.message}")

	data class OtherDatabaseError(val name: String, val throwable: Throwable) :
		ChannelError("Error from Database: ${throwable.message}")

	// other cases that need different business-specific handling code
}


sealed class MusicError(override val message: String) : BaseError {
	data class NotFound(val name: String) :
		MusicError("No such Music Found: $name")

	data class MalformedName(val name: String) :
		MusicError("Malformed Music name: $name")

	data class DatabaseError(val name: String, val throwable: Throwable) :
		MusicError("Error from Database: ${throwable.message}")

	data class OtherDatabaseError(val name: String, val throwable: Throwable) :
		MusicError("Error from Database: ${throwable.message}")

	// other cases that need different business-specific handling code
}

sealed class UserError(override val message: String) : BaseError {
	data class NotFound(val name: String) :
		UserError("No such Channel Found: $name")

	data class MalformedName(val name: String) :
		UserError("Malformed Channel name: $name")

	data class DatabaseError(val name: String, val throwable: Throwable) :
		UserError("Error from Database: ${throwable.message}")

	data class OtherDatabaseError(val name: String, val throwable: Throwable) :
		UserError("Error from Database: ${throwable.message}")

	// other cases that need different business-specific handling code
}