
using System.ComponentModel.DataAnnotations;

namespace ProjectAPI.Models
{
    public class Cargoman
    {
        [Key]
        public int id { get; set; }
        public string Fullname { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public string Profile { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
    }
}
