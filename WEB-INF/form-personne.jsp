<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="app.model.UserModel" %>
<%@ page import="app.model.AdresseModel" %>
<%!
    // Evite que du HTML saisi dans le formulaire soit interprete par la page
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
    UserModel p = (UserModel) request.getAttribute("personne");
%>
<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Utilisateur</title>
</head>

<body>
    <h1>Nouvel utilisateur</h1>

    <form action="<%= request.getContextPath() %>/app/personne2" method="post">
        <p>
            <label for="username">Username :</label>
            <input type="text" id="username" name="value.username" required>
        </p>
        <p>
            <label for="password">Password :</label>
            <input type="password" id="password" name="value.password" required>
        </p>
        <p>
            <label for="street">Rue :</label>
            <input type="text" id="street" name="value.adresse.street" required>
        </p>
        <p>
            <label for="city">Ville :</label>
            <input type="text" id="city" name="value.adresse.city" required>
        </p>
        <p>
            <label for="postalCode">Code postal :</label>
            <input type="number" id="postalCode" name="value.adresse.postalCode" required>
        </p>
        <button type="submit">Envoyer</button>
    </form>

    <% if (p != null) { %>
        <h2>Utilisateur recu</h2>
        <ul>
            <li>username : <%= esc(p.getUsername()) %></li>
            <li>password : <%= esc(p.getPassword()) %></li>

            <% AdresseModel a = p.getAdresse(); %>
            <% if (a != null) { %>
                <li>adresse :
                    <ul>
                        <li>rue : <%= esc(a.getStreet()) %></li>
                        <li>ville : <%= esc(a.getCity()) %></li>
                        <li>code postal : <%= a.getPostalCode() %></li>
                    </ul>
                </li>
            <% } else { %>
                <li>adresse : aucune</li>
            <% } %>
        </ul>
    <% } %>
</body>

</html>