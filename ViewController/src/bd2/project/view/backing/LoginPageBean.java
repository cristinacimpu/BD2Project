package bd2.project.view.backing;

import java.io.IOException;

import java.util.Enumeration;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.adf.model.BindingContext;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import weblogic.servlet.security.ServletAuthentication;

public class LoginPageBean {
    
    private RichInputText j_username;
    private RichInputText j_password;
    
    public LoginPageBean() {
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
        HttpServletRequest request = null;
        request = (HttpServletRequest)fctx.getExternalContext().getRequest();
        request.getMethod();
        HttpServletResponse response = null;
        String passwordStr = null;
        String usernameStr = null;
        response =
                (HttpServletResponse)fctx.getExternalContext().getResponse();
        UIViewRoot viewRoot = fctx.getViewRoot();
        String result = "";
        
        if (j_username.getValue() != null && j_password.getValue() != null) {
            usernameStr = j_username.getValue().toString();
            AdfFacesContext.getCurrentInstance().getPageFlowScope().put("user_prm", usernameStr);
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
        
        //apeleaza functia din appModule care returneaza 'D' daca utilizatorul exista
        // si 'N' daca utilizatorul nu exista
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding ob = bindings.getOperationBinding("doLoginDB");
        Map params = ob.getParamsMap();
        params.put("userName", usernameStr);
        params.put("password", passwordStr);
        ob.execute();
        
        if (ob.getResult() != null) {
            result = ob.getResult().toString();
            System.out.println("login = " + result);
        }
        
        if ("N".equals(result)) {
            FacesMessage fm = new FacesMessage();
            fm.setDetail("Cont inexistent! Parola sau numele de utilizator au fost scrise gresit.");
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, fm);
        }
        
//        String loginURL = "/adfAuthentication?succes_url=/faces/MainPage.jspx";
//        ADFContext.getCurrent().getSessionScope().put("from",
//                                                      "/BD2Project/faces/Login.jspx");
//        ServletAuthentication.generateNewSessionID(request);
//        response =
//                (HttpServletResponse)fctx.getExternalContext().getResponse();
//        
//        Enumeration en = request.getParameterNames();
//
//        while (en.hasMoreElements()) {
//            String currEl = en.nextElement().toString();
//            ADFContext.getCurrent().getSessionScope().put(currEl,
//                                                          request.getParameter(currEl));
//        }
//        sendForward(request, response, loginURL);
//        String sessionId;
//        sessionId = request.getSession().getId();
//        String ipAddress = request.getRemoteAddr();
//        ADFContext.getCurrent().getSessionScope().put("sessionId", sessionId);
//        ADFContext.getCurrent().getSessionScope().put("SelectedLanguage",
//                                                      ADFContext.getCurrent().getLocale().toString());
//        ADFContext.getCurrent().getSessionScope().put("IPAddress", ipAddress);
//        
        return result.equals("D") ? "ok" : null;  
//        return "ok";
    }
    
    private void sendForward(HttpServletRequest request,
                             HttpServletResponse response, String forwardUrl) {
        FacesContext ctx;
        ctx = FacesContext.getCurrentInstance();
        //        Enumeration en =request.getParameterNames();
        //        System.out.println("//////////////forwardUrl"+forwardUrl);
        //        while (en.hasMoreElements()) {
        //            String curel=en.nextElement().toString();
        //            System.out.println("//////////////"+curel+"-----"+ request.getParameter(curel));
        //        }
        RequestDispatcher dispatcher =
            request.getRequestDispatcher(forwardUrl);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException se) {
            reportUnexpectedLoginError("ServletException", se);
        } catch (IOException ie) {
            reportUnexpectedLoginError("IOException", ie);
        }
        ctx.responseComplete();
    }
    
    private void reportUnexpectedLoginError(String errType, Exception e) {
        FacesMessage msg =
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unexpected error during login",
                             "Unexpected error during login (" + errType +
                             ") please consult logs for detail");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext.getCurrentInstance().renderResponse();
    }
}
