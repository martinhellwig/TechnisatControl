/**
 * Copyright (c) 2023, Martin Hellwig
 */

package com.mhellwig.technisatcontrol

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.Dispatchers

class TechnisatControl(private val ipAddress: String) {

    suspend fun sendControlEnums(controlEnums: List<ControlEnum>) {
        val selectorManager = SelectorManager(Dispatchers.IO)
        val socket = aSocket(selectorManager)
            .udp()
            .bind(InetSocketAddress("0.0.0.0", 8090))
        val receiverAddress = InetSocketAddress(ipAddress, 8090)

        socket.use { autoCloseSocket ->
            controlEnums.forEach {
                var sendData = ("<rcuButtonRequest code=\"${it.technicalValue}\" state=\"pressed\" />").toByteArray()
                var sendPacket = Datagram(ByteReadPacket(sendData), receiverAddress)
                autoCloseSocket.send(sendPacket)

                sendData = "<rcuButtonRequest code=\"${it.technicalValue}\" state=\"released\" />".toByteArray()
                sendPacket = Datagram(ByteReadPacket(sendData), receiverAddress)
                autoCloseSocket.send(sendPacket)
            }
        }
    }

    suspend fun sendControlEnum(controlEnum: ControlEnum) {
        sendControlEnums(listOf(controlEnum))
    }
}