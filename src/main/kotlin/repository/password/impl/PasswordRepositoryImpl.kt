package repository.password.impl

import core.models.CredentialSort
import core.models.Result
import core.models.criteria.CredentialSearchCriteria
import core.models.dto.PasswordDto
import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import repository.common.errors.DatabaseError
import repository.password.Password
import repository.password.PasswordRepository
import repository.password.PasswordsTable
import repository.password.projection.PasswordSummary
import java.time.LocalDateTime
import java.util.*

class PasswordRepositoryImpl(
    private val db: Database,
    private val logger: Logger
) : PasswordRepository {

    override suspend fun findById(id: UUID): Result<Password> {
        return try {
            return transaction(db) {
                Password.findById(id)
            }?.let { Result.Success(it) } ?: Result.Error("Password not found")
        } catch (e: Exception) {
            logger.error(e.message, e)
            Result.Error(DatabaseError.fromException(e).extractMessage())
        }
    }

    override suspend fun findSummaries(searchCriteria: CredentialSearchCriteria): Result<List<PasswordSummary>> {
        delay(550)
        return try {
            return transaction(db) {

                val query = PasswordsTable.select(
                    listOf(
                        PasswordsTable.id,
                        PasswordsTable.name,
                        PasswordsTable.username,
                        PasswordsTable.email,
                        PasswordsTable.favorite
                    )
                )

                searchCriteria.userId?.let {
                    query.andWhere { PasswordsTable.user eq it }
                }

                query.orderBy(toSort(searchCriteria.sort), toOrder(searchCriteria.sort)).map { resultRow ->
                    PasswordSummary(
                        id = resultRow[PasswordsTable.id].value,
                        name = resultRow[PasswordsTable.name].replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        },
                        username = resultRow[PasswordsTable.username],
                        email = resultRow[PasswordsTable.email],
                        favorite = resultRow[PasswordsTable.favorite]
                    )
                }
            }.let { Result.Success(it) }
        } catch (e: Exception) {
            logger.error(e.message, e)
            Result.Error(DatabaseError.fromException(e).extractMessage())
        }
    }

    override suspend fun save(password: PasswordDto): Result<Boolean> {
        return try {
            return transaction(db) {
                Password.new {
                    this.user = password.user
                    this.username = password.userName?.lowercase()
                    this.email = password.email?.lowercase()
                    this.password = password.password
                    this.name = password.name.lowercase()
                    this.website = password.website
                    this.websiteIcon = password.icon
                    this.createdBy = password.user.userName
                    this.lastUpdatedBy = password.user.userName
                }
            }.let {
                Result.Success(true)
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
            Result.Error(DatabaseError.fromException(e).extractMessage())
        }
    }

    override suspend fun update(id: UUID, user: String, password: PasswordDto): Result<Boolean> {
        return try {
            return transaction(db) {
                Password.findById(id)?.let {
                    it.username = password.userName?.lowercase()
                    it.email = password.email
                    it.password = password.password
                    it.name = password.name.lowercase()
                    it.website = password.website
                    it.websiteIcon = password.icon
                    it.lastUpdatedBy = user
                    it.lastUpdateDateTime = LocalDateTime.now()
                    it.version += 1
                }
            }.let {
                Result.Success(true)
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
            Result.Error(DatabaseError.fromException(e).extractMessage())
        }
    }

    private fun toSort(sort: CredentialSort): Expression<*> {
        return when (sort) {
            CredentialSort.NAME -> PasswordsTable.name
            CredentialSort.CREATED -> PasswordsTable.createdBy
            CredentialSort.FAVORITE -> PasswordsTable.favorite
        }
    }

    private fun toOrder(sort: CredentialSort): SortOrder {
        return when (sort) {
            CredentialSort.NAME -> SortOrder.ASC
            CredentialSort.CREATED -> SortOrder.ASC
            CredentialSort.FAVORITE -> SortOrder.DESC
        }
    }

}