package me.petko.hellochat;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/nick")
public class SelectNickServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectNickServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("current_nickname", "");
        Utils.getCookie("hello_chat_nickname", request).ifPresent(cookie -> request.setAttribute("current_nickname", cookie.getValue()));
        RequestDispatcher view = request.getRequestDispatcher("NickForm.jsp");
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie = new Cookie("hello_chat_nickname", request.getParameter("nickname"));
        response.addCookie(cookie);
        response.sendRedirect("messages");
    }
}
