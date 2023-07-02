package com.novsu.core_ble_communication

import java.util.UUID

object LampUUID {
    val paramsServiceUUID: UUID = UUID.fromString("4460e510-15be-11ee-be56-0242ac120002")
    val modeCharacteristicUUID: UUID = UUID.fromString("4460e511-15be-11ee-be56-0242ac120002")
    val tonicCharacteristicUUID: UUID = UUID.fromString("4460e512-15be-11ee-be56-0242ac120002")

    val colorServiceUUID: UUID = UUID.fromString("baa179a0-16a0-4529-90b1-d359a6e2ab65")
    val colorRCharacteristicUUID: UUID = UUID.fromString("baa179a1-16a0-4529-90b1-d359a6e2ab65")
    val colorGCharacteristicUUID: UUID = UUID.fromString("baa179a2-16a0-4529-90b1-d359a6e2ab65")
    val colorBCharacteristicUUID: UUID = UUID.fromString("baa179a3-16a0-4529-90b1-d359a6e2ab65")



}