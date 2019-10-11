using System.Collections.Generic;
using ProjectAPI.Models;

namespace ProjectAPI.Services
{
    public interface ICustomerServices
    {
        CustomerModel AddCustomer(CustomerModel customer);
        Dictionary<string, CustomerModel> GetCustomer();
    }
}
