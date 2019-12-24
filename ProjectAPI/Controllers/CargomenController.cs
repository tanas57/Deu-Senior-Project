using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using ProjectAPI.Data;
using ProjectAPI.Models;

namespace ProjectAPI.Controllers
{
    [Route("cargoman")]
    [ApiController]
    public class CargomenController : ControllerBase
    {
        private readonly ProjectContext _context;

        public CargomenController(ProjectContext context)
        {
            _context = context;
        }

        // GET: api/Cargomen
        [HttpGet]
        public IEnumerable<Cargoman> GetCargoman()
        {
            return _context.Cargoman;
        }

        // GET: api/Cargomen/5
        [HttpGet("{username}/{password}")]
        public async Task<IActionResult> GetCargoman([FromRoute] string username, [FromRoute] string password)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            try
            {
                var cargoman = await _context.Cargoman.Where(x => x.Username == username && x.Password == password).FirstAsync();

                if (cargoman == null)
                {
                    return Ok(null);
                }
                return Ok(cargoman);
            }
            catch(Exception e)
            {
                return Ok(null);
            }
            
            
        }

        // GET: api/Restaurants/5
        [HttpGet("image/{id}")]
        public async Task<IActionResult> GetImage([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var cargoman = await _context.Cargoman.FindAsync(id);

            if (cargoman == null)
            {
                return NotFound();
            }

            string img = cargoman.Profile;
            var image = System.IO.File.OpenRead("img/" + img);
            return File(image, "image/jpeg");
        }

        // PUT: api/Cargomen/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutCargoman([FromRoute] int id, [FromBody] Cargoman cargoman)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != cargoman.id)
            {
                return BadRequest();
            }

            _context.Entry(cargoman).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!CargomanExists(id))
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

        // POST: api/Cargomen
        [HttpPost]
        public async Task<IActionResult> PostCargoman([FromBody] Cargoman cargoman)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.Cargoman.Add(cargoman);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetCargoman", new { id = cargoman.id }, cargoman);
        }

        // DELETE: api/Cargomen/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteCargoman([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var cargoman = await _context.Cargoman.FindAsync(id);
            if (cargoman == null)
            {
                return NotFound();
            }

            _context.Cargoman.Remove(cargoman);
            await _context.SaveChangesAsync();

            return Ok(cargoman);
        }

        private bool CargomanExists(int id)
        {
            return _context.Cargoman.Any(e => e.id == id);
        }
    }
}