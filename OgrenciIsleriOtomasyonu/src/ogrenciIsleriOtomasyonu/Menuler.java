
package ogrenciIsleriOtomasyonu;

import java.util.Scanner;

public class Menuler  {
       
    static void anaMenu(){
        int sayi;
        Scanner input = new Scanner(System.in);
        System.out.println("ÖĞRENCİ İŞLERİ OTOMASYONU\n"); 
            System.out.println("Ana Menü\n----------------------"
                + "----------------------------------");
            System.out.println("1.Öğrenci Girişi          2.Öğrenci Kaydı Oluştur\n"
                + "3.Öğretim Üyesi Girişi    4.Öğretim Üyesi Kaydı Oluştur\n"
                + "5.Admin Girişi            0.Çıkış\n--------------------------"
                    + "------------------------------\n"
                + "Yapmak istediğiniz işlemi seçiniz: ");
        do{
            
            sayi = input.nextInt();
            switch(sayi){
                case 1: {
                    Ogrenci ogr = new Ogrenci();
                    ogr.giris();
                    ogrenciMenusu();
                    break;
                }

                case 2: {
                    Ogrenci ogr = new Ogrenci();
                    ogr.kayitol();
                    break;
                }

                case 3: {
                    OgretimUyesi ogr_uyesi = new OgretimUyesi();
                    ogr_uyesi.giris();
                    ogretimUyesiMenusu();
                    break;
                }
                
                case 4: {
                    OgretimUyesi ogr_uyesi = new OgretimUyesi();
                    ogr_uyesi.kayitol();
                    break;
                }
                
                case 5: {
                    Admin.giris();
                    break;
                }
                
                case 0: {
                    System.out.println("Çıkış yapıldı!");
                    System.exit(0);
                    //break;
                }
                
                default: {
                    System.out.println("Gecersiz deger, tekrar giriniz!");
                    break;
                } 
            }
        }while(sayi != 0);
    }
    
    static void ogrenciMenusu(){
        int sayi;
        Scanner input = new Scanner(System.in);
        System.out.println("Öğrenci Menüsü"); 
        System.out.println("----------------------------------------------------");
        System.out.println("1.Ana Menü(Oturumu Kapat)     0.Çıkış\n"
                + "2.Not Bilgileri               3.Yoklama Bilgileri\n"
                + "4.Ders Listesi                5.Ders Seçme\n"
                + "6.Şifre Değiştir\n----------------------------------------------------\n"
                + "Yapmak istediğiniz işlemi seçiniz: ");
        do{
            
            sayi = input.nextInt();
            switch(sayi){
                case 1: {
                    anaMenu();
                    break;
                }

                case 2: {
                    Ogrenci.notlar();
                    break;
                }

                case 3: {
                    Ogrenci.yoklama();
                    break;
                }
                
                case 4: {
                    Ogrenci.ogrenciDersleri();
                    break;
                }
                
                case 5: {
                    Ogrenci.dersSec();
                    break;
                }
                
                case 6: {
                    Ogrenci ogr = new Ogrenci();
                    ogr.sifreDegistir();
                    break;
                }
                
                case 0: {
                    System.out.println("Çıkış yapıldı!");
                    System.exit(0);
                    //break;
                }
                
                default: {
                    System.out.println("Gecersiz deger, tekrar giriniz!");
                    break;
                } 
            }
        }while(sayi != 0);
    }
    
    static void ogretimUyesiMenusu(){
        int sayi;
        Scanner input = new Scanner(System.in);
        System.out.println("Öğretim Üyesi Menüsü"); 
        System.out.println("---------------------------------------------------");
        System.out.println("1.Ana Menü(Oturumu Kapat)     0.Cıkış\n"
                + "2.Not Gir                     3.Yoklama Bilgisi Gir\n"
                + "4.Öğrencilerin Notlarını Gör  5.Ders Listesi\n"
                + "6.Harf Notu Belirle           7.Şifre Değiştir"
                + "\n----------------------------------------------------\n"
                + "Yapmak istediğiniz işlemi seçiniz: ");
        do{
            
            sayi = input.nextInt();
            switch(sayi){
                case 1: {
                    anaMenu();
                    break;
                }

                case 2: {
                    OgretimUyesi.notGir();
                    break;
                }

                case 3: {
                    OgretimUyesi.yoklamaGir();
                    break;
                }
                
                case 4: {
                    OgretimUyesi.notListesi();
                    break;
                }
                
                case 5: {
                    OgretimUyesi.ogretimUyesiDersleri();
                    break;
                }
                
                case 6: {
                    OgretimUyesi.harfNotuBelirle();
                    break;
                }
                
                case 7: {
                    OgretimUyesi ogr = new OgretimUyesi();
                    ogr.sifreDegistir();
                    break;
                }
                
                case 0: {
                    System.out.println("Çıkış yapıldı!");
                    System.exit(0);
                    //break;
                }
                
                default: {
                    System.out.println("Gecersiz deger, tekrar giriniz!");
                    break;
                } 
            }
        }while(sayi != 0);
    }
    
    static void adminMenusu(){
        int sayi;
        Scanner input = new Scanner(System.in);
        System.out.println("Admin Menüsü"); 
        System.out.println("---------------------------------------------------------");
        System.out.println("1.Ana Menü(Oturumu Kapat)  0.Çıkış\n"
                + "2.Öğrenci Listesi          3.Ogretim Uyesi Listesi\n"
                + "4.Ders Programı Hazırla    5.Ogretim Uyesine Ders Tanımla"
                + "\n---------------------------------------------------------\n"
                + "Yapmak istediğiniz işlemi seçiniz: ");
        do{
            
            sayi = input.nextInt();
            switch(sayi){
                case 1: {
                    anaMenu();
                    break;
                }

                case 2: {
                    Admin.ogrenciListele();
                    break;
                }

                case 3: {
                    Admin.ogretimUyesiListele();
                    break;
                }
                
                case 4: {
                    Admin.dersProgramiHazirla();
                    break;
                }
                
                case 5: {
                    Admin.dersTanimla();
                    break;
                }
                
                case 0: {
                    System.out.println("Çıkış yapıldı!");
                    System.exit(0);
                    //break;
                }
                
                default: {
                    System.out.println("Gecersiz deger, tekrar giriniz!");
                    break;
                } 
            }
        }while(sayi != 0);
    }
}

