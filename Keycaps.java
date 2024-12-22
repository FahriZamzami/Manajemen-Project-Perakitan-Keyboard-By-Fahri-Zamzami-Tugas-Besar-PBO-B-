import java.sql.Connection; //import Connection
import java.sql.PreparedStatement; //import PreparedStatement
import java.sql.ResultSet; //import Resultset
import java.sql.SQLException; //import SQL Exception
import java.util.ArrayDeque; //import ArrayDeque
import java.util.Queue; //import Queue
import java.util.Scanner; //Import Scanner

public class Keycaps extends RakitKeyboard{ //Subclass dari kelas RakitKeyboard

    //objek Connection dan PreparedStatement
    static Connection con;
    static PreparedStatement preparedstm;

    //Deklarasi variabel untuk menyimpan data
    public String keycaps_material;
    public String keycaps_profile;
    public String keycaps_brand;
    public static int keycaps_price;

    //variabel untuk menyimpan harga baru
    public int newHargaSwitch;
    public int newHargaKeycaps;
    public int newHargaBarebone;

    //variabel untuk mengecek apakah keycaps telah ditamabahkan
    public static boolean isAddKeycaps = false;

    //deklarasi Collection framework yaitu queue untuk menyimpan data sementara
    Queue <String> idProject = new ArrayDeque<>();
    Queue <String> keycapsProfile = new ArrayDeque<>();
    Queue <String> keycapsMaterial = new ArrayDeque<>();
    Queue <String> keycapsBrand = new ArrayDeque<>();
    Queue <Integer> keycapsPrice = new ArrayDeque<>();

    //constructor kelas keycaps
    public Keycaps(String id_project){ 
        super(id_project); //melakukan super ke super class
    }

    //method untuk memasukkan inputan ke dalam queue untuk disimpan sementara
    public void tempKeycapsData (Scanner input){

        System.out.println("\nTambahkan Keycaps");

        System.out.print("\nKeycaps Profile (0 = Batal): ");
        keycaps_profile = input.nextLine();
        String profile_keycaps = keycaps_profile.toUpperCase(); //string manipulation

        //percabangan if ketika profile keycaps bukan 0
        if(!profile_keycaps.equals("0")){

            System.out.print("Keycaps Material: ");
            keycaps_material = input.nextLine();
            String material_keycaps = keycaps_material.toUpperCase(); //string manipulation

            System.out.print("Keycaps Brand: ");
            keycaps_brand = input.nextLine();
            String brand_keycaps = keycaps_brand.toUpperCase(); //string manipulation

            System.out.print("Keycaps Price: ");
            keycaps_price = input.nextInt();
            input.nextLine();

            idProject.add(id_project);
            keycapsProfile.add(profile_keycaps);
            keycapsMaterial.add(material_keycaps);
            keycapsBrand.add(brand_keycaps);
            keycapsPrice.add(keycaps_price);

            isAddKeycaps = true;
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
                String sql = "INSERT INTO keycaps (id_project, keycaps_profile, keycaps_material, keycaps_brand, keycaps_price)  VALUES (?,?,?,?,?)";
                PreparedStatement preparedstm = con.prepareStatement(sql);

                preparedstm.setString(1, idProject.peek());
                preparedstm.setString(2, keycapsProfile.peek());
                preparedstm.setString(3, keycapsMaterial.peek());
                preparedstm.setString(4, keycapsBrand.peek());
                preparedstm.setInt(5, keycapsPrice.peek());

                int row = preparedstm.executeUpdate();

                //percabanag if ketika data berhasil diupdate
                if(row > 0){
                    System.out.println("\nKeycaps Berhasil Ditambahkan");
                    deleteTempData(); //memanggil method untuk menghapus data yang disimpan sementara
                }
            }

            //percabangan else ketika koneksi gagal
            else {
                System.out.println("\nKeycaps Gagal Ditambahkan");
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
        keycapsProfile.clear();
        keycapsMaterial.clear();
        keycapsBrand.clear();
        keycapsPrice.clear();
    }

    //getter untuk mengambil nilai dari harga keycaps
    public static int getKeycapsPrice(){
        return keycaps_price;
    }

    //method untuk menampilkan detail dari keycaps
    public void detailProjectKeycaps (){
    
        //try untuk mengambil data dari database
        try {
            con = Koneksi.getConnection();
    
            //percabanagn if ketika koneksi berhasil
            if (con != null) {
                String sql = "SELECT * FROM keycaps WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);
    
                try (ResultSet resultSet = preparedstm.executeQuery()) {
                    System.out.println("\nKeycaps");

                    if (resultSet.next()) {
                        System.out.println("Keycaps Profile  : " + resultSet.getString("keycaps_profile"));
                        System.out.println("Keycaps Material : " + resultSet.getString("keycaps_material"));
                        System.out.println("Keycaps Brand    : " + resultSet.getString("keycaps_brand"));
                        System.out.println("Keycaps Price    : " + resultSet.getInt("keycaps_price"));
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

    //methd untuk mengupddate keycaps
    public void keycapsUpdate (Scanner input){

        detailProjectKeycaps(); //memanggil method untuk menampilkan detail keycaps

        System.out.print("\nKeycaps Profile Baru: ");
        keycaps_profile = input.nextLine();
        String profile_keycaps = keycaps_profile.toUpperCase(); //string manipulation

        System.out.print("Keycaps Material Baru: ");
        keycaps_material = input.nextLine();
        String material_keycaps = keycaps_material.toUpperCase(); //string manipulation

        System.out.print("Keycaps Brand Baru: ");
        keycaps_brand = input.nextLine();
        String brand_keycaps = keycaps_brand.toUpperCase(); //string manipulation

        System.out.print("Keycaps Price Baru: ");
        keycaps_price = input.nextInt();
        input.nextLine();

        //percabanagn if ketika profile_keycaps bukan 0
        if(!profile_keycaps.equals("0")){
            String sql = "UPDATE keycaps SET keycaps_profile = ?, keycaps_material = ?, keycaps_brand = ?, keycaps_price = ? WHERE id_project = ?";

            //try untuk mengupdate database
            try {

                con = Koneksi.getConnection(); 
                preparedstm = con.prepareStatement(sql);

                preparedstm.setString(1, profile_keycaps);
                preparedstm.setString(2, material_keycaps);
                preparedstm.setString(3, brand_keycaps);
                preparedstm.setInt(4, keycaps_price);
                preparedstm.setString(5, id_project);

                int rowUpdate = preparedstm.executeUpdate();

                //percabanagn if ketika keycaps berhasil diupdate
                if (rowUpdate > 0){
                    System.out.println("\nKeycaps Berhasil Diupdate");
                    updateTotalHarga(); //memanggil method untuk mengupdate total harga
                }

                //percabanagn else ketika keycaps gagal diupdate
                else {
                    System.out.println("\nKeycaps Gagal Diupate");
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

    //method untuk menghapus keycaps
    public void deleteKeycaps (){

        //try untuk menghapus keycaps
        try {
            con = Koneksi.getConnection();

            //percabangan if ketika koneksi berhasil
            if(con != null){
                String sql = "DELETE FROM keycaps WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);

                
                int rowDelete = preparedstm.executeUpdate();

                //percabnagn if ketika keycaps berhasil di hapus
                if(rowDelete > 0){
                    System.out.println("Keycaps Berhasil Dihapus");
                }

                //percabanagn else ketika keycaps gagal dihapus
                else{
                    System.out.println("keycaps Gagal Dihapus");
                }
            }
        }

        //catch berisi exception handling saat mengakses database
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}