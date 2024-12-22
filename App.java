import java.util.Date;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        String id_project;

        Koneksi koneksi = new Koneksi();
        koneksi.checkConnection(); //memanggil method dari kelas Koneksi untuk memeriksa koneksi

        RakitKeyboard rakitKeyboard;
        Switch switchobj;
        Keycaps keycaps;
        Barebone barebone;
    
        Scanner input = new Scanner(System.in);

        //perulangan agar program bisa terus dijalankan
        while (true){

            System.out.println("==================================================");
            System.out.println("  Manajemen Project Perakitan Keyboard");
            Date date = new Date(); //membuat objek date
            String str = String.format("Current Date/Time : %tc", date); //Date
            System.out.printf(str);
            System.out.println("\n==================================================");

            //perulangan agar program bisa terus dijalankan
            while (true){

                System.out.println("==================================================");
                System.out.println("Menu");
                System.out.println("\n1. Buat Project");
                System.out.println("2. Daftar Project");
                System.out.println("3. Update Project");
                System.out.println("4. Hapus Project");
                System.out.println("0. Keluar");

                System.out.print("\nPilihan Anda: ");
                String menu = input.nextLine();

                System.out.println("==================================================");

                //pilihan pertama untuk membuat project baru
                if (menu.equals("1")){
                    System.out.println("==================================================");
                    
                    System.out.println("Buat Project");
                    System.out.print("\nID Project (0 = Batal): ");
                    id_project = input.nextLine();

                    rakitKeyboard= new RakitKeyboard(id_project);
                    switchobj = new Switch(id_project);
                    keycaps = new Keycaps(id_project);
                    barebone = new Barebone(id_project);

                    if (!id_project.equals("0")){

                        //Memanggil method untuk menambahkan project
                        rakitKeyboard.tempProjectData(input); //memanggil method dari kelas RakitKeyboard untuk menyimpan data sementara ke dalam queue

                        //memanggil method untuk menambahkan barebone
                        if(RakitKeyboard.isAddProject){
                            barebone.tempBareboneData(input); //memanggil method dari kelas Barebone untuk menyimpan data sementara ke dalam queue
                        }
                        
                        //Memanggil method untuk menambahkan switch
                        if(Barebone.isAddBarebone){
                            switchobj.tempSwitchData(input); //memanggil method dari kelas Switch untuk menyimpan data sementara ke dalam queue
                        }

                        //Memanggil method untuk menambahkan keycaps
                        if(Switch.isAddSwitch){
                            keycaps.tempKeycapsData(input); //memanggil method dari kelas Keycaps untuk menyimpan data sementara ke dalam queue
                        }

                        //menambahkan data yang sebelumnya disimpan sementara di queue ke database
                        if(Keycaps.isAddKeycaps){
                            rakitKeyboard.addData(); //memanggil method dari kelas RakitKeyboard untuk menyimpan data ke database
                            barebone.addData(); //memanggil method dari kelas Barebone untuk menyimpan data ke database
                            switchobj.addData(); //memanggil method dari kelas Switch untuk menyimpan data ke database
                            keycaps.addData(); //memanggil method dari kelas keycaps untuk menyimpan data ke database
                            System.out.println("==================================================");
                        }

                        //batal membuat project
                        if (!Keycaps.isAddKeycaps && !rakitKeyboard.isThereProjectId(id_project)){
                            
                            System.out.println("Batal Menambahkan Data");
                            System.out.println("==================================================");

                            //mengahapus data di queue ketika batal membuat project
                            if(!RakitKeyboard.isAddProject){
                                rakitKeyboard.deleteTempData(); //memanggil method dari kelas RakitKeyboard untuk menghapus data yang disimpan sementara
                            }

                            //mengahapus data di queue ketika batal membuat project
                            else if(!Barebone.isAddBarebone ){
                                rakitKeyboard.deleteTempData(); //memanggil method dari kelas RakitKeyboard untuk menghapus data yang disimpan sementara
                                barebone.deleteTempData(); //memanggil method dari kelas Barebone untuk menghapus data yang disimpan sementara
                            }

                            //mengahapus data di queue ketika batal membuat project
                            else if(!Switch.isAddSwitch){
                                rakitKeyboard.deleteTempData(); //memanggil method dari kelas RakitKeyboard untuk menghapus data yang disimpan sementara
                                barebone.deleteTempData(); //memanggil method dari kelas Barebone untuk menghapus data yang disimpan sementara
                                switchobj.deleteTempData(); //memanggil method dari kelas Switch untuk menghapus data yang disimpan sementara
                            }

                            //mengahapus data di queue ketika batal membuat project
                            else if(!Keycaps.isAddKeycaps){
                                rakitKeyboard.deleteTempData(); //memanggil method dari kelas RakitKeyboard untuk menghapus data yang disimpan sementara
                                barebone.deleteTempData(); //memanggil method dari kelas Barebone untuk menghapus data yang disimpan sementara
                                switchobj.deleteTempData(); //memanggil method dari kelas Switch untuk menghapus data yang disimpan sementara
                                keycaps.deleteTempData(); //memanggil method dari kelas Keycaps untuk menghapus data yang disimpan sementara
                            }
                        }

                        //menghapus data di queue setelah berhasil disimpan ke database
                        if(RakitKeyboard.isAddProject && Barebone.isAddBarebone && Switch.isAddSwitch && Keycaps.isAddKeycaps){
                            rakitKeyboard.deleteTempData(); //memanggil method dari kelas RakitKeyboard untuk menghapus data yang disimpan sementara
                            barebone.deleteTempData(); //memanggil method dari kelas Barebone untuk menghapus data  yang disimpan sementara
                            switchobj.deleteTempData(); //memanggil method dari kelas Switch untuk menghapus data yang disimpan  sementara
                            keycaps.deleteTempData(); //memanggil method dari kelas Keycaps untuk menghapus data yang disimpan sementara
                        }
                    }

                    //batal membuat project
                    else if (id_project.equals("0")){
                        System.out.println("Batal Menambahkan Project");
                        System.out.println("==================================================");
                    }
                }

                //pilihan 2 untuk melihat daftar project
                else if (menu.equals("2")){
                    System.out.println("Daftar Project");
                    rakitKeyboard = new RakitKeyboard("");

                    rakitKeyboard.projectList(); //memanggil method dari kelas RakitKeyboard untuk menampilkan daftar project

                    System.out.println("==================================================");
                    rakitKeyboard.detailProject(input); //memanggil method dari kelas RakitKeyboard untuk menampilkan detail project
                    System.out.println("==================================================");
                }

                //pilihan 3 untuk memperbarui project
                else if (menu.equals("3")){
                    rakitKeyboard = new RakitKeyboard("");

                    rakitKeyboard.projectList(); //memanggil method dari kelas RakitKeyboard untuk menampilkan daftar project

                    System.out.println("==================================================");
                    rakitKeyboard.updateProject(input); //memanggil method dari kelas RakitKeyboard untuk mengupdate project
                    System.out.println("==================================================");
                }

                //pilihan 4 untuk menghapus project
                else if(menu.equals("4")){
                    rakitKeyboard = new RakitKeyboard("");

                    System.out.println("==================================================");
                    rakitKeyboard.deleteProject(input); //memanggil method dari kelas RakitKeyboard untuk mengahapus project
                    System.out.println("==================================================");
                }

                //pilihan 0 untuk keluar dari program dan menutup koneksi
                else if (menu.equals("0")){

                    koneksi.closeConnection(); //memanggil method dari kelas Koneksi untuk menutup koneksi

                    //untuk keluar dari perulangan
                    if(!Koneksi.koneksiBerhasil){
                        break;
                    }
                }

                //percabangan else ketika pilihan tidak sesuai dengan menu
                else {
                    System.out.println("Pilih Menu Yang Tertera");
                }
            }

            //untuk kelura dari perulangan
            if (!Koneksi.koneksiBerhasil){
                break;
            }
        }
    }
}