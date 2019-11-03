using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectAPI.Models
{
    public class Package
    {
        [Key]
        public int Id { get; set; }
        public long Barcode { get; set; }
        public float PackageWeigth { get; set; }
        public float PackageDesi { get; set; }
        public Branch PackageOutBranch { get; set; }
        public Branch PackageInBranch { get; set; }
        public Customer Customer { get; set; }
        public ICollection<PackageStatus> PackageStatus { get; set; }
    }
}
