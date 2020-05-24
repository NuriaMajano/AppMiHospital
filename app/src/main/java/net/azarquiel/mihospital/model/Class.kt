package net.azarquiel.mihospital.model

import java.io.Serializable


data class Paciente(var nombre:String="", var numsegsocial:String="", var codhab:String="", var estadopaciente:String="", var descestadopaciente:String="", var iddoctor:String="", var idenfermera:String="", var idpaciente:String="") :Serializable
data class Sanitario(var nombre:String="", var numcolegiado:String="", var rol:String="", var especialidad:String="", var email:String=""): Serializable
data class MaterialSanitario(var nombre:String="", var cantidad:Long=0)
data class Habitacion(var nombrehabitacion:String="", var limpia:Boolean, var ocupada:Boolean, var idhabitacion:String=""):Serializable
data class Planta(var nombre:String="", var codplanta:String=""): Serializable
data class Agenda(var nombre:String="", var hora:String="", var descripcion:String="")
data class Citas(var respuestas:ArrayList<String>, var id:String=""):Serializable
data class Limpieza(var nombre:String="")

