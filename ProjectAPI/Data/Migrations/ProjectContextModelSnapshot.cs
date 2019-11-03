﻿// <auto-generated />
using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using ProjectAPI.Data;

namespace ProjectAPI.Data.Migrations
{
    [DbContext(typeof(ProjectContext))]
    partial class ProjectContextModelSnapshot : ModelSnapshot
    {
        protected override void BuildModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "2.1.11-servicing-32099")
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("ProjectAPI.Models.Branch", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("BranchAddress");

                    b.Property<string>("BranchName");

                    b.Property<string>("BranchPhone");

                    b.HasKey("Id");

                    b.ToTable("Branches");
                });

            modelBuilder.Entity("ProjectAPI.Models.Customer", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Address");

                    b.Property<string>("FullName");

                    b.Property<string>("Phone");

                    b.HasKey("Id");

                    b.ToTable("Customers");
                });

            modelBuilder.Entity("ProjectAPI.Models.Package", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<long>("Barcode");

                    b.Property<int?>("CustomerId");

                    b.Property<float>("PackageDesi");

                    b.Property<int?>("PackageInBranchId");

                    b.Property<int?>("PackageOutBranchId");

                    b.Property<float>("PackageWeigth");

                    b.HasKey("Id");

                    b.HasIndex("CustomerId");

                    b.HasIndex("PackageInBranchId");

                    b.HasIndex("PackageOutBranchId");

                    b.ToTable("Packages");
                });

            modelBuilder.Entity("ProjectAPI.Models.PackageStatus", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int?>("PackageIdId");

                    b.Property<string>("Status");

                    b.Property<int?>("StatusBranchId");

                    b.HasKey("Id");

                    b.HasIndex("PackageIdId");

                    b.HasIndex("StatusBranchId");

                    b.ToTable("PackageStatuses");
                });

            modelBuilder.Entity("ProjectAPI.Models.Package", b =>
                {
                    b.HasOne("ProjectAPI.Models.Customer", "Customer")
                        .WithMany()
                        .HasForeignKey("CustomerId");

                    b.HasOne("ProjectAPI.Models.Branch", "PackageInBranch")
                        .WithMany()
                        .HasForeignKey("PackageInBranchId");

                    b.HasOne("ProjectAPI.Models.Branch", "PackageOutBranch")
                        .WithMany()
                        .HasForeignKey("PackageOutBranchId");
                });

            modelBuilder.Entity("ProjectAPI.Models.PackageStatus", b =>
                {
                    b.HasOne("ProjectAPI.Models.Package", "PackageId")
                        .WithMany("PackageStatus")
                        .HasForeignKey("PackageIdId");

                    b.HasOne("ProjectAPI.Models.Branch", "StatusBranch")
                        .WithMany()
                        .HasForeignKey("StatusBranchId");
                });
#pragma warning restore 612, 618
        }
    }
}
