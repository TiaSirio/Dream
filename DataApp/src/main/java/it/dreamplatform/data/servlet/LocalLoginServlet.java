package it.dreamplatform.data.servlet;

import it.dreamplatform.data.bean.UserBean;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

@WebServlet("/login/local")
public class LocalLoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

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
        String path = "templates/public/login";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
        templateEngine.process(path, ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       ServletContext ctx = getServletContext();
        String mail = req.getParameter("mail");
        String password = req.getParameter("password");
        if(mail.equals(ctx.getInitParameter("pm-mail")) && password.equals(ctx.getInitParameter("pm-password"))){
            UserBean user = new UserBean();
            user.setMail(mail);
            user.setPolicyMakerID("TestPolicyMaker");
            user.setAreaOfResidence(ctx.getInitParameter("pm-area-of-residence"));
            user.setDateOfBirth(new Timestamp(Calendar.getInstance().getTime().getTime()));
            user.setCreatedAt(new Timestamp(Calendar.getInstance().getTime().getTime()));
            user.setName("Policy");
            user.setSurname("Maker");
            user.setUserId(1L);
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath()+"/policymaker");
        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Wrong mail or password");
        }
    }

}