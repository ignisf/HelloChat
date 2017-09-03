<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">


        <title>Choose a nick for HelloChat</title>

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

            <form action="nick" class="col-sm-4 col-sm-offset-4" accept-charset="UTF-8" method="post">
                <header>
                    <legend>
                        <h3>Choose a nickname</h3>
                    </legend>
                </header>
                <div class="form-inputs">
                    <div class="form-group">
                        <label class="control-label" for="nickname">Nickname</label>
                        <input class="form-control" value="<%= StringEscapeUtils.escapeHtml4((String) request.getAttribute("current_nickname")) %>" autofocus="autofocus" name="nickname" id="nickname" type="text">
                    </div>
                </div>
                <div class="form-actions">
                    <input name="commit" value="Select" class="btn btn-default" type="submit">
                </div>
            </form>
        </div>
    </body>
</html>
