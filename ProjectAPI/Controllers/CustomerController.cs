using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using ProjectAPI.Data;
using ProjectAPI.Models;
using ProjectAPI.Services;

namespace ProjectAPI.Controllers
{
    [Route("customer")]
    [ApiController]
    public class CustomerController : ControllerBase
    {
        private readonly ICustomerServices _services;

        public CustomerController(ICustomerServices services)
        {
            _services = services;
        }
        [HttpGet]
        [Route("deneme")]
        public ActionResult deneme()
        {
            
            return Ok();
        }

        [HttpPost]
        [Route("AddCustomer")]
        public ActionResult<Customer> AddCustomer(Customer customer)
        {
            var Addingcustomer = _services.AddCustomer(customer);

            //if (Addingcustomer == null) return NotFound();

            return Addingcustomer;
        }
        [HttpGet]
        [Route("CustomerList")]
        public ActionResult<List<Customer>> GetCustomers()
        {
            var customers = _services.GetCustomer();

            if (customers.Result.Count == 0) return NotFound();

            return customers.Result;
        }
    }
}