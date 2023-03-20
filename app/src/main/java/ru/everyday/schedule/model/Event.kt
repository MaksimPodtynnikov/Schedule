package ru.everyday.schedule.model

import java.sql.Time

class Event(
    var id:Int,
    var idDay:Int,
    var timeStart:Time,
    var timeStop:Time,
    var title: String,
    var place: String,
    var description: String
    ):java.io.Serializable