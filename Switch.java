import java.sql.Connection; //import Connection
import java.sql.PreparedStatement; //import PreparedStatement
import java.sql.ResultSet; //import Resultset
import java.sql.SQLException; //import SQL Exception
import java.util.ArrayDeque; //import ArrayDeque
import java.util.Queue; //import Queue
import java.util.Scanner; //Import Scanner

public class Switch extends RakitKeyboard { //Subclass dari kelas RakitKeyboard

    //objek Connection dan PreparedStatement
    static Connection con;
    static PreparedStatement preparedstm;

    //Deklarasi variabel untuk menyimpan data
    public String switch_type; 
    public String switch_brand;
    public int switch_total;
    public int per_switch_price; 
    public static  int switch_total_price;

    //variabel untuk menyimpan harga baru
    public int newHargaSwitch;
    public int newHargaKeycaps;
    public int newHargaBarebone;

    //variabel untuk mengecek apakah switch telah ditamabahkan
    public static boolean isAddSwitch = false;

    //deklarasi Collection framework yaitu queue untuk menyimpan data sementara
    Queue <String> idProject = new ArrayDeque<>();
    Queue <String> switchType = new ArrayDeque<>();
    Queue <String> switchBrand = new ArrayDeque<>();
    Queue <Integer> switchTotal = new ArrayDeque<>(); 
    Queue <Integer> perSwitchPrice = new ArrayDeque<>();
    Queue <Integer> switchTotalPrice = new ArrayDeque<>();

    //constructor kelas Switch
    public Switch (String id_project){
        super(id_project); //melakukan super ke super class
    }

    //method untuk memasukkan inputan ke dalam queue untuk disimpan sementara
    public void tempSwitchData (Scanner input){

        System.out.println("\nTambahkan Switch");

        System.out.print("\nSwitch Type (0 = Batal): ");
        switch_type = input.nextLine();
        String type_switch = switch_type.toUpperCase(); //string manipulation

        //percabangan if ketika type switch 0
        if(!type_switch.equals("0")){
            System.out.print("Switch Brand: ");
            switch_brand = input.nextLine();
            String brand_switch = switch_brand.toUpperCase(); //string manipulation

            System.out.print("Switch Total: ");
            switch_total = input.nextInt();
            input.nextLine();

            System.out.print("Per Switch Price: ");
            per_switch_price = input.nextInt();
            input.nextLine();

            total_price(); //memanggil method untuk menghitung harga total switch

            idProject.add(id_project);
            switchType.add(type_switch);
            switchBrand.add(brand_switch);
            switchTotal.add(switch_total);
            perSwitchPrice.add(per_switch_price);
            switchTotalPrice.add(switch_total_price);

            isAddSwitch = true;
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
                String sql = "INSERT INTO switch  (id_project, switch_type, switch_brand, switch_total, per_switch_price, switch_total_price) VALUES (?,?,?,?,?,?)";
                PreparedStatement preparedstm = con.prepareStatement(sql);

                preparedstm.setString(1, idProject.peek());
                preparedstm.setString(2, switchType.peek());
                preparedstm.setString(3, switchBrand.peek());
                preparedstm.setInt(4, switchTotal.peek());
                preparedstm.setInt(5, perSwitchPrice.peek());
                preparedstm.setInt(6, switchTotalPrice.peek());

                int row = preparedstm.executeUpdate();

                //percabanag if fketika data berhasil diupdate
                if(row > 0){
                    System.out.println("\nSwitch Berhasil Ditambahkan");
                    deleteTempData(); //memanggil method untuk menghapus data yang disimpan sementara
                }
            }

            //percabangan else ketika koneksi gagal
            else {
                System.out.println("\nSwitch Gagal Ditambahkan");
            }
        }

        //catch berisi exception handling ketika mengakses database
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method implementasi dari interface untuk menghapus data yang disimpan sementara
    @Override
    public void deleteTempData (){
        idProject.clear();
        switchType.clear();
        switchBrand.clear();
        switchTotal.clear();
        perSwitchPrice.clear();
        switchTotalPrice.clear();
    }

