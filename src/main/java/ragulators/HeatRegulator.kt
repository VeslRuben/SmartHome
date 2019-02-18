package ragulators

import comunication.I2cCom
import java.time.LocalDateTime

class HeatRegulator(private var wantedTemperature: Double, private var deadBand: Int = 0) {

    //TODO make flexible for different comunications mathods.

    private var temprature: Double
    private var heaterOn: Boolean
    private var startPreHeatTime: LocalDateTime
    private var turnOffTime: LocalDateTime

    private var timeTurnedOn: Long
    private var heatingTimingActive: Boolean
    private var timeToHeatUp: Int         // time in sec

    private var nightTemperature: Double
    private var nightTimeStart: LocalDateTime
    private var nightTimeEnd: LocalDateTime

    private val ioSlave: I2cCom //TODO implement difretn com posibilitys.

    init { //TODO move initilasation for better constructor setup.
        require(deadBand >= 0)

        this.temprature = this.wantedTemperature
        this.heaterOn = true
        this.startPreHeatTime = LocalDateTime.now().plusYears(1) // sets the time to before the current time.
        this.turnOffTime = LocalDateTime.now().plusYears(1) // sets the time to after the current time.

        this.timeTurnedOn = System.currentTimeMillis()
        this.heatingTimingActive = true
        this.timeToHeatUp = 10*60    //10 min  //TODO find a good start point for the value.

        this.nightTemperature = 10.0
        this.nightTimeStart = LocalDateTime.now().plusYears(1)
        this.nightTimeEnd = LocalDateTime.now().plusYears(-1)


        this.ioSlave = I2cCom(4)
    }

    fun run() {
        val temperature = (ioSlave.readByte()).toDouble()
        println("temp $temperature, sepoint ${this.temprature}, heter on/off $heaterOn")
        if (this.heaterOn) {
            if (temperature < (this.temprature - this.deadBand/2)) {  // Turn on
                //TODO only send the value onece.
                ioSlave.sendByte(0.toByte(),11)     // (pin, on/off, 200 is off)
            } else if (temperature > (this.temprature + this.deadBand/2)) {  // Turn Off
                ioSlave.sendByte(200.toByte(),11)     // (pin, on/off, 0 is on)

                if (this.heatingTimingActive){
                    this.heatingTimingActive = false
                    val time: Long = (System.currentTimeMillis() - this.timeTurnedOn)/1000  // time in sec
                    this.timeToHeatUp = ((this.timeToHeatUp + time) /2).toInt()
                }
            }
            if (LocalDateTime.now().isAfter(this.turnOffTime)){
                this.heaterOn = false
                this.turnOffTime = LocalDateTime.now().plusYears(1) // sets the time to after the current time.
            }

            // heater off
        } else {
            ioSlave.sendByte(200.toByte(),11)     // (pin, on/off, 200 is off)
            if (LocalDateTime.now().isAfter(this.startPreHeatTime)){
                this.heaterOn = true
                this.startPreHeatTime = LocalDateTime.now().plusYears(1)  // sets the time to before the current time.
            }
        }
        if (LocalDateTime.now().isAfter(this.nightTimeStart) && LocalDateTime.now().isBefore(this.nightTimeEnd)){
            this.temprature = this.nightTemperature
        } else {
            this.temprature = this.wantedTemperature
        }

    }

    fun turnOnHeater(){
        this.heaterOn = true
        this.heatingTimingActive = true
        this.timeTurnedOn = System.currentTimeMillis()
    }

    fun turnOffHeater(){
        this.heaterOn = false
    }

    fun setTurnOffTime(turnOffTime: LocalDateTime){
        this.turnOffTime = turnOffTime
    }

    fun setFullyHeatedTime(time: LocalDateTime){
        this.startPreHeatTime = time.plusSeconds(-this.timeToHeatUp.toLong())

    }

    fun setDeadBand(deadBand: Int){
        this.deadBand = deadBand
    }

    fun setWantedTemperature(wantedTemperature: Double){
        this.wantedTemperature = wantedTemperature
    }

    fun setNightTemperature(temperature: Double){
        this.nightTemperature = temperature
    }

    fun setNightStart(time: LocalDateTime) {
        this.nightTimeStart = time
    }

    fun setNightEnd(time: LocalDateTime) {
        this.nightTimeEnd = time
    }

}