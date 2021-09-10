package com.wavetogether.infrastructure.converter

import com.wavetogether.lib.util.toByteArray
import com.wavetogether.lib.util.toUUID
import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Jul - 2020
 */
@Converter
class ByteArrayUuidConverter : AttributeConverter<UUID, ByteArray> {
  override fun convertToDatabaseColumn(attribute: UUID?): ByteArray =
    attribute?.toByteArray() ?: throw IllegalArgumentException("UUID must not be null.")

  override fun convertToEntityAttribute(dbData: ByteArray?): UUID =
    dbData?.toUUID() ?: throw IllegalArgumentException("Cannot convert null to UUID.")
}
