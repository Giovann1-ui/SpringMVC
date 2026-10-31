<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%!
    private String esc(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString()
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;");
    }
%>
<%
    Object erreur = request.getAttribute("erreur");
    Object message = request.getAttribute("message");
%>
<!DOCTYPE html>
<html lang="fr">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Connexion</title>
    </head>

    <body>
        <h1>Connexion</h1>

        <% if (message != null) { %>
        <p style="color: green;"><%= esc(message) %></p>
        <% } %>
        <% if (erreur != null) { %>
        <p style="color: red;"><%= esc(erreur) %></p>
        <% } %>

        <form action="<%= request.getContextPath() %>/app/login" method="post">
            <p>
                <label for="username">Username :</label>
                <input type="text" id="username" name="username" required>
            </p>
            <p>
                <label for="password">Password :</label>
                <input type="password" id="password" name="password" required>
            </p>
            <button type="submit">Se connecter</button>
        </form>

        <p>Pas encore de compte ? <a href="<%= request.getContextPath() %>/app/inscription">Creer un compte</a></p>
    </body>

</html>