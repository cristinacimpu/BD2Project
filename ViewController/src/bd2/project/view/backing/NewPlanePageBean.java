package bd2.project.view.backing;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

public class NewPlanePageBean {

    private RichInputText airplane_name_prm;
    private RichInputText airplane_code_prm;
    private RichInputText seats_prm;
    private RichInputText airplane_length_prm;
    private RichInputText airplane_height_prm;
    private RichInputText airplane_weight_prm;

    public NewPlanePageBean() {
        super();
    }

    public String addNewPlane() {

        if (airplane_name_prm.getValue() == null ||
            airplane_code_prm.getValue() == null ||
            seats_prm.getValue() == null ||
            airplane_length_prm.getValue() == null ||
            airplane_height_prm.getValue() == null ||
            airplane_weight_prm.getValue() == null) {

            System.out.println("Este necesar sa completati toate campurile!");
            FacesMessage fm = new FacesMessage();
            fm.setDetail("Este necesar sa completati toate campurile!");
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, fm);

            return null;
        }

        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding ob = bindings.getOperationBinding("newPlaneDB");
        Map params = ob.getParamsMap();
        params.put("airplane_name_prm", airplane_name_prm.getValue().toString());
        params.put("airplane_code_prm", airplane_code_prm.getValue().toString());
        params.put("seats_prm", seats_prm.getValue().toString());
        params.put("airplane_length_prm", airplane_length_prm.getValue().toString());
        params.put("airplane_height_prm", airplane_height_prm.getValue().toString());
        params.put("airplane_weight_prm", airplane_weight_prm.getValue().toString());
        ob.execute();

        return "ok";
    }

    public void setAirplane_name_prm(RichInputText airplane_name_prm) {
        this.airplane_name_prm = airplane_name_prm;
    }

    public RichInputText getAirplane_name_prm() {
        return airplane_name_prm;
    }

    public void setAirplane_code_prm(RichInputText airplane_code_prm) {
        this.airplane_code_prm = airplane_code_prm;
    }

    public RichInputText getAirplane_code_prm() {
        return airplane_code_prm;
    }

    public void setSeats_prm(RichInputText seats_prm) {
        this.seats_prm = seats_prm;
    }

    public RichInputText getSeats_prm() {
        return seats_prm;
    }

    public void setAirplane_length_prm(RichInputText airplane_length_prm) {
        this.airplane_length_prm = airplane_length_prm;
    }

    public RichInputText getAirplane_length_prm() {
        return airplane_length_prm;
    }

    public void setAirplane_height_prm(RichInputText airplane_height_prm) {
        this.airplane_height_prm = airplane_height_prm;
    }

    public RichInputText getAirplane_height_prm() {
        return airplane_height_prm;
    }

    public void setAirplane_weight_prm(RichInputText airplane_weight_prm) {
        this.airplane_weight_prm = airplane_weight_prm;
    }

    public RichInputText getAirplane_weight_prm() {
        return airplane_weight_prm;
    }
}
