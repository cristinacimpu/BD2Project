package bd2.project.view.backing;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;

import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.ViewCriteria;
import oracle.jbo.server.ViewObjectImpl;

public class MainPageBean {

    private RichInputText card_no;
    private RichTable t1;

    public MainPageBean() {
        super();
    }

    public String doReservation() {

        System.out.println(ADFContext.getCurrent().getPageFlowScope().get("user_prm").toString());

        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding configSettings =
            (DCIteratorBinding)bindings.get("FlightsVOIterator");
        Row[] allconfigRows = configSettings.getAllRowsInRange();

        System.out.println(">>> iterator length = " + allconfigRows.length);
        return "ok";
    }

    public void buyTicket(ActionEvent actionEvent) {
        //System.out.println("Am cumparat.");

        FacesContext fctx;
        fctx = FacesContext.getCurrentInstance();
        String card_noStr = null;

        if (card_no.getValue() != null) {
            card_noStr = card_no.getValue().toString();
            System.out.println(card_noStr);
        } else {
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Introduceti numarul de card.",
                                 "");
            fctx.addMessage("card_no", msg);

            return;
        }

        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding ob = bindings.getOperationBinding("buyTicketDB");
        Map params = ob.getParamsMap();
        params.put("reservation_id",
                   ADFContext.getCurrent().getPageFlowScope().get("ReservationIdPrm").toString());
        params.put("card_no", card_noStr);
        ob.execute();

        String result = ob.getResult().toString();
        if (result.startsWith("D")) {
            FacesMessage fm = new FacesMessage();
            fm.setDetail("Va multumim ca ati ales sa folositi serviciile noastre!");
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, fm);

            OperationBinding ob1 =
                bindings.getOperationBinding("ExecuteWithParams");
            ob1.execute();

        } else if (result.equals("N")) {
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "S-a produs o eroare.",
                                 "Biletul nu a putut fi cumparat.");
            fctx.addMessage(null, msg);
            return;
        }

        card_no.setValue("");
    }

    public void cancelFlight(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding ob = bindings.getOperationBinding("cancelFlightDB");
        Map params = ob.getParamsMap();
        params.put("flightId",
                   ADFContext.getCurrent().getPageFlowScope().get("FlightIdPrm").toString());
        ob.execute();
        
        //resetare tabel
        DCIteratorBinding iterBind =
            (DCIteratorBinding)bindings.get("FlightsVOIterator");
        iterBind.executeQuery();
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(t1);
    }

    public void setCard_no(RichInputText card_no) {
        this.card_no = card_no;
    }

    public RichInputText getCard_no() {
        return card_no;
    }

    public void setT1(RichTable t1) {
        this.t1 = t1;
    }

    public RichTable getT1() {
        return t1;
    }
}
