using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using ProjectAPI.Models;
using ProjectAPI.Services;

namespace ProjectAPI.Controllers
{
    [Route("api/")]
    [ApiController]
    public class CustomerController : ControllerBase
    {
        private readonly ICustomerServices _services;

        public CustomerController(ICustomerServices services)
        {
            _services = services;
        }

        [HttpPost]
        [Route("AddCustomer")]
        public ActionResult<CustomerModel> AddCustomer(CustomerModel customer)
        {
            var Addingcustomer = _services.AddCustomer(customer);

            if (Addingcustomer == null) return NotFound();

            return Addingcustomer;
        }
        [HttpGet]
        [Route("CustomerList")]
        public ActionResult<Dictionary<string, CustomerModel>> GetCustomers()
        {
            var customers = _services.GetCustomer();

            if (customers.Count == 0) return NotFound();

            return customers;
        }
    }
}