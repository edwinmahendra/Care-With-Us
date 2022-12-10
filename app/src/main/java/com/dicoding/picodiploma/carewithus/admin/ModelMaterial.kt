package com.dicoding.picodiploma.carewithus.admin

class ModelMaterial {
    var categoryId: String = ""
    var description: String =""
    var id: String =""
    var timestamp: Long = 0
    var title: String = ""
    var uid: String =""



    constructor()
    constructor(
        categoryId: String,
        description: String,
        id: String,
        timestamp: Long,
        title: String,
        uid: String,



        ) {
        this.uid = uid
        this.id = id
        this.title = title
        this.description = description
        this.categoryId = categoryId
        this.timestamp = timestamp
    }

}