using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectAPI.Models
{
    public class PackageModel
    {
        [Key]
        public int Id { get; set; }
        public string Barcode { get; set; }
        [Key]
        public int CustomerId { get; set; }
        public float PackageWeigth { get; set; }
        public float PackageDesi { get; set; }
        public int PackageOutBranch { get; set; }
        public int PackageInBranch { get; set; }
    }
}
