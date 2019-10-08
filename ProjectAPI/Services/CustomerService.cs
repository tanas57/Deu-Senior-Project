using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ProjectAPI.Models;

namespace ProjectAPI.Services
{
    public class CustomerService : ICustomerServices
    {
        private readonly Dictionary<string, CustomerModel> _customers;

        public CustomerService()
        {
            _customers = new Dictionary<string, CustomerModel>();
        }
        public CustomerModel AddCustomer(CustomerModel customer)
        {
            _customers.Add(customer.FullName, customer);

            return customer;
        }
        
        public Dictionary<string, CustomerModel> GetCustomer()
        {
            return _customers;
        }
    }
}
