
package ogrenciIsleriOtomasyonu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract public class Kullanici {
    abstract void giris();
    abstract void kayitol();
    abstract void sifreDegistir();
    static boolean numaraKayitliMi(int no, int tip){
        //tip 1 için öğrenci 2 için öğretim üyesi
        Connection baglanti = null;
        PreparedStatement mystatObj = null;
        ResultSet myresObj = null;
        try{
            baglanti = DriverManager.getConnection("jdbc:derby://localhost:1527/otomasyon","admin","123456");
            if(tip == 1){
                mystatObj = baglanti.prepareStatement("select * from ADMIN.OGRENCIDERS");
            }
            else mystatObj = baglanti.prepareStatement("select * from ADMIN.OGRETIMUYESIDERS");
            myresObj = mystatObj.executeQuery();           
            while(myresObj.next()){
                if((no == myresObj.getInt(1))){
                    return true;
                }    
            }
            return false;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    static int numara;
    static String isim;
    static String sifre;
}
