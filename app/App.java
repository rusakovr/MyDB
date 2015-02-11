package app;
import java.sql.*;
import java.io.PrintStream;
import java.util.Scanner;

public class App {
public static void main(String[] args) throws Exception{
        String base_addr = "jdbc:firebirdsql://localhost/C://db/book_store2.fdb";
	Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
        PrintStream printStream = new PrintStream(System.out, true, "cp866");
	Scanner sc = new Scanner(System.in);
	Connection conn=null;
        //Создаём подключение к базе данных
	conn = DriverManager.getConnection(base_addr,"SYSDBA", "masterkey");
	if (conn==null)
	{
            System.err.println("Could not connect to database");
	} 
        // Класс для выполнения SQL запросов
        Statement st = conn.createStatement();
        ResultSet rs;
        int t=0;
        while(t!=4)
        {
       
                printStream.println("1. Show genres");
                printStream.println("2. Add genres");
                printStream.println("3. Execute procedure");
                printStream.println("4. Exit");
                if(sc.hasNextInt())
                t=sc.nextInt();
                else
                {
                    sc.next();
                    continue;
                }    
            
            if(t==1)
            { 
                //Выполняем SQL запрос.
          
           rs = st.executeQuery("select * FROM  genres;");
           
           

                // Выводим результат.
                while(rs.next())
                {
                        System.out.println();
                        printStream.println(rs.getString(1)+ "." + rs.getString(2) );
                }
                printStream.println(" ");
                continue;
            }
            if(t==2)
            { 
                printStream.println("Insert genre_id");
                int kol;
                        if(sc.hasNextInt())
                        {
                            kol=sc.nextInt();
                            printStream.println("Insert genre_name");
                        }
                        else 
                        {
                            sc.next();
                            printStream.println("Wrong value, insert  genre again");
                            continue;
                        }
                    String tstr=sc.next();
                    tstr="INSERT INTO genres (genre_id,name) VALUES ("+String.valueOf(kol)+",'"+tstr+"')";
                    try
                    {
                        st.executeUpdate(tstr);
                    }
                    catch(org.firebirdsql.jdbc.FBSQLException e)
                    {
                        printStream.println("Wrong genre_id, genre was not inserted");
                        continue;
                    }           
                continue;
            }
            if(t==3)
            { 
                printStream.println("Insert   order id");
                int t1=0;
                    if(sc.hasNextInt())
                    {
                        t1=sc.nextInt();
                        rs = st.executeQuery("select order_id FROM  orders where order_id=" + Integer.toString(t1) +  ";");
                        if (!rs.isBeforeFirst() ) {    
                            printStream.println("This order is not exist");
                            continue;
                        } 
                    }
                    else
                    {
                        sc.next();
                        printStream.println("Wrong value");
                        continue;
                    }    
                CallableStatement call_stmt=conn.prepareCall("{call lab5(?)}");
                call_stmt.setInt(1, t1);
                call_stmt.registerOutParameter(1, Types.INTEGER);
                call_stmt.execute();
                float result = call_stmt.getFloat(1);
                System.out.println("Cost: "+result);           
            }   
        }
	st.close();
	conn.close();
    }
}
