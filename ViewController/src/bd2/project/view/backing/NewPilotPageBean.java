package bd2.project.view.backing;

import java.text.SimpleDateFormat;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

public class NewPilotPageBean {

    private RichInputText cnp;
    private RichInputText firstname;
    private RichInputText lastname;
    private RichInputText address;
    private RichInputText phone;
    private RichInputText sal;
    private RichSelectOneChoice rank;
    private RichInputDate hiredate;
    private RichInputText comm;

    public NewPilotPageBean() {
        super();
    }

    public String addNewPilot() {

        if (cnp.getValue() == null || firstname.getValue() == null ||
            lastname.getValue() == null || address.getValue() == null ||
            phone.getValue() == null || sal.getValue() == null ||
            rank.getValue() == null || hiredate.getValue() == null ||
            comm.getValue() == null) {

            System.out.println("Este necesar sa completati toate campurile!");
            FacesMessage fm = new FacesMessage();
            fm.setDetail("Este necesar sa completati toate campurile!");
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, fm);

            return null;
        }
        
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        String date = DATE_FORMAT.format(hiredate.getValue());
        String job = "";
        if (rank.getValue().toString().equals("P")) {
            job = "pilot";
        } else if (rank.getValue().toString().equals("C")) {
            job = "copilot";
        }
        
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding ob =
            bindings.getOperationBinding("addPilotDB");
        Map params = ob.getParamsMap();
        params.put("cnp_prm", cnp.getValue().toString());
        params.put("firstname_prm", firstname.getValue().toString());
        params.put("lastname_prm", lastname.getValue().toString());
        params.put("address_prm", address.getValue().toString());
        params.put("phone_prm", phone.getValue().toString());
        params.put("sal_prm", sal.getValue().toString());
        params.put("rank_prm", job);
        params.put("hiredate_prm", date);
        params.put("comm_prm", comm.getValue().toString());
        ob.execute();

        return "ok";
    }

    public void setCnp(RichInputText cnp) {
        this.cnp = cnp;
    }

    public RichInputText getCnp() {
        return cnp;
    }

    public void setFirstname(RichInputText firstname) {
        this.firstname = firstname;
    }

    public RichInputText getFirstname() {
        return firstname;
    }

    public void setLastname(RichInputText lastname) {
        this.lastname = lastname;
    }

    public RichInputText getLastname() {
        return lastname;
    }

    public void setAddress(RichInputText address) {
        this.address = address;
    }

    public RichInputText getAddress() {
        return address;
    }

    public void setPhone(RichInputText phone) {
        this.phone = phone;
    }

    public RichInputText getPhone() {
        return phone;
    }

    public void setSal(RichInputText sal) {
        this.sal = sal;
    }

    public RichInputText getSal() {
        return sal;
    }

    public void setHiredate(RichInputDate hiredate) {
        this.hiredate = hiredate;
    }

    public RichInputDate getHiredate() {
        return hiredate;
    }

    public void setComm(RichInputText comm) {
        this.comm = comm;
    }

    public RichInputText getComm() {
        return comm;
    }

    public void setRank(RichSelectOneChoice rank) {
        this.rank = rank;
    }

    public RichSelectOneChoice getRank() {
        return rank;
    }
}
