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
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;


@WebServlet("/login/*")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    @Inject
    UserController userController;

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
        //Delete the part above
        String path = "templates/login";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
        templateEngine.process(path, ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        String mail = req.getParameter("mail");
        String password = req.getParameter("password");

        UserBean user = null;


        if(mail.equals(ctx.getInitParameter("pm-mail")) && password.equals(ctx.getInitParameter("pm-password"))){
            user = userController.searchUser(mail);
            if(user != null){
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath()+"/");
                return;
            }
            user = new UserBean();
            user.setMail(mail);
            user.setPolicyMakerID("TestPolicyMaker");
            user.setAreaOfResidence(ctx.getInitParameter("pm-area-of-residence"));
            user.setDateOfBirth(new Timestamp(Calendar.getInstance().getTime().getTime()));
            user.setCreatedAt(new Timestamp(Calendar.getInstance().getTime().getTime()));
            user.setName("Policy");
            user.setSurname("Maker");
            Long id = userController.createUser(user);
            user.setUserId(id);
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath()+"/");

        } else if(mail.equals(ctx.getInitParameter("u-mail")) && password.equals(ctx.getInitParameter("u-password"))){
            user = userController.searchUser(mail);
            if(user != null){
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath()+"/");
                return;
            }
            user = new UserBean();
            user.setMail(mail);
            user.setPolicyMakerID(null);
            user.setAreaOfResidence(ctx.getInitParameter("pm-area-of-residence"));
            user.setDateOfBirth(new Timestamp(Calendar.getInstance().getTime().getTime()));
            user.setCreatedAt(new Timestamp(Calendar.getInstance().getTime().getTime()));
            user.setName("User");
            user.setSurname("Test");
            Long id = userController.createUser(user);
            user.setUserId(id);
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath()+"/");
        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Wrong username or password");
        }
    }

}
