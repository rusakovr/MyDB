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
	conn = DriverManager.getConnection(base_addr,"SYSDBA", "masterkey");
	if (conn==null)
	{
            System.err.println("Could not connect to database");
	} 
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
                rs = st.executeQuery("select * FROM  genres order by genre_id;");   
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
                int kol=0,cnt=0;
                    printStream.println("Insert genre_name");
                    
                    String tstr=sc.next();
                    rs = st.executeQuery("select  count(genre_id) FROM  genres where name ='"  + tstr +  "';");
                    while(rs.next())
                    {
                        cnt=rs.getInt(1);
                    }
                    if(cnt!=0)
                    {
                        printStream.println("This genre  already exist");
                        continue;
                    }
                    rs = st.executeQuery("select first 1 genre_id FROM  genres order  by genre_id desc ;");
                     while(rs.next())
                {
                        kol=rs.getInt(1);
                }
                    kol++;
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
                printStream.println("Insert   genre_id");
                int t1=0;
                if(sc.hasNextInt())
                {
                    t1=sc.nextInt();
                    rs = st.executeQuery("select genre_id FROM  genres where genre_id=" + Integer.toString(t1) +  ";");
                    if (!rs.isBeforeFirst() ) {    
                        printStream.println("This genre is not exist");
                        continue;
                    } 
                }
                else
                {
                    sc.next();
                    printStream.println("Wrong value");
                    continue;
                }  
                Date s1,s2;
                String tstr1=sc.next();
                try
                {
                    s1=Date.valueOf(tstr1);
                }
                catch(java.lang.IllegalArgumentException e)
                {
                    printStream.println("Wrong date");
                    continue;
                }
                String tstr2=sc.next(); 
                try
                {
                    s2=Date.valueOf(tstr2);
                }
                catch(java.lang.IllegalArgumentException e)
                {
                    printStream.println("Wrong date");
                    continue;
                }
                CallableStatement call_stmt=conn.prepareCall("{call lab4_1(?,?,?)}");
                call_stmt.setInt(1, t1);
                call_stmt.setDate(2, s1);
                call_stmt.setDate(3, s2);
                call_stmt.registerOutParameter(1, Types.FLOAT);
                call_stmt.execute();
                float result = call_stmt.getFloat(1);
                System.out.println("Cost: "+result);           
            }   
        }
	st.close();
	conn.close();
    }
}
