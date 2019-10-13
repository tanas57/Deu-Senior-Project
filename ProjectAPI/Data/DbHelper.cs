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
            }
        }
        public static List<Customer> GetCustomers()
        {
            List<Customer> customers = new List<Customer>()
            {
                new Customer{ FullName = "Tayyip Muslu", Address = "Kuruçeşme Mah. 205/7 Sok. No 16/1 D 8 Buca/İzmir", Phone = "5462003052" },
                new Customer{ FullName = "Enes Demirdere", Address = "Atatürk Mahallesi Buca İzmir", Phone = "541313131" },
                new Customer{ FullName = "Mehmet İnce", Address = "Hoca Ahmet Yesevi Cad. No 202 Buca İzmir", Phone = "541232131" },
                new Customer{ FullName = "Oğuz Soydemir", Address = "Buca Koop Mahallesi Manolya Apartmanı Buca İzmir", Phone = "5131321" }
            };
            return customers;
        }

        public static List<Package> GetPackages(ProjectContext context)
        {
            List<Package> packages = new List<Package>()
            {
                new Package{ Barcode = 12345678910, Customer = context.Customers.FirstOrDefault(x=> x.Id == 1), PackageDesi = 2, PackageWeigth =3 },
                new Package{ Barcode = 12345678911, Customer = context.Customers.FirstOrDefault(x=> x.Id == 2), PackageDesi = 1, PackageWeigth = 5 },
                new Package{ Barcode = 12345678912, Customer = context.Customers.FirstOrDefault(x=> x.Id == 4), PackageDesi = 6, PackageWeigth =3 },
            };
            return packages;
        }
    }
}