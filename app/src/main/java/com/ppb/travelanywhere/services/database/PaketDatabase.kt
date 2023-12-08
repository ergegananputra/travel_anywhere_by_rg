package com.ppb.travelanywhere.services.database

import com.ppb.travelanywhere.services.model.PaketModel

object PaketDatabase {

    val paketList = listOf<PaketModel>(
        PaketModel(
            id = 1,
            name = "Paket 1",
            description = "Pemandangan Panorama",
            price = 15000,
        ),
        PaketModel(
            id = 2,
            name = "Paket 2",
            description = "Minuman Signature",
            price = 200000,
        ),
        PaketModel(
            id = 3,
            name = "Paket 3",
            description = "Makanan Berat",
            price = 50000,
        ),
        PaketModel(
            id = 4,
            name = "Paket 4",
            description = "Akses Hiburan Film",
            price = 40000,
        ),
        PaketModel(
            id = 5,
            name = "Paket 5",
            description = "Makanan Ringan",
            price = 25000,
        ),
        PaketModel(
            id = 6,
            name = "Paket 6",
            description = "Jaminan Duduk Dekat jendela",
            price = 15000,
        ),
        PaketModel(
            id = 7,
            name = "Paket 7",
            description = "Berkebutuhan Khusus",
            price = 0,
        ),
    )

}