package com.reginaldateya.tiba.Model

class User {

    private var fullName: String = ""
    private var idNumber: String = ""
    private var registryNumber: String = ""
    private var clinicName: String = ""
    private var interviewDate: String = ""
    private var gender: String = ""
    private var phoneNumber: String = ""
    private var district: String = ""
    private var originationDate: String = ""
    private var lastRevisionDate: String = ""
    private var uid: String = ""


    constructor()

    constructor(fullName: String, idNumber: String, registryNumber: String, clinicName: String, interviewDate: String, gender: String, phoneNumber: String, district: String, originationDate: String, lastRevisionDate: String, uid: String)
    {
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
        this.uid = uid

    }

    fun getFullName(): String {
        return fullName
    }

    fun setFullName(fullName: String){
        this.fullName = fullName
    }

    fun getIdNumber(): String {
        return idNumber
    }

    fun setIdNumber(idNumber: String){
        this.idNumber = idNumber
    }
    fun getRegistryNumber(): String {
        return registryNumber
    }

    fun setRegistryNumber(registryNumber: String){
        this.registryNumber = registryNumber
    }
    fun getClinicName(): String {
        return clinicName
    }

    fun setClinicName(clinicName: String){
        this.clinicName = clinicName
    }
    fun getInterviewDate(): String {
        return interviewDate
    }

    fun setInterviewDate(interviewDate: String){
        this.interviewDate = interviewDate
    }
    fun getGender(): String {
        return gender
    }

    fun setGender(gender: String){
        this.gender = gender
    }
    fun getPhoneNumber(): String {
        return phoneNumber
    }

    fun setPhoneNumber(phoneNumber: String){
        this.phoneNumber = phoneNumber
    }
    fun getDistrict(): String {
        return district
    }

    fun setDistrict(district: String){
        this.district = district
    }
    fun getOriginationDate(): String {
        return originationDate
    }

    fun setOriginationDate(originationDate: String){
        this.originationDate = originationDate
    }
    fun getLastRevisionDate(): String {
        return lastRevisionDate
    }

    fun setLastRevisionDate(lastRevisionDate: String){
        this.lastRevisionDate = lastRevisionDate
    }

    fun getUid(): String {
        return uid
    }

    fun setUid(uid: String){
        this.uid = uid
    }


}