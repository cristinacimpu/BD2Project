package bd2.project.view.backing;

import java.text.SimpleDateFormat;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.QueryOperationEvent;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.ViewCriteria;
import oracle.jbo.server.ViewObjectImpl;


public class MainPageBean {

    private RichInputText card_no;
    private RichTable t1;
    private RichInputDate afterDay;
    private RichSelectOneChoice soc;

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

        ViewObjectImpl vo = (ViewObjectImpl)configSettings.getViewObject();
        System.out.println(vo.getNamedWhereClauseParam("depart_day_prm").toString());

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

    public String logout() {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iterBind =
            (DCIteratorBinding)bindings.get("FlightsVOIterator");
        ViewObjectImpl vo = (ViewObjectImpl)iterBind.getViewObject();
        String name = vo.getName();
        ViewCriteria vc = vo.getViewCriteria("FlightsVOCriteria");
        vo.removeViewCriteria("FlightsVOCriteria");
        vo.executeEmptyRowSet();
        vc.resetCriteria();
        vo.setNamedWhereClauseParam("src_prm", "");
        vo.setNamedWhereClauseParam("dest_prm", "");
        vo.applyViewCriteria(vc);
        vo.executeQuery();
        
        return "logout";
    }
    
    public void queryOperationListener(QueryOperationEvent queryOperationEvent) {
        invokeMethodExpression("#{bindings.FlightsVOCriteriaQuery.processQueryOperation}",
                               Object.class, QueryOperationEvent.class,
                               queryOperationEvent);

        if (queryOperationEvent.getOperation().name().toUpperCase().equals("RESET")) {
            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding operationBinding =
                bindings.getOperationBinding("doQueryResultReset");
            operationBinding.execute();

            AdfFacesContext.getCurrentInstance().addPartialTarget(t1);
        }
    }
    
    public Object invokeMethodExpression(String expr, Class returnType,
                                         Class argType, Object argument) {
        return invokeMethodExpression(expr, returnType,
                                      new Class[] { argType },
                                      new Object[] { argument });
    }
    
    public Object invokeMethodExpression(String expr, Class returnType,
                                         Class[] argTypes, Object[] args) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ELContext elctx = fc.getELContext();
        ExpressionFactory elFactory =
            fc.getApplication().getExpressionFactory();
        MethodExpression methodExpr =
            elFactory.createMethodExpression(elctx, expr, returnType,
                                             argTypes);
        return methodExpr.invoke(elctx, args);
    }
    
    public void generateReportTickets(ActionEvent actionEvent) {
        
        if (afterDay.getValue() == null) {
            FacesMessage fm = new FacesMessage();
            fm.setDetail("Este necesar sa completati data pentru generarea raportului!");
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, fm);

            return;
        } else {
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
            String date = DATE_FORMAT.format(afterDay.getValue());
            
            BindingContainer bindings =
                BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding ob =
                bindings.getOperationBinding("EWPTickets");
            Map params = ob.getParamsMap();
            params.put("date_prm", date);
            ob.execute();
        }
        
        
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

    public void setAfterDay(RichInputDate afterDay) {
        this.afterDay = afterDay;
    }

    public RichInputDate getAfterDay() {
        return afterDay;
    }

    public void generateSalReport(ActionEvent actionEvent) {
        if (soc.getValue() != null) { 
            BindingContainer bindings =
                BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding ob =
                bindings.getOperationBinding("EWPSalary");
            Map params = ob.getParamsMap();
            if (soc.getValue().toString().equals("P")) {
                params.put("rank_prm", "pilot");
            } else if (soc.getValue().toString().equals("C")) {
                params.put("rank_prm", "copilot");
            }
            ob.execute();
        } else {
            FacesMessage fm = new FacesMessage();
            fm.setDetail("Este necesar sa selectati functia pentru a genera raportul!");
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, fm);
            return;
        }
    }

    public void setSoc(RichSelectOneChoice soc) {
        this.soc = soc;
    }

    public RichSelectOneChoice getSoc() {
        return soc;
    }
}
