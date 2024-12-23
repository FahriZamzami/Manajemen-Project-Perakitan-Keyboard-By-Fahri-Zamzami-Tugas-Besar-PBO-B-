import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Koneksi {
    public static boolean koneksiBerhasil = false;
    public static Connection con;
    public static Statement stm;

    private final static String url = "jdbc:mysql://localhost/tbpbo"; //url ke database MySQL
    private final static String username = "root";  //username database
    private final static String password = ""; //password database

    //constructor Untuk melakukan koneksi ke database
    public Koneksi(){
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
            stm = con.createStatement();
            koneksiBerhasil = true;
        }

        //exception ketika gagal terhubung ke database
        catch (Exception e){
            System.err.println("Gagal Terhubung ke Databse" + e.getMessage());
            koneksiBerhasil = false;
        }
    }

    //method untuk digunakan di kelas lain agar mendapatkan koneksi ke database
    public static Connection getConnection () {
        return con;
    }

    //method untuk mengecek apakah berhasil terhubung ke database
    public void checkConnection(){
        if(koneksiBerhasil){
            System.out.println("\nKoneksi Berhasil");
        }

        else {
            System.out.println("Koneksi Gagal");
        }
    }

    //method untuk menutup koneksi ke database
    public void closeConnection (){

        if (con != null){
            try{
                con.close();
                stm.close();

                if (RakitKeyboard.preparedstm != null){
                    RakitKeyboard.preparedstm.close();
                }

                if (Switch.preparedstm != null){
                    Switch.preparedstm.close();
                }

                if (Keycaps.preparedstm != null){
                    Keycaps.preparedstm.close();
                }

                if (Barebone.preparedstm != null){
                    Barebone.preparedstm.close();
                }

                System.out.println("==================================================");
                System.out.println("Berhasil Keluar");
                System.out.println("Menutup Koneksi Berhasil");
                System.out.println("==================================================");

                koneksiBerhasil = false;
            }

            //exeception ketika gagal menutup database
            catch (Exception e){
                System.err.println("Gagal Menutup Koneksi" + e.getMessage());
                koneksiBerhasil = true;
            }
        }
    }

}
