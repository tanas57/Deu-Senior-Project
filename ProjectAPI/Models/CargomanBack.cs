using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectAPI.Models
{
    public class CargomanBack
    {
        [Key]
        public int id { get; set; }
        public Cargoman Cargoman { get; set; }
        public Package Package { get; set; }
        public DateTime DeliveryDay { get; set; }
        public Boolean isDelivered { get; set; }
    }
}
