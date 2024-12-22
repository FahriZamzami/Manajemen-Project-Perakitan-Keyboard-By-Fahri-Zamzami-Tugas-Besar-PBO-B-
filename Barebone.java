import java.sql.Connection; //import Connection
import java.sql.PreparedStatement; //import PreparedStatement
import java.sql.ResultSet; //import Resultset
import java.sql.SQLException; //import SQL Exception
import java.util.ArrayDeque; //import ArrayDeque
import java.util.Queue; //import Queue
import java.util.Scanner; //Import Scanner

public class Barebone extends RakitKeyboard{ //Subclass dari kelas RakitKeyboard

    //objek Connection dan PreparedStatement
    static Connection con;
    static PreparedStatement preparedstm;

    //Deklarasi variabel untuk menyimpan data
    public String barebone_layout;
    public String barebone_brand;
    public static int barebone_price;

    //variabel untuk menyimpan harga baru
    public int newHargaSwitch;
    public int newHargaKeycaps;
    public int newHargaBarebone;

    //variabel untuk mengecek apakah keycaps telah ditamabahkan
    public static boolean isAddBarebone = false;

     //deklarasi Collection framework yaitu queue untuk menyimpan data sementara
    Queue <String> idProject = new ArrayDeque<>();
    Queue <String> bareboneLayout = new ArrayDeque<>();
    Queue <String> bareboneBrand = new ArrayDeque<>();
    Queue <Integer> barebonePrice = new ArrayDeque<>();

     //constructor kelas barebone
    public Barebone (String id_project){
        super(id_project); //melakukan super ke super class
    }
    
     //method untuk memasukkan inputan ke dalam queue untuk disimpan sementara
    public void tempBareboneData (Scanner input){

        System.out.println("\nTambahkan Barebone");

        System.out.print("\nBarebone Layout (0 = Batal): ");
        barebone_layout = input.nextLine();
        String layout_barebone = barebone_layout.toUpperCase(); //string manipulation

        //percabangan if ketika layout barebone bukan 0
        if(!layout_barebone.equals("0")){

            System.out.print("Barebone Brand: ");
            barebone_brand = input.nextLine();
            String brand_barebone = barebone_brand.toUpperCase(); //string manipulation

            System.out.print("Barebone Price: ");
            barebone_price = input.nextInt();
            input.nextLine();

            idProject.add(id_project);
            bareboneLayout.add(layout_barebone);
            bareboneBrand.add(brand_barebone);
            barebonePrice.add(barebone_price);

            isAddBarebone = true;
        }
    }

