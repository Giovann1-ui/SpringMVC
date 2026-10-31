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
    Object username = request.getAttribute("username");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profil</title>
</head>

<body>
    <% if (username != null) { %>
        <h1>Connecte : <%= esc(username) %></h1>
        <p><a href="<%= ctx %>/app/logout">Se deconnecter</a></p>
    <% } else { %>
        <h1>Non connecte</h1>
        <p>
            <a href="<%= ctx %>/app/login">Se connecter</a>
            |
            <a href="<%= ctx %>/app/inscription">Creer un compte</a>
        </p>
    <% } %>
</body>

</html>