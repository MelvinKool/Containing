/* 
sudo apt-get install libmysqlclient-dev
g++ -o my $(mysql_config --cflags) mysqlapi.cpp $(mysql_config --libs)
*/
#include <mysql.h>
#include <stdio.h>

int main(int argc, char* argv[]) {
   MYSQL *conn;
   MYSQL_RES *res;
   MYSQL_ROW row;
  /* Change me */
   char *server = "178.84.137.198";
   char *user = "containing";
   char *password = "e0d2c603414413df6b6d15dcb4f7c1fb";
   char *database = "containing";
   
   conn = mysql_init(NULL);
   
   /* Connect to database */
   if (!mysql_real_connect(conn, server,
         user, password, database, 0, NULL, 0)) {
      fprintf(stderr, "%s\n", mysql_error(conn));
      //exit(1);
   }

   /* send SQL query */
   if (mysql_query(conn, "show tables;")) {
      fprintf(stderr, "%s\n", mysql_error(conn));
      //exit(1);
   }

   res = mysql_use_result(conn);
   
   /* output table name */
   printf("MySQL Tables in mysql database:\n");
   while ((row = mysql_fetch_row(res)) != NULL)
      printf("%s \n", row[0]);

   /* close connection */
   mysql_free_result(res);
   mysql_close(conn);
  
  return 0;
}
