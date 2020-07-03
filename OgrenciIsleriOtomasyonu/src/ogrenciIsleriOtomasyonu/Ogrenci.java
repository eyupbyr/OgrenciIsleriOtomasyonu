
package ogrenciIsleriOtomasyonu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Ogrenci extends Kullanici {
        
    @Override
    void giris(){
        Scanner input = new Scanner(System.in);
        System.out.println("\nÖğrenci Girişi\n--------------------------------------"
                + "---------------------------------------------------"
                + "\nSırasıyla numara ve şifrenizi giriniz.(Ana menüye dönmek için 0 girebilirsiniz.)");
        System.out.println("Numara:");
        Ogrenci.numara = Integer.parseInt(input.nextLine());
        if(Ogrenci.numara == 0){Menuler.anaMenu();}
        System.out.println("Şifreniz:");
        sifre = input.nextLine();
        if(sifre.equals("0")){Menuler.anaMenu();}
        
        Connection baglanti = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            baglanti = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            mystatObj = baglanti.prepareStatement("select * from ADMIN.OGRENCI");
            myresObj = mystatObj.executeQuery();
            
            int count = 0;            
            while(myresObj.next()){
                if((Ogrenci.numara == myresObj.getInt(1)) && (sifre.equals(myresObj.getString(3)))){
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
        Menuler.ogrenciMenusu();
    }
    
    @Override
    void kayitol(){
        
        Scanner input = new Scanner(System.in);
        System.out.println("\nKayıt olmak için istenen bilgileri sirasiyla giriniz."
                + "(0 girerek kayit olma islemini iptal edip ana menüye dönebilirsiniz.)");
        
        System.out.println("Numara:");
        int numara = Integer.parseInt(input.nextLine()); 
        if(numara == 0){Menuler.anaMenu();}
        
        if((numaraKayitliMi(numara, 1))){
            System.out.println("Bu numarayla kayıt olamazsınız!\n");
            kayitol();
        }
        
        System.out.println("İsim:");
        String isim = input.nextLine();
        if(isim.equals("0")){Menuler.anaMenu();}
        
        System.out.println("Şifreniz:");
        String sifre = input.nextLine();
        if(sifre.equals("0")){Menuler.anaMenu();}
        
        System.out.println("Şifreyi tekrar giriniz:");
        String confirmPassword = input.nextLine();
        if(confirmPassword.equals("0")){Menuler.anaMenu();}
        
        if(!sifre.equals(confirmPassword)){
            System.out.println("Sifreler uyusmuyor, tekrar kayit olun.");
            kayitol();
        }
        
        Connection baglanti2=null;
        PreparedStatement insertstate=null;
        PreparedStatement insertstate2=null;
        
        try{
            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            insertstate=baglanti2.prepareStatement("insert into OGRENCI(numara,isim,sifre)values(?, ?, ?)");        
            insertstate2=baglanti2.prepareStatement("insert into OGRENCIDERS(numara)values(?)");
                
                insertstate.setInt(1, numara);
                insertstate.setString(2, isim); 
                insertstate.setString(3, sifre); 
                insertstate.execute();
                insertstate2.setInt(1, numara);
                insertstate2.execute();
                
                System.out.println("Ogrenci kaydiniz basariyla olusturuldu,"
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
        System.out.println("\nŞifrenizi değiştirmek için eski şifrenizi ve yeni şifrenizi giriniz."
                + "(0 girerek işlemi iptal edip öğrenci menüsüne dönebilirsiniz.");

        System.out.println("\nEski şifreniz:");
        sifre = input.nextLine();
        if(sifre.equals("0")){Menuler.ogrenciMenusu();}
        
        Connection baglanti = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            baglanti = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            mystatObj = baglanti.prepareStatement("select * from ADMIN.OGRENCI");
            myresObj = mystatObj.executeQuery();
            
            int count = 0;            
            while(myresObj.next()){
                if((Ogrenci.numara == myresObj.getInt(1)) && (sifre.equals(myresObj.getString(3)))){
                    count++;
                    break;
                }    
            }
            if(count == 0){
                System.out.println("Eski şifreniz yanlış. Tekrar deneyiniz");
                sifreDegistir();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        System.out.println("Yeni şifreniz:");
        String yeniSifre = input.nextLine();
        System.out.println("Yeni şifreyi tekrar giriniz.");
        if(!yeniSifre.equals(input.nextLine())){
            System.out.println("\nŞifreler eşleşmiyor, tekrar deneyin.");
            sifreDegistir();
        }
        
        Connection baglanti2=null;
        PreparedStatement updatestate=null;
        
        try{
            baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            updatestate=baglanti2.prepareStatement("update OGRENCI set sifre = ? where numara = ?");        
                                  
                updatestate.setString(1, yeniSifre); 
                updatestate.setInt(2, Ogrenci.numara); 
                updatestate.executeUpdate();
                
                System.out.println("\nŞifreniz başarıyla değiştirildi,"
                        + "menüye yönlendiriliyorsunuz...\n");
                Menuler.ogrenciMenusu();
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
    
    static void dersSec(){
        
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
        System.out.println("\nDersi seçmek için dersin kodunu giriniz.(Menüye dönmek için 0 girebilirsiniz.)");
        
        System.out.println("Ders Kodu:");
        String kod = input.nextLine();
       
        Connection baglanti2=null;
        PreparedStatement updatestate=null;
        PreparedStatement insertstate = null;
        PreparedStatement showstate = null;
        ResultSet myresObj2 = null;
        
        switch(kod){
            case "01": {
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select kod1 from OGRENCIDERS where numara = ?");
                    showstate.setInt(1, Ogrenci.numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                    if(myresObj2.getString(1).equals("secilmemis")){
                        updatestate=baglanti2.prepareStatement("update OGRENCIDERS set kod1 = ?, ders1 = ? where numara = ?");        
                        updatestate.setString(1, kod);
                        updatestate.setString(2, "Nesneye Yönelik Programlama");
                        updatestate.setInt(3, Ogrenci.numara); 
                        updatestate.executeUpdate();
                        
                        insertstate=baglanti2.prepareStatement("insert into NESNEYEYÖNELIKPROGRAMLAMA (numara)values(?)");
                        insertstate.setInt(1, Ogrenci.numara);
                        insertstate.execute();

                        System.out.println("Ders seçimi yapıldı, menüye yönlendiriliyorsunuz...\n");
                        break;
                    }
                    else{ 
                        System.out.println("Bu dersi zaten seçmişsiniz, başka ders deneyin.");
                        dersSec();
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();  
                }
            }
            case "02":{
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select kod2 from OGRENCIDERS where numara = ?");
                    showstate.setInt(1, Ogrenci.numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                    if(myresObj2.getString(1).equals("secilmemis")){
                        updatestate=baglanti2.prepareStatement("update OGRENCIDERS set kod2 = ?, ders2 = ? where numara = ?");        
                        updatestate.setString(1, kod);
                        updatestate.setString(2, "Veri Yapıları");
                        updatestate.setInt(3, Ogrenci.numara); 
                        updatestate.executeUpdate();
                        
                        insertstate=baglanti2.prepareStatement("insert into VERIYAPILARI (numara)values(?)");
                        insertstate.setInt(1, Ogrenci.numara);
                        insertstate.execute();

                        System.out.println("Ders seçimi yapıldı, menüye yönlendiriliyorsunuz...\n");
                        break;
                    }
                    else{ 
                        System.out.println("Bu dersi zaten seçmişsiniz, başka ders deneyin.");
                        dersSec();
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();  
                }
            }
            case "03":{
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select kod3 from OGRENCIDERS where numara = ?");
                    showstate.setInt(1, Ogrenci.numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                    if(myresObj2.getString(1).equals("secilmemis")){
                        updatestate=baglanti2.prepareStatement("update OGRENCIDERS set kod3 = ?, ders3 = ? where numara = ?");        
                        updatestate.setString(1, kod);
                        updatestate.setString(2, "Lojik Devre Tasarımı");
                        updatestate.setInt(3, Ogrenci.numara); 
                        updatestate.executeUpdate();
                        
                        insertstate=baglanti2.prepareStatement("insert into LOJIKDEVRETASARIMI (numara)values(?)");
                        insertstate.setInt(1, Ogrenci.numara);
                        insertstate.execute();

                        System.out.println("Ders seçimi yapıldı, menüye yönlendiriliyorsunuz...\n");
                        break;
                    }
                    else{ 
                        System.out.println("Bu dersi zaten seçmişsiniz, başka ders deneyin.");
                        dersSec();
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();  
                }
            }
            case "04":{
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select kod4 from OGRENCIDERS where numara = ?");
                    showstate.setInt(1, Ogrenci.numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                    if(myresObj2.getString(1).equals("secilmemis")){
                        updatestate=baglanti2.prepareStatement("update OGRENCIDERS set kod4 = ?, ders4 = ? where numara = ?");        
                        updatestate.setString(1, kod);
                        updatestate.setString(2, "Devreler ve Sistemler");
                        updatestate.setInt(3, Ogrenci.numara); 
                        updatestate.executeUpdate();
                        
                        insertstate=baglanti2.prepareStatement("insert into DEVRELERVESISTEMLER (numara)values(?)");
                        insertstate.setInt(1, Ogrenci.numara);
                        insertstate.execute();

                        System.out.println("Ders seçimi yapıldı, menüye yönlendiriliyorsunuz...\n");
                        break;
                    }
                    else{ 
                        System.out.println("Bu dersi zaten seçmişsiniz, başka ders deneyin.");
                        dersSec();
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();  
                }
            }
            case "05":{
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select kod5 from OGRENCIDERS where numara = ?");
                    showstate.setInt(1, Ogrenci.numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                    if(myresObj2.getString(1).equals("secilmemis")){
                        updatestate=baglanti2.prepareStatement("update OGRENCIDERS set kod5 = ?, ders5 = ? where numara = ?");        
                        updatestate.setString(1, kod);
                        updatestate.setString(2, "Olasılık Teorisi ve İstatistik");
                        updatestate.setInt(3, Ogrenci.numara); 
                        updatestate.executeUpdate();
                        
                        insertstate=baglanti2.prepareStatement("insert into OLASILIKTEORISIVEİSTATISTIK (numara)values(?)");
                        insertstate.setInt(1, Ogrenci.numara);
                        insertstate.execute();

                        System.out.println("Ders seçimi yapıldı, menüye yönlendiriliyorsunuz...\n");
                        break;
                    }
                    else{ 
                        System.out.println("Bu dersi zaten seçmişsiniz, başka ders deneyin.");
                        dersSec();
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();  
                }
            }
            case "06":{
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select kod6 from OGRENCIDERS where numara = ?");
                    showstate.setInt(1, Ogrenci.numara);
                    myresObj2 = showstate.executeQuery();
                    myresObj2.next();
                    if(myresObj2.getString(1).equals("secilmemis")){
                        updatestate=baglanti2.prepareStatement("update OGRENCIDERS set kod6 = ?, ders6 = ? where numara = ?");        
                        updatestate.setString(1, kod);
                        updatestate.setString(2, "Differential Equations");
                        updatestate.setInt(3, Ogrenci.numara); 
                        updatestate.executeUpdate();
                        
                        insertstate=baglanti2.prepareStatement("insert into DIFFERENTIALEQUATIONS (numara)values(?)");
                        insertstate.setInt(1, Ogrenci.numara);
                        insertstate.execute();

                        System.out.println("Ders seçimi yapıldı, menüye yönlendiriliyorsunuz...\n");
                        break;
                    }
                    else{ 
                        System.out.println("Bu dersi zaten seçmişsiniz, başka ders deneyin.");
                        dersSec();
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();  
                }
            }
            case "0":{
                Menuler.ogrenciMenusu();
                break;
            }
            default:{
                System.out.println("\nBöyle bir ders kodu bulunmamakta, tekrar deneyin.");
                dersSec();
                break;
            }
        }

        try{
            baglanti2.close();
        }
        catch (SQLException e){
            e.printStackTrace(); 
        }
        
        Menuler.ogrenciMenusu();
    }
     
    static void ogrenciDersleri(){
        
        System.out.println("Bu dönem aldığınız dersler:\n\nKod   Ders İsmi");
        
        Connection myconObj = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            myconObj = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            mystatObj = myconObj.prepareStatement("select * from ADMIN.OGRENCIDERS");
            myresObj = mystatObj.executeQuery();
            while(myresObj.next()){
                if(Ogrenci.numara == myresObj.getInt(1)){
                    for(int i = 2; i <= 12; i += 2){
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
        Menuler.ogrenciMenusu();
    }
    
    static boolean dersiAliyorMu(int no, String kod){
        Connection baglanti2=null;
        PreparedStatement showstate = null;
        ResultSet myresObj2 = null;
        
        switch(kod){
            case "01": {
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select * from NESNEYEYÖNELIKPROGRAMLAMA");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getInt(1) == no){
                            return true;
                        }
                    }
                    return false;
                }
                catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
            }
            case "02":{
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select * from VERIYAPILARI");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getInt(1) == no){
                            return true;
                        }
                    }
                    return false;
                }
                catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
            }
            case "03":{
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select * from LOJIKDEVRETASARIMI");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getInt(1) == no){
                            return true;
                        }
                    }
                    return false;
                }
                catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
            }
            case "04":{
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select * from DEVRELERVESISTEMLER");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getInt(1) == no){
                            return true;
                        }
                    }
                    return false;
                }
                catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
            }
            case "05":{
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select * from OLASILIKTEORISIVEİSTATISTIK");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getInt(1) == no){
                            return true;
                        }
                    }
                    return false;
                }
                catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
            }
            case "06":{
                try{
                    baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                    showstate = baglanti2.prepareStatement("select * from DIFFERENTIALEQUATIONS");
                    myresObj2 = showstate.executeQuery();
                    while(myresObj2.next()){
                        if(myresObj2.getInt(1) == no){
                            return true;
                        }
                    }
                    return false;
                }
                catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }  
    
    static void notlar(){
        
        System.out.println("\nNotlarını görmek istediğiniz dersin kodunu giriniz.\n"
                + "(Aldığınız dersleri ve kodlarını görmek için menüden ders listesine bakın.)"
                + "(0 girerek işlemi iptal edip menüye dönebilirsiniz.)\n");
        
        Scanner input = new Scanner(System.in);
       
        System.out.println("Ders kodu:");
        String kod = input.nextLine();
        
        Connection baglanti2=null;
        PreparedStatement showstate = null;
        ResultSet myresObj2 = null;
        
        switch(kod){
            case "01": {
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select VIZE,FINAL,HARFNOTU from NESNEYEYÖNELIKPROGRAMLAMA"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Nesneye Yönelik Programlama");
                        System.out.println("Vize: "+myresObj2.getString(1)+"\nFinal: "+myresObj2.getString(2)+"\nHarf Notu:"
                                + myresObj2.getString(3));                        
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "02":{
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select VIZE,FINAL,HARFNOTU from VERIYAPILARI"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Veri Yapıları");
                        System.out.println("Vize: "+myresObj2.getString(1)+"\nFinal: "+myresObj2.getString(2)+"\nHarf Notu:"
                                + myresObj2.getString(3));
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "03":{
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select VIZE,FINAL,HARFNOTU from LOJIKDEVRETASARIMI"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Lojik Devre Tasarımı");
                        System.out.println("Vize: "+myresObj2.getString(1)+"\nFinal: "+myresObj2.getString(2)+"\nHarf Notu:"
                                + myresObj2.getString(3));
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "04":{
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select VIZE,FINAL,HARFNOTU from DEVRELERVESISTEMLER"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Devreler ve Sistemler");
                        System.out.println("Vize: "+myresObj2.getString(1)+"\nFinal: "+myresObj2.getString(2)+"\nHarf Notu:"
                                + myresObj2.getString(3));
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "05":{
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select VIZE,FINAL,HARFNOTU from OLASILIKTEORISIVEİSTATISTIK"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Olasılık Teorisi ve İstatistik");
                        System.out.println("Vize: "+myresObj2.getString(1)+"\nFinal: "+myresObj2.getString(2)+"\nHarf Notu:"
                                + myresObj2.getString(3));
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "06":{
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select VIZE,FINAL,HARFNOTU from DIFFERENTIALEQUATIONS"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Differential Equations");
                        System.out.println("Vize: "+myresObj2.getString(1)+"\nFinal: "+myresObj2.getString(2)+"\nHarf Notu:"
                                + myresObj2.getString(3));
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "0":{
                Menuler.ogrenciMenusu();
                break;
            }
            default:{
                System.out.println("Böyle bir ders kodu bulunmamakta, tekrar deneyin.");
                notlar();
                break;
            }
        }
        
        System.out.println("\nMenüye dönülüyor...\n");
        Menuler.ogrenciMenusu();            
    }
    
    static void yoklama(){
        System.out.println("Devamsizlik yapılan gün sayısını görmek istediğiniz dersin kodunu giriniz."
                + "\n(aldığınız dersleri ve kodlarını görmek için menüden ders listesine bakabilirsiniz.)"
                + "(0 girerek işlemi iptal edip menüye dönebilirsiniz.)\n");
        
        Scanner input = new Scanner(System.in);

        System.out.println("Ders kodu:");
        String kod = input.nextLine();
        
        Connection baglanti2=null;
        PreparedStatement showstate = null;
        ResultSet myresObj2 = null;
        
        switch(kod){
            case "01": {
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select YOKLAMA from NESNEYEYÖNELIKPROGRAMLAMA"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Nesneye Yönelik Programlama");
                        System.out.println("Yoklama: "+myresObj2.getString(1)+" gün devamsızlığınız bulunmaktadır.");                        
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "02":{
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select YOKLAMA from VERIYAPILARI"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Veri Yapıları");
                        System.out.println("Yoklama: "+myresObj2.getString(1)+"gün devamsızlığınız bulunmaktadır.");
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "03":{
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select YOKLAMA from LOJIKDEVRETASARIMI"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Lojik Devre Tasarımı");
                        System.out.println("Yoklama: "+myresObj2.getString(1)+"gün devamsızlığınız bulunmaktadır.");
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "04":{
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select YOKLAMA from DEVRELERVESISTEMLER"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Devreler ve Sistemler");
                        System.out.println("Yoklama: "+myresObj2.getString(1)+"gün devamsızlığınız bulunmaktadır.");
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "05":{
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select YOKLAMA from OLASILIKTEORISIVEİSTATISTIK"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Olasılık Teorisi ve İstatistik");
                        System.out.println("Yoklama: "+myresObj2.getString(1)+"gün devamsızlığınız bulunmaktadır.");
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "06":{
                if(dersiAliyorMu(Ogrenci.numara, kod) == true){
                    try{
                        baglanti2=DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
                        showstate = baglanti2.prepareStatement("select YOKLAMA from DIFFERENTIALEQUATIONS"
                                    + " where numara = ?");
                        showstate.setInt(1, Ogrenci.numara);
                        myresObj2 = showstate.executeQuery();
                        myresObj2.next();
                        System.out.println("Differential Equations");
                        System.out.println("Yoklama: "+myresObj2.getString(1)+"gün devamsızlığınız bulunmaktadır.");
                    }
                    catch (SQLException e){
                        e.printStackTrace();  
                    }
                }
                else{
                    System.out.println("Bu dersi almıyorsunuz!\n");
                }
                break;
            }
            case "0":{
                Menuler.ogrenciMenusu();
                break;
            }
            default:{
                System.out.println("Böyle bir ders kodu bulunmamakta, tekrar deneyin.");
                notlar();
                break;
            }
        }
        
        System.out.println("Menüye dönülüyor...\n");
        Menuler.ogrenciMenusu();
    }
}
