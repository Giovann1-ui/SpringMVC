<% String nom=(String) request.getAttribute("message"); %>

    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Document</title>
    </head>

    <body>
        Index.jsp vous dit bonjour

        <% if (nom !=null) { %>
            <p>
                <%= nom.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;") %>
            </p>
            <% } %>
    </body>

    </html>