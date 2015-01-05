package bd2.project.view.backing;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichInputDate;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

public class NewFlightPageBean {

    private RichInputText src;
    private RichInputText dest;
    private RichInputText departHour;
    private RichInputDate departDay;
    private RichInputText duration;
    private RichInputText noSeats;
    private RichInputText price;

    public NewFlightPageBean() {
        super();
    }

    public void setSrc(RichInputText src) {
        this.src = src;
    }

    public RichInputText getSrc() {
        return src;
    }

    public void setDest(RichInputText dest) {
        this.dest = dest;
    }

    public RichInputText getDest() {
        return dest;
    }

    public void setDepartHour(RichInputText departHour) {
        this.departHour = departHour;
    }

    public RichInputText getDepartHour() {
        return departHour;
    }

    public void setDepartDay(RichInputDate departDay) {
        this.departDay = departDay;
    }

    public RichInputDate getDepartDay() {
        return departDay;
    }

    public void setDuration(RichInputText duration) {
        this.duration = duration;
    }

    public RichInputText getDuration() {
        return duration;
    }

    public void setNoSeats(RichInputText noSeats) {
        this.noSeats = noSeats;
    }

    public RichInputText getNoSeats() {
        return noSeats;
    }

    public void setPrice(RichInputText price) {
        this.price = price;
    }

    public RichInputText getPrice() {
        return price;
    }

    public String addNewFlight() {

        if (src.getValue() == null || dest.getValue() == null ||
            departDay.getValue() == null || departHour.getValue() == null ||
            duration.getValue() == null || noSeats.getValue() == null ||
            price.getValue() == null) {

            System.out.println("Este necesar sa completati toate campurile!");
            FacesMessage fm = new FacesMessage();
            fm.setDetail("Este necesar sa completati toate campurile!");
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, fm);

            return null;
        }

        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding ob =
            bindings.getOperationBinding("newFlightDB");
        Map params = ob.getParamsMap();
        params.put("src_prm", src.getValue().toString());
        params.put("dest_prm", dest.getValue().toString());
        params.put("departHour_prm", departHour.getValue().toString());
        params.put("departDay_prm", departDay.getValue().toString());
        System.out.println("data introdusa = " + departDay.getValue().toString());
        params.put("duration_prm", duration.getValue().toString());
        params.put("noSeats_prm", noSeats.getValue().toString());
        params.put("price_prm", price.getValue().toString());
        ob.execute();
        
        return "ok";
    }
}
