package components;
/**
 * this class takes creates an appliance instance,
 * one of the two switchable components
 *
 * @author Julio Cuello
 */
public class Appliance extends Switchable {
    private int rating;

    /**
     * this is the constructor of the appliance instance which uses the
     * Switchable class constructor which extends the component constructor
     * @param name: name of the component
     * @param source: parent of the component
     * @param rating: integer that represents the current that the
     *              appliance sends to its source
     */
    public Appliance(String name, Component source, int rating) {
        super(name,source);
        this.rating = rating;
    }

    /**
     * this metho gets the rating of the appliance
     * @return: how much current it sends
     */
    public int getRating(){
        return rating;
    }

    /**
     * this method turns an appliance on and sends its draw to its source
     */
    public void turnOn() {
        this.isOn = true;
        report(this, Msg.SWITCHING_ON);
        if (this.getSource().isEngaged) {
            getSource().changeDraw(this.rating);
        }
    }
    /**
     * this method turns an appliance on and sends its negative draw to its source
     * in the case that it's engaged
     */
    public void turnOff() {
        this.isOn = false;
        report(this, Msg.SWITCHING_OFF);
        if (this.getSource().isEngaged) {
            getSource().changeDraw(-this.rating);
        }
    }

    /**
     * this method disengages the appliance and sends the negative draw
     * to its source
     */
    public void disengage() {
        if (this.isEngaged) {
            toggleIsEngaged();
            report(this, Msg.DISENGAGING);
            this.changeDraw(-rating);
        }
    }
}
