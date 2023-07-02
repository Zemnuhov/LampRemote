package com.novsu.core_ble_communication_api.model

enum class LedMode {
    FILL, PULSE, RAINBOW, FIRE, GRADIENT, RELAX;

    fun toName(): String{
        return when(this){
            FILL -> "Заполнение"
            PULSE -> "Пульсация"
            RAINBOW -> "Радуга"
            FIRE -> "Огонь"
            GRADIENT -> "Градиент"
            RELAX -> "Релаксация"
        }

    }
}