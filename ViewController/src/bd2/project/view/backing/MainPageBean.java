package bd2.project.view.backing;

import oracle.adf.share.ADFContext;

public class MainPageBean {
    public MainPageBean() {
        super();
    }
    
    public String doReservation() {
        
        System.out.println(ADFContext.getCurrent().getPageFlowScope().get("user_prm").toString());
        return "ok";
    }
}
