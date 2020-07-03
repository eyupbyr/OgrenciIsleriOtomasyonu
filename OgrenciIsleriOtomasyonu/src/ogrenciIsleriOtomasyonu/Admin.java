
package ogrenciIsleriOtomasyonu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class Admin {
    
    final static String ad = "admin";
    final static String sifre = "123456";
    
    static void giris(){
        String a,s;
        Scanner input = new Scanner(System.in);
        System.out.println("(Ana menüye dönmek için 0 girebilirsiniz.)");
        System.out.println("Admin ismi:");
        a = input.nextLine();
        if(a.equals("0")){Menuler menu = new Menuler(); menu.anaMenu();}
        System.out.println("Sifre:");
        s = input.nextLine();
        if(s.equals("0")){Menuler menu = new Menuler(); menu.anaMenu();}
        
        if(a.equals(ad) && s.equals(sifre)){}
        else{
            System.out.println("Sifre veya isim yanlis! Tekrar giriniz.");
            giris();
        }
        System.out.println("Giriş Yapıldı!");
        Menuler.adminMenusu();
    }
    
    static void ogrenciListele(){
        
        System.out.println("\nOtomasyona kayitli öğrenci listesi:\n");      
        Connection myconObj = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            mystatObj = myconObj.prepareStatement("select * from ADMIN.OGRENCI");
            myresObj = mystatObj.executeQuery();
            System.out.println("Numara\tİsim\tŞifre");
            while(myresObj.next()){
                 System.out.println(myresObj.getInt(1)+"\t"+myresObj.getString(2)+"\t"+myresObj.getString(3));
            }
            System.out.println("\n Menüye yönlendiriliyorsunuz...\n");
            Menuler.adminMenusu();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    static void ogretimUyesiListele(){
        
        System.out.println("\nOtomasyona kayitli öğretim üyesi listesi:\n");      
        Connection myconObj = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            mystatObj = myconObj.prepareStatement("select * from ADMIN.OGRETIMUYESI");
            myresObj = mystatObj.executeQuery();
            System.out.println("Numara\tİsim\tŞifre");
            while(myresObj.next()){
                 System.out.println(myresObj.getInt(1)+"\t"+myresObj.getString(2)+"\t"+myresObj.getString(3));
            }
            System.out.println("\nMenüye yönlendiriliyorsunuz...\n");
            Menuler.adminMenusu();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    static void dersTanimla(){
        System.out.println("\nDönem dersleri:\n");      
        Connection myconObj = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            mystatObj = myconObj.prepareStatement("select * from ADMIN.DERSLER");
            myresObj = mystatObj.executeQuery();
            System.out.println("Kod\tDers İsmi");
            while(myresObj.next()){
                 System.out.println(myresObj.getString(1)+"\t"+myresObj.getString(2));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        Scanner input = new Scanner(System.in);
        System.out.println("\nOgretim uyesinin vereceği dersi belirlemek için öğretim üyesinin numarasını"
                + " ve dersin kodunu giriniz.(0 girerek menüye dönebilirsiniz.)");
        System.out.println("Numara:");
        int numara = Integer.parseInt(input.nextLine());
        if(numara == 0){Menuler.adminMenusu();}
        
        if(!(Kullanici.numaraKayitliMi(numara, 2))){
            System.out.println("Numara yanlış! Tekrar deneyin");
            dersTanimla();
        }
        
        System.out.println("Ders Kodu");
        String kod = input.nextLine();
        if(kod.equals("0")){Menuler.adminMenusu();}
        
        Connection baglanti=null;
        PreparedStatement updatestate=null;
        PreparedStatement showstate = null;
        ResultSet myresObj2 = null;
        
        switch(kod){
            case "01":{
                try{
                    baglanti = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from ADMIN.OGRETIMUYESIDERS where numara = ?");
                    showstate.setInt(1, numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                        if(myresObj2.getString(2).equals("secilmemis")){
                            updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod1 = ?, ders1 = ? where numara = ?");        
                            updatestate.setString(1, kod);
                            updatestate.setString(2, "Nesneye Yönelik Programlama");
                            updatestate.setInt(3, numara); 
                            updatestate.executeUpdate();
                            System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                            break;
                        }
                        else if(myresObj2.getString(4).equals("secilmemis")){
                            if(myresObj2.getString(2).equals("01")){
                                System.out.println("Bu öğretim üyesi bu dersi zaten veriyor!)");
                                dersTanimla();
                            }
                            else{
                                updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod2 = ?, ders2 = ? where numara = ?");        
                                updatestate.setString(1, kod);
                                updatestate.setString(2, "Nesneye Yönelik Programlama");
                                updatestate.setInt(3, numara); 
                                updatestate.executeUpdate();
                                System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                                break;
                            }
                        }
                        else{
                            System.out.println("Bu öğretim üyesi şu anda maksimum sayıda(2)"
                                    + "ders veriyor!");
                            dersTanimla();
                        }
                    }
                catch(SQLException e){
                    e.printStackTrace();
                }              
            }
            case "02":{
                try{
                    baglanti = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from ADMIN.OGRETIMUYESIDERS where numara = ?");
                    showstate.setInt(1, numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                        if(myresObj2.getString(2).equals("secilmemis")){
                            updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod1 = ?, ders1 = ? where numara = ?");        
                            updatestate.setString(1, kod);
                            updatestate.setString(2, "Veri Yapıları");
                            updatestate.setInt(3, numara); 
                            updatestate.executeUpdate();
                            System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                            break;
                        }
                        else if(myresObj2.getString(4).equals("secilmemis")){
                            if(myresObj2.getString(2).equals("02")){
                                System.out.println("Bu öğretim üyesi bu dersi zaten veriyor!)");
                                dersTanimla();
                            }
                            else{
                                updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod2 = ?, ders2 = ? where numara = ?");        
                                updatestate.setString(1, kod);
                                updatestate.setString(2, "Veri Yapıları");
                                updatestate.setInt(3, numara); 
                                updatestate.executeUpdate();
                                System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                                break;
                            }
                        }
                        else{
                            System.out.println("Bu öğretim üyesi şu anda maksimum sayıda(2)"
                                    + "ders veriyor!");
                            dersTanimla();
                        }
                    }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "03":{
                try{
                    baglanti = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from ADMIN.OGRETIMUYESIDERS where numara = ?");
                    showstate.setInt(1, numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                        if(myresObj2.getString(2).equals("secilmemis")){
                            updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod1 = ?, ders1 = ? where numara = ?");        
                            updatestate.setString(1, kod);
                            updatestate.setString(2, "Lojik Devre Tasarımı");
                            updatestate.setInt(3, numara); 
                            updatestate.executeUpdate();
                            System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                            break;
                        }
                        else if(myresObj2.getString(4).equals("secilmemis")){
                            if(myresObj2.getString(2).equals("03")){
                                System.out.println("Bu öğretim üyesi bu dersi zaten veriyor!)");
                                dersTanimla();
                            }
                            else{
                                updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod2 = ?, ders2 = ? where numara = ?");        
                                updatestate.setString(1, kod);
                                updatestate.setString(2, "Lojik Devre Tasarımı");
                                updatestate.setInt(3, numara); 
                                updatestate.executeUpdate();
                                System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                                break;
                            }
                        }
                        else{
                            System.out.println("Bu öğretim üyesi şu anda maksimum sayıda(2)"
                                    + "ders veriyor!");
                            dersTanimla();
                        }
                    }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "04":{
                try{
                    baglanti = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from ADMIN.OGRETIMUYESIDERS where numara = ?");
                    showstate.setInt(1, numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                        if(myresObj2.getString(2).equals("secilmemis")){
                            updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod1 = ?, ders1 = ? where numara = ?");        
                            updatestate.setString(1, kod);
                            updatestate.setString(2, "Devreler ve Sistemler");
                            updatestate.setInt(3, numara); 
                            updatestate.executeUpdate();
                            System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                            break;
                        }
                        else if(myresObj2.getString(4).equals("secilmemis")){
                            if(myresObj2.getString(2).equals("04")){
                                System.out.println("Bu öğretim üyesi bu dersi zaten veriyor!)");
                                dersTanimla();
                            }
                            else{
                                updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod2 = ?, ders2 = ? where numara = ?");        
                                updatestate.setString(1, kod);
                                updatestate.setString(2, "Devreler ve Sistemler");
                                updatestate.setInt(3, numara); 
                                updatestate.executeUpdate();
                                System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                                break;
                            }
                        }
                        else{
                            System.out.println("Bu öğretim üyesi şu anda maksimum sayıda(2)"
                                    + "ders veriyor!");
                            dersTanimla();
                        }
                    }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "05":{
                try{
                    baglanti = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from ADMIN.OGRETIMUYESIDERS where numara = ?");
                    showstate.setInt(1, numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                        if(myresObj2.getString(2).equals("secilmemis")){
                            updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod1 = ?, ders1 = ? where numara = ?");        
                            updatestate.setString(1, kod);
                            updatestate.setString(2, "Olasılık Teorisi ve İstatistik");
                            updatestate.setInt(3, numara); 
                            updatestate.executeUpdate();
                            System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                            break;
                        }
                        else if(myresObj2.getString(4).equals("secilmemis")){
                            if(myresObj2.getString(2).equals("05")){
                                System.out.println("Bu öğretim üyesi bu dersi zaten veriyor!)");
                                dersTanimla();
                            }
                            else{
                                updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod2 = ?, ders2 = ? where numara = ?");        
                                updatestate.setString(1, kod);
                                updatestate.setString(2, "Olasılık Teorisi ve İstatistik");
                                updatestate.setInt(3, numara); 
                                updatestate.executeUpdate();
                                System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                                break;
                            }
                        }
                        else{
                            System.out.println("Bu öğretim üyesi şu anda maksimum sayıda(2)"
                                    + "ders veriyor!");
                            dersTanimla();
                        }
                    }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "06":{
                try{
                    baglanti = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from ADMIN.OGRETIMUYESIDERS where numara = ?");
                    showstate.setInt(1, numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                        if(myresObj2.getString(2).equals("secilmemis")){
                            updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod1 = ?, ders1 = ? where numara = ?");        
                            updatestate.setString(1, kod);
                            updatestate.setString(2, "Differential Equations");
                            updatestate.setInt(3, numara); 
                            updatestate.executeUpdate();
                            System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                            break;
                        }
                        else if(myresObj2.getString(4).equals("secilmemis")){
                            if(myresObj2.getString(2).equals("06")){
                                System.out.println("Bu öğretim üyesi bu dersi zaten veriyor!)");
                                dersTanimla();
                            }
                            else{
                                updatestate=baglanti.prepareStatement("update OGRETIMUYESIDERS set kod2 = ?, ders2 = ? where numara = ?");        
                                updatestate.setString(1, kod);
                                updatestate.setString(2, "Differential Equations");
                                updatestate.setInt(3, numara); 
                                updatestate.executeUpdate();
                                System.out.println("İşlem başarıyla gerçekleştirildi, menüye dönüyorsunuz...");
                                break;
                            }
                        }
                        else{
                            System.out.println("Bu öğretim üyesi şu anda maksimum sayıda(2)"
                                    + "ders veriyor!");
                            dersTanimla();
                        }
                    }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        Menuler.adminMenusu();
    }
        
    static void dersCikar(String kod){
        Connection baglanti=null;
        PreparedStatement updatestate=null;
        PreparedStatement showstate = null;
        ResultSet myresObj2 = null;
        
        switch(kod){
            case "01":{
                try{
                    baglanti=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti.prepareStatement("select * from ADMIN.DERSPROGRAMI");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getString(2).equals("Nesneye Yonelik Programlama")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS1 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(3).equals("Nesneye Yonelik Programlama")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS2 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(4).equals("Nesneye Yonelik Programlama")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS3 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                    }
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            case "02":{
                try{
                    baglanti=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti.prepareStatement("select * from ADMIN.DERSPROGRAMI");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getString(2).equals("Veri Yapilari")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS1 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(3).equals("Veri Yapilari")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS2 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(4).equals("Veri Yapilari")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS3 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                    }
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            case "03":{
                try{
                    baglanti=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti.prepareStatement("select * from ADMIN.DERSPROGRAMI");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getString(2).equals("Lojik Devre Tasarimi")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS1 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(3).equals("Lojik Devre Tasarimi")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS2 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(4).equals("Lojik Devre Tasarimi")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS3 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                    }
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            case "04":{
                try{
                    baglanti=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti.prepareStatement("select * from ADMIN.DERSPROGRAMI");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getString(2).equals("Devreler ve Sistemler")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS1 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(3).equals("Devreler ve Sistemler")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS2 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(4).equals("Devreler ve Sistemler")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS3 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                    }
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            case "05":{
                try{
                    baglanti=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti.prepareStatement("select * from ADMIN.DERSPROGRAMI");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getString(2).equals("Olasilik ve İstatistik")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS1 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(3).equals("Olasilik ve İstatistik")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS2 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(4).equals("Olasilik ve İstatistik")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS3 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                    }
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            case "06":{
                try{
                    baglanti=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti.prepareStatement("select * from ADMIN.DERSPROGRAMI");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getString(2).equals("Differential Equations")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS1 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(3).equals("Differential Equations")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS2 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                        else if(myresObj2.getString(4).equals("Differential Equations")){
                            updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS3 = ? where GUN = ?");
                            updatestate.setString(1, "bos");
                            updatestate.setString(2, myresObj2.getString(1));
                            updatestate.executeUpdate();
                            break;
                        }
                    }
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    static void dersProgramiHazirla(){
        System.out.println("\nMevcut Ders Programi\n");
        Connection myconObj = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            mystatObj = myconObj.prepareStatement("select * from ADMIN.DERSPROGRAMI");
            myresObj = mystatObj.executeQuery();
            while(myresObj.next()){
                System.out.printf(myresObj.getString(1)+": ");
                if(!(myresObj.getString(2).equals("bos"))){System.out.printf(myresObj.getString(2)+" ");}
                if(!(myresObj.getString(3).equals("bos"))){System.out.printf(myresObj.getString(3)+" ");}
                if(!(myresObj.getString(4).equals("bos"))){System.out.println(myresObj.getString(4)+" ");}
                else{System.out.println();}
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        Scanner input = new Scanner(System.in);
        System.out.println("\nGününü değiştirmek istediğiniz dersin kodunu giriniz.(Menüye dönmek için 0 girebilirsiniz.");
        System.out.println("Ders kodlari:\n 01->Nesneye Yönelik Programlama 02->Veri Yapıları 03->Lojik Devre Tasarımı\n"
                + "04->Devreler ve Sistemler 05->İstatistik ve Olasılık 06->Differential Equations\n");
        System.out.println("Kod:");
        String kod = input.nextLine();
        if(kod.equals("0")){Menuler.adminMenusu();}
        if(!(kod.equals("01") || kod.equals("02") || kod.equals("03") || kod.equals("04") || kod.equals("05")|| kod.equals("06"))){
            System.out.println("Böyle bir ders kodu bulunmamakta!");
            dersProgramiHazirla();
        }
        
        System.out.println("\nGünü seçiniz.\n(Pazartesi için 01 Salı için 02 Çarşamba için 03 Perşembe için 04"
                + " Cuma için 05 giriniz.)(Menüye dönmek için 0 girebilirsiniz.)");
        
        String gun = input.nextLine();
        if(gun.equals("0")){Menuler.adminMenusu();}
        if(!(gun.equals("01") || gun.equals("02") || gun.equals("03") || gun.equals("04") || gun.equals("05")|| gun.equals("06"))){
            System.out.println("Böyle bir gun bulunmamakta!");
            dersProgramiHazirla();
        }
        
        Connection baglanti=null;
        PreparedStatement updatestate=null;
        PreparedStatement showstate = null;
        ResultSet myresObj2 = null;
        
        switch(gun){
            case "01":{
                try{
                    baglanti=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti.prepareStatement("select * from ADMIN.DERSPROGRAMI where GUN = ?");
                    showstate.setString(1, "Pazartesi");
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                    if(myresObj2.getString(2).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS1 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Pazartesi");
                        updatestate.executeUpdate();
                        break;
                    }
                    else if(myresObj2.getString(3).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS2 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Pazartesi");
                        updatestate.executeUpdate();
                        break;
                    }
                    else if(myresObj2.getString(4).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS3 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Pazartesi");
                        updatestate.executeUpdate();
                        break;
                    }
                    else{
                        System.out.println("Bu güne daha fazla ders ekleyemezsiniz!");
                        dersProgramiHazirla();
                    }
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            case "02":{
                try{
                    baglanti=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti.prepareStatement("select * from ADMIN.DERSPROGRAMI where GUN = ?");
                    showstate.setString(1, "Sali");
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                    if(myresObj2.getString(2).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS1 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Sali");
                        updatestate.executeUpdate();
                        break;
                    }
                    else if(myresObj2.getString(3).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS2 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Sali");
                        updatestate.executeUpdate();
                        break;
                    }
                    else if(myresObj2.getString(4).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS3 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Sali");
                        updatestate.executeUpdate();
                        break;
                    }
                    else{
                        System.out.println("Bu güne daha fazla ders ekleyemezsiniz!");
                        dersProgramiHazirla();
                    }
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            case "03":{
                try{
                    baglanti=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti.prepareStatement("select * from ADMIN.DERSPROGRAMI where GUN = ?");
                    showstate.setString(1, "Carsamba");
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                    if(myresObj2.getString(2).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS1 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Carsamba");
                        updatestate.executeUpdate();
                        break;
                    }
                    else if(myresObj2.getString(3).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS2 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Carsamba");
                        updatestate.executeUpdate();
                        break;
                    }
                    else if(myresObj2.getString(4).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS3 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Carsamba");
                        updatestate.executeUpdate();
                        break;
                    }
                    else{
                        System.out.println("Bu güne daha fazla ders ekleyemezsiniz!");
                        dersProgramiHazirla();
                    }
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            case "04":{
                try{
                    baglanti=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti.prepareStatement("select * from ADMIN.DERSPROGRAMI where GUN = ?");
                    showstate.setString(1, "Persembe");
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                    if(myresObj2.getString(2).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS1 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Persembe");
                        updatestate.executeUpdate();
                        break;
                    }
                    else if(myresObj2.getString(3).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS2 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Persembe");
                        updatestate.executeUpdate();
                        break;
                    }
                    else if(myresObj2.getString(4).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS3 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Persembe");
                        updatestate.executeUpdate();
                        break;
                    }
                    else{
                        System.out.println("Bu güne daha fazla ders ekleyemezsiniz!");
                        dersProgramiHazirla();
                    }
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            case "05":{
                try{
                    baglanti=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti.prepareStatement("select * from ADMIN.DERSPROGRAMI where GUN = ?");
                    showstate.setString(1, "Cuma");
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                    if(myresObj2.getString(2).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS1 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Cuma");
                        updatestate.executeUpdate();
                        break;
                    }
                    else if(myresObj2.getString(3).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS2 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Cuma");
                        updatestate.executeUpdate();
                        break;
                    }
                    else if(myresObj2.getString(4).equals("bos")){
                        dersCikar(kod);
                        updatestate = baglanti.prepareStatement("update DERSPROGRAMI set DERS3 = ? where GUN = ?");
                        if(kod.equals("01")){updatestate.setString(1, "Nesneye Yonelik Programlama");}
                        else if(kod.equals("02")){updatestate.setString(1, "Veri Yapilari");}
                        else if(kod.equals("03")){updatestate.setString(1, "Lojik Devre Tasarimi");}
                        else if(kod.equals("04")){updatestate.setString(1, "Devreler ve Sistemler");}
                        else if(kod.equals("05")){updatestate.setString(1, "Olasilik ve İstatistik");}
                        else if(kod.equals("06")){updatestate.setString(1, "Differential Equations");}
                        updatestate.setString(2, "Cuma");
                        updatestate.executeUpdate();
                        break;
                    }
                    else{
                        System.out.println("Bu güne daha fazla ders ekleyemezsiniz!");
                        dersProgramiHazirla();
                    }
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        System.out.println("İşlem tamamlandı! Menüye dönüyorsunuz...\n");
        Menuler.adminMenusu();
    }
}
