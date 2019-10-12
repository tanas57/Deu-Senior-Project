using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectAPI.Models
{
    public class PackageStatus
    {
        [Key]
        public int Id { get; set; }
        public Package PackageId { get; set; }
        public Branch StatusBranch { get; set; }
        public string Status { get; set; }
    }
}
