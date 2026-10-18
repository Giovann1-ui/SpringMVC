<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="app.model.FichierModel" %>
<%!
    // Evite que du HTML contenu dans un nom de fichier soit interprete par la page
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
    List<FichierModel> fichiers = (List<FichierModel>) request.getAttribute("fichiers");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="fr">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Fichiers enregistres</title>
    </head>

    <body>
        <h1>Fichiers enregistres</h1>
        <p><a href="<%= ctx %>/app/upload">Envoyer d'autres fichiers</a></p>

        <% if (fichiers == null || fichiers.isEmpty()) { %>
        <p>Aucun fichier.</p>
        <% } else { %>
        <table border="1" cellpadding="6">
            <tr>
                <th>Id</th>
                <th>Nom</th>
                <th>Extension</th>
                <th>Contenu</th>
            </tr>
            <% for (FichierModel f : fichiers) {
                String ext = f.getExtension() == null ? "" : f.getExtension().toLowerCase();
                boolean image = ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg")
                || ext.equals("gif") || ext.equals("webp");
                String url = ctx + "/app/fichier?id=" + f.getId();
            %>
            <tr>
                <td><%= f.getId() %></td>
                <td><%= esc(f.getNom()) %></td>
                <td><%= esc(f.getExtension()) %></td>
                <td>
                    <% if (image) { %>
                    <img src="<%= url %>" alt="<%= esc(f.getNom()) %>" width="120">
                    <% } else if (ext.equals("txt")) { %>
                    <iframe src="<%= url %>" width="300" height="100"></iframe>
                    <% } else { %>
                    <a href="<%= url %>" target="_blank">Ouvrir / telecharger</a>
                    <% } %>
                </td>
            </tr>
            <% } %>
        </table>
        <% } %>
    </body>

</html>