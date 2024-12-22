import java.sql.Connection; //import Connection
import java.sql.PreparedStatement; //import PreparedStatement
import java.sql.ResultSet; //import Resultset
import java.sql.SQLException; //import SQL Exception
import java.util.ArrayDeque; //import ArrayDeque
import java.util.Queue; //import Queue
import java.util.Scanner; //Import Scanner

public class RakitKeyboard implements Interface{ //Superclass dengan mengimplementasikan interface

    //objek untuk Connection dan PreparedStatement
    static Connection con;
    static PreparedStatement preparedstm;

    //Deklarasi id_project dan project_date yang nanti digunakan di banyak method
    protected String id_project;
    private String project_date;

    //total_price sebagai varible untuk menyimpan total harga untuk project yang dibuat
    private int total_price;

    //variabel untuk mengecek apakah project telah ditambahkan
    public static boolean isAddProject = false;

    //objek untuk kelas Switch, Keycaps, dan Barebone
    Switch switchobj;
    Keycaps keycaps;
    Barebone barebone;

    //Collection FrameWork yaitu queue untuk menyimpan data sebelum dimasukkan ke database
    Queue <String> idProject = new ArrayDeque<>();
    Queue <String> projectDate = new ArrayDeque<>();
    Queue <String> projectStatus = new ArrayDeque<>();

    //constructor kelas RakitKeyboard
    public RakitKeyboard (String id_project){
        this.id_project = id_project;
    }

    //method untuk memriksa apakah nama untuk project baru sama dengan project yang ada di database
    public boolean isThereProjectId (String id_project){
        String sql = "SELECT 1 FROM rakit_keyboard WHERE id_project = ?";

        //try untuk mengecek data di database
        try {
            con = Koneksi.getConnection(); 
            preparedstm = con.prepareStatement(sql);

            preparedstm.setString(1, id_project);

            try (ResultSet resultSet = preparedstm.executeQuery()){

                return resultSet.next();
            }
        }

        //catch berisi exception handling saat berinteraksi dengan database
        catch (SQLException e){
            e.printStackTrace();
            return true;
        }
    }

    //method untuk memasukkan inputan ke dalam queue untuk disimpan sementara
    public void tempProjectData (Scanner input){

        //percabangan if dengan kondisi ketika ada project dengan naam yang sama di database
        if (isThereProjectId(id_project)){
            System.out.println("\nProject ID Sudah Digunakan");
        }

        //percabangan else ketika kondisi if tidak terpenuhi
        else{
            System.out.print("Project Date (date/month/year): ");
            project_date = input.nextLine();
            String date_project = project_date.toUpperCase(); //string manipulation

            idProject.add(id_project);
            projectDate.add(date_project);
            projectStatus.add("In Progress");

            isAddProject = true;
        }
    }

