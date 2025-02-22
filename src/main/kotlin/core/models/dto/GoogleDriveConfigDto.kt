package core.models.dto

import repository.user.User
import java.time.LocalDateTime

data class GoogleDriveConfigDto(
    val user: User,
    val configFile: ByteArray,
    val storedCredential: ByteArray? = null,
    val folderId: String? = null,
    val lastBackupId: String? = null,
    val lastBackupDate: LocalDateTime? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GoogleDriveConfigDto

        if (user != other.user) return false
        if (!configFile.contentEquals(other.configFile)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + configFile.contentHashCode()
        return result
    }
}
