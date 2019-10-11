using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectAPI.Models
{
    public class PackageStatusModel
    {
        [Key]
        public int Id { get; set; }
        [Key]
        public int PackageId { get; set; }
        public int StatusBranch { get; set; }
        public string Status { get; set; }
    }
}
