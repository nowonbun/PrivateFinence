package Common;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSession;

import Model.User;

public class WebFilter implements Filter {

	private ArrayList<String> passUrl = null;
	private String contextPath;

	public void init(FilterConfig config) throws ServletException {
		contextPath = config.getServletContext().getContextPath();
		passUrl = new ArrayList<String>();
		String[] ignoredPaths = config.getInitParameter("passPage").split(",");
		for (String ignoredPath : ignoredPaths) {
			passUrl.add(contextPath + ignoredPath);
		}
	}

	@Override
	public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) sreq;
		HttpServletResponse res = (HttpServletResponse) sres;
		HttpSession session = req.getSession();
		String url = req.getRequestURI();
		for (String buf : passUrl) {
			if (buf.equals(url)) {
				chain.doFilter(req, res);
				return;
			}
		}
		if (session.getAttribute(Define.USER_SESSION_NAME) == null) {
			res.sendRedirect("./");
			return;
		}
		User user = (User)session.getAttribute(Define.USER_SESSION_NAME);
		if(user.Isdeleted()) {
			res.setStatus(401);
			return;
		}
		chain.doFilter(req, res);
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