    //method implementasi dari interface untuk menghitung harga total switch
    @Override
    public void total_price(){
        switch_total_price = switch_total * per_switch_price; //perhitungan matematika untuk menghitung total harga switch
    }

    //getter untuk mengambil nilai dari total harga switch
    public static int getSwitchTotalPrice(){
        return switch_total_price;
    }

    //method untuk menampilkan detail dari switch
    public void detailProjectSwitch (){
    
        //try untuk mengambil data dari database
        try {
            con = Koneksi.getConnection();
    
            //percabanagn if ketika koneksi berhasil
            if (con != null) {
                String sql = "SELECT * FROM switch WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);
    
                try (ResultSet resultSet = preparedstm.executeQuery()) {
                    System.out.println("\nSwitch");

                    if (resultSet.next()) {
                        System.out.println("Switch type        : " + resultSet.getString("switch_type"));
                        System.out.println("Switch Brand       : " + resultSet.getString("switch_brand"));
                        System.out.println("Switch Total       : " + resultSet.getInt("switch_total"));
                        System.out.println("Per Switch Price   : " + resultSet.getInt("per_switch_price"));
                        System.out.println("Switch Total Price : " + resultSet.getInt("switch_total_price"));
                    }
                }
            }
    
            //percabangan else ketika koneksi gagal
            else {
                System.out.println("Koneksi gagal");
            }
        }
    
        //catct berisi exception handling ketika mengakses database
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //methd untuk mengupddate switch
    public void switchUpdate (Scanner input){

        detailProjectSwitch(); //memanggil method untuk menampilkan detail switch

        System.out.print("\nSwitch Type Baru: ");
        switch_type = input.nextLine();
        String type_switch = switch_type.toUpperCase(); //string manipulation

        System.out.print("Switch Brand Baru: ");
        switch_brand = input.nextLine();
        String brand_switch = switch_brand.toUpperCase(); //string manipulation

        System.out.print("Switch Total Baru: ");
        switch_total = input.nextInt();
        input.nextLine();

        System.out.print("Per Switch Price Baru: ");
        per_switch_price = input.nextInt();
        input.nextLine();

        total_price();

        //percabanagn if ketika type switch bukan 0
        if(!type_switch.equals("0")){
            String sql = "UPDATE switch SET switch_type = ?, switch_brand = ?, switch_total = ?, per_switch_price = ?, switch_total_price = ? WHERE id_project = ?";

            //try untuk mengupdate database
            try {

                con = Koneksi.getConnection(); 
                preparedstm = con.prepareStatement(sql);

                preparedstm.setString(1, type_switch);
                preparedstm.setString(2, brand_switch);
                preparedstm.setInt(3, switch_total);
                preparedstm.setInt(4, per_switch_price);
                preparedstm.setInt(5, switch_total_price);
                preparedstm.setString(6, id_project);

                int rowUpdate = preparedstm.executeUpdate();

                //percabanagn if ketika switch berhasil diupdate 
                if (rowUpdate > 0){
                    System.out.println("\nSwitch Berhasil Diupdate");
                    updateTotalHarga(); //memanggil method untuk mengupdate total harga
                }

                //percabangan else ketika switch gagal diupdate
                else {
                    System.out.println("\nSwitch Gagal Diupate");
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

    //method untuk menghapus switch
    public void deleteSwitch (){

        //try untuk menghapus switch
        try {
            con = Koneksi.getConnection();

            //percabangan if ketika koneksi berhasil
            if(con != null){
                String sql = "DELETE FROM switch WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);

                int rowDelete = preparedstm.executeUpdate();

                //percabnagn if ketika switch berhasil di hapus
                if(rowDelete > 0){
                    System.out.println("Switch Berhasil Dihapus");
                }

                //percabanagn else ketika switch gagal dihapus
                else{
                    System.out.println("Switch Gagal Dihapus");
                }
            }
        }

        //catch berisi exception handling saat mengakses database
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}