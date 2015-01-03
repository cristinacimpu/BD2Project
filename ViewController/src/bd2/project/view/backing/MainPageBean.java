package bd2.project.view.backing;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

import oracle.binding.BindingContainer;

import oracle.jbo.Row;

public class MainPageBean {
    public MainPageBean() {
        super();
    }
    
    public String doReservation() {
        
        System.out.println(ADFContext.getCurrent().getPageFlowScope().get("user_prm").toString());
        
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding configSettings =
                    (DCIteratorBinding)bindings.get("FlightsVOIterator");
        Row[] allconfigRows = configSettings.getAllRowsInRange();
        
        System.out.println(">>> iterator length = " + allconfigRows.length);
        return "ok";
    }
}
