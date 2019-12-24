using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using ProjectAPI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectAPI.Data
{
    public class DbHelper
    {
        public static void Initialize(IApplicationBuilder applicationBuilder)
        {
            using (var serviceScope = applicationBuilder.ApplicationServices.GetService<IServiceScopeFactory>().CreateScope())
            {
                var context = serviceScope.ServiceProvider.GetService<ProjectContext>();
                context.Database.EnsureCreated();

                if (context.Customers != null && context.Customers.Any())
                    return;

                var customers = GetCustomers().ToArray();
                context.Customers.AddRange(customers);
                context.SaveChanges();

                var packages = GetPackages(context).ToArray(); ;
                context.Packages.AddRange(packages);
                context.SaveChanges();

                var cargoman = GetCargoman().ToArray(); ;
                context.Cargoman.AddRange(cargoman);
                context.SaveChanges();
            }
        }
        public static List<Customer> GetCustomers()
        {
            List<Customer> customers = new List<Customer>()
            {
                new Customer{ FullName = "Tayyip Muslu", Address = "Kuruçeşme Mah. 205/7 Sok. No 16/1 D 8 Buca/İzmir", Phone = "5462003052" },
                new Customer{ FullName = "Enes Demirdere", Address = "Atatürk Mahallesi 202/49 sokak no 24 Buca İzmir", Phone = "541313131" },
                new Customer{ FullName = "Mehmet İnce", Address = "Hoca Ahmet Yesevi Cad. No 202 Buca İzmir", Phone = "541232131" },
                new Customer{ FullName = "Oğuz Soydemir", Address = "Buca Koop Mahallesi Manolya Apartmanı Buca İzmir", Phone = "5131321" },
                new Customer{ FullName = "Osman Marankoz", Address = "Kuruçeşme Mahallesi Hacı Bektaşi Veli Caddesi No 10 Buca İzmir", Phone = "5462003052" },
                new Customer{ FullName = "Ziya Göktürk", Address = "Atatürk Mahallesi 203/1 sokak no 2 Buca İzmir", Phone = "541313131" },
                new Customer{ FullName = "Mehmet Eraslan", Address = "Atatürk Mahallesi 202/14 sokak no 1 Buca İzmir", Phone = "541232131" },
                new Customer{ FullName = "Oğuz Soydemir", Address = "Atatürk Mahallesi 201/7 sok no 3 Buca İzmir", Phone = "5131321" },
                new Customer{ FullName = "Fadime Öztürk", Address = "201/4 sokak Atatürk Mh. No 14 D 1 Buca İzmir", Phone = "5462003052" },
                new Customer{ FullName = "Aleyna Tilki", Address = "Kuruçeşme Mah. 205/11 sokak No 2 Buca İzmir", Phone = "541313131" },
                new Customer{ FullName = "Didem Sert", Address = "Kuruçeşme Mahallesi 203/21 sokak No 15 Buca İzmir", Phone = "541232131" },
                new Customer{ FullName = "Sena Zeybek", Address = "Atatürk Mahallesi 202/2 Sokak No 24 Daire 5 Buca İzmir", Phone = "5131321" },
                new Customer{ FullName = "Erdem Sevinç", Address = "Kozağaç, Gazeteci Yazar İsmail Sivri Blv No:103/A, 35390 Buca/İzmir", Phone = "5462003052" },
                new Customer{ FullName = "Emre Demirdere", Address = "Çamlıpınar, 254. Sk. No:295 D:1B, 35390 Buca/İzmir", Phone = "541313131" },
                new Customer{ FullName = "Sena Nur Muslu", Address = "Yıldız Mah., 206/5 Sok No:86, 35678 Buca/İzmir", Phone = "541232131" },
                new Customer{ FullName = "Pınar Çınar", Address = "Buca Koop., 200/66. Sk. No:3/A, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Mustafa Kaya", Address = "Atatürk Mah, 204/26. Sk. No: 4-6/A, 35390 Buca/İzmir", Phone = "5462003052" },
                new Customer{ FullName = "Kadir Türk", Address = "Yıldız, 206/34 Sok. No:21, 35390 Buca/İzmir", Phone = "541313131" },
                new Customer{ FullName = "Mehmet Kalın", Address = "Yenigün, 266/2. Sk. No:36, 35390 Buca Organize Sanayi Bölgesi/Buca/İzmir", Phone = "541232131" },
                new Customer{ FullName = "Gülüm Kafe", Address = "Adatepe, 62/5. Sk. No:2, 35400 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Ayaz Öğrenci Yurdu", Address = "Atatürk, 207. Sk. No:27, 35390 Buca/İzmir", Phone = "5462003052" },
                new Customer{ FullName = "Baki Bakkaliyesi", Address = "Yıldız, 220/10. Sk. No: 25/A, 35390 Buca/İzmir", Phone = "541313131" },
                new Customer{ FullName = "BİM TINAZTEPE", Address = "Tınaztepe Mah, 242/16. Sk. No: 58, 35390 Buca/İzmir", Phone = "541232131" },
                new Customer{ FullName = "Laktis", Address = "Yıldız, 220. Sk. No:79, 35400 Buca Osb/Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Yataş Bedding", Address = "Kuruçeşme, Doğuş Caddesi, 205/5. Sk. A D:No :25, 35390 Buca/İzmir", Phone = "5462003052" },
                new Customer{ FullName = "Kuruçeşme Muhtarlığı", Address = "Kuruçeşme, 205. Sk. No:30, 35678 Buca/İzmir", Phone = "541313131" },
                new Customer{ FullName = "Ata Matbaacılık", Address = "Bucaosb Mahallesi 3/19 Sok. No:11, Begos Sitesi, 35400 Buca", Phone = "541232131" },
                new Customer{ FullName = "Nilüfer Çeyiz Evi", Address = "Ataturk mh, 204/7. Sk. no 7 D: 4, 35150 Buca/İzmir", Phone = "5131321" },


                new Customer{ FullName = "Kampüs Öğrenci Yurdu", Address = "Kuruçeşme, 205/59. Sk. No:5, 35390 Tınaztepe / Buca/Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "ELEKTRİKÇİ KAYA ELEKTRİK &UYDU", Address = "Kuruçeşme, 205/8. Sk. No:11, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "ArBe Beyaz Eşya Teknik Servis ve Ticaret", Address = "Kuruçeşme, 205/2. Sk. No:43, 35160 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Can Market", Address = "Kuruçeşme, 205/37. Sk. No: 2A, 35110 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "İnci&Kasap SteakHouse", Address = "Kuruçeşme, 205. Sk. No.62/A, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Ayak6 Mağazası", Address = "Kuruçeşme, 205. Sk. No:68A, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Uht Bilişim Yazılım Web Tasarım ve Bilgi Teknolojileri", Address = "Buca Koop., Buca Koop, 1408. Sk., 35090 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "SOil Benzinlik", Address = "Yıldız, 206/68. Sokak No:4/27, 35400 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Seçkin İnternet Kafe", Address = "Yıldız, Hoca Ahmet Yesevi Cd. no:110/a, 35160 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Food Bomb", Address = "Yıldız, 206/50. Sk. No: 3/A, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Özel Dokuz Eylül Sürücü Kursu", Address = "Atatürk, Hoca Ahmet Yesevi Cd. No 66, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Üniversiteliler Apartmanı", Address = "Atatürk, 202/6. Sk. No:29, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "ÖZ İPEK", Address = "Atatürk Mah, 202/9. Sk. No:2/A, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Suyapı Gayrimenkul & İnşaat", Address = "Yıldız, 206/31. Sk. No:1A, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Relax Gayrimenkul Yatırımları", Address = "Atatürk, 202/12. Sk. NO 37/C, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Knauf Mavikale", Address = "Adatepe, No:41/ İzmir, 63. Sk. No:41, 35400 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Deniz bilgisayar", Address = "Atatürk, 202/21. Sk. No:11, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Renkli Düşler Itriyat Deposu", Address = "Atatürk Mah, 204/21. Sk. No: 10/A, 35390 Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "Efsane Berber", Address = "Yıldız, 206/6. Sk. No:5, 35390 Buca Osb/Buca/İzmir", Phone = "5131321" },
                new Customer{ FullName = "NZR Organizasyon", Address = "Yıldız, 242/21. Sk. 12/A, 35100 Buca/İzmir", Phone = "5131321" }

            };
            return customers;
        }

        public static List<Package> GetPackages(ProjectContext context)
        {
            List<Package> packages = new List<Package>()
            {
                new Package{ Barcode = 12345678910, Customer = context.Customers.FirstOrDefault(x=> x.Id == 1), PackageDesi = 2, PackageWeigth =3, Priority = 2 },
                new Package{ Barcode = 12345678911, Customer = context.Customers.FirstOrDefault(x=> x.Id == 2), PackageDesi = 1, PackageWeigth = 5 , Priority = 2},
                new Package{ Barcode = 12345678912, Customer = context.Customers.FirstOrDefault(x=> x.Id == 3), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678913, Customer = context.Customers.FirstOrDefault(x=> x.Id == 4), PackageDesi = 2, PackageWeigth =3 },
                new Package{ Barcode = 12345678914, Customer = context.Customers.FirstOrDefault(x=> x.Id == 5), PackageDesi = 1, PackageWeigth = 5 },
                new Package{ Barcode = 12345678915, Customer = context.Customers.FirstOrDefault(x=> x.Id == 6), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678916, Customer = context.Customers.FirstOrDefault(x=> x.Id == 7), PackageDesi = 2, PackageWeigth =3 , Priority = 3},
                new Package{ Barcode = 12345678917, Customer = context.Customers.FirstOrDefault(x=> x.Id == 8), PackageDesi = 1, PackageWeigth = 5 },
                new Package{ Barcode = 12345678918, Customer = context.Customers.FirstOrDefault(x=> x.Id == 9), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678919, Customer = context.Customers.FirstOrDefault(x=> x.Id == 10), PackageDesi = 2, PackageWeigth =3 },
                new Package{ Barcode = 12345678920, Customer = context.Customers.FirstOrDefault(x=> x.Id == 11), PackageDesi = 1, PackageWeigth = 5 },
                new Package{ Barcode = 12345678921, Customer = context.Customers.FirstOrDefault(x=> x.Id == 12), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678922, Customer = context.Customers.FirstOrDefault(x=> x.Id == 13), PackageDesi = 2, PackageWeigth =3 },
                new Package{ Barcode = 12345678923, Customer = context.Customers.FirstOrDefault(x=> x.Id == 14), PackageDesi = 1, PackageWeigth = 5 },
                new Package{ Barcode = 12345678924, Customer = context.Customers.FirstOrDefault(x=> x.Id == 15), PackageDesi = 6, PackageWeigth =3 , Priority = 3},
                new Package{ Barcode = 12345678925, Customer = context.Customers.FirstOrDefault(x=> x.Id == 16), PackageDesi = 2, PackageWeigth =3 },
                new Package{ Barcode = 12345678926, Customer = context.Customers.FirstOrDefault(x=> x.Id == 17), PackageDesi = 1, PackageWeigth = 5 },
                new Package{ Barcode = 12345678927, Customer = context.Customers.FirstOrDefault(x=> x.Id == 18), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678928, Customer = context.Customers.FirstOrDefault(x=> x.Id == 19), PackageDesi = 2, PackageWeigth =3 },
                new Package{ Barcode = 12345678929, Customer = context.Customers.FirstOrDefault(x=> x.Id == 20), PackageDesi = 1, PackageWeigth = 5 },
                new Package{ Barcode = 12345678930, Customer = context.Customers.FirstOrDefault(x=> x.Id == 21), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678931, Customer = context.Customers.FirstOrDefault(x=> x.Id == 22), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678932, Customer = context.Customers.FirstOrDefault(x=> x.Id == 23), PackageDesi = 2, PackageWeigth =3 },
                new Package{ Barcode = 12345678933, Customer = context.Customers.FirstOrDefault(x=> x.Id == 24), PackageDesi = 1, PackageWeigth = 5 },
                new Package{ Barcode = 12345678934, Customer = context.Customers.FirstOrDefault(x=> x.Id == 25), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678935, Customer = context.Customers.FirstOrDefault(x=> x.Id == 26), PackageDesi = 2, PackageWeigth =3 },
                new Package{ Barcode = 12345678936, Customer = context.Customers.FirstOrDefault(x=> x.Id == 27), PackageDesi = 1, PackageWeigth = 5 },
                new Package{ Barcode = 12345678937, Customer = context.Customers.FirstOrDefault(x=> x.Id == 28), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678938, Customer = context.Customers.FirstOrDefault(x=> x.Id == 29), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678939, Customer = context.Customers.FirstOrDefault(x=> x.Id == 30), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678940, Customer = context.Customers.FirstOrDefault(x=> x.Id == 31), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678941, Customer = context.Customers.FirstOrDefault(x=> x.Id == 32), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678942, Customer = context.Customers.FirstOrDefault(x=> x.Id == 33), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678943, Customer = context.Customers.FirstOrDefault(x=> x.Id == 34), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678944, Customer = context.Customers.FirstOrDefault(x=> x.Id == 35), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678945, Customer = context.Customers.FirstOrDefault(x=> x.Id == 36), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678946, Customer = context.Customers.FirstOrDefault(x=> x.Id == 37), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678947, Customer = context.Customers.FirstOrDefault(x=> x.Id == 38), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678948, Customer = context.Customers.FirstOrDefault(x=> x.Id == 39), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678949, Customer = context.Customers.FirstOrDefault(x=> x.Id == 40), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678950, Customer = context.Customers.FirstOrDefault(x=> x.Id == 41), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678951, Customer = context.Customers.FirstOrDefault(x=> x.Id == 42), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678952, Customer = context.Customers.FirstOrDefault(x=> x.Id == 43), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678953, Customer = context.Customers.FirstOrDefault(x=> x.Id == 44), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678954, Customer = context.Customers.FirstOrDefault(x=> x.Id == 45), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678955, Customer = context.Customers.FirstOrDefault(x=> x.Id == 46), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678956, Customer = context.Customers.FirstOrDefault(x=> x.Id == 47), PackageDesi = 6, PackageWeigth =3 },
                new Package{ Barcode = 12345678957, Customer = context.Customers.FirstOrDefault(x=> x.Id == 48), PackageDesi = 6, PackageWeigth =3 }
            };
            return packages;
        }

        public static List<Cargoman> GetCargoman()
        {
            List<Cargoman> customers = new List<Cargoman>()
            {
                new Cargoman { Fullname = "Enes Demirdere", Username = "demirdere", Password = "6332", Profile = "cargoman_0000001.jpeg" },
                new Cargoman { Fullname = "Tayyip Muslu", Username = "tanas57", Password = "123456", Profile = "cargoman_0000002.jpeg" }

            };

            return customers;
        }
    }
}