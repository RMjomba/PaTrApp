package com.reginaldateya.tiba.Model

class Ticket {

    var UserUID: String? = null
    var fullName: String? = null
    var idNumber: String? = null
    var registryNumber: String? = null
    var clinicName: String? = null
    var interviewDate: String? = null
    var gender: String? = null
    var phoneNumber: String? = null
    var district: String? = null
    var originationDate: String? = null
    var lastRevisionDate: String? = null

    constructor(UserUID: String?, fullName: String?, idNumber: String?, registryNumber: String, clinicName: String, interviewDate: String, gender: String, phoneNumber: String, district: String, originationDate: String, lastRevisionDate: String)
    {
        this.UserUID = UserUID
        this.fullName = fullName
        this.idNumber = idNumber
        this.registryNumber = registryNumber
        this.clinicName = clinicName
        this.interviewDate = interviewDate
        this.gender = gender
        this.phoneNumber = phoneNumber
        this.district = district
        this.originationDate = originationDate
        this.lastRevisionDate = lastRevisionDate


    }
}