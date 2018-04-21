package com.github.octoacademy.sedona.repositories

import com.github.octoacademy.sedona.models.Lodging
import com.github.octoacademy.sedona.schemas.Lodgings
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository

@Repository
object LodgingRepository {

    fun list(): List<Lodging> =
            Lodgings.selectAll()
                    .orderBy(Lodgings.name)
                    .map { fromRow(it) }

    fun create(lodging: Lodging): Lodging {
        Lodgings.insert(toRow(lodging))
        return lodging
    }

    fun read(id: Int): Lodging? =
            Lodgings.select { Lodgings.id eq id }
                    .map { fromRow(it) }
                    .firstOrNull()

    fun update(id: Int, lodging: Lodging): Lodging {
        Lodgings.update({ Lodgings.id eq id }, body = toRow(lodging))
        return lodging
    }

    fun remove(id: Int) {
        Lodgings.deleteWhere { Lodgings.id eq id }
    }

    private fun toRow(lodging: Lodging): Lodgings.(UpdateBuilder<*>) -> Unit = {
        it[Lodgings.name] = lodging.name
        it[Lodgings.type] = lodging.type
        it[Lodgings.price] = lodging.price
        it[Lodgings.stars] = lodging.stars
        it[Lodgings.rating] = lodging.rating.toBigDecimal()
    }

    private fun fromRow(row: ResultRow): Lodging {
        return Lodging(
                row[Lodgings.name],
                row[Lodgings.type],
                row[Lodgings.price],
                row[Lodgings.stars],
                row[Lodgings.rating].toDouble()
        )
    }
}