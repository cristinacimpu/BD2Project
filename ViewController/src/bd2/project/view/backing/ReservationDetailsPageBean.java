package bd2.project.view.backing;

import oracle.adf.model.BindingContext;
import oracle.adf.share.ADFContext;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

public class ReservationDetailsPageBean {
    public ReservationDetailsPageBean() {
        super();
    }
    
    public String doReservation() {
        
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        //System.out.println(ADFContext.getCurrent().getPageFlowScope().get("user_prm").toString());
        
        AttributeBinding flightIdBinding =
            (AttributeBinding)bindings.getControlBinding("FlightId");
        String flight_id_prm = null;
        if (flightIdBinding.getInputValue() != null) {
            flight_id_prm = flightIdBinding.getInputValue().toString();
            System.out.println("flight_id_prm = "  + flight_id_prm);
        }
        
        AttributeBinding clientIdBinding =
            (AttributeBinding)bindings.getControlBinding("ClientId");
        String client_id_prm = null;
        if (clientIdBinding.getInputValue() != null) {
            client_id_prm = clientIdBinding.getInputValue().toString();
            System.out.println("client_id_prm = "  + client_id_prm);
        }
        
        OperationBinding ob =
            (OperationBinding)bindings.getOperationBinding("doReservationDB");
        ob.getParamsMap().put("clientId", client_id_prm);
        ob.getParamsMap().put("flightId", flight_id_prm);
        ob.execute();
        
        return "ok";
    }
}
