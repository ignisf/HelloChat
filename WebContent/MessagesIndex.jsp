<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="me.petko.hellochat.model.Message" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">


        <title>Hello Chat!</title>

        <!-- Bootstrap core CSS -->
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>

    <body>
        <div class="container">

            <div class="starter-template">
                <h1>
                    Hello <%= StringEscapeUtils.escapeHtml4((String) request.getAttribute("current_nickname")) %>!
                    <small><a href="nick" class="btn btn-primary">Change nick</a></small>
                </h1>
                <div id="container" class="well" style="height: 500px; overflow-y: scroll;">
                    <%
                    List<Message> messages = (ArrayList<Message>) request.getAttribute("messages");

                    for (Message message : messages) {
	              out.println("<blockquote>");
	              out.println("<p>");
	              out.println(StringEscapeUtils.escapeHtml4(message.getText()));
	              out.println("</p>");
	              out.println("<footer>");
	              out.println("<cite>");
	              out.println(StringEscapeUtils.escapeHtml4(message.getAuthor()));
	              out.println("</cite>");
	              out.println(" on ");
	              out.println(new SimpleDateFormat("d.MM.yyyy г., H:mm:ss").format(message.getTimeStamp()));
	              out.println("</footer>");
	              out.println("</blockquote>");
                    }
                    %>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <input type="hidden" id="author" name="author" value="<%= StringEscapeUtils.escapeHtml4((String) request.getAttribute("current_nickname")) %>" />
                        <input type="text" class="form-control" id="message" name="message"  />
                        <span class="input-group-btn">
                            <button type="button" id="send" class="btn btn-primary" onclick="send()">Send</button>
                        </span>
                    </div>
                </div>
            </div>

        </div><!-- /.container -->


        <!-- Bootstrap core JavaScript
             ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script>
         $('#container').scrollTop($('#container')[0].scrollHeight);
         var chatClient = new WebSocket("ws://localhost:8080/HelloChat/chat");

         chatClient.onmessage = function(evt) {
           addMessage(JSON.parse(evt.data));
         }

         function send() {
           var now = new Date();
           var input = $("#message");
           var author = $("#author");
           var message = {author: author.val(), text: input.val(), timeStamp: now.toLocaleString('bg')};
	   addMessage(message);


           chatClient.send(JSON.stringify(message));
           input.val("");
         }

         function addMessage(message) {
           var displayedMessage = $("<p></p>").text(message['text']);
           var blockquote = $("<blockquote></blockquote>");
           var footer = $("<footer></footer>");
           var cite = $("<cite></cite>").text(message['author']);
           var separator = $("<span> on </span>");
           var date = $("<span></span>").text(message['timeStamp'])
           footer.append(cite);
           footer.append(separator);
           footer.append(date);
           $("#container").append(blockquote.append(displayedMessage).append(footer));
           $('#container').scrollTop($('#container')[0].scrollHeight);
         }
        </script>
    </body>
</html>
