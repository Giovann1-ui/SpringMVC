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
<% Object erreur = request.getAttribute("erreur"); %>
<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription</title>
</head>

<body>
    <h1>Inscription</h1>

    <% if (erreur != null) { %>
        <p style="color: red;"><%= esc(erreur) %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/app/inscription" method="post">
        <p>
            <label for="username">Username :</label>
            <input type="text" id="username" name="username" required>
        </p>
        <p>
            <label for="password">Password :</label>
            <input type="password" id="password" name="password" required>
        </p>
        <p>
            <label for="confirmation">Confirmer le password :</label>
            <input type="password" id="confirmation" name="confirmation" required>
        </p>
        <button type="submit">Creer le compte</button>
    </form>

    <p>Deja un compte ? <a href="<%= request.getContextPath() %>/app/login">Se connecter</a></p>
</body>

</html>