<h1>REST API Voting system for deciding where to have lunch.</h1>

<ul>
    <li>2 types of users: admin and regular users</li>
    <li>Admin can input a restaurant and it's lunch lunch of the day (2-5 items usually, just a dish name and price)
    </li>
    <li>Menu changes each day (admins do the updates)</li>
    <li>Users can vote on which restaurant they want to have lunch at</li>
    <li>Only one vote counted per user</li>
    <li>If user votes again the same day:
        <ul>
            <li>If it is before 11:00 we asume that he changed his mind.</li>
            <li>If it is after 11:00 then it is too late, vote can't be changed</li>
        </ul>
    </li>
</ul>
Each restaurant provides new lunch each day.

<h2>User voting and getting menus:</h2>

<h3>Get all today's menus:</h3>
<code>curl -i -s -X GET http://localhost:8080/menus --user user@yandex.ru:userPassword</code>
<pre>HTTP/1.1 200 OK
[{"restaurant":
{"id":1,"name":"Mirazur Restaurant"},
"dishes":
[{"name":"Cheese Burger","price":655},
{"name":"Sundried Tomato Chicken Kabobs","price":998},
{"name":"Balsamic Grilled Chicken","price":1300}]},
{"restaurant":
{"id":2,"name":"Noma Restaurant"},
"dishes":
[{"name":"Fresh Salmon","price":760},
{"name":"Crispy Fish Tacos","price":780},
{"name":"Cheese Burger","price":655},
{"name":"Sundried Tomato Chicken Kabobs","price":998},
{"name":"Balsamic Grilled Chicken","price":1300}]}
]}]</pre>

<h3>Get lunch menu for Restaurant:</h3>
<code>curl -i -s -X GET http://localhost:8080/restaurants/1/menu --user user@yandex.ru:userPassword</code>
<pre>HTTP/1.1 200 OK
{"restaurant":
{"id":1,"name":"Mirazur Restaurant"},
"dishes":
[{"name":"Cheese Burger","price":655},
{"name":"Sundried Tomato Chicken Kabobs","price":998},
{"name":"Balsamic Grilled Chicken","price":1300}]}</pre>

<h3>Vote for Restaurant (the number is the restaurant ID):</h3>
<code>curl -i -s -X POST http://localhost:8080/restaurants/1/vote --user user2@yandex.ru:user2Password</code>
<pre>HTTP/1.1 204 No Content</pre>

<h3>Update vote for Restaurant:</h3>
<code>curl -i -s -X PUT http://localhost:8080/restaurants/1/vote --user user@yandex.ru:userPassword</code>
<ul>
<li>If before 11AM:</li>
    <pre>HTTP/1.1 204 No Content</pre>

<li>If after 11AM:</li>
    <pre>HTTP/1.1 422 Unprocessable Entity
    {"url":"http://localhost:8080/restaurants/1/vote",
    "type":"VALIDATION_ERROR",
    "typeMessage":"Validation error",
    "details":["It is too late, for today vote can't be changed"]}</pre>

</ul>

<h3>Get the Menu voted for:</h3>
<code>curl -i -s -X GET http://localhost:8080/menus/vote --user user@yandex.ru:userPassword</code>
<ul>
<li>If vote exists:</li>
    <pre>HTTP/1.1 200 OK
    {"restaurant":
    {"id":1,"name":"Mirazur Restaurant"},
    "dishes":
    [{"name":"Cheese Burger","price":655},
    {"name":"Sundried Tomato Chicken Kabobs","price":998},
    {"name":"Balsamic Grilled Chicken","price":1300}]}</pre>
<li>If vote doesn't exist:</li>
    <pre>HTTP/1.1 422 Unprocessable Entity
    {"url":"http://localhost:8080/menus/vote",
    "type":"DATA_NOT_FOUND",
    "typeMessage":"Data not found",
    "details":["You haven't voted yet"]}</pre>
</ul>

<h2>User administration:</h2>

<h3>Create user:</h3>
<code>curl -i -s -X POST -d '{"name":"New User","email":"new_user@yandex.ru","password":"newPassword","roles":["ROLE_USER"]}' -H 'Content-Type: application/json' http://localhost:8080/users --user admin@gmail.com:adminPassword</code>
<pre>HTTP/1.1 201 Created
{"id":4,
"name":"New User",
"email":"new_user@yandex.ru",
"registered":"2020-01-02T18:42:25.834+0000",
"roles":["ROLE_USER"]}</pre>

<h3>Update user:</h3>
<code>curl -i -s -X PUT -d '{"name":"Edited Name","email":"editedemail@yandex.ru","password":"editedPassword","roles":["ROLE_ADMIN"]}' -H 'Content-Type: application/json' http://localhost:8080/users/2 --user admin@gmail.com:adminPassword</code>
<pre>HTTP/1.1 204 No Content</pre>

<h3>Get user:</h3>
<code>curl -i -s -X GET http://localhost:8080/users/2 --user admin@gmail.com:adminPassword</code>

<pre>HTTP/1.1 200 OK
{"id":2,
"name":"User",
"email":"user@yandex.ru",
"registered":"2020-01-02T19:23:41.496+0000",
"roles":["ROLE_USER"]}</pre>

<h3>Delete user:</h3>
<code>curl -i -s -X DELETE http://localhost:8080/users/2 --user admin@gmail.com:adminPassword</code>
<pre>HTTP/1.1 204 No Content</pre>

<h2>Restaurant administration:</h2>

<h3>Add lunch menu for restaurant:</h3>
<code>curl -i -s -X POST -d '[{"name":"Cobb Salad", "price":756},{"name":"Fluffy Pancakes", "price":50},{"name":"Broiled Grapefruit", "price":380},{"name":"Eggnog French Toast", "price":395},{"name":"Spicy Grilled Cheese Sandwich", "price":998}]' -H 'Content-Type: application/json' http://localhost:8080/restaurants/3/menu --user admin@gmail.com:adminPassword</code>

<pre>HTTP/1.1 201 Created
{"restaurant":
{"id":3,"name":"Asador Restaurant"},
"dishes":
[{"name":"Cobb Salad","price":756},
{"name":"Fluffy Pancakes","price":50},
{"name":"Broiled Grapefruit","price":380},
{"name":"Eggnog French Toast","price":395},
{"name":"Spicy Grilled Cheese Sandwich","price":998}]}</pre>

<h3>Get Restaurant:</h3>
<code>curl -i -s -X GET http://localhost:8080/restaurants/1 --user admin@gmail.com:adminPassword</code>
<pre>HTTP/1.1 200 OK
{"id":1, "name":"Mirazur Restaurant"}</pre>

<h3>Add Restaurant:</h3>
<code>curl -i -s -X POST -d '{"name":"Geranium Restaurant"}' -H 'Content-Type: application/json' http://localhost:8080/restaurants --user admin@gmail.com:adminPassword</code>
<pre>HTTP/1.1 201 Created
{"id":4,"name":"Geranium Restaurant"}</pre>

<h3>Edit Restaurant:</h3>
<code>curl -i -s -X PUT -d '{"name":"New Noma Restaurant"}' -H 'Content-Type: application/json' http://localhost:8080/restaurants/2 --user admin@gmail.com:adminPassword</code>
<pre>HTTP/1.1 204 No Content</pre>

<h3>Delete Restaurant:</h3>
<code>curl -i -s -X DELETE http://localhost:8080/restaurants/1 --user admin@gmail.com:adminPassword</code>
<pre>HTTP/1.1 204 No Content</pre>