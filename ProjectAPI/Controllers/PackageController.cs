using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace ProjectAPI.Controllers
{
    [Route("package")]
    [ApiController]
    public class PackageController : ControllerBase
    {
        [HttpGet]
        [Route("deneme")]
        public ActionResult<IEnumerable<string>> deneme()
        {
            return new string[]{ "sa", "naber", "canım" };
        }
    }
}