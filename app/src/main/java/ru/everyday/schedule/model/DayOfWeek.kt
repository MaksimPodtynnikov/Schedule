package ru.everyday.schedule.model

class DayOfWeek(
    var id:Int,
    var events: List<Event>,
    var date:java.sql.Date,
)