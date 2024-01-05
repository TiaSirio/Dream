package it.dreamplatform.forum.servlet;

import it.dreamplatform.forum.bean.UserBean;
import it.dreamplatform.forum.controller.UserController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Calendar;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Inject
    UserController userController;

    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    private final String[] parameters = {"name", "surname", "birthdate", "areaOfResidence", "mail"};

    @Override
    public void init() throws ServletException {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getSession().getAttribute("registerUser") == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You cannot access this page before accessing SSO page");
            return;
        }
        UserBean user = (UserBean) req.getSession().getAttribute("registerUser") ;
        String path = "templates/register";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
        ctx.setVariable("user", user);
        templateEngine.process(path, ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        for(String p : parameters){
            if(req.getParameter(p)==null || req.getParameter(p).isEmpty()){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or missing parameters. Please go back and retry");
                return;
            }
        }
        UserBean userBean = new UserBean();
        userBean.setName((String) req.getParameter("name"));
        userBean.setSurname((String) req.getParameter("surname"));
        userBean.setAreaOfResidence((String) req.getParameter("areaOfResidence"));
        userBean.setMail((String) req.getParameter("mail"));
        userBean.setPolicyMakerID(((String) req.getParameter("policyMakerID")).isEmpty() ? null : (String) req.getParameter("policyMakerID"));
        try{
            userBean.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse((String) req.getParameter("birthdate")));
        } catch (Exception e){
            userBean.setDateOfBirth(new Timestamp(Calendar.getInstance().getTime().getTime()));
        }
        userBean.setUserId(userController.createUser(userBean));
        HttpSession session = req.getSession();
        session.removeAttribute("registerUser");
        session.setAttribute("user", userBean);
        resp.sendRedirect(req.getContextPath()+"/");
    }
}
