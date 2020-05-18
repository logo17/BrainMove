package com.loguito.brainmove.models.local

enum class MeasurementOption(val index: Int) {
    WEIGHT(0),
    BMI(1),
    BODY_FAT(2),
    FAT_FREE_BODY(3),
    SUBCUTANEOUS_FAT(4),
    VISCERAL_FAT(5),
    BODY_WATER(6),
    SKELETAL_MUSCLE(7),
    MUSCLE_MASS(8),
    BONE_MASS(9),
    PROTEIN(10),
    BMR(11),
    METABOLICAL_AGE(12);

    companion object {
        fun fromInt(value: Int) = values().first { it.index == value }
    }
}