    //method implementasi method dari interface untuk menambahkan data project ke database
    @Override
    public void addData (){
        
        total_price(); //memanggil method untuk menghitung total harga

        //try untuk memasukkan data ke database
        try {
            con = Koneksi.getConnection();

            //percabangan if ketika koneksi berhasil
            if (con != null) {
                String sql = "INSERT INTO rakit_keyboard (id_project, project_date, total_price, project_status) VALUES (?,?,?,?)";
                preparedstm = con.prepareStatement(sql);

                preparedstm.setString(1, idProject.peek());
                preparedstm.setString(2, projectDate.peek());
                preparedstm.setInt(3, total_price);
                preparedstm.setString(4, projectStatus.peek());

                int row = preparedstm.executeUpdate();

                if(row > 0){
                    System.out.println("\nProject Berhasil Ditambahkan");
                    
                    deleteTempData(); //memanggil method untuk menghapus data yang disimpan sementara
                }
            }

            //pecabangan else ketika kondisi if tidak terpenuhi
            else {
                System.out.println("\nProject Gagal Ditambahkan");
            }
        }

        //catch berisi exception handling saat berinterasksi dengan database
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method implementasi method dari interface untuk menghapus data yang ada di dalam queue
    @Override
    public void deleteTempData (){
        //menghapus data yang ada di dalam queue
        idProject.clear(); 
        projectDate.clear(); 
        projectStatus.clear(); 
    }

    //method implementasi method dari interface untuk menghitung total harga dari project
    @Override
    public void total_price (){

        int switchPrice = Switch.getSwitchTotalPrice(); //mengambil harga dari kelas Switch
        int keycapsPrice = Keycaps.getKeycapsPrice(); //menagmbil harga dari kelas keycaps
        int barebonePrice = Barebone.getBarebonePrice(); //mengambil harga dari kelas Barebone

        total_price = switchPrice + keycapsPrice + barebonePrice; //perhitungan matematika untuk menghitung total harga
    }

    //method untuk menampilkan daftar project yang ada di database
    public void projectList (){

        //try untuk menampilkan data dari database
        try {
            con = Koneksi.getConnection();
    
            //percabangan if ketika koneksi berhasil
            if (con != null) {
                String sql = "SELECT * FROM rakit_keyboard";
                preparedstm = con.prepareStatement(sql);
    
                try (ResultSet resultSet = preparedstm.executeQuery()) {

                    //perulangan untuk menampilkan daftar project
                    while (resultSet.next()) {
                        System.out.println("==================================================");
                        System.out.println("ID Project     : " + resultSet.getString("id_project"));
                        System.out.println("Project Date   : " + resultSet.getString("project_date"));
                        System.out.println("Total Price    : " + resultSet.getInt("total_price"));
                        System.out.println("Project Status : " + resultSet.getString("project_status"));
                        System.out.println("==================================================");
                    }
                }
            }
    
            //percabanagn else ketika kondisi if tidak terpenuhi
            else {
                System.out.println("Koneksi gagal");
            }
        }
    
        //catch berisi exception handling saat berinteraksi dengan database
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method untuk menampilkan detail project dari project yang dipilih dari daftar project
    public void detailProject (Scanner input){

        System.out.println("\nDetail Project");
        System.out.print("Masukkan ID Project (0 = Batal): ");
        id_project = input.nextLine();

        switchobj = new Switch(id_project); //objek dari switch
        keycaps = new Keycaps (id_project); //objek dari keycaps
        barebone = new Barebone(id_project); //objek dari barebone

        //percabangan if ketika ada project dengan nama yang sama
        if(isThereProjectId(id_project) && !id_project.equals("0")){

            //try untuk menampilkan data detail project dari database
            try {
                con = Koneksi.getConnection();
        
                //percabanagn if ketika koneksi berhasil
                if (con != null) {
                    String sql = "SELECT * FROM rakit_keyboard WHERE id_project = ?";
                    preparedstm = con.prepareStatement(sql);
                    preparedstm.setString(1, id_project);
        
                    try (ResultSet resultSet = preparedstm.executeQuery()) {
                        System.out.println("\nDetail Project");

                        if (resultSet.next()) {
                            System.out.println("ID Project:     " + resultSet.getString("id_project"));
                            System.out.println("Project Date:   " + resultSet.getString("project_date"));
                            System.out.println("Total Price:    " + resultSet.getInt("total_price"));
                            System.out.println("Project Status: " + resultSet.getString("project_status"));
                        }
                    }
                }
        
                //percabangan else ketika kondisi if tidak memenuhi
                else {
                    System.out.println("Koneksi gagal");
                }
            }
        
            //catch berisi dengan exception handling saat berinteraksi dengan database
            catch (SQLException e) {
                e.printStackTrace();
            }
            
            switchobj.detailProjectSwitch(); //untuk menampilkan detail dari switch
            keycaps.detailProjectKeycaps(); //untuk menampilkan detail dari keycaps
            barebone.detailProjectBarebone(); //untuk menampilkan detail dari barebone
        }

        //percabangan else if ketika id project 0
        else if(id_project.equals("0")){
            System.out.println("Batal Menampilkan Detail Project");
        }

        //percabangan else ketika kondisi if tidak terpenuhi
        else if (!isThereProjectId(id_project)){
            System.out.println("Project Tidak Ditemukan");
        }
    }

    //method untuk mengupdate data project di database
    public void updateProject(Scanner input){
        System.out.println("\nUpdate Project");
        System.out.println("\n1. Update Status");
        System.out.println("2. Update Project Date");
        System.out.println("3. Update Switch");
        System.out.println("4. Update Keycaps");
        System.out.println("5. Update Barebone");
        System.out.println("0. Batal");

        System.out.print("\nPilihan Anda: ");
        String pilihan = input.nextLine();

        //switch case untuk memperbarui data sesuai dengan pilihan
        switch (pilihan) {
            case "1":
                System.out.println("\nUpdate Status");
                statusUpdate(input); //memanggil method untuk mengupdate status
                break;

            case "2":
                System.out.println("\nUpdate Tanggal");
                dateUpdate(input); //memanggil method untuk merubah tanggal
                break;
            
            case "3":
                System.out.println("\nUpdate Switch");
                System.out.print("\nProject ID (0 = Batal): ");
                id_project = input.nextLine(); 

                //percabangan if untuk memeriksa apakah id project ada di database dna id project bukan 0
                if(isThereProjectId(id_project) && !id_project.equals("0")){
                    switchobj = new Switch(id_project); //object dari switch
                    switchobj.switchUpdate(input); //memanggil methgod dari kelas switch untuk mengupdate data switch
                    break;
                }

                //percabanag else if ketika id project 0
                else if (id_project.equals("0")){
                    System.err.println("\nSwitch batal dirubah");
                    break;
                }

                //percabangan else if ketika id project tidak ditemukan
                else if (!isThereProjectId(id_project)){
                    System.out.println("\nID Project Tidak Ditemukan");
                    break;
                }

            case "4":
                System.out.println("\nUpdate Keycaps");
                System.out.print("\nProject ID (0 = Batal): ");
                id_project = input.nextLine();

                //percabangan if untuk memeriksa apakah id project ada di database dan id project bukan 0
                if(isThereProjectId(id_project) && !id_project.equals("0")){
                    keycaps = new Keycaps (id_project); //objek dari keycaps 
                    keycaps.keycapsUpdate(input); //memanggil methgod dari kelas keycaps untuk mengupdate data keycaps
                    break;
                }

                //percabanag else if ketika id project 0
                else if (id_project.equals("0")){
                    System.err.println("\nKeycaps batal dirubah");
                    break;
                }

                //percabangan else if ketika id project tidak ditemukan
                else if (!isThereProjectId(id_project)) {
                    System.out.println("\nID Project Tidak Ditemukan");
                    break;
                }

            case "5":
                System.out.println("\nUpdate Barbone");
                System.out.print("\nProject ID (0 = Batal): ");
                id_project = input.nextLine();

                //percabangan if untuk memeriksa apakah id project ada di database dan id project bukan 0
                if(isThereProjectId(id_project) && !id_project.equals("0")){
                    barebone = new Barebone(id_project); //objek dair barebone
                    barebone.bareboneUpdate(input); //memanggil methgod dari kelas barebone untuk mengupdate data barebone
                    break;
                }

                //percabanag else if ketika id project 0
                else if (id_project.equals("0")){
                    System.err.println("\nBarebone batal dirubah");
                    break;
                }

                //percabangan else if ketika id project tidak ditemukan
                else if (!isThereProjectId(id_project)){
                    System.out.println("\nID Project Tidak Ditemukan");
                    break;
                }

            case "0":
                System.out.println("\nBatal Memperbarui Data");
                break;

            default:
                break;
        }
    }

    //method untuk mengupdate status dari project
    public void statusUpdate (Scanner input){
        System.out.print("\nProject ID (0 = Batal): ");
        id_project = input.nextLine();

        con = Koneksi.getConnection();

        //percabanagn if ketika id project ditemukan dan id project bukan 0
        if(isThereProjectId(id_project) && !id_project.equals("0")){
            //try untuk menampilkan status dari project yang dipilih
            try {
            
                String sql = "SELECT * FROM rakit_keyboard WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);

                try (ResultSet resultset = preparedstm.executeQuery()){
                    if(resultset.next()){
                        System.out.println("\nProject ID       : " + resultset.getString("id_project"));
                        System.out.println("Project Status : " + resultset.getString("project_status"));
                    }
                }
            }

            catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        //percabanagn else if ketika id project 0
        else if(id_project.equals("0")){
            System.out.println("\nStatus Batal Diperbarui");
        }

        //percabangan else ketika id project tidak ada
        else if (!isThereProjectId(id_project)){
            System.out.println("\nID Project tidak ditemukan");
        }

        if(isThereProjectId(id_project)){
            System.out.println("\n1. Complete");
            System.out.println("0. Batal");

            System.out.print("\nUpdate Status: ");
            String status = input.nextLine();

            //percabanagn if ketika dipilih 1
            if(status.equals("1")){
                String sql = "UPDATE rakit_keyboard SET project_status = ? WHERE id_project = ?";

                //try untuk memperbarui status project
                try {

                    con = Koneksi.getConnection(); 
                    preparedstm = con.prepareStatement(sql);

                    preparedstm.setString(1, "Complete");
                    preparedstm.setString(2, id_project);

                    int rowUpdate = preparedstm.executeUpdate();

                    //percabangan if ketika status berhasil diupdate
                    if (rowUpdate > 0){
                        System.out.println("\nStatus Berhasil Diupdate");
                    }

                    //percabangan else ketika status gagal diupdate
                    else {
                        System.out.println("\nStatus Gagal Diupate");
                    }
                }

                //catch berisi exception handling saat berinteraksi dengan database
                catch (SQLException e){
                    e.printStackTrace();
                }
            }

            //percabanagn else if ketika diinput 0
            else if (status.equals("0")){
                System.out.println("\nStatus batal diperbarui");
            }
        }
    }

    //method untuk memperbarui tanggal pelaksaan project
    public void dateUpdate (Scanner input){
        System.out.print("\nProject ID (0 = Batal): ");
        id_project = input.nextLine();

        con = Koneksi.getConnection();

        //percabangan if ketika id project ditemukan dan id project buka 0
        if(isThereProjectId(id_project) && !id_project.equals("0")){
            
            //try untuk mengambil data project
            try {
            
                String sql = "SELECT * FROM rakit_keyboard WHERE id_project = ?";
                preparedstm = con.prepareStatement(sql);
                preparedstm.setString(1, id_project);

                try (ResultSet resultset = preparedstm.executeQuery()){
                    if(resultset.next()){
                        System.out.println("\nProject ID     : " + resultset.getString("id_project"));
                        System.out.println("Project Date : " + resultset.getString("project_date"));
                    }
                }
            }

            //cath berisi exception handling saat berinteraksi dengan database
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //percabangan else if ketika id project 0
        else if(id_project.equals("0")){
            System.out.println("\nTanggal Batal Diperbarui");
        }

        //percabanagn else if ketika id project tidak ditemukan
        else if (!isThereProjectId(id_project)){
            System.out.println("\nId Project Tidak Ditemukan");
        }

        //percabanagn if unutuk memeriksa apakah ada id project
        if(isThereProjectId(id_project)){
            System.out.print("\nTanggal Baru (0 = Batal): ");
            String tanggal = input.nextLine();
            String date = tanggal.toUpperCase(); //string manipulation

            //percabanagn if ketika pilihan adalah 1
            if(!tanggal.equals("0")){
                String sql = "UPDATE rakit_keyboard SET project_date = ? WHERE id_project = ?";

                //try untuk memperbarui tanggal project
                try {

                    con = Koneksi.getConnection(); 
                    preparedstm = con.prepareStatement(sql);

                    preparedstm.setString(1, date);
                    preparedstm.setString(2, id_project);

                    int rowUpdate = preparedstm.executeUpdate();

                    //percabangan if ketika tanggal berhasil diperbarui
                    if (rowUpdate > 0){
                        System.out.println("\nTanggal Berhasil Diupdate");
                    }

                    //percabangan else ketika tanggal gagal diperbarui
                    else {
                        System.out.println("\nTanggal Gagal Diupate");
                    }
                }

                //catch berisi exception handling saat berinteraksi dengan database
                catch (SQLException e){
                    e.printStackTrace();
                }
            }

            //percabangan else if ketika pilihan adalah 0
            else {
                System.out.println("\nTanggal Batal Diperbarui");
            }
        }
    }

    //method untuk menghapus project dari database
    public void deleteProject (Scanner input){

        System.out.print("\nID Project (0 = Batal): ");
        id_project = input.nextLine();

        //percabanag if ketika project ditemukan dan id project bukan 0
        if(isThereProjectId(id_project) && !id_project.equals("0")){
            switchobj = new Switch(id_project);
            keycaps = new Keycaps (id_project);
            barebone = new Barebone(id_project);

            //try untuk menghapus data dari database
            try {
                con = Koneksi.getConnection();

                if(con != null){
                    String sql = "DELETE FROM rakit_keyboard WHERE id_project = ?";
                    preparedstm = con.prepareStatement(sql);
                    preparedstm.setString(1, id_project);

                    int rowDelete = preparedstm.executeUpdate();

                    //percabanagn if ketika project berhasil dihapus
                    if(rowDelete > 0){
                        System.out.println("\nProject Berhasil Dihapus");

                        switchobj.deleteSwitch(); //memanggil methgod dari kelas switch untuk menghapus data switch
                        keycaps.deleteKeycaps(); //memanggil methgod dari kelas keycaps untuk mengupdate data keycaps
                        barebone.deleteBarebone(); //memanggil methgod dari kelas barebone untuk mengupdate data barebone
                    }

                    //percabangan else ketika project gagal dihapus
                    else{
                        System.out.println("\nProject Gagal Dihapus");
                    }
                }
            }

            //catch berisi exception handling saat berinteraksi dengan database
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        //percabanagn else if ketika project tidak ditemukan
        else if (!isThereProjectId(id_project)){
            System.out.println("\nProject Tidak Ditemukan");
        }

        //percabanagn else if ketika id project 0
        else if (id_project.equals("0")){
            System.out.println("\nBatal Mengahpus Project");
        }
    }
}