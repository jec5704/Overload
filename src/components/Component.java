package components;

import java.util.ArrayList;

/**
 * This class takes care of most of the behavior of all the components
 * each will have a list of children and several other variables
 * used in the code to complete certain actions
 *
 * @author Julio Cuello
 */
public abstract class Component extends Reporter {
    private String name;
    protected boolean isEngaged;
    protected int draw;
    private Component source;
    private ArrayList<Component> children;

    /**
     * Create a new Component.
     * Attach it to its source. (See attach(Component)
     * If there is a source and it is engaged, this component will immediately have power.
     * @param name: the name of this component(for tracing, debugging)
     * @param source: the source component providing this one with power(null if this component generates its own
     *              power and therefore does not have a source aka powersource)
     */
    protected Component(String name, Component source) {
        this.name = name;
        this.source= source;
        children= new ArrayList<>();
        report(this, Msg.CREATING);
        if(this.source!= null){
            this.source.attach(this);
        }
    }

    /**
     * Get component's name
     * @return: the name assigned in the constructor
     */
    public String getName() {
        return name;
    }

    /**Precondition: load has already been created, and therefore knows this Component is its source
     *
     * Add a new load (something that draws current) to this Component.
     * If this component is engaged, the new load becomes engaged.
     * @param load: the component to be "plugged in"
     */
    protected void attach(Component load){
        report(this, load, Msg.ATTACHING);
        this.addLoad(load);
        if(isEngaged){
            engageLoads();
        }
    }

    /**
     * Change the amount of current passing through this Component.
     * This method is called by one of this Component's loads when something changes.
     * If the result is a change in current draw in this component, that information is relayed up to the
     * source Component via a call to its changeDraw method.
     * @param delta: The number of amperes by which to raise(+) or lower(-) the draw
     */
    protected void changeDraw(int delta){
        this.draw+=delta;
        report(this, Msg.DRAW_CHANGE, delta);
        if(this.source!= null){
            this.source.changeDraw(delta);
        }
    }

    /**
     * this method sets the drwa of a certain component to whatever the draw is
     * @param draw: new draw to make the component have
     */
    protected int setDraw(int draw){
        return draw;
    }

    /**
     * this returns the boolean value saying if the component is engaged or not
     * @return: true or false
     */
    public boolean getIsEngaged() {
        return this.isEngaged;
    }

    /**
     * this method rteurns the source of a component
     * @return
     */
    protected Component getSource(){
        return this.source;
    }

    /**
     * this method returns the full list of the children that the component has
     * @return
     */
    protected ArrayList<Component> getLoads(){
        return this.children;
    }

    /**
     * Add a new load to this component. This is a simple internal method.
     * attach(Component) calls this to do the connection.
     * @param newLoad: the new component to be added
     */
    protected void addLoad(Component newLoad){
        this.children.add(newLoad);
    }

    /**
     * Inform all Components to which this Component acts as a source that they may now draw current.
     * In other words, engage all of this component's loads. This is a simple internal method.
     */
    protected void engageLoads(){

        if (this instanceof CircuitBreaker && ((Switchable) this).isSwitchOn()){
            for (Component child : children){
                child.engage();
            }
        }
        else if(this instanceof CircuitBreaker &&(!((Switchable) this).isSwitchOn())){
            return;
        }
        else{
            for (Component child : children){
                child.engage();
            }
        }
    }

    /**
     * if isEngaged is true it makes it false and if its false it makes it true
     */
    public void toggleIsEngaged() {
        this.isEngaged=!this.isEngaged;
    }

    /**
     * this method returns the integer of the draw of a component
     * @return: draw of component
     */
    public int getDraw() {
        return this.draw;
    }

    /**Precondition: !this.isEngaged()
     *
     * The source for this component is now being powered.
     * For those Components that have sources, the source Component is the one that calls this method.
     * If this is not a switchable Component that has been switched off, this Component then passes on the information
     * to its loads that they are now engaged. The expected followup is that each of the loads will inform
     * this Component of how much current they need to draw.
     */
    public void engage(){
        if (!this.isEngaged) {
            this.toggleIsEngaged();
            report(this, Msg.ENGAGING);
            engageLoads();
            if(this instanceof Appliance){
                if(this.isEngaged) {
                    if (((Appliance) this).isOn) {
                        report(this, Msg.DRAW_CHANGE, ((Appliance) this).getRating());
                        this.source.changeDraw(((Appliance) this).getRating());
                    }
                }

            }
        }
    }

    /**Precondition: this.isEngaged()
     *
     * This Component tells its loads that they can no longer draw current from it.
     * If this is not a switchable Component that has been switched off,
     * this Component then passes on the information to its loads that they are now disengaged.
     */
    public void disengage() {
        if (this.isEngaged) {
            this.toggleIsEngaged();
            report(this, Msg.DISENGAGING);
            this.disengageLoads();
        }
    }

    /**
     * Inform all Components to which this Component acts as a source that they will no longer get any current.
     * In other words, disengage all of this component's loads. This is a simple internal method.
     */
    protected void disengageLoads(){
            for(Component child: this.children){
                child.disengage();
            }
        }

    /**
     * this method has the job of printing longer tabs the deeper
     * the recursion of the tree is
     * @param tab: how long the spacing will be
     */
    public void helperFunction(String tab){
        System.out.println(tab+"+ "+identify(this));
        for(Component child: this.children){
            child.helperFunction("    "+tab);
        }
    }
    /**
     * Display this (sub)tree vertically, with indentation. Format:
     *  + this component's identity
     *      + a load's identity
     *          + a load's load's identity
     *          + a load's load
     *      + a load's identity
     *          + a load's load's identity
     *          + a load's load
     */
    public void display(){
    System.out.println();
    helperFunction("");
    System.out.println();
    }
}

