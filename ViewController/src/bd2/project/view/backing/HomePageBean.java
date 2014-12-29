package bd2.project.view.backing;

import java.sql.CallableStatement;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.SQLException;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.jdbc.internal.OracleTypes;

public class HomePageBean {
    
    private RichInputText j_username;
    private RichInputText j_password;
    
    public HomePageBean() {
        super();
    }

    public void setJ_username(RichInputText j_username) {
        this.j_username = j_username;
    }

    public RichInputText getJ_username() {
        return j_username;
    }

    public void setJ_password(RichInputText j_password) {
        this.j_password = j_password;
    }

    public RichInputText getJ_password() {
        return j_password;
    }
    
    
    
    public String doLogin() {
        FacesContext fctx;
        fctx = FacesContext.getCurrentInstance();
        String passwordStr = null;
        String usernameStr = null;
        String result = "";
        
        if (j_username.getValue() != null && j_password.getValue() != null) {
            usernameStr = j_username.getValue().toString();
            passwordStr = j_password.getValue().toString();
            
        } else if (j_username.getValue() == null) {
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Username or Password",
                                 "Introduceti nume utilizator");
            fctx.addMessage("j_username", msg);
            return null;
        } else if (j_password.getValue() == null) {
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Username or Password",
                                 "Introduceti parola");
            fctx.addMessage("j_password", msg);
            return null;
        }
        
        //apeleaza functia din bd care returneaza 'D' daca utilizatorul exista
        // si 'N' daca utilizatorul nu exista
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding ob = bindings.getOperationBinding("doLoginDB");
        Map params = ob.getParamsMap();
        params.put("userName", usernameStr);
        params.put("password", passwordStr);
        ob.execute();
        
        if (ob.getResult() != null) {
            result = ob.getResult().toString();
            System.out.println(result);
        }
        
        if ("N".equals(result)) {
            FacesMessage fm = new FacesMessage();
            fm.setDetail("Cont inexistent.");
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, fm);
        }
        
        return result.equals("D") ? "ok" : null;  
    }
}
