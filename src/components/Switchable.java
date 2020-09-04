package components;
/**
 * This class has the behavior and state specific to the components
 * that can be switched on and off in a single class to avoid duplicate code
 * had different turn on and off due to different implementations
 * between the appliance and circuitbreaker class
 *
 * @author Julio Cuello
 */
public abstract class Switchable extends Component {
    protected boolean isOn;
    public Switchable(String name, Component source) {
        super(name,source);
    }

    /**
     * returns if the switch is on or off
     * @return
     */
    public boolean getIson(){
        return this.isOn;
    }

    /**
     * abstract method utilized in the appliance and the circuitbreaker classes
     * that turn the switchable object on
     */
    public abstract void turnOn();

    /**
     * abstract method utilized in the appliance and the circuitbreaker classes
     * that turns the switchable object off
     */
    public abstract void turnOff();

    public boolean isSwitchOn(){
        return isOn;
    }

}
