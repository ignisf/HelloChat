package me.petko.hellochat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import me.petko.hellochat.model.Message;

@WebServlet("/messages")
public class MessagesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MessagesServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utils.getCookie("hello_chat_nickname", request).ifPresent(cookie -> request.setAttribute("current_nickname", cookie.getValue()));

        Context context;
        DataSource dataSource;
        Connection connection = null;

        try {
            context = new InitialContext();
            dataSource = (DataSource)context.lookup("java:comp/env/jdbc/postgres");
            connection = dataSource.getConnection();

            List<Message> messages = Message.last(100, connection);

            request.setAttribute("messages", messages);

            RequestDispatcher view = request.getRequestDispatcher("MessagesIndex.jsp");
            view.forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new ServletException(e);
                }
            }

        }
    }
}