    //method implementasi dari interface untuk menambahkan data ke database
    @Override
    public void addData(){

        //try untuk menambahkan data ke database
        try {
            Connection con = Koneksi.getConnection();

            //percabangan if ketika koneksi berhasil
            if (con != null) {
                String sql = "INSERT INTO barebone (id_project, barebone_layout, barebone_brand, barebone_price) VALUES (?,?,?,?)";
                PreparedStatement preparedstm = con.prepareStatement(sql);

                preparedstm.setString(1, idProject.peek());
                preparedstm.setString(2, bareboneLayout.peek());
                preparedstm.setString(3, bareboneBrand.peek());
                preparedstm.setInt(4, barebonePrice.peek());

                int row = preparedstm.executeUpdate();

                //percabanag if ketika data berhasil diupdate
                if(row > 0){
                    System.out.println("\nBarebone Berhasil Ditambahkan");
                    deleteTempData(); //memanggil method untuk menghapus data yang disimpan sementara
                }
            }

            //percabangan else ketika koneksi gagal
            else {
                System.out.println("\nBarebone Gagal Ditambahkan");
            }
        }

        //catch berisi exception handling ketika mengakses database
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method implementasi dari interface untuk menghapus data  yang disimpan sementara
    @Override
    public void deleteTempData (){
        idProject.clear();
        bareboneLayout.clear();
        bareboneBrand.clear();
        barebonePrice.clear();
    }

    //getter untuk mengambil nilai dari harga barebone
    public static int getBarebonePrice(){
        return barebone_price;
    }

    //method untuk menampilkan detail dari keycaps
    public void detailProjectBarebone (){
    
        //try untuk mengambil data dari database
        try {
            con = Koneksi.getConnection();
    
            //percabanagn if ketika koneksi berhasil
            if (con != null) {
                String sql = "SELECT * FROM barebone WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);
    
                try (ResultSet resultSet = preparedstm.executeQuery()) {
                    System.out.println("\nBarebone");

                    while (resultSet.next()) {
                        System.out.println("Barebone Layout : " + resultSet.getString("barebone_layout"));
                        System.out.println("Barebone Brand  : " + resultSet.getString("barebone_brand"));
                        System.out.println("Barebone Price  : " + resultSet.getInt("barebone_price"));
                    }
                }
            }
    
            //percabangan else ketika koneksi gagal
            else {
                System.out.println("Koneksi gagal");
            }
        }
    
        //catch berisi exception handling ketika mengakses database
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //methd untuk mengupddate barebone
    public void bareboneUpdate (Scanner input){

        detailProjectBarebone(); //memanggil method untuk menampilkan detail barebone

        System.out.print("\nBarebone Layout Baru: ");
        barebone_layout = input.nextLine();
        String layout_barebone = barebone_layout.toUpperCase(); //string manipulation

        System.out.print("Barebone Brand Baru: ");
        barebone_brand = input.nextLine();
        String brand_barebone = barebone_brand.toUpperCase(); //string manipulation

        System.out.print("Barebone Price Baru: ");
        barebone_price = input.nextInt();
        input.nextLine();

         //percabanagn if ketika profile_keycaps bukan 0
        if(!layout_barebone.equals("0")){
            String sql = "UPDATE barebone SET barebone_layout = ?, barebone_brand = ?, barebone_price = ? WHERE id_project = ?";

            //try untuk mengupdate database
            try {

                con = Koneksi.getConnection(); 
                preparedstm = con.prepareStatement(sql);

                preparedstm.setString(1, layout_barebone);
                preparedstm.setString(2, brand_barebone);
                preparedstm.setInt(3, barebone_price);
                preparedstm.setString(4, id_project);

                int rowUpdate = preparedstm.executeUpdate();

                //percabanagn if ketika keycaps berhasil diupdate
                if (rowUpdate > 0){
                    System.out.println("\nBarebone Berhasil Diupdate");
                    updateTotalHarga(); //memanggil method untuk mengupdate total harga
                }

                //percabanagn else ketika keycaps gagal diupdate
                else {
                    System.out.println("\nBarebone Gagal Diupate");
                }
            }

            //catch berisi exception handling saat mengakses database
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    //method untuk mengupdate total harga project
    public void updateTotalHarga (){

        con = Koneksi.getConnection();

        //percabanagn if ketika koneksi berhasil
        if(con != null){

            //try untuk mengambil data harga switch
            try{
                String sql = "SELECT * FROM switch  WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);

                try (ResultSet resultset = preparedstm.executeQuery()){
                    if(resultset.next()){
                        newHargaSwitch = resultset.getInt("switch_total_price");
                    }
                }
            }

            //catch berisi exception handling saat mengakses database
            catch (SQLException e){
                e.printStackTrace();
            }

            //try untuk mengambil data harga keycaps
            try{
                String sql = "SELECT * FROM keycaps  WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);

                try (ResultSet resultset = preparedstm.executeQuery()){
                    if(resultset.next()){
                        newHargaKeycaps = resultset.getInt("keycaps_price");
                    }
                }
            }

            //catch berisi exception handling saat mengakses database
            catch (SQLException e){
                e.printStackTrace();
            }

            //try untuk mengambil data harga barebone
            try{
                String sql = "SELECT * FROM barebone  WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);

                try (ResultSet resultset = preparedstm.executeQuery()){
                    if(resultset.next()){
                        newHargaBarebone = resultset.getInt("barebone_price");
                    }
                }
            }

            //catch berisi exception handling saat mengakses database
            catch (SQLException e){
                e.printStackTrace();
            }

            int newTotalHarga = newHargaBarebone + newHargaKeycaps + newHargaSwitch; //perhitungan matematika untuk menghitung total harga baru

            //try untuk mengupdate harga total project
            try{
                String sql = "UPDATE rakit_keyboard SET total_price = ?  WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);

                preparedstm.setInt(1, newTotalHarga);
                preparedstm.setString(2, id_project);

                int rowUpdate = preparedstm.executeUpdate();

                //percabanagn if ketika data berhasil di update
                if (rowUpdate > 0){
                    System.out.println("Total Harga Berhasil Diperbarui");
                }
            }

            //catch berisi exception handling saat mengakses database
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    //method untuk menghapus barebone
    public void deleteBarebone (){

        //try untuk menghapus barebone
        try {
            con = Koneksi.getConnection();

            //percabangan if ketika koneksi berhasil
            if(con != null){
                String sql = "DELETE FROM barebone WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);

                int rowDelete = preparedstm.executeUpdate();

                //percabnagn if ketika keycaps berhasil di hapus
                if(rowDelete > 0){
                    System.out.println("Barebone Berhasil Dihapus");
                }

                //percabanagn else ketika keycaps gagal dihapus
                else{
                    System.out.println("Barebone Gagal Dihapus");
                }
            }
        }

        //catch berisi exception handling saat mengakses database
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}