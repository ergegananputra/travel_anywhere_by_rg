package com.ppb.travelanywhere

class TripPerjalanan(
    stasiunKeberangkatan: String,
    stasiunTujuan: String,
    tanggalKeberangkatan: String,
    namaKereta: String,
    kelasKereta: String,
    hargaTiket: Int,
) {

    var stasiunKeberangkatan: String
        get() {
            return stasiunKeberangkatan
        }
        set(value) {
            stasiunKeberangkatan = value
        }

    var stasiunTujuan: String
        get() {
            return stasiunTujuan
        }
        set(value) {
            stasiunTujuan = value
        }

    var tanggalKeberangkatan: String
        get() {
            return tanggalKeberangkatan
        }
        set(value) {
            tanggalKeberangkatan = value
        }

    var namaKereta: String
        get() {
            return namaKereta
        }
        set(value) {
            namaKereta = value
        }

    var kelasKereta: String
        get() {
            return kelasKereta
        }
        set(value) {
            kelasKereta = value
        }

    var hargaTiket: Int
        get() {
            return hargaTiket
        }
        set(value) {
            hargaTiket = value
        }

    init {
        this.stasiunKeberangkatan = stasiunKeberangkatan
        this.stasiunTujuan = stasiunTujuan
        this.tanggalKeberangkatan = tanggalKeberangkatan
        this.namaKereta = namaKereta
        this.kelasKereta = kelasKereta
        this.hargaTiket = hargaTiket
    }

}