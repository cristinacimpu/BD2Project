package bd2.project.view.backing;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

public class NewAccountPageBean {

    private RichInputText cnp;
    private RichInputText firstname;
    private RichInputText lastname;
    private RichInputText address;
    private RichInputText phone;
    private RichInputText email;
    private RichInputText username;
    private RichInputText password;
    private RichInputText confirmation;

    public NewAccountPageBean() {
        super();
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

    public void setEmail(RichInputText email) {
        this.email = email;
    }

    public RichInputText getEmail() {
        return email;
    }

    public void setUsername(RichInputText username) {
        this.username = username;
    }

    public RichInputText getUsername() {
        return username;
    }

    public void setPassword(RichInputText password) {
        this.password = password;
    }

    public RichInputText getPassword() {
        return password;
    }

    public void setConfirmation(RichInputText confirmation) {
        this.confirmation = confirmation;
    }

    public RichInputText getConfirmation() {
        return confirmation;
    }

    public String createNewAccount() {
        
        if (cnp.getValue() == null || firstname.getValue() == null ||
            lastname.getValue() == null || address.getValue() == null ||
            phone.getValue() == null || email.getValue() == null ||
            password.getValue() == null || username.getValue() == null ||
            confirmation.getValue() == null) {
            
            System.out.println("Este necesar sa completati toate campurile!");
            FacesMessage fm = new FacesMessage();
            fm.setDetail("Este necesar sa completati toate campurile!");
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, fm);
            
            return null;
        }

        if (password.getValue() != null && confirmation.getValue() != null) {
            if (password.getValue().toString().equals(confirmation.getValue().toString())) {
                System.out.println(cnp.getValue().toString() + " " +
                                   username.getValue().toString());
                
                BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
                OperationBinding ob = bindings.getOperationBinding("createNewAccountDB");
                Map params = ob.getParamsMap();
                params.put("cnp_prm", cnp.getValue().toString() );
                params.put("firstname_prm", firstname.getValue().toString());
                params.put("lastname_prm", lastname.getValue().toString());
                params.put("address_prm", address.getValue().toString());
                params.put("phone_prm", phone.getValue().toString());
                params.put("email_prm", email.getValue().toString());
                params.put("username_prm", username.getValue().toString());
                params.put("passwd_prm", password.getValue().toString());
                ob.execute();
                String result = ob.getResult().toString();
                if (result.startsWith("N")) {
                    FacesMessage fm = new FacesMessage();
                    fm.setDetail(result.substring(1));
                    fm.setSeverity(FacesMessage.SEVERITY_INFO);
                    FacesContext.getCurrentInstance().addMessage(null, fm);
                    return null;
                }
                return "ok";
            } else {
                FacesMessage fm = new FacesMessage();
                fm.setDetail("Parolele nu corespund.");
                fm.setSeverity(FacesMessage.SEVERITY_INFO);
                FacesContext.getCurrentInstance().addMessage(null, fm);
                return null;
            }
        }
        
        return null;
    }
}
