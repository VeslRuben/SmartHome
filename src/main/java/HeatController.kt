import ragulators.HeatRegulator
import java.time.LocalDateTime

fun controllHeat(){
    val hRegulator = HeatRegulator(20.0,2)
    hRegulator.setNightTemperature(15.0)

    val today = LocalDateTime.now()

    var nightStart = LocalDateTime.of(today.year, today.month,today.dayOfMonth,23,30)
    var nightEnd = LocalDateTime.of(today.year, today.month,today.dayOfMonth.plus(1),6,30)
    hRegulator.setNightStart(nightStart)
    hRegulator.setNightEnd(nightEnd)

    var turnOff = LocalDateTime.of(today.year, today.month,today.dayOfMonth.plus(1),7,40)
    var heated = LocalDateTime.of(today.year, today.month,today.dayOfMonth.plus(1),14,30)
    hRegulator.setTurnOffTime(turnOff)
    hRegulator.setFullyHeatedTime(heated)

    while (true) {
        hRegulator.run()

        if (LocalDateTime.now().isAfter(nightEnd)) {
            nightStart = nightStart.minusDays(1)
            nightEnd = nightEnd.minusDays(1)
            hRegulator.setNightStart(nightStart)
            hRegulator.setNightEnd(nightEnd)
        }
        if (LocalDateTime.now().isAfter(heated.plusMinutes(1))){
            heated = heated.plusDays(1)
            turnOff = turnOff.plusDays(1)
            hRegulator.setTurnOffTime(turnOff)
            hRegulator.setFullyHeatedTime(heated)
        }
        Thread.sleep(10000)
    }
}