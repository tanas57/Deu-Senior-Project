using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using ProjectAPI.Data;
using ProjectAPI.Models;

namespace ProjectAPI.Controllers
{
    [Route("package")]
    [ApiController]
    public class PackagesController : ControllerBase
    {
        private readonly ProjectContext _context;

        public PackagesController(ProjectContext context)
        {
            _context = context;
        }

        // GET: api/Packages
        [HttpGet]
        [Route("list")]
        public IEnumerable<Package> GetPackages()
        {
            return _context.Packages
                .Include(x => x.PackageInBranch)
                .Include(y => y.PackageOutBranch)
                .Include(c => c.Customer)
                .Include(s => s.PackageStatus);
        }

        // GET: api/Packages/5
        //[HttpGet("{barcode}")]
        [Route("getPackage/{barcode}")]
        public async Task<IActionResult> GetPackage([FromRoute] long barcode)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var package = await _context.Packages.FirstOrDefaultAsync(x => x.Barcode == barcode);

            if (package == null)
            {
                return NotFound();
            }

            return Ok(package);
        }
        [Route("customer/{barcode}")]
        public async Task<IActionResult> GetPackageOwner([FromRoute] long barcode)
        {
            if(!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var package = await _context.Packages.Include(x=> x.Customer).FirstOrDefaultAsync(x => x.Barcode == barcode);

            if (package == null)
            {
                return NotFound();
            }

            return Ok(package.Customer);
        }

        // PUT: api/Packages/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutPackage([FromRoute] int id, [FromBody] Package package)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != package.Id)
            {
                return BadRequest();
            }

            _context.Entry(package).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PackageExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/Packages
        [HttpPost]
        public async Task<IActionResult> PostPackage([FromBody] Package package)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.Packages.Add(package);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetPackage", new { id = package.Id }, package);
        }

        // DELETE: api/Packages/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeletePackage([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var package = await _context.Packages.FindAsync(id);
            if (package == null)
            {
                return NotFound();
            }

            _context.Packages.Remove(package);
            await _context.SaveChangesAsync();

            return Ok(package);
        }

        private bool PackageExists(int id)
        {
            return _context.Packages.Any(e => e.Id == id);
        }
    }
}