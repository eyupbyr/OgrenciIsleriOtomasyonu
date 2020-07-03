
package ogrenciIsleriOtomasyonu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class OgretimUyesi extends Kullanici {
    
    @Override
    void giris(){
        Scanner input = new Scanner(System.in);
        System.out.println("\nÖğretim Üyesi Girişi\n---------------------------------------------"
                + "\nSırasıyla numara ve şifrenizi giriniz.(Ana menüye dönmek için"
                + " 0 girebilirsiniz.)");
        System.out.println("Numara:");
        numara = Integer.parseInt(input.nextLine());
        if(numara == 0){Menuler menu = new Menuler(); menu.anaMenu();}
        System.out.println("Şifreniz:");
        sifre = input.nextLine();
        if(sifre.equals("0")){Menuler menu = new Menuler(); menu.anaMenu();}
        
        Connection baglanti = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            baglanti = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            mystatObj = baglanti.prepareStatement("select * from ADMIN.OGRETIMUYESI");
            myresObj = mystatObj.executeQuery();
            
            int count = 0;            
            while(myresObj.next()){
                if((numara == myresObj.getInt(1)) && (sifre.equals(myresObj.getString(3)))){
                    System.out.println("\nGiris yapildi!\n\nHoşgeldiniz " + myresObj.getString(2) + "\n");
                    count++;
                    break;
                }    
            }
            if(count == 0){
                System.out.println("Numara veya sifre yanlış! Tekrar giriniz.\n");
                giris();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }
    
    @Override
    void kayitol(){
        
        Scanner input = new Scanner(System.in);
        System.out.println("\nKayıt olmak için istenen bilgileri sirasiyla giriniz."
                + "(0 girerek kayit olma islemini iptal edip ana menüye dönebilirsiniz.)");
        
        System.out.println("Numara:");
        numara = Integer.parseInt(input.nextLine());
        if(numara == 0){Menuler menu = new Menuler(); menu.anaMenu();}
        
        if((numaraKayitliMi(numara, 2))){
            System.out.println("Bu numarayla kayıt olamazsınız!\\n");
            kayitol();
        }
        
        System.out.println("İsim:");
        isim = input.nextLine();
        if(isim.equals("0")){Menuler menu = new Menuler(); menu.anaMenu();}
        
        System.out.println("Şifreniz:");
        sifre = input.nextLine();
        if(sifre.equals("0")){Menuler menu = new Menuler(); menu.anaMenu();}
        
        System.out.println("Şifreyi tekrar giriniz:");
        String confirmPassword = input.nextLine();
        if(confirmPassword.equals("0")){Menuler menu = new Menuler(); menu.anaMenu();}
        
        if(!sifre.equals(confirmPassword)){
            System.out.println("Sifreler uyuşmuyor, bilgileri tekrar giriniz.");
            kayitol();
        }
        
        Connection baglanti2=null;
        PreparedStatement insertstate=null;
        PreparedStatement insertstate2=null;
        
        try{
            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            insertstate=baglanti2.prepareStatement("insert into OGRETIMUYESI(numara,isim,sifre)values(?, ?, ?)");        
            insertstate2=baglanti2.prepareStatement("insert into OGRETIMUYESIDERS(numara)values(?)");
            
                insertstate.setInt(1, numara);
                insertstate.setString(2, isim); 
                insertstate.setString(3, sifre); 
                insertstate.execute();
                insertstate2.setInt(1, numara);
                insertstate2.execute();
                
                System.out.println("Ogretim üyesi kaydiniz basariyla olusturuldu,"
                        + "ana menüye yönlendiriliyorsunuz...\n");
                Menuler.anaMenu();
        }
        catch (SQLException e){
            e.printStackTrace();  
        }
        try{
            baglanti2.close();
        }
        catch (SQLException e){
            e.printStackTrace(); 
        }
    }
    
    @Override
    void sifreDegistir(){
        Scanner input = new Scanner(System.in);
        System.out.println("\nŞifrenizi değiştirmek eski şifrenizi ve "
                + "(0 girerek işlemi iptal edip öğrenci menüsüne dönebilirsiniz.");
        
        Connection baglanti = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            baglanti = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            mystatObj = baglanti.prepareStatement("select * from ADMIN.OGRETIMUYESI");
            myresObj = mystatObj.executeQuery();
            
            int count = 0;            
            while(myresObj.next()){
                if((OgretimUyesi.numara == myresObj.getInt(1)) && (sifre.equals(myresObj.getString(3)))){
                    count++;
                    break;
                }    
            }
            if(count == 0){
                System.out.println("Numara veya eski şifreniz yanlış. Tekrar deneyiniz");
                sifreDegistir();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        System.out.println("Yeni şifreniz:");
        String yeniSifre = input.nextLine();
        if(yeniSifre.equals("0")){Menuler.ogretimUyesiMenusu();}
        System.out.println("Yeni şifreyi tekrar giriniz.");
        if(!yeniSifre.equals(input.nextLine())){
            System.out.println("Şifreler eşleşmiyor, tekrar deneyin.");
            sifreDegistir();
        }
        
        Connection baglanti2=null;
        PreparedStatement updatestate=null;
        
        try{
            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            updatestate=baglanti2.prepareStatement("update OGRETIMUYESI set sifre = ? where numara = ?");        
                                  
                updatestate.setString(1, yeniSifre); 
                updatestate.setInt(2, OgretimUyesi.numara); 
                updatestate.executeUpdate();
                
                System.out.println("Şifreniz başarıyla değiştirildi,"
                        + "menüye yönlendiriliyorsunuz...\n");
                Menuler.ogretimUyesiMenusu();
        }
        catch (SQLException e){
            e.printStackTrace();  
        }
        try{
            baglanti.close();
        }
        catch (SQLException e){
            e.printStackTrace(); 
        }
        
    }
    
    static void ogretimUyesiDersleri(){
        
        System.out.println("Bu dönem verdiğiniz dersler:\nKod   Ders İsmi");
        
        Connection myconObj = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            mystatObj = myconObj.prepareStatement("select * from ADMIN.OGRETIMUYESIDERS");
            myresObj = mystatObj.executeQuery();
            while(myresObj.next()){
                if(OgretimUyesi.numara == myresObj.getInt(1)){
                    for(int i = 2; i <= 4; i += 2){
                        if(!(myresObj.getString(i).equals("secilmemis"))){
                            System.out.println(myresObj.getString(i) + "    " + myresObj.getString(i+1));
                        }
                    }
                    break;
                }    
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        System.out.println();
        Menuler.ogretimUyesiMenusu();
    }
    
    static boolean dersiVeriyorMu(int no, String kod){
        
        Connection myconObj = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            mystatObj = myconObj.prepareStatement("select * from ADMIN.OGRETIMUYESIDERS");
            myresObj = mystatObj.executeQuery();
            while(myresObj.next()){
                if(no == myresObj.getInt(1)){
                    for(int i = 2; i <= 4; i += 2){
                        if((myresObj.getString(i).equals(kod))){
                            return true;
                        }
                    }
                    return false;
                }    
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    static void notGir(){
        System.out.println("\nNot girmek istediğiniz dersin kodunu giriniz."
                + "(verdiğiniz dersleri ve kodlarını görmek için menüden ders listesine bakın.)\n"
                + "(0 girerek işlemi iptal edip menüye dönebilirsiniz.)\n");
        
        Scanner input = new Scanner(System.in);
        System.out.println("Ders kodu:");
        String kod = input.nextLine();
        if(kod.equals("0")){Menuler.ogretimUyesiMenusu();}
        
        if(!(dersiVeriyorMu(OgretimUyesi.numara,kod))){
            System.out.println("Bu dersi vermiyorsunuz ya da kodu yanlış girdiniz!");
            notGir();
        }
        
        do{
            System.out.println("Notunu girmek istediğiniz öğrencinin numarasını girip vize için 1 final için 2 girin."
                    + "(0 girene kadar not girmeye devam edebilirsiniz.");
            
            System.out.println("Öğrencinin numarası:");
            int numara = Integer.parseInt(input.nextLine());
            if(Integer.toString(numara).equals("0")){Menuler.ogretimUyesiMenusu();}
            while(!(Ogrenci.dersiAliyorMu(numara, kod))){
                System.out.println("Öğrenci bu dersi almıyor ya da numarayı yanlış girdiniz. Tekrar deneyin."
                        + "\nÖğrencinin Numarası:");
                numara = Integer.parseInt(input.nextLine());
            }
            System.out.println("Vize için 1 Final için 2:");
            String sinavTuru = input.nextLine();
            if(sinavTuru.equals("0")){Menuler.ogretimUyesiMenusu();}
            System.out.println("Not:");
            String not = input.nextLine();
            if(not.equals("0")){Menuler.ogretimUyesiMenusu();}
            
            Connection baglanti2=null;
            PreparedStatement updatestate=null;

            switch(kod){
                case "01": {
                    try{
                        if(sinavTuru.equals("1")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update NESNEYEYÖNELIKPROGRAMLAMA set VIZE = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Vize Notu Girildi!\n");
                                break;
                        }
                        else if(sinavTuru.equals("2")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update NESNEYEYÖNELIKPROGRAMLAMA set FINAL = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Final Notu Girildi!\n");
                                break;
                        }
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                    break;
                }
            
                case "02":{
                    try{
                        if(sinavTuru.equals("1")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update VERIYAPILARI set VIZE = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Vize Notu Girildi!\n");
                                break;
                        }
                        else if(sinavTuru.equals("2")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update VERIYAPILARI set FINAL = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Final Notu Girildi!\n");
                                break;
                        }
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                case "03":{
                    try{
                        if(sinavTuru.equals("1")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update LOJIKDEVRETASARIMI set VIZE = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Vize Notu Girildi!\n");
                                break;
                        }
                        else if(sinavTuru.equals("2")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update LOJIKDEVRETASARIMI set FINAL = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Final Notu Girildi!\n");
                                break;
                        }
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                case "04":{
                    try{
                        if(sinavTuru.equals("1")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update DEVRELERVESISTEMLER set VIZE = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Vize Notu Girildi!\n");
                                break;
                        }
                        else if(sinavTuru.equals("2")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update DEVRELERVESISTEMLER set FINAL = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Final Notu Girildi!\n");
                                break;
                        }
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                case "05":{
                    try{
                        if(sinavTuru.equals("1")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update OLASILIKTEORISIVEİSTATISTIK set VIZE = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Vize Notu Girildi!\n");
                                break;
                        }
                        else if(sinavTuru.equals("2")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update OLASILIKTEORISIVEİSTATISTIK set FINAL = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Final Notu Girildi!\n");
                                break;
                        }
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                case "06":{
                    try{
                        if(sinavTuru.equals("1")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update DIFFERENTIALEQUATIONS set VIZE = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Vize Notu Girildi!\n");
                                break;
                        }
                        else if(sinavTuru.equals("2")){
                            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                            updatestate=baglanti2.prepareStatement("update DIFFERENTIALEQUATIONS set FINAL = ? "
                                    + "where numara = ?");        
                                updatestate.setString(1, not); 
                                updatestate.setInt(2, numara); 
                                updatestate.executeUpdate();
                                System.out.println("Final Notu Girildi!\n");
                                break;
                        }
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
            }
        }while(true);
    }
    
    static void yoklamaGir(){
        System.out.println("\nYoklama bilgisini girmek istediğiniz dersin kodunu giriniz."
                + "(verdiğiniz dersleri ve kodlarını görmek için menüden ders listesine bakın.)\n"
                + "(0 girerek işlemi iptal edip menüye dönebilirsiniz.)\n");
        
        Scanner input = new Scanner(System.in);
        System.out.println("Ders kodu:");
        String kod = input.nextLine();
        if(kod.equals("0")){Menuler.ogretimUyesiMenusu();}
        
        if(!(dersiVeriyorMu(OgretimUyesi.numara,kod))){
            System.out.println("Bu dersi vermiyorsunuz ya da kodu yanlış girdiniz!");
            yoklamaGir();
        }
        
        do{
            System.out.println("Yoklama bilgisini girmek istediğiniz öğrencinin numarasını girin."
                    + "(0 girene kadar yoklama bilgisi girmeye devam edebilirsiniz.");
            
            System.out.println("Öğrencinin numarası:");
            int numara = Integer.parseInt(input.nextLine());
            if(Integer.toString(numara).equals("0")){Menuler.ogretimUyesiMenusu();}
            while(!(Ogrenci.dersiAliyorMu(numara, kod))){
                System.out.println("Öğrenci bu dersi almıyor ya da numarayı yanlış girdiniz. Tekrar deneyin."
                        + "\nÖğrencinin Numarası:");
                numara = Integer.parseInt(input.nextLine());
            }
            
            System.out.println("Devamsızlık yapılan gün sayısı:(burada menüye dönmek isterseniz 00 girin.");
            String yoklama = input.nextLine();
            if(yoklama.equals("00")){Menuler.ogretimUyesiMenusu();}
            
            Connection baglanti2=null;
            PreparedStatement updatestate=null;

            switch(kod){
                case "01": {
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        updatestate=baglanti2.prepareStatement("update NESNEYEYÖNELIKPROGRAMLAMA set YOKLAMA = ? "
                                + "where numara = ?");        
                            updatestate.setString(1, yoklama); 
                            updatestate.setInt(2, numara); 
                            updatestate.executeUpdate();
                            System.out.println("Yoklama bilgisi girildi!\n");
                            break;
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                    break;
                }
            
                case "02":{
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        updatestate=baglanti2.prepareStatement("update VERIYAPILARI set YOKLAMA = ? "
                                + "where numara = ?");        
                            updatestate.setString(1, yoklama); 
                            updatestate.setInt(2, numara); 
                            updatestate.executeUpdate();
                            System.out.println("Yoklama bilgisi girildi!\n");
                            break;
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                    break;
                }
                case "03":{
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        updatestate=baglanti2.prepareStatement("update LOJIKDEVRETASARIMI set YOKLAMA = ? "
                                + "where numara = ?");        
                            updatestate.setString(1, yoklama); 
                            updatestate.setInt(2, numara); 
                            updatestate.executeUpdate();
                            System.out.println("Yoklama bilgisi girildi!\n");
                            break;
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                    break;
                }
                case "04":{
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        updatestate=baglanti2.prepareStatement("update DEVRELERVESISTEMLER set YOKLAMA = ? "
                                + "where numara = ?");        
                            updatestate.setString(1, yoklama); 
                            updatestate.setInt(2, numara); 
                            updatestate.executeUpdate();
                            System.out.println("Yoklama bilgisi girildi!\n");
                            break;
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                    break;
                }
                case "05":{
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        updatestate=baglanti2.prepareStatement("update OLASILIKTEORISIVEİSTATISTIK set YOKLAMA = ? "
                                + "where numara = ?");        
                            updatestate.setString(1, yoklama); 
                            updatestate.setInt(2, numara); 
                            updatestate.executeUpdate();
                            System.out.println("Yoklama bilgisi girildi!\n");
                            break;
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                    break;
                }
                case "06":{
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        updatestate=baglanti2.prepareStatement("update DIFFERENTIALEQUATIONS set YOKLAMA = ? "
                                + "where numara = ?");        
                            updatestate.setString(1, yoklama); 
                            updatestate.setInt(2, numara); 
                            updatestate.executeUpdate();
                            System.out.println("Yoklama bilgisi girildi!\n");
                            break;
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                    break;
                }
            }
        }while(true);
    }
    
    static void notListesi(){
        System.out.println("Öğrencilerin notlarını görmek istediğiniz dersin kodunu girin.(Menüye dönmek için 0)");
        
        Scanner input = new Scanner(System.in);
        System.out.println("Ders kodu:");
        String kod = input.nextLine();
        
        if(!(dersiVeriyorMu(OgretimUyesi.numara,kod))){
            System.out.println("Bu dersi vermiyorsunuz ya da kodu yanlış girdiniz!");
            notListesi();
        }
        
        Connection myconObj = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
                
        switch(kod){
            case "01":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    mystatObj = myconObj.prepareStatement("select * from NESNEYEYÖNELIKPROGRAMLAMA");
                    myresObj = mystatObj.executeQuery();
                    System.out.println("ÖğrenciNo   Vize   Final   Harf Notu   Yoklama");
                    while(myresObj.next()){
                         System.out.println(myresObj.getInt(1)+"\t"+myresObj.getString(2)+"\t"+myresObj.getString(3)+
                                 "\t"+myresObj.getString(4)+"\t"+myresObj.getString(5));
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }               
            }
            case "02":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    mystatObj = myconObj.prepareStatement("select * from VERIYAPILARI");
                    myresObj = mystatObj.executeQuery();
                    System.out.println("Öğrenci Numarası\tVize\tFinal\tHarf Notu\tYoklama");
                    while(myresObj.next()){
                         System.out.println(myresObj.getInt(1)+"\t"+myresObj.getString(2)+"\t"+myresObj.getString(3)+
                                 "\t"+myresObj.getString(4)+"\t"+myresObj.getString(5));
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "03":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    mystatObj = myconObj.prepareStatement("select * from LOJIKDEVRETASARIMI");
                    myresObj = mystatObj.executeQuery();
                    System.out.println("Öğrenci Numarası\tVize\tFinal\tHarf Notu\tYoklama");
                    while(myresObj.next()){
                         System.out.println(myresObj.getInt(1)+"\t"+myresObj.getString(2)+"\t"+myresObj.getString(3)+
                                 "\t"+myresObj.getString(4)+"\t"+myresObj.getString(5));
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "04":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    mystatObj = myconObj.prepareStatement("select * from DEVRELERVESISTEMLER");
                    myresObj = mystatObj.executeQuery();
                    System.out.println("Öğrenci Numarası\tVize\tFinal\tHarf Notu\tYoklama");
                    while(myresObj.next()){
                         System.out.println(myresObj.getInt(1)+"\t"+myresObj.getString(2)+"\t"+myresObj.getString(3)+
                                 "\t"+myresObj.getString(4)+"\t"+myresObj.getString(5));
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "05":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    mystatObj = myconObj.prepareStatement("select * from OLASILIKTEORISIVEİSTATISTIK");
                    myresObj = mystatObj.executeQuery();
                    System.out.println("Öğrenci Numarası\tVize\tFinal\tHarf Notu\tYoklama");
                    while(myresObj.next()){
                         System.out.println(myresObj.getInt(1)+"\t"+myresObj.getString(2)+"\t"+myresObj.getString(3)+
                                 "\t"+myresObj.getString(4)+"\t"+myresObj.getString(5));
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "06":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    mystatObj = myconObj.prepareStatement("select * from DIFFERENTIALEQUATIONS");
                    myresObj = mystatObj.executeQuery();
                    System.out.println("Öğrenci Numarası\tVize\tFinal\tHarf Notu\tYoklama");
                    while(myresObj.next()){
                         System.out.println(myresObj.getInt(1)+"\t"+myresObj.getString(2)+"\t"+myresObj.getString(3)+
                                 "\t"+myresObj.getString(4)+"\t"+myresObj.getString(5));
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    static String harfNotuHesapla(int vizeNotu, int finalNotu){
        int ortalama = (vizeNotu + finalNotu) / 2;
        
        if(ortalama < 35){
            return "FF";
        }
        if(ortalama < 43){
            return "DD";
        }
        if(ortalama < 50){
            return "DC";
        }
        if(ortalama < 60){
            return "CC";
        }
        if(ortalama < 70){
            return "BC";
        }
        if(ortalama < 80){
            return "BB";
        }
        if(ortalama < 90){
            return "BA";
        }
        return "AA";
    }
    
    static void harfNotuBelirle(){
        System.out.println("Harf notlarını belirlemek istediğiniz dersin kodunu giriniz.(Menüye dönmek için 0)");
        Scanner input = new Scanner(System.in);
        
        System.out.println("Ders kodu:");
        String kod = input.nextLine();
        if(kod.equals("0")){Menuler.ogretimUyesiMenusu();}
        
        if(!(dersiVeriyorMu(OgretimUyesi.numara,kod))){
            System.out.println("Bu dersi vermiyorsunuz ya da kodu yanlış girdiniz!");
            harfNotuBelirle();
        }
        
        System.out.println("0'dan farklı bir şey girerseniz harf notlari belirlenecek, iptal etmek için 0 girebilirsiniz.");
        String islem = input.nextLine();
        if(islem.equals("0")){Menuler.ogretimUyesiMenusu();}
        Connection myconObj = null;
        PreparedStatement showstate = null;
        PreparedStatement updatestate = null;
        ResultSet myresObj = null;
        
        switch(kod){
            case "01":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from NESNEYEYÖNELIKPROGRAMLAMA");
                    myresObj = showstate.executeQuery();
                    while(myresObj.next()){
                        if((myresObj.getString(2).equals("Girilmedi")) || (myresObj.getString(3).equals("Girilmedi"))){
                            System.out.println("Bütün notlar girilmemiş! Harf notlarını hesaplamadan önce vize ve "
                                    + "final notlarını girin.");
                            Menuler.ogretimUyesiMenusu();
                        }
                        else{
                            String harfNotu = harfNotuHesapla(Integer.parseInt(myresObj.getString(2)),Integer.parseInt(myresObj.getString(3)));
                            updatestate=myconObj.prepareStatement("update NESNEYEYÖNELIKPROGRAMLAMA set HARFNOTU = ? where NUMARA = ?");              
                            updatestate.setString(1, harfNotu); 
                            updatestate.setInt(2, myresObj.getInt(1)); 
                            updatestate.executeUpdate();
                        }
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }               
            }
            case "02":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from VERIYAPILARI");
                    myresObj = showstate.executeQuery();
                    while(myresObj.next()){
                        if((myresObj.getString(2).equals("Girilmedi")) || (myresObj.getString(3).equals("Girilmedi"))){
                            System.out.println("Bütün notlar girilmemiş! Harf notlarını hesaplamadan önce vize ve "
                                    + "final notlarını girin.");
                            Menuler.ogretimUyesiMenusu();
                        }
                        else{
                            String harfNotu = harfNotuHesapla(Integer.parseInt(myresObj.getString(2)),Integer.parseInt(myresObj.getString(3)));
                            updatestate=myconObj.prepareStatement("update VERIYAPILARI set HARFNOTU = ? where NUMARA = ?");              
                            updatestate.setString(1, harfNotu); 
                            updatestate.setInt(2, myresObj.getInt(1)); 
                            updatestate.executeUpdate();
                        }
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "03":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from LOJIKDEVRETASARIMI");
                    myresObj = showstate.executeQuery();
                    while(myresObj.next()){
                        if((myresObj.getString(2).equals("Girilmedi")) || (myresObj.getString(3).equals("Girilmedi"))){
                            System.out.println("Bütün notlar girilmemiş! Harf notlarını hesaplamadan önce vize ve "
                                    + "final notlarını girin.");
                            Menuler.ogretimUyesiMenusu();
                        }
                        else{
                            String harfNotu = harfNotuHesapla(Integer.parseInt(myresObj.getString(2)),Integer.parseInt(myresObj.getString(3)));
                            updatestate=myconObj.prepareStatement("update LOJIKDEVRETASARIMI set HARFNOTU = ? where NUMARA = ?");              
                            updatestate.setString(1, harfNotu); 
                            updatestate.setInt(2, myresObj.getInt(1)); 
                            updatestate.executeUpdate();
                        }
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "04":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from DEVRELERVESISTEMLER");
                    myresObj = showstate.executeQuery();
                    while(myresObj.next()){
                        if((myresObj.getString(2).equals("Girilmedi")) || (myresObj.getString(3).equals("Girilmedi"))){
                            System.out.println("Bütün notlar girilmemiş! Harf notlarını hesaplamadan önce vize ve "
                                    + "final notlarını girin.");
                            Menuler.ogretimUyesiMenusu();
                        }
                        else{
                            String harfNotu = harfNotuHesapla(Integer.parseInt(myresObj.getString(2)),Integer.parseInt(myresObj.getString(3)));
                            updatestate=myconObj.prepareStatement("update DEVRELERVESISTEMLER set HARFNOTU = ? where NUMARA = ?");              
                            updatestate.setString(1, harfNotu); 
                            updatestate.setInt(2, myresObj.getInt(1)); 
                            updatestate.executeUpdate();
                        }
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "05":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from OLASILIKTEORISIVEİSTATISTIK");
                    myresObj = showstate.executeQuery();
                    while(myresObj.next()){
                        if((myresObj.getString(2).equals("Girilmedi")) || (myresObj.getString(3).equals("Girilmedi"))){
                            System.out.println("Bütün notlar girilmemiş! Harf notlarını hesaplamadan önce vize ve "
                                    + "final notlarını girin.");
                            Menuler.ogretimUyesiMenusu();
                        }
                        else{
                            String harfNotu = harfNotuHesapla(Integer.parseInt(myresObj.getString(2)),Integer.parseInt(myresObj.getString(3)));
                            updatestate=myconObj.prepareStatement("update OLASILIKTEORISIVEİSTATISTIK set HARFNOTU = ? where NUMARA = ?");              
                            updatestate.setString(1, harfNotu); 
                            updatestate.setInt(2, myresObj.getInt(1)); 
                            updatestate.executeUpdate();
                        }
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            case "06":{
                try{
                    myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = myconObj.prepareStatement("select * from DIFFERENTIALEQUATIONS");
                    myresObj = showstate.executeQuery();
                    while(myresObj.next()){
                        if((myresObj.getString(2).equals("Girilmedi")) || (myresObj.getString(3).equals("Girilmedi"))){
                            System.out.println("Bütün notlar girilmemiş! Harf notlarını hesaplamadan önce vize ve "
                                    + "final notlarını girin.");
                            Menuler.ogretimUyesiMenusu();
                        }
                        else{
                            String harfNotu = harfNotuHesapla(Integer.parseInt(myresObj.getString(2)),Integer.parseInt(myresObj.getString(3)));
                            updatestate=myconObj.prepareStatement("update DIFFERENTIALEQUATIONS set HARFNOTU = ? where NUMARA = ?");              
                            updatestate.setString(1, harfNotu); 
                            updatestate.setInt(2, myresObj.getInt(1)); 
                            updatestate.executeUpdate();
                        }
                    }
                    Menuler.ogretimUyesiMenusu();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }       
    }  
}


        
        
    

