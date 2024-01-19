This description is mostly a placeholder for when the project will be completed.

### The project
This is a simple project, made in **Java** using **Swing** and **JDBC** with a **MySQL** database.  
It's meant to be a small run-time application to select, insert, update and delete rows of at least one table of the complete database, to show knowledge of SQL and its driver.  

### The choices
Let's be honest: most of the choices... are not good choices. But are required by project guidelines.  

I chose to use Swing because the project's scope was really small, even if I find Swing somewhat painful to work with.  
To make the UI a bit more bearable I used Flatlaf look and feel.  
Of course I had to access to the database using raw parameterized SQL queries and JDBC, but I would've used better options if that was possible (maybe a lightweight ORM too).  

### On being future-proof
I overstepped the project requirements by implementing data validation and error management, and I also put effort to make the code readable and tidy.
I also worked on two tables instead than one, and I made sure more tables could easily be added in any future update.