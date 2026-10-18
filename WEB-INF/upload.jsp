<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Upload</title>
</head>
<body>
    <h1>Envoyer des fichiers</h1>
    <form action="<%= request.getContextPath() %>/app/upload" method="post" enctype="multipart/form-data">
        <input type="file" name="fichiers" multiple required>
        <button type="submit">Envoyer</button>
    </form>
</body>
</html>