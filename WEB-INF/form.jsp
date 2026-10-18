<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% Object resultat = request.getAttribute("resultat"); %>
<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Addition</title>
</head>

<body>
    <h1>Addition</h1>

    <form action="<%= request.getContextPath() %>/app/addition" method="post">
        <label for="a">a :</label>
        <input type="number" id="a" name="a" required>

        <label for="b">b :</label>
        <input type="number" id="b" name="b" required>

        <button type="submit">Calculer</button>
    </form>

    <% if (resultat != null) { %>
        <p>Resultat : <%= resultat %></p>
    <% } %>
</body>

</html>