package it.dreamplatform.data.servlet;

import it.dreamplatform.data.bean.UserBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class implements registration and login for user authenticated by Shibboleth SP
 */
@WebServlet("/dream/login")
public class DreamLoginServlet extends HttpServlet {

    private final String[] requiredParameters = {"sn", "givenName", "mail", "areaOfResidence", "policyMakerID", "dateOfBirth"};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Mail parameter is required
        if(req.getAttribute("mail") == null || req.getAttribute("policyMakerID") == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The user does not have the required attribute");
            return;
        }

        UserBean user = new UserBean();
        user.setAreaOfResidence((String) req.getAttribute("areaOfResidence"));
        user.setCreatedAt(new Timestamp(Calendar.getInstance().getTime().getTime()));
        user.setName((String) req.getAttribute("givenName"));
        user.setMail((String) req.getAttribute("mail"));
        user.setPolicyMakerID((String) req.getAttribute("policyMakerID"));
        user.setSurname((String) req.getAttribute("sn"));
        try{
            user.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse((String) req.getAttribute("dateOfBirth")));
        } catch (Exception e){
            user.setDateOfBirth(new Timestamp(Calendar.getInstance().getTime().getTime()));
        }
        req.getSession().setAttribute("user", user);
        resp.sendRedirect(getServletContext().getContextPath() + "/policymaker");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }


}
