using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using ProjectAPI.Data;
using ProjectAPI.Models;

namespace ProjectAPI.Controllers
{
    [Route("Customer")]
    [ApiController]
    public class CustomerController : ControllerBase
    {
        private readonly ProjectContext _context;
        public CustomerController(ProjectContext context)
        {
            _context = context;
        }

        [HttpPost]
        [Route("AddCustomer")]
        public ActionResult<Customer> AddCustomer(Customer customer)
        {
            //var Addingcustomer = _services.AddCustomer(customer);

            //if (Addingcustomer == null) return NotFound();

            return null;
        }
        [HttpGet]
        [Route("List")]
        public IEnumerable<Customer> GetCustomers()
        {
            return _context.Customers;
        }
    }
}