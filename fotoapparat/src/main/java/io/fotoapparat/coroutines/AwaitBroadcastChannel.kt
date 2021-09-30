package io.fotoapparat.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel

/**
 * A [ConflatedBroadcastChannel] which exposes a [getValue] which will [await] for at least one value.
 */
internal class AwaitBroadcastChannel<T>(
        private val channel: ConflatedBroadcastChannel<T> = ConflatedBroadcastChannel(),
        private val deferred: CompletableDeferred<Boolean> = CompletableDeferred()
) : BroadcastChannel<T> by channel, Deferred<Boolean> by deferred {

    /**
     * The most recently sent element to this channel.
     */
    suspend fun getValue(): T {
        deferred.await()
        return channel.value
    }

    override fun offer(element: T): Boolean {
        deferred.complete(true)
        return channel.offer(element)
    }

    override suspend fun send(element: T) {
        deferred.complete(true)
        channel.send(element)
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    override fun cancel(cause: Throwable?): Boolean {
        cancelInternal(cause)
        return true
    }

    override fun cancel(cause: CancellationException?) {
        cancelInternal(cause)
    }

    private fun cancelInternal(cause: Throwable?) {
        val exception = cause?.toCancellationException()
        channel.cancel(exception)
        deferred.cancel(exception)
    }

    private fun Throwable.toCancellationException(message: String? = null): CancellationException =
            this as? CancellationException ?: CancellationException(message, this)
}
