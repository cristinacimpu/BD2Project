package bd2.project.view.backing;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.share.ADFContext;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

public class ReservationDetailsPageBean {
    public ReservationDetailsPageBean() {
        super();
    }
    
    public String doReservation() {
        
        FacesContext fctx;
        fctx = FacesContext.getCurrentInstance();
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        //System.out.println(ADFContext.getCurrent().getPageFlowScope().get("user_prm").toString());
        
        AttributeBinding flightIdBinding =
            (AttributeBinding)bindings.getControlBinding("FlightId");
        String flight_id_prm = null;
        if (flightIdBinding.getInputValue() != null) {
            flight_id_prm = flightIdBinding.getInputValue().toString();
            System.out.println("flight_id_prm = "  + flight_id_prm);
        }
        
        OperationBinding ob =
            (OperationBinding)bindings.getOperationBinding("doReservationDB");
        ob.getParamsMap().put("userName", AdfFacesContext.getCurrentInstance().getPageFlowScope().get("user_prm").toString());
        ob.getParamsMap().put("flightId", flight_id_prm);
        ob.execute();
        
        String result = ob.getResult().toString();
        if (result.startsWith("D")) {
            FacesMessage fm = new FacesMessage();
            fm.setDetail("S-a facut rezervarea. ID-ul rezervarii este " + result.substring(1) +
                         ". Va multumim!");
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, fm);
        } else if (result.equals("N")) {
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "S-a produs o eroare.",
                                 "Nu s-a facut rezervarea.");
            fctx.addMessage(null, msg);
            return null;
        }
        
        return "ok";
    }
}
