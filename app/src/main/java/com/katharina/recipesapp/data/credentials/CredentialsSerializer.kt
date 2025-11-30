package com.katharina.recipesapp.data.credentials

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object CredentialsSerializer : Serializer<Credentials> {
    override val defaultValue: Credentials
        get() = Credentials()

    override suspend fun readFrom(input: InputStream): Credentials =
        try {
            Json.decodeFromString<Credentials>(
                deserializer = Credentials.serializer(),
                string = input.readBytes().decodeToString(),
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPrefs", serialization)
        }

    override suspend fun writeTo(
        t: Credentials,
        output: OutputStream,
    ) {
        output.write(
            Json.encodeToString(Credentials.serializer(), t).encodeToByteArray(),
        )
    }
}
