package components;
/**
 * this is a class that creates the circuitbreaker instance,
 * one of the two switchable components
 *
 * @author Julio Cuello
 */
public class CircuitBreaker extends Switchable {
    private int limit;
    private boolean tripped;

    /**
     * ciruitBreaker constructor
     * @param name: name of circuitBreaker
     * @param source: parent of circuitBreaker
     * @param limit: max current the circuitBreaker can handle
     */
    public CircuitBreaker(String name, Component source, int limit){
        super(name,source);
        this.limit= limit;
    }

    /**
     * method for circuitbreaker that turns it on and engages its loads
     */
    public void turnOn(){
    if(!(isSwitchOn())){
        this.isOn= true;
        report(this, Msg.SWITCHING_ON);
        engageLoads();
        }
    }
    /**
     * method for circuitbreaker that turns it off and disengages its loads
     */
    public void turnOff() {
        if (isSwitchOn()) {
            this.isOn = false;
            report(this, Msg.SWITCHING_OFF);
            disengageLoads();
        }
    }

    /**
     * this is the method that does the actions when the circuitbreaker blows
     * @param drawBeforeBlow: draw before circuitbreaker blows
     */
    public void turnOffBlows(int drawBeforeBlow) {
        if (isSwitchOn()) {
            this.isOn = false;
            report(this, Msg.SWITCHING_OFF);
            this.getSource().changeDraw(-drawBeforeBlow);
            disengageLoads();
        }
    }
    /**
     * this method gets the state variable limit
     * @return: limit of the circuitbreaker
     */
    public int getLimit() {
        return limit;
    }

    /**
     * overriden changedraw of the circuitBreaker that has the difference of
     * dealing with the
     * case where the circuitBreaker blows
     * @param delta: The number of amperes by which
     *             to raise(+) or lower(-) the draw
     */
    @Override
    public void changeDraw(int delta){

        int oldDraw= this.getDraw();
        this.draw+=delta;
        if(this.isOn) {
            if (this.draw > this.limit) {
                report(this, Msg.BLOWN, this.draw);
                this.tripped = true;
                this.turnOffBlows(oldDraw);
                this.draw = 0;
            }
        }
        if(0<this.draw && this.draw<=this.limit && this.isOn){
            report(this, Msg.DRAW_CHANGE, delta);
            this.getSource().changeDraw(delta);
        }
    }
}
