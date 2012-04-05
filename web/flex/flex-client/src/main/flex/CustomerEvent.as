package {
import flash.events.Event;

public class CustomerEvent extends Event {
    public static const CREATED:String = "customerCreated";
    public static const UPDATED:String = "customerUpdated";

    public var customer:Customer;

    public function CustomerEvent(type:String, c:Customer, bubbles:Boolean = true, cancelable:Boolean = false) {
        this.customer = c;
        super(type, bubbles, cancelable);
    }
}
}