package ragulators



/**
 * A basic PID regulator
 * @param Kp Proportional amplifier. (1 is no amplification, 0 to disable)
 * @param Kd Derivetiv amplifier. (1 is no amplification, 0 to disable)
 * @param Ki Integral amplifier. (1 is no amplification, 0 to disable)
 * @param setpoint setpoint of the system.
 */
class PidRgulator(private var Kp: Double, private var Kd: Double, private var Ki: Double, private var setpoint: Double = 0.0) {

    private var prevError: Double = 0.0
    private var integralSum = 0.0
    private var lastRunTime = System.currentTimeMillis()

    init {
        require(Kd >= 0 && Kp >= 0 && Ki >= 0)
    }

    /**
     * Calculats the next output value baset on the input value.
     * @param messurdValue Used to calculate the next response.
     * @return Calulated vlaue (0 is no resopns).
     */
    fun run(messurdValue: Double): Double {
        val dt = System.currentTimeMillis() - lastRunTime
        val error = this.setpoint - messurdValue
        this.integralSum += error * dt
        val difError = (error - prevError) / dt

        return Kp*error + difError*Kd + Ki*integralSum
        TODO("add integral deth band")
    }

    /**
     * Sets a new setpoint.
     * @param setpoint New setpoint.
     */
    fun setSetpoint(setpoint: Double){
        this.setpoint = setpoint
    }

}

fun main(){
    val pid = PidRgulator(1.0,9.0,0.0,1.0)
    Thread.sleep(100)
    println(pid.run(1.0))
    Thread.sleep(100)
    println(pid.run(20.0))
}