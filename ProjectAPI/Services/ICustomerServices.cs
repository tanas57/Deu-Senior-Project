using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ProjectAPI.Models;

namespace ProjectAPI.Services
{
    public interface ICustomerServices
    {
        CustomerModel AddCustomer(CustomerModel customer);
        Dictionary<string, CustomerModel> GetCustomer();
    }
}